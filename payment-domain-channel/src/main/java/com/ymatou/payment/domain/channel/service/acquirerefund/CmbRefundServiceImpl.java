/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.acquirerefund;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.service.AcquireRefundService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.infrastructure.db.mapper.PaymentMapper;
import com.ymatou.payment.infrastructure.db.mapper.RefundMiscRequestLogMapper;
import com.ymatou.payment.infrastructure.db.mapper.RefundRequestMapper;
import com.ymatou.payment.infrastructure.db.model.PaymentExample;
import com.ymatou.payment.infrastructure.db.model.PaymentPo;
import com.ymatou.payment.infrastructure.db.model.RefundMiscRequestLogWithBLOBs;
import com.ymatou.payment.infrastructure.db.model.RefundRequestExample;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;
import com.ymatou.payment.integration.common.CmbSignature;
import com.ymatou.payment.integration.model.CmbDoRefundRequest;
import com.ymatou.payment.integration.model.CmbDoRefundResponse;
import com.ymatou.payment.integration.service.cmb.DoRefundService;

/**
 * 招行一网通退款接口
 * 
 * @author wangxudong 2016年11月21日 下午7:01:24
 *
 */
@Component
public class CmbRefundServiceImpl implements AcquireRefundService {

    private static final Logger logger = LoggerFactory.getLogger(CmbRefundServiceImpl.class);

    @Resource
    private InstitutionConfigManager configManager;

    @Resource
    private RefundMiscRequestLogMapper refundMiscRequestLogMapper;

    @Resource
    private DoRefundService doRefundService;

    @Resource
    private SignatureService singatureService;

    @Resource
    private RefundRequestMapper refundRequestMapper;

    @Resource
    private PaymentMapper paymentMapper;

    @Override
    public RefundStatusEnum notifyRefund(RefundRequestPo refundRequest, Payment payment,
            HashMap<String, String> header) {
        InstitutionConfig config = configManager.getConfig(PayTypeEnum.parse(refundRequest.getPayType()));
        Date requestTime = new Date();

        CmbDoRefundRequest cmbDoRefundRequest = generateRequest(refundRequest, payment, config, null);// 组装退款请求
        try {
            CmbDoRefundResponse response = doRefundService.doService(cmbDoRefundRequest, header);// 提交退款申请
            saveRefundMiscRequestLog(refundRequest, requestTime, cmbDoRefundRequest, response, null);// 保存退款应答
            RefundStatusEnum refundStatus = generateRefundStatus(response, config);

            // 如果银行返回状态为退款成功，直接更新支付单状态
            // 招行不提供单笔订单退款查询，所以在提交退款的时候就要更新支付单状态
            if (RefundStatusEnum.THIRDPART_REFUND_SUCCESS.equals(refundStatus)) {
                updatePaymentRefund(refundRequest, payment);
            }

            // TODO:后期需要根据实际运作情况判断COMPLETE_FAILED的情况，此时需要扣除RefundAmt

            updateRefundRequestStatus(refundRequest, refundStatus);// 更新退款状态

            return refundStatus;
        } catch (Exception e) {
            logger.error("call Cmb Refund fail. RefundBatchNo: " + refundRequest.getRefundBatchNo(), e);
            saveRefundMiscRequestLog(refundRequest, requestTime, cmbDoRefundRequest, null, e);
            updateRefundRequestStatus(refundRequest, RefundStatusEnum.REFUND_FAILED);

            return RefundStatusEnum.REFUND_FAILED;
        }
    }

    private CmbDoRefundRequest generateRequest(RefundRequestPo refundRequest, Payment payment,
            InstitutionConfig config, HashMap<String, String> header) {

        CmbDoRefundRequest req = new CmbDoRefundRequest();
        req.getReqData().setBranchNo(config.getBranchNo());
        req.getReqData().setMerchantNo(config.getMerchantId());
        req.getReqData().setDate(payment.getBussinessOrder().getOrderTime().substring(0, 8));
        req.getReqData().setOrderNo(payment.getPaymentId());
        req.getReqData().setAmount(refundRequest.getRefundAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        req.getReqData().setRefundSerialNo(refundRequest.getRefundBatchNo());
        req.getReqData().setOperatorNo(config.getOperatorNo());
        req.getReqData().setEncrypType("RC4");
        // 密码需要进行RC4签名，9999用户的密码默认是商户号
        req.getReqData().setPwd(CmbSignature.rc4Sign(config.getMd5Key(), config.getOperatorPwd()));

        String sign = CmbSignature.shaSign(config.getMd5Key(), req.buildSignString());
        req.setSign(sign);

        return req;
    }

    private void saveRefundMiscRequestLog(RefundRequestPo refundRequest, Date requestTime,
            CmbDoRefundRequest cmbRefundRequest, CmbDoRefundResponse response, Exception e) {

        RefundMiscRequestLogWithBLOBs requestLog = new RefundMiscRequestLogWithBLOBs();
        requestLog.setCorrelateId(String.valueOf(refundRequest.getRefundId()));
        requestLog.setMethod("CmbRefund");
        requestLog.setRequestData(JSON.toJSONString(cmbRefundRequest));
        requestLog.setRequestTime(requestTime);
        requestLog.setResponseTime(new Date());
        if (e != null) {
            requestLog.setIsException(true);
            requestLog.setExceptionDetail(e.toString());
        }
        if (response != null) {
            requestLog.setResponseData(JSON.toJSONString(response));
        }
        requestLog.setRefundBatchNo(refundRequest.getRefundBatchNo());
        refundMiscRequestLogMapper.insertSelective(requestLog);
    }

    private RefundStatusEnum generateRefundStatus(CmbDoRefundResponse response, InstitutionConfig config) {
        if ("SUC0000".equals(response.getRspData().getRspCode())) {
            return RefundStatusEnum.THIRDPART_REFUND_SUCCESS;
        } else {
            return RefundStatusEnum.REFUND_FAILED;
        }
    }

    private void updateRefundRequestStatus(RefundRequestPo refundRequest, RefundStatusEnum refundStatus) {
        RefundRequestPo record = new RefundRequestPo();
        record.setRefundStatus(refundStatus.getCode());
        record.setRefundTime(new Date());
        RefundRequestExample example = new RefundRequestExample();
        example.createCriteria().andRefundBatchNoEqualTo(refundRequest.getRefundBatchNo());

        refundRequestMapper.updateByExampleSelective(record, example);
    }

    /**
     * 更新支付单状态和退款完成金额
     * 
     * @param refundRequest
     * @param payment
     */
    private void updatePaymentRefund(RefundRequestPo refundRequest, Payment payment) {
        PaymentPo paymentPo = new PaymentPo();
        BigDecimal refundAmt = payment.getCompletedRefundAmt() == null ? refundRequest.getRefundAmount()
                : refundRequest.getRefundAmount().add(payment.getCompletedRefundAmt());

        paymentPo.setPaymentId(payment.getPaymentId());
        paymentPo.setPayStatus(PayStatusEnum.Refunded.getIndex());
        paymentPo.setCompletedRefundAmt(refundAmt); // 更新退款完成金额

        PaymentExample example = new PaymentExample();
        example.createCriteria().andPaymentIdEqualTo(paymentPo.getPaymentId());
        paymentMapper.updateByExampleSelective(paymentPo, example);
    }
}
