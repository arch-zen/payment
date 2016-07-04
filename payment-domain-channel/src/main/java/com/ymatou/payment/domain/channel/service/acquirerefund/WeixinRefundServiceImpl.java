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

        WxRefundRequest wxRefundRequest = generateRequest(refundRequest, payment, config, null);
        try {
            WxRefundResponse response = wxRefundService.doService(wxRefundRequest, header);

            // save RefundMiscRequestLog
            RefundMiscRequestLogWithBLOBs requestLog = new RefundMiscRequestLogWithBLOBs();
            requestLog.setCorrelateId(refundRequest.getRefundBatchNo());
            requestLog.setMethod("WeixinRefund");
            requestLog.setRequestData(wxRefundRequest.getRequestData());
            requestLog.setResponseData(response.getOriginalResponse());
            requestLog.setRefundBatchNo(refundRequest.getRefundBatchNo());
            requestLog.setRequestTime(requestTime);
            requestLog.setResponseTime(new Date());
            refundMiscRequestLogMapper.insertSelective(requestLog);

            // update RefundRequest
            RefundStatusEnum refundStatus =
                    isSuccess(response, config) ? RefundStatusEnum.COMMIT : RefundStatusEnum.REFUND_FAILED;
            updateRefundRequestStatus(refundRequest, refundStatus);
            return refundStatus;
        } catch (Exception e) {
            logger.error("call WeiXin Refund fail. RefundBatchNo: " + refundRequest.getRefundBatchNo(), e);

            RefundMiscRequestLogWithBLOBs requestLog = new RefundMiscRequestLogWithBLOBs();
            requestLog.setCorrelateId(refundRequest.getPaymentId());
            requestLog.setMethod("WeiXinRefund");
            requestLog.setRequestData(wxRefundRequest.getRequestData());
            requestLog.setExceptionDetail(e.toString());
            requestLog.setRequestTime(requestTime);
            requestLog.setResponseTime(new Date());
            refundMiscRequestLogMapper.insertSelective(requestLog);

            updateRefundRequestStatus(refundRequest, RefundStatusEnum.REFUND_FAILED);
            return RefundStatusEnum.REFUND_FAILED;
        }
    }

    private boolean isSuccess(WxRefundResponse response, InstitutionConfig config) {
        if (!WeixinPayConstants.SUCCESS_FLAG.equals(response.getReturn_code())
                || !WeixinPayConstants.SUCCESS_FLAG.equals(response.getResult_code())
                || !config.getMerchantId().equalsIgnoreCase(response.getMch_id())) {
            return false;
        } else {
            return true;
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
