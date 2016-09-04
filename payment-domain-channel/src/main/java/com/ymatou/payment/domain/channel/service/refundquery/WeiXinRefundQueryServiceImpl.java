/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.refundquery;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

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
        Date requestTime = new Date();
        RefundStatusEnum refundStatus = null;

        QueryRefundRequest queryRefundRequest = generateRequest(refundRequest, payment, config, header);// 组装退款请求
        try {
            QueryRefundResponse response = wxRefundQueryService.doService(queryRefundRequest, header);// 提交微信退款查询
            saveRefundMiscRequestLog(refundRequest, requestTime, queryRefundRequest, response, null);// 保存退款查询应答
            refundStatus = calcRefundStatus(refundRequest, response);// 计算退款状态
        } catch (Exception e) {
            logger.error("weixin refund query error, refundBatchNo:" + refundRequest.getRefundBatchNo(), e);

            saveRefundMiscRequestLog(refundRequest, requestTime, queryRefundRequest, null, e);
            refundStatus = RefundStatusEnum.REFUND_FAILED;
        }
        logger.info("weixin refund query end. RefundStatus:{}", refundStatus);
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
        /*
         * 以下报文表示查询微信失败，此时直接返回PO中退款单的状态，不做更改
         * <xml>
         * <return_code><![CDATA[FAIL]]></return_code>
         * <return_msg><![CDATA[System Error]]></return_msg>
         * </xml>
         */
        if ("FAIL".equals(response.getReturn_code()) && "System Error".equals(response.getReturn_msg())) {
            return RefundStatusEnum.withCode(refundRequest.getRefundStatus().intValue());
        }

        /*
         * 以下报文表示查询微信失败，此时直接返回PO中退款单的状态，不做更改
         * <xml>
         * <appid> <![CDATA[wxf51a439c0416f182]]> </appid>
         * <err_code> <![CDATA[SYSTEMERROR]]> </err_code>
         * <err_code_des> <![CDATA[SystemError]]> </err_code_des>
         * <mch_id> <![CDATA[1234079001]]> </mch_id>
         * <nonce_str> <![CDATA[jYUgGjUiheAmPHpd]]> </nonce_str>
         * <result_code> <![CDATA[FAIL]]> </result_code>
         * <return_code> <![CDATA[SUCCESS]]> </return_code>
         * <return_msg> <![CDATA[OK]]> </return_msg>
         * <sign> <![CDATA[814C5FE997BF52000F162CB0885F802B]]> </sign>
         * </xml>
         */
        if ("SYSTEMERROR".equals(response.getErr_code())) {
            return RefundStatusEnum.withCode(refundRequest.getRefundStatus().intValue());
        }

        if ("FAIL".equals(response.getResult_code()) && "REFUNDNOTEXIST".equals(response.getErr_code())) {
            return RefundStatusEnum.INIT;
        }

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
            logger.error("{}, weixin refund query not find refundbatchNo:{}", response.getErr_code(),
                    refundRequest.getRefundBatchNo());
            return RefundStatusEnum.COMPLETE_FAILED;
        }

        // 从退款单列表找出对应的退款批次号
        Optional<RefundOrderData> refundOrderData = response.getRefundOrderDataList().stream()
                .filter(refund -> refund.getOutRefundNo().equals(refundRequest.getRefundBatchNo()))
                .findFirst();
        if (refundOrderData.isPresent() == false) {
            logger.error("weixin refund query not find refundbatchNo:" + refundRequest.getRefundBatchNo());
            return RefundStatusEnum.COMPLETE_FAILED;
        }

        // 校验退款信息
        RefundOrderData refundDetail = refundOrderData.get();
        if (!refundRequest.getPaymentId().equals(response.getOut_trade_no())
                || !refundRequest.getRefundBatchNo().equals(refundDetail.getOutRefundNo())
                || new Money(refundRequest.getRefundAmount()).getCent() != refundDetail.getRefundFee()
                || StringUtils.isBlank(refundDetail.getRefundStatus())) {
            logger.error(
                    "weixin refund query find complete failed with refundbatchNo:" + refundRequest.getRefundBatchNo());
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
        requestLog.setCorrelateId(String.valueOf(refundRequest.getRefundId()));
        requestLog.setMethod("WeixinRefundQuery");
        requestLog.setRequestData(queryRefundRequest.getRequestData());
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
