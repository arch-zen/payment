/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.acquirerefund;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.constants.WeixinPayConstants;
import com.ymatou.payment.domain.channel.service.AcquireRefundService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.infrastructure.Money;
import com.ymatou.payment.infrastructure.db.mapper.RefundMiscRequestLogMapper;
import com.ymatou.payment.infrastructure.db.mapper.RefundRequestMapper;
import com.ymatou.payment.infrastructure.db.model.RefundMiscRequestLogWithBLOBs;
import com.ymatou.payment.infrastructure.db.model.RefundRequestExample;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;
import com.ymatou.payment.integration.model.WxRefundRequest;
import com.ymatou.payment.integration.model.WxRefundResponse;
import com.ymatou.payment.integration.service.wxpay.WxRefundService;

/**
 * 通知微信支付退款
 * 
 * @author qianmin 2016年6月6日 上午11:40:36
 *
 */
@Component
public class WeixinRefundServiceImpl implements AcquireRefundService {

    public static final Logger logger = LoggerFactory.getLogger(WeixinRefundServiceImpl.class);

    @Autowired
    private InstitutionConfigManager configManager;

    @Autowired
    private RefundRequestMapper refundRequestMapper;

    @Autowired
    private SignatureService signatureService;

    @Autowired
    private WxRefundService wxRefundService;

    @Autowired
    private RefundMiscRequestLogMapper refundMiscRequestLogMapper;

    @Override
    public RefundStatusEnum notifyRefund(RefundRequestPo refundRequest, Payment payment,
            HashMap<String, String> header) {

        InstitutionConfig config = configManager.getConfig(PayTypeEnum.parse(refundRequest.getPayType()));
        Date requestTime = new Date();

        WxRefundRequest wxRefundRequest = generateRequest(refundRequest, payment, config, null);// 组装退款请求
        try {
            WxRefundResponse response = wxRefundService.doService(wxRefundRequest, header);// 提交微信退款申请
            saveRefundMiscRequestLog(refundRequest, requestTime, wxRefundRequest, response, null);// 保存退款应答
            RefundStatusEnum refundStatus = generateRefundStatus(response, config);
            updateRefundRequestStatus(refundRequest, refundStatus);// 更新退款状态

            return refundStatus;
        } catch (Exception e) {
            logger.error("call WeiXin Refund fail. RefundBatchNo: " + refundRequest.getRefundBatchNo(), e);
            saveRefundMiscRequestLog(refundRequest, requestTime, wxRefundRequest, null, e);
            updateRefundRequestStatus(refundRequest, RefundStatusEnum.REFUND_FAILED);

            return RefundStatusEnum.REFUND_FAILED;
        }
    }

    private void saveRefundMiscRequestLog(RefundRequestPo refundRequest, Date requestTime,
            WxRefundRequest wxRefundRequest, WxRefundResponse response, Exception e) {

        RefundMiscRequestLogWithBLOBs requestLog = new RefundMiscRequestLogWithBLOBs();
        requestLog.setCorrelateId(String.valueOf(refundRequest.getRefundId()));
        requestLog.setMethod("WeiXinRefund");
        requestLog.setRequestData(wxRefundRequest.getRequestData());
        requestLog.setRequestTime(requestTime);
        requestLog.setResponseTime(new Date());
        if (e != null) {
            requestLog.setIsException(true);
            requestLog.setExceptionDetail(e.toString());
        }
        if (response != null) {
            requestLog.setResponseData(response.getOriginalResponse());
        }
        requestLog.setRefundBatchNo(refundRequest.getRefundBatchNo());
        refundMiscRequestLogMapper.insertSelective(requestLog);
    }

    private RefundStatusEnum generateRefundStatus(WxRefundResponse response, InstitutionConfig config) {
        if (!WeixinPayConstants.SUCCESS_FLAG.equals(response.getReturn_code())
                || !WeixinPayConstants.SUCCESS_FLAG.equals(response.getResult_code())
                || !config.getMerchantId().equalsIgnoreCase(response.getMch_id())) {

            /**
             * 以下报文表示用户已经和微信支付解绑，直接返回永久失败
             * <xml>
             * <return_code><![CDATA[SUCCESS]]></return_code>
             * <return_msg><![CDATA[OK]]></return_msg>
             * <appid><![CDATA[wxf51a439c0416f182]]></appid>
             * <mch_id><![CDATA[1234079001]]></mch_id>
             * <nonce_str><![CDATA[6Jfr9yz7AQeL3Tr3]]></nonce_str>
             * <sign><![CDATA[4292534ECC3F2D59C24691C57CF910AC]]></sign>
             * <result_code><![CDATA[FAIL]]></result_code>
             * <err_code><![CDATA[USER_ACCOUNT_ABNORMAL]]></err_code>
             * <err_code_des><![CDATA[用户账户异常或已注销]]></err_code_des>
             * </xml>
             * 
             */
            if ("USER_ACCOUNT_ABNORMAL".equals(response.getErr_code())) {
                return RefundStatusEnum.COMPLETE_FAILED_WX_USER_ABNORMAL;
            } else {
                return RefundStatusEnum.REFUND_FAILED;
            }

        } else {
            return RefundStatusEnum.COMMIT;
        }
    }

    private WxRefundRequest generateRequest(RefundRequestPo refundRequest, Payment payment,
            InstitutionConfig config, HashMap<String, String> header) {

        WxRefundRequest request = new WxRefundRequest();
        request.setAppid(config.getAppId());
        request.setMch_id(config.getMerchantId());
        request.setDevice_info("");
        request.setNonce_str(RandomStringUtils.randomAlphabetic(16));
        request.setOut_trade_no(refundRequest.getPaymentId());
        // request.setTransaction_id("");
        request.setOut_refund_no(refundRequest.getRefundBatchNo());
        request.setTotal_fee((int) payment.getActualPayPrice().getCent());
        request.setRefund_fee((int) new Money(refundRequest.getRefundAmount()).getCent());
        request.setRefund_fee_type("CNY");
        request.setOp_user_id(config.getMerchantId());

        String sign = signatureService.signMessage(request.mapForSign(), config, header);
        request.setSign(sign);

        return request;
    }

    private void updateRefundRequestStatus(RefundRequestPo refundRequest, RefundStatusEnum refundStatus) {
        RefundRequestPo record = new RefundRequestPo();
        record.setRefundStatus(refundStatus.getCode());
        record.setRefundTime(new Date());
        RefundRequestExample example = new RefundRequestExample();
        example.createCriteria().andRefundBatchNoEqualTo(refundRequest.getRefundBatchNo());

        refundRequestMapper.updateByExampleSelective(record, example);
    }

}
