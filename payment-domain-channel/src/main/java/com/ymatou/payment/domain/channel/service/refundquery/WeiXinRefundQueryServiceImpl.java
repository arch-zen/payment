/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.refundquery;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.service.RefundQueryService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.infrastructure.Money;
import com.ymatou.payment.infrastructure.db.mapper.RefundMiscRequestLogMapper;
import com.ymatou.payment.infrastructure.db.model.RefundMiscRequestLogWithBLOBs;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;
import com.ymatou.payment.integration.model.QueryRefundRequest;
import com.ymatou.payment.integration.model.QueryRefundResponse;
import com.ymatou.payment.integration.model.RefundOrderData;
import com.ymatou.payment.integration.service.wxpay.WxRefundQueryService;

/**
 * 微信支付退款查询
 * 
 * @author qianmin 2016年6月8日 下午3:35:11
 *
 */
@Component
public class WeiXinRefundQueryServiceImpl implements RefundQueryService {

    private static final Logger logger = LoggerFactory.getLogger(WeiXinRefundQueryServiceImpl.class);

    @Autowired
    private WxRefundQueryService wxRefundQueryService;

    @Autowired
    private InstitutionConfigManager configManager;

    @Autowired
    private SignatureService signatureService;

    @Autowired
    private RefundMiscRequestLogMapper refundMiscRequestLogMapper;

    @Override
    public RefundStatusEnum queryRefund(RefundRequestPo refundRequest, Payment payment,
            HashMap<String, String> header) {

        logger.info("weixin refund query begin. PaymentId:{}, RefundNo:{}", refundRequest.getPaymentId(),
                refundRequest.getRefundBatchNo());

        InstitutionConfig config = configManager.getConfig(payment.getPayType());
        QueryRefundRequest queryRefundRequest = generateRequest(refundRequest, payment, config, header);
        Date requestTime = new Date();
        RefundStatusEnum refundStatus = null;

        try {
            QueryRefundResponse response = wxRefundQueryService.doService(queryRefundRequest, header);

            saveRefundMiscRequestLog(refundRequest, requestTime, queryRefundRequest, response, null);
            refundStatus = calcRefundStatus(refundRequest, response);
        } catch (Exception e) {
            logger.error("weixin refund query error.", e);

            saveRefundMiscRequestLog(refundRequest, requestTime, queryRefundRequest, null, e);
            refundStatus = RefundStatusEnum.REFUND_FAILED;
        }

        logger.info("weixin refund query end. RefundStatus:{}", refundRequest.getRefundStatus());
        return refundStatus;
    }

    /**
     * 根据查询结果计算退款状态
     * 
     * @param refundRequest
     * @param response
     * @return
     */
    private RefundStatusEnum calcRefundStatus(RefundRequestPo refundRequest, QueryRefundResponse response) {

        if ("FAIL".equals(response.getResult_code()) && "REFUNDNOTEXIST".equals(response.getErr_code())) {
            return RefundStatusEnum.INIT;
        }

        RefundOrderData refundDetail = response.getRefundOrderDataList().get(0);
        if (!refundRequest.getPaymentId().equals(response.getOut_trade_no())
                || !refundRequest.getRefundBatchNo().equals(refundDetail.getOutRefundNo())
                || new Money(refundRequest.getRefundAmount()).getCent() != refundDetail.getRefundFee()
                || StringUtils.isBlank(refundDetail.getRefundStatus())) {
            return RefundStatusEnum.COMPLETE_FAILED;
        }

        switch (refundDetail.getRefundStatus()) {
            case "SUCCESS":
                return RefundStatusEnum.THIRDPART_REFUND_SUCCESS;
            case "CHANGE":
                return RefundStatusEnum.COMPLETE_FAILED;
            case "PROCESSING":
                return RefundStatusEnum.WAIT_THIRDPART_REFUND;
            case "NOTSURE":
                return RefundStatusEnum.INIT;
            default:
                return RefundStatusEnum.REFUND_FAILED;
        }
    }

    /**
     * 记录查询结果
     * 
     * @param refundRequest
     * @param requestTime
     * @param queryRefundRequest
     * @param response
     * @param e
     */
    private void saveRefundMiscRequestLog(RefundRequestPo refundRequest, Date requestTime,
            QueryRefundRequest queryRefundRequest, QueryRefundResponse response, Exception e) {

        RefundMiscRequestLogWithBLOBs requestLog = new RefundMiscRequestLogWithBLOBs();
        requestLog.setCorrelateId(refundRequest.getPaymentId());
        requestLog.setMethod("WeiXinRefund");
        requestLog.setRequestData(queryRefundRequest.getRequestData());
        requestLog.setRequestTime(requestTime);
        requestLog.setResponseTime(new Date());
        if (e != null) {
            requestLog.setExceptionDetail(e.toString());
        }
        if (response != null) {
            requestLog.setResponseData(response.getOriginalResponse());
        }
        // OriginResult TODO
        refundMiscRequestLogMapper.insertSelective(requestLog);
    }

    /**
     * 组装微信退款查询Request
     * 
     * @param refundRefund
     * @param payment
     * @param config
     * @param header
     * @return
     */
    private QueryRefundRequest generateRequest(RefundRequestPo refundRequest, Payment payment, InstitutionConfig config,
            HashMap<String, String> header) {

        QueryRefundRequest request = new QueryRefundRequest();
        request.setAppid(config.getAppId());
        request.setMch_id(config.getMerchantId());
        request.setNonce_str(RandomStringUtils.randomAlphabetic(16));
        // transaction_id, out_trade_no,out_refund_no, refund_id 四选一
        request.setOut_refund_no(refundRequest.getRefundBatchNo());
        String sign = signatureService.signMessage(request.mapForSign(), config, header);
        request.setSign(sign);

        return request;
    }
}
