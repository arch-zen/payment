/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.pay.integration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.domain.pay.PaymentConfig;
import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.integration.model.PaymentCallbackRequest;
import com.ymatou.payment.integration.service.ymatou.PaymentCallbackService;


/**
 * 支付成功通知服务
 * 
 * @author wangxudong 2016年6月24日 上午11:53:11
 *
 */
@Component
public class PaymentNotifyService {

    private static Logger logger = LoggerFactory.getLogger(PaymentNotifyService.class);

    @Resource
    PaymentCallbackService paymentCallbackService;

    @Resource
    PaymentConfig paymentConfig;

    @Resource
    SignatureService signatureService;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");


    /**
     * 通知业务系统支付成功
     * 
     * @param payment
     * @param bussinessOrder
     */
    public boolean notifyBizSystem(Payment payment, BussinessOrder bussinessOrder, HashMap<String, String> mockHeader) {
        PaymentCallbackRequest request = buildRequest(payment, bussinessOrder);

        try {
            return paymentCallbackService.doService(bussinessOrder.getNotifyUrl(), request, mockHeader);
        } catch (IOException e) {
            logger.error("pay notify biz system failed:" + payment.getPaymentId(), e);
            return false;
        }
    }


    /**
     * 构建支付通知请求
     * 
     * @param payment
     * @param bussinessOrder
     * @return
     */
    private PaymentCallbackRequest buildRequest(Payment payment, BussinessOrder bussinessOrder) {
        PaymentCallbackRequest request = new PaymentCallbackRequest();
        request.setAppId("2");
        request.setBizCode(bussinessOrder.getBizCode().code().toString());
        request.setCurrency(payment.getPayCurrencyType());
        request.setMemo(bussinessOrder.getMemo());
        request.setTradingId(bussinessOrder.getOrderId());
        request.setPaymentId(payment.getPaymentId());
        request.setPayPrice(payment.getPayPrice().toString());
        request.setPayTime(payment.getPayTime() == null ? new Date() : payment.getPayTime());
        request.setSignMethod("MD5");
        request.setTraceId(UUID.randomUUID().toString());
        request.setInstPaymentId(payment.getInstitutionPaymentId());
        request.setInternalUserId(String.valueOf(bussinessOrder.getUserId()));
        request.setExternalUserId(filterAliInternalUsers(payment.getPayerId()));
        request.setPayChannel(convertPayChannel(payment.getPayType()));
        request.setPayType(bussinessOrder.getPayType());
        request.setSign(sign(request));


        return request;
    }

    /**
     * PayType转PayChannel
     * 
     * @param payTypeEnum
     * @return
     */
    private String convertPayChannel(PayTypeEnum payTypeEnum) {
        switch (payTypeEnum) {
            case AliPayApp:
            case AliPayPc:
            case AliPayWap:
                return "AliPay";
            case WeiXinApp:
            case WeiXinJSAPI:
                return "Weixin";
            default:
                return payTypeEnum.toString();
        }
    }

    /**
     * 过滤支付宝内部账号
     * 所有的匿名支付都会使用这些账号，为避免风控将这些账号替换成null
     * 
     * @param externalUserId
     * @return
     */
    private String filterAliInternalUsers(String externalUserId) {
        if (StringUtils.isEmpty(externalUserId) || paymentConfig.getAliInternalBuyerId().contains(externalUserId)) {
            return null;
        } else {
            return externalUserId;
        }
    }

    /**
     * 获取到支付通知签名
     * 
     * @param req
     * @return
     */
    private String sign(PaymentCallbackRequest req) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("AppId", req.getAppId());
        map.put("BizCode", req.getBizCode());
        map.put("Currency", req.getCurrency());
        map.put("Memo", req.getMemo());
        map.put("TradingId", req.getTradingId());
        map.put("PayPrice", req.getPayPrice());
        map.put("PayTime", req.getPayTime());
        map.put("SignMethod", req.getSignMethod());
        map.put("TraceId", req.getTraceId());
        map.put("InstPaymentId", req.getInstPaymentId());
        map.put("InternalUserId", req.getInternalUserId());
        map.put("ExternalUserId", req.getExternalUserId());
        map.put("PayChannel", req.getPayChannel());
        map.put("PayType", req.getPayType());

        String sign = signatureService.signNotify(map);

        return sign;
    }
}
