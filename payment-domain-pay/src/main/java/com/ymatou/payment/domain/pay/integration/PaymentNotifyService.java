/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.pay.integration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.pay.PaymentConfig;
import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.infrastructure.security.MD5Util;
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
        } catch (Exception e) {
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
        request.setVersion(1);
        request.setBizCode(bussinessOrder.getBizCode().code().toString());
        request.setCurrency(payment.getPayCurrencyType());
        request.setMemo(bussinessOrder.getMemo());
        request.setTradingId(bussinessOrder.getOrderId());
        request.setPaymentId(payment.getPaymentId());
        request.setPayPrice(payment.getPayPrice().toString());
        request.setPayTime(payment.getPayTime() == null ? new Date() : payment.getPayTime());
        request.setSignMethod("MD5");
        request.setTraceId(UUID.randomUUID().toString());
        // request.setTraceId("43f231c6-caa5-4caf-86ed-3bf17f98121b");
        request.setInstPaymentId(payment.getInstitutionPaymentId());
        request.setInternalUserId(String.valueOf(bussinessOrder.getUserId()));
        String payerId = StringUtils.isBlank(payment.getPayerEmail()) ? payment.getPayerId() : payment.getPayerEmail();
        request.setExternalUserId(filterAliInternalUsers(payerId));
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
                return "Alipay";
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
        map.put("Version", req.getVersion().toString());
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
        map.put("PaymentId", req.getPaymentId());

        String sign = signNotify(map);

        return sign;
    }


    /**
     * 支付通知签名
     * 
     * @param notifyData
     * @return
     */
    public String signNotify(Map<String, String> notifyData) {
        String rawString = flatNotifyMessage(notifyData);

        try {
            String targetMessage = rawString + paymentConfig.getNotifySignSalt();
            String sign = MD5Util.encode(targetMessage).toUpperCase();

            return sign;
        } catch (Exception e) {
            throw new BizException(ErrorCode.FAIL, "notify md5 sign failed with rawString: " + rawString, e);
        }
    }

    /**
     * 将NotifyMesssage转成String
     * 
     * @param map
     * @param instConfig
     * @return
     */
    private String flatNotifyMessage(Map<String, String> map) {
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue() != "" && entry.getValue() != null
                    && !entry.getKey().equals("serialVersionUID")
                    && !entry.getKey().equals("Sign")) {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        Collections.sort(list, new Comparator<String>() {
            public int compare(String arg0, String arg1) {
                return arg0.compareToIgnoreCase(arg1);
            }
        });

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(list.get(i));
        }

        return sb.substring(0, sb.length() - 1);
    }
}
