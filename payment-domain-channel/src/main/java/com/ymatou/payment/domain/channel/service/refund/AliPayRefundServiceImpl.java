/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.refund;

import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.constants.AliPayConsts;
import com.ymatou.payment.domain.channel.service.RefundService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.infrastructure.db.mapper.PaymentMapper;
import com.ymatou.payment.infrastructure.db.mapper.RefundMiscRequestLogMapper;
import com.ymatou.payment.infrastructure.db.mapper.RefundRequestMapper;
import com.ymatou.payment.infrastructure.db.model.PaymentPo;
import com.ymatou.payment.infrastructure.db.model.RefundMiscRequestLogWithBLOBs;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;
import com.ymatou.payment.integration.model.AliPayRefundRequest;
import com.ymatou.payment.integration.model.AliPayRefundResponse;
import com.ymatou.payment.integration.service.alipay.AliPayRefundService;

/**
 * 通知支付宝退款
 * 
 * @author qianmin 2016年6月6日 上午11:40:16
 *
 */
@Component
public class AliPayRefundServiceImpl implements RefundService {

    private static final Logger logger = LoggerFactory.getLogger(AliPayRefundServiceImpl.class);

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private InstitutionConfigManager configManager;

    @Autowired
    private RefundRequestMapper refundRequestMapper;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private SignatureService signatureService;

    @Autowired
    private AliPayRefundService aliPayRefundService;

    @Autowired
    private RefundMiscRequestLogMapper refundMiscRequestLogMapper;

    @Override
    public void notifyRefund(String refundBatchNo, HashMap<String, String> header) {
        taskExecutor.execute(new Runnable() {

            @Override
            public void run() {
                RefundRequestPo refundRequest = refundRequestMapper.selectByPrimaryKey(refundBatchNo); // TODO
                PaymentPo payment = paymentMapper.selectByPrimaryKey(refundRequest.getPaymentId());
                InstitutionConfig config = configManager.getConfig(PayTypeEnum.parse(refundRequest.getPayType()));
                Date requestTime = new Date();

                AliPayRefundRequest aliPayRefundRequest = generateRequest(refundRequest, payment, config, null);
                try {
                    AliPayRefundResponse response = aliPayRefundService.doService(aliPayRefundRequest, header);

                    // save RefundMiscRequestLog
                    RefundMiscRequestLogWithBLOBs requestLog = new RefundMiscRequestLogWithBLOBs();
                    requestLog.setCorrelateId(refundRequest.getPaymentId());
                    requestLog.setMethod("AliRefund");
                    requestLog.setRequestData(aliPayRefundRequest.getRequestData());
                    requestLog.setResponseData(response.getOriginalResponse());
                    requestLog.setRefundBatchNo(refundBatchNo);
                    requestLog.setRequestTime(requestTime); // TODO
                    requestLog.setResponseTime(new Date()); // TODO
                    refundMiscRequestLogMapper.insertSelective(requestLog);

                    // update RefundRequest
                    RefundStatusEnum refundStatus =
                            isSuccess(response) ? RefundStatusEnum.COMMIT : RefundStatusEnum.REFUND_FAILED;
                    refundRequest.setRefundStatus(refundStatus.getCode());
                    refundRequestMapper.updateByPrimaryKeySelective(refundRequest);

                } catch (Exception e) {
                    logger.error("call AliPay Refund fail", e);

                    RefundMiscRequestLogWithBLOBs requestLog = new RefundMiscRequestLogWithBLOBs();
                    requestLog.setCorrelateId(refundRequest.getPaymentId());
                    requestLog.setMethod("AliRefund");
                    requestLog.setRequestData(aliPayRefundRequest.getRequestData());
                    requestLog.setExceptionDetail(e.toString());
                    requestLog.setRequestTime(requestTime); // TODO
                    requestLog.setResponseTime(new Date()); // TODO
                    refundMiscRequestLogMapper.insertSelective(requestLog);
                    // update refundRequest status? //TODO
                }
            }
        });
    }

    private boolean isSuccess(AliPayRefundResponse response) {
        return AliPayConsts.REFUND_SYNC_STATU_P.equals(response.getIsSuccess())
                || AliPayConsts.REFUND_SYNC_STATU_T.equals(response.getIsSuccess());
    }

    private AliPayRefundRequest generateRequest(RefundRequestPo refundRequest, PaymentPo payment,
            InstitutionConfig config, HashMap<String, String> header) {

        AliPayRefundRequest request = new AliPayRefundRequest();
        request.setPartner(config.getMerchantId());
        request.setNotifyUrl(""); // TODO
        request.setSignType(config.getSignType());
        request.setDbackNotifyUrl("http://ymatou.com"); // TODO
        request.setBatchNo(refundRequest.getRefundBatchNo());
        request.setRefundDate(new Date());
        request.setBatchNum("1");
        String detailData = new StringBuilder().append(refundRequest.getInstPaymentId()).append("^")
                .append(refundRequest.getRefundAmount()).append("^").append(AliPayConsts.REFUND_REASON).toString();
        request.setDetailData(detailData);
        request.setUseFreezeAmount("N");

        String sign = signatureService.signMessage(request.mapForSign(), config, header);
        request.setSign(sign);

        return request;
    }

}
