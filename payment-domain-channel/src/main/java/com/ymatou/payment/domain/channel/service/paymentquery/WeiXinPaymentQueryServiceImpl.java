/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.paymentquery;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.constants.WeixinPayConstants;
import com.ymatou.payment.domain.channel.model.PaymentQueryResp;
import com.ymatou.payment.domain.channel.service.PaymentQueryService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.integration.model.QueryOrderRequest;
import com.ymatou.payment.integration.model.QueryOrderResponse;
import com.ymatou.payment.integration.service.wxpay.OrderQueryService;

/**
 * 
 * @author qianmin 2016年5月19日 下午4:54:25
 *
 */
@Component
public class WeiXinPaymentQueryServiceImpl implements PaymentQueryService {

    private static final Logger logger = LoggerFactory.getLogger(WeiXinPaymentQueryServiceImpl.class);

    @Autowired
    private OrderQueryService orderQueryService;

    @Autowired
    private SignatureService signatureService;

    @Autowired
    private InstitutionConfigManager institutionConfigManager;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public PaymentQueryResp queryPayment(String paymentId, String payType, HashMap<String, String> header) {
        QueryOrderRequest orderQueryRequest = generateRequest(paymentId, payType, header);

        try {
            // 调用微信支付查询订单接口
            QueryOrderResponse response = orderQueryService.doService(orderQueryRequest, header);

            PaymentQueryResp resp = new PaymentQueryResp();
            if (QueryOrderResponse.SUCCESS.equals(response.getResult_code())
                    && QueryOrderResponse.SUCCESS.equals(response.getReturn_code())
                    && (QueryOrderResponse.SUCCESS.equals(response.getTrade_state())
                            || QueryOrderResponse.REFUND.equals(response.getTrade_state()))) {
                resp = generateResponse(response);
                resp.setPayStatus(PayStatusEnum.Paied);
                return resp;
            } else if (QueryOrderResponse.UNKONW.equals(response.getErr_code())) {
                resp.setPayStatus(PayStatusEnum.UNKNOW);
                return resp;
            } else {
                resp.setPayStatus(PayStatusEnum.Failed);
                return resp;
            }

        } catch (Exception e) {
            logger.error("call weixin order query failed", e);
            throw new BizException(ErrorCode.SERVER_SIDE_ACQUIRE_ORDER_FAILED,
                    "Paymentid: " + paymentId, e);
        }
    }

    private PaymentQueryResp generateResponse(QueryOrderResponse response) throws ParseException {
        PaymentQueryResp resp = new PaymentQueryResp();
        resp.setActualPayCurrency("CNY");
        resp.setActualPayPrice(new BigDecimal(response.getTotal_fee() / 100d)); // 微信支付结果单位为分
        resp.setBankId(response.getBank_type());
        resp.setCardType(getCardType(response.getBank_type()));
        resp.setInstitutionPaymentId(response.getTransaction_id());
        resp.setOriginMessage(response.getResponseOriginString());
        resp.setPayerId(response.getOpenid());
        resp.setPaymentId(response.getOut_trade_no());
        resp.setPayStatus(response.isValid() ? PayStatusEnum.Paied : PayStatusEnum.Failed);
        resp.setPayTime(sdf.parse(response.getTime_end()));
        resp.setTraceId(response.getNonce_str());

        return resp;
    }

    private QueryOrderRequest generateRequest(String paymentId, String payType, HashMap<String, String> header) {
        // 根据payType获取appId,mchId信息
        InstitutionConfig institutionConfig = institutionConfigManager.getConfig(PayTypeEnum.parse(payType));
        String appId = institutionConfig.getAppId();
        String mchId = institutionConfig.getMerchantId();

        // 组装request
        QueryOrderRequest orderQueryRequest = new QueryOrderRequest();
        orderQueryRequest.setAppid(appId);
        orderQueryRequest.setMch_id(mchId);
        orderQueryRequest.setNonce_str(RandomStringUtils.randomAlphabetic(32));
        orderQueryRequest.setOut_trade_no(paymentId);
        orderQueryRequest.setTransaction_id("");
        @SuppressWarnings("unchecked")
        String sign = signatureService.signMessage(new ObjectMapper().convertValue(orderQueryRequest, HashMap.class),
                institutionConfig, header); // 加签
        orderQueryRequest.setSign(sign);

        logger.info("order query request: {}", JSON.toJSONString(orderQueryRequest));
        return orderQueryRequest;
    }

    private Integer getCardType(String weixinBankType) {
        if (StringUtils.isBlank(weixinBankType)) {
            return null;
        }
        if (weixinBankType.indexOf("DEBIT") > 0) {
            return WeixinPayConstants.DEBIT_CARD;
        }
        if (weixinBankType.indexOf("CREDIT") > 0) {
            return WeixinPayConstants.CREDIT_CARD;
        }
        return null;
    }
}
