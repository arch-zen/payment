/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.paymentquery;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.constants.PayStatus;
import com.ymatou.payment.domain.channel.constants.WeixinPayConstants;
import com.ymatou.payment.domain.channel.model.PaymentQueryResp;
import com.ymatou.payment.domain.channel.service.PaymentQueryService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.model.CheckPaymentRequset;
import com.ymatou.payment.integration.model.OrderQueryRequest;
import com.ymatou.payment.integration.model.OrderQueryResponse;
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
    public PaymentQueryResp paymentQuery(CheckPaymentRequset request) {
        OrderQueryRequest orderQueryRequest = generateRequest(request);

        try {
            // 调用微信支付查询订单接口
            OrderQueryResponse response = orderQueryService.doService(orderQueryRequest, request.getHeader());

            if (OrderQueryResponse.SUCCESS.equals(response.getResult_code())
                    && OrderQueryResponse.SUCCESS.equals(response.getReturn_code())) {
                PaymentQueryResp resp = generateResponse(response);
                return resp;
            } else {
                throw new BizException(ErrorCode.SERVER_SIDE_ACQUIRE_ORDER_FAILED,
                        "Paymentid:" + request.getPaymentId());
            }

        } catch (Exception e) {
            logger.error("call weixin order query failed", e);
            throw new BizException(ErrorCode.SERVER_SIDE_ACQUIRE_ORDER_FAILED,
                    "Paymentid: " + request.getPaymentId());
        }
    }

    private PaymentQueryResp generateResponse(OrderQueryResponse response) throws ParseException {
        PaymentQueryResp resp = new PaymentQueryResp();
        resp.setActualPayCurrency("CNY");
        resp.setActualPayPrice(new BigDecimal(response.getTotal_fee() / 100d)); // 微信支付结果单位为分
        resp.setBankId(response.getBank_type());
        resp.setCardType(getCardType(response.getBank_type()));
        resp.setInstitutionPaymentId(response.getTransaction_id());
        resp.setOriginMessage(response.getResponseOriginString());
        resp.setPayerId(response.getOpenid());
        resp.setPaymentId(response.getOut_trade_no());
        resp.setPayStatus(response.isValid() ? PayStatus.Paied : PayStatus.Failed);
        resp.setPayTime(sdf.parse(response.getTime_end()));
        resp.setTraceId(response.getNonce_str());

        return resp;
    }

    private OrderQueryRequest generateRequest(CheckPaymentRequset req) {
        // 根据payType获取appId,mchId信息
        InstitutionConfig institutionConfig = institutionConfigManager.getConfig(req.getPayType());
        String appId = institutionConfig.getAppId();
        String mchId = institutionConfig.getMerchantId();

        // 组装request
        OrderQueryRequest orderQueryRequest = new OrderQueryRequest();
        orderQueryRequest.setAppid(appId);
        orderQueryRequest.setMch_id(mchId);
        orderQueryRequest.setNonce_str(UUID.randomUUID().toString());
        orderQueryRequest.setOut_trade_no(req.getPaymentId());
        orderQueryRequest.setTransaction_id("");
        @SuppressWarnings("unchecked")
        String sign = signatureService.signMessage(new ObjectMapper().convertValue(orderQueryRequest, HashMap.class),
                institutionConfig, req.getHeader()); // 加签
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
