/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.paymentquery;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.constants.AliPayConsts;
import com.ymatou.payment.domain.channel.constants.PayStatus;
import com.ymatou.payment.domain.channel.model.PaymentQueryResp;
import com.ymatou.payment.domain.channel.service.PaymentQueryService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.model.CheckPaymentRequset;
import com.ymatou.payment.integration.model.SingleTradeQueryRequest;
import com.ymatou.payment.integration.model.SingleTradeQueryResponse;
import com.ymatou.payment.integration.service.alipay.SingleTradeQueryService;

/**
 * 
 * @author qianmin 2016年5月19日 下午4:54:46
 *
 */
@Component
public class AliPayPaymentQueryServiceImpl implements PaymentQueryService {

    private static final Logger logger = LoggerFactory.getLogger(AliPayPaymentQueryServiceImpl.class);

    @Autowired
    private SingleTradeQueryService singleTradeQueryService;

    @Autowired
    private InstitutionConfigManager institutionConfigManager;

    @Autowired
    private SignatureService signatureService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public PaymentQueryResp paymentQuery(CheckPaymentRequset requset) {
        SingleTradeQueryRequest checkPaymentRequset = generateRequest(requset);

        try {
            // 调用支付宝单笔交易查询接口
            SingleTradeQueryResponse response =
                    singleTradeQueryService.doService(checkPaymentRequset, requset.getHeader());

            if (SingleTradeQueryResponse.SUCCESS.equals(response.getIs_success())) {
                PaymentQueryResp resp = generateResponse(response);
                return resp;
            } else {
                if ("TRADE_IS_NOT_EXIST".equalsIgnoreCase(response.getError())) {
                    PaymentQueryResp resp = new PaymentQueryResp();
                    resp.setPayStatus(PayStatus.Failed);
                }
                logger.error(
                        "error response from aplipay when check payment status on paymentId {}, error message {}",
                        requset.getPaymentId(), response.getError());
                throw new BizException(ErrorCode.SERVER_SIDE_ACQUIRE_ORDER_FAILED,
                        "Paymentid:" + requset.getPaymentId());
            }

        } catch (Exception e) {
            logger.error("call alipay single trade query failed", e);
            throw new BizException(ErrorCode.SERVER_SIDE_ACQUIRE_ORDER_FAILED,
                    "Paymentid: " + requset.getPaymentId());
        }
    }

    private SingleTradeQueryRequest generateRequest(CheckPaymentRequset req) {
        InstitutionConfig institutionConfig = institutionConfigManager.getConfig(req.getPayType());

        SingleTradeQueryRequest request = new SingleTradeQueryRequest();
        request.setService("single_trade_query");
        request.setPartner(institutionConfig.getMerchantId());
        request.setSign_type(institutionConfig.getSignType());
        request.setOut_trade_no(req.getPaymentId());
        request.setTrade_no("");
        logger.info(institutionConfig.getInstPublicKey());
        String sign = signatureService.signMessage(new ObjectMapper().convertValue(request, HashMap.class),
                institutionConfig, req.getHeader());
        request.setSign(sign);

        return request;
    }

    private PaymentQueryResp generateResponse(SingleTradeQueryResponse resp) {
        PaymentQueryResp response = new PaymentQueryResp();
        response.setActualPayCurrency("CNY");
        response.setActualPayPrice(new BigDecimal(resp.getTotal_fee()));
        response.setInstitutionPaymentId(resp.getTrade_no());
        response.setOriginMessage(resp.getResponseOriginString());
        response.setPayerId(resp.getBuyer_email());
        response.setPaymentId(resp.getOut_trade_no());
        PayStatus payStatus = (AliPayConsts.ALI_TRADE_OK_STATUS.contains(resp.getTrade_status()) ||
                AliPayConsts.TRADE_REFUND_SUCCESS.equalsIgnoreCase(resp.getRefund_status()))
                        ? PayStatus.Paied : PayStatus.Failed;
        response.setPayStatus(payStatus);
        response.setPayTime(resp.getGmt_payment());

        return response;
    }
}
