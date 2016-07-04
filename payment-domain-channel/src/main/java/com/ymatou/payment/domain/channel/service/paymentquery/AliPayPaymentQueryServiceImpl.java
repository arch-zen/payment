/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.paymentquery;

import java.math.BigDecimal;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.constants.AliPayConsts;
import com.ymatou.payment.domain.channel.model.PaymentQueryResp;
import com.ymatou.payment.domain.channel.service.PaymentQueryService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.integration.model.QuerySingleTradeRequest;
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

    @Override
    public PaymentQueryResp queryPayment(String paymentId, String payType,
            HashMap<String, String> header) {
        QuerySingleTradeRequest checkPaymentRequset = generateRequest(paymentId, payType, header);

        try {
            // 调用支付宝单笔交易查询接口
            SingleTradeQueryResponse response =
                    singleTradeQueryService.doService(checkPaymentRequset, header);

            if (SingleTradeQueryResponse.SUCCESS.equals(response.getIs_success())) {
                PaymentQueryResp resp = generateResponse(response);
                return resp;
            } else if (SingleTradeQueryResponse.TRADE_NOT_EXIST.equals(response.getError())) {
                PaymentQueryResp resp = new PaymentQueryResp();
                resp.setPayStatus(PayStatusEnum.Failed);
                return resp;
            } else {
                PaymentQueryResp resp = new PaymentQueryResp();
                resp.setPayStatus(PayStatusEnum.UNKNOW);
                return resp;
            }

        } catch (Exception e) {
            logger.warn("CheckPayment, call alipay single trade query failed. PaymentId:" + paymentId, e);
            throw new BizException(ErrorCode.SERVER_SIDE_ACQUIRE_ORDER_FAILED,
                    "Paymentid: " + paymentId, e);
        }
    }

    private QuerySingleTradeRequest generateRequest(String paymentId, String payType,
            HashMap<String, String> header) {
        InstitutionConfig institutionConfig = institutionConfigManager.getConfig(PayTypeEnum.parse(payType));

        QuerySingleTradeRequest request = new QuerySingleTradeRequest();
        request.setService("single_trade_query");
        request.setPartner(institutionConfig.getMerchantId());
        request.setSign_type(institutionConfig.getSignType());
        request.setOut_trade_no(paymentId);
        request.setTrade_no("");
        logger.info(institutionConfig.getInstPublicKey());
        @SuppressWarnings("unchecked")
        String sign = signatureService.signMessage(new ObjectMapper().convertValue(request, HashMap.class),
                institutionConfig, header);
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
        PayStatusEnum payStatus = (AliPayConsts.ALI_TRADE_OK_STATUS.contains(resp.getTrade_status()) ||
                AliPayConsts.TRADE_REFUND_SUCCESS.equalsIgnoreCase(resp.getRefund_status()))
                        ? PayStatusEnum.Paied : PayStatusEnum.Failed;
        response.setPayStatus(payStatus);
        response.setPayTime(resp.getGmt_payment());

        return response;
    }
}
