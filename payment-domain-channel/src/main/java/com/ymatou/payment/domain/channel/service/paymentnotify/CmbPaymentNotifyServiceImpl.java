/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.paymentnotify;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ymatou.payment.domain.channel.model.PaymentNotifyMessage;
import com.ymatou.payment.domain.channel.service.PaymentNotifyService;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.constants.PaymentNotifyType;
import com.ymatou.payment.facade.model.PaymentNotifyReq;
import com.ymatou.payment.infrastructure.util.HttpUtil;
import com.ymatou.payment.integration.common.CmbSignature;
import com.ymatou.payment.integration.model.CmbPayNotifyRequest;
import com.ymatou.payment.integration.model.CmbPayNotifyRequest.PayNoticeData;

@Component
public class CmbPaymentNotifyServiceImpl implements PaymentNotifyService {

    @Override
    public PaymentNotifyMessage resloveNotifyMessage(PaymentNotifyReq notifyRequest) {
        // 解析报文
        Map<String, String> map = new HashMap<String, String>();
        try {
            map = HttpUtil.parseQueryStringToMap(notifyRequest.getRawString());;
        } catch (Exception e) {
            throw new BizException(ErrorCode.FAIL, "parse form data when receive cmb payment notify.", e);
        }

        CmbPayNotifyRequest cmbPayNotifyRequest = null;
        if (PaymentNotifyType.Server.equals(notifyRequest.getNotifyType())) {
            String reqData = map.get("jsonRequestData");
            cmbPayNotifyRequest = JSONObject.parseObject(reqData, CmbPayNotifyRequest.class);
        }

        // 验签
        boolean isSignValidate = CmbSignature.isValidSignature(cmbPayNotifyRequest.buildSignString(),
                cmbPayNotifyRequest.getSign(), "xxx");
        if (!isSignValidate) {
            throw new BizException(ErrorCode.SIGN_NOT_MATCH,
                    "paymentId:" + cmbPayNotifyRequest.getNoticeData().getOrderNo());
        }

        // 从报文中获取有效信息
        PayNoticeData noticeData = cmbPayNotifyRequest.getNoticeData();
        PaymentNotifyMessage paymentNotifyMessage = new PaymentNotifyMessage();
        paymentNotifyMessage.setTraceId(UUID.randomUUID().toString());
        paymentNotifyMessage.setPayerId("");
        paymentNotifyMessage.setPayerEmail("");
        paymentNotifyMessage.setActualPayCurrency("CNY");
        paymentNotifyMessage.setActualPayPrice(new BigDecimal(noticeData.getAmount()));
        paymentNotifyMessage.setInstitutionPaymentId(noticeData.getBankSerialNo());
        paymentNotifyMessage.setPaymentId(noticeData.getOrderNo());


        return paymentNotifyMessage;
    }

    @Override
    public String buildResponse(PaymentNotifyMessage notifyMessage, Payment payment, PaymentNotifyType notifyType) {
        return "success";
    }

}
