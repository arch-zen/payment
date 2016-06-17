/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.refundquery;

import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.constants.AliPayConsts;
import com.ymatou.payment.domain.channel.service.RefundQueryService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.infrastructure.db.mapper.RefundMiscRequestLogMapper;
import com.ymatou.payment.infrastructure.db.model.RefundMiscRequestLogWithBLOBs;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;
import com.ymatou.payment.integration.model.AliPayRefundQueryRequest;
import com.ymatou.payment.integration.model.AliPayRefundQueryResponse;
import com.ymatou.payment.integration.model.AliPayRefundQueryResponse.RefundDetailData;
import com.ymatou.payment.integration.service.alipay.AliPayRefundQueryService;

/**
 * 支付宝退款查询
 * 
 * @author qianmin 2016年6月8日 下午3:34:38
 *
 */
@Component
public class AliPayRefundQueryServiceImpl implements RefundQueryService {

    private static final Logger logger = LoggerFactory.getLogger(AliPayRefundQueryServiceImpl.class);

    @Autowired
    private AliPayRefundQueryService aliPayRefundQueryService;

    @Autowired
    private InstitutionConfigManager configManager;

    @Autowired
    private SignatureService signatureService;

    @Autowired
    private RefundMiscRequestLogMapper refundMiscRequestLogMapper;

    @Override
    public RefundStatusEnum queryRefund(RefundRequestPo refundRequest, Payment payment,
            HashMap<String, String> header) {
        logger.info("AliPay refund query begin. PaymentId:{}, RefundNo:{}", refundRequest.getPaymentId(),
                refundRequest.getRefundBatchNo());

        InstitutionConfig config = configManager.getConfig(payment.getPayType());
        AliPayRefundQueryRequest request = generateRequest(refundRequest, payment, config, header);
        Date requestTime = new Date();
        RefundStatusEnum refundStatus = null;

        try {
            AliPayRefundQueryResponse response = aliPayRefundQueryService.doService(request, header);
            saveRefundMiscRequestLog(refundRequest, requestTime, request, response, null);
            refundStatus = calcRefundStatus(refundRequest, payment, response);
        } catch (Exception e) {
            logger.error("AliPay refund query error.", e);

            saveRefundMiscRequestLog(refundRequest, requestTime, request, null, e);
            refundStatus = RefundStatusEnum.REFUND_FAILED;
        }

        logger.info("AliPay refund query end. RefundStatus:{}", refundRequest.getRefundStatus());
        return refundStatus;
    }

    private RefundStatusEnum calcRefundStatus(RefundRequestPo refundRequest, Payment payment,
            AliPayRefundQueryResponse response) {
        RefundStatusEnum refundStatus = null;
        if (AliPayConsts.REFUND_QUERY_STATU_T.equals(response.getIsSuccess())) { // 查询成功
            RefundDetailData resultDetailData = response.resolveResultDetails();
            if (resultDetailData == null) { // 无退款明细
                refundStatus = RefundStatusEnum.COMPLETE_FAILED;
            } else {
                if (!refundRequest.getInstPaymentId().equalsIgnoreCase(resultDetailData.getInstPaymentId())
                        || resultDetailData.getRefundAmount().compareTo(refundRequest.getRefundAmount()) != 0) {
                    refundStatus = RefundStatusEnum.COMPLETE_FAILED;
                } else {
                    refundStatus = resultDetailData.getRefundStatus();
                }
            }
        } else {
            if (!"REFUND_NOT_EXIST".equals(response.getErrorCode())) {
                refundStatus = RefundStatusEnum.INIT;
            } else {
                refundStatus = RefundStatusEnum.REFUND_FAILED;
            }
        }

        return refundStatus;
    }

    // private RefundStatusEnum calcRefundStatus(RefundRequestPo refundRequest, Payment payment,
    // AliPayRefundQueryResponse response) {
    // RefundStatusEnum refundStatus = null;
    // if (AliPayConsts.REFUND_QUERY_STATU_T.equals(response.getIsSuccess())) { // 查询成功
    // RefundDetailData resultDetailData = response.resolveResultDetails();
    // if (resultDetailData == null) { // 无退款明细
    // refundStatus = RefundStatusEnum.COMPLETE_FAILED;
    // } else if (resultDetailData.isRefundOk() != null) {
    // if (!refundRequest.getInstPaymentId().equalsIgnoreCase(resultDetailData.getInstPaymentId()))
    // {
    // refundStatus = RefundStatusEnum.COMPLETE_FAILED;
    // } else if (resultDetailData.isRefundOk()) {
    // if (resultDetailData.getRefundAmount().equals(refundRequest.getRefundAmount())) {
    // refundStatus = RefundStatusEnum.COMPLETE_FAILED;
    // }
    // refundStatus = RefundStatusEnum.THIRDPART_REFUND_SUCCESS;
    // } else {
    // refundStatus = RefundStatusEnum.REFUND_FAILED;
    // }
    // } else {
    // switch (resultDetailData.getChargeRefundStatus()) {
    // case FAILED:
    // case NA:
    // refundStatus = RefundStatusEnum.COMPLETE_FAILED;
    // break;
    // case SUCCESS:
    // refundStatus = RefundStatusEnum.THIRDPART_REFUND_SUCCESS;
    // break;
    // case PROCESSING:
    // default:
    // refundStatus = RefundStatusEnum.WAIT_THIRDPART_REFUND;
    // break;
    // }
    // }
    // } else {
    // if (!"REFUND_NOT_EXIST".equals(response.getErrorCode())) {
    // refundStatus = RefundStatusEnum.INIT;
    // } else {
    // refundStatus = RefundStatusEnum.REFUND_FAILED;
    // }
    // }
    //
    // return refundStatus;
    // }

    private void saveRefundMiscRequestLog(RefundRequestPo refundRequest, Date requestTime,
            AliPayRefundQueryRequest queryRefundRequest, AliPayRefundQueryResponse response, Exception e) {

        RefundMiscRequestLogWithBLOBs requestLog = new RefundMiscRequestLogWithBLOBs();
        requestLog.setCorrelateId(refundRequest.getRefundBatchNo());
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
    private AliPayRefundQueryRequest generateRequest(RefundRequestPo refundRequest, Payment payment,
            InstitutionConfig config, HashMap<String, String> header) {

        AliPayRefundQueryRequest request = new AliPayRefundQueryRequest();
        request.setPartner(config.getMerchantId());
        request.setSignType(config.getSignType());
        request.setBatchNo(refundRequest.getRefundBatchNo());
        String sign = signatureService.signMessage(request.mapForSign(), config, header);
        request.setSign(sign);
        return request;
    }

}
