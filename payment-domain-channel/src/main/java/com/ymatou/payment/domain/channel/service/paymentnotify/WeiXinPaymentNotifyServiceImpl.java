/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.paymentnotify;

import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.model.PaymentNotifyMessage;
import com.ymatou.payment.domain.channel.service.PaymentNotifyService;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.model.PaymentNotifyRequest;
import com.ymatou.payment.facade.model.PaymentNotifyType;

/**
 * 微信 APP 回调报文解析器 (13)
 * 
 * @author wangxudong 2016年5月19日 下午2:16:06
 *
 */
@Component
public class WeiXinPaymentNotifyServiceImpl implements PaymentNotifyService {

    /*
     * (non-Javadoc)
     * 
     * @see com.ymatou.payment.domain.channel.service.PaymentNotifyService#ResloveNotifyMessage(com.
     * ymatou.payment.facade.model.PaymentNotifyRequest)
     */
    @Override
    public PaymentNotifyMessage resloveNotifyMessage(PaymentNotifyRequest notifyRequest) {
        PaymentNotifyMessage paymentNotifyMessage = new PaymentNotifyMessage();
        paymentNotifyMessage.setActualPayCurrency("CNY");
        return paymentNotifyMessage;
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.ymatou.payment.domain.channel.service.PaymentNotifyService#buildResponse(com.ymatou.
     * payment.domain.channel.model.PaymentNotifyMessage,
     * com.ymatou.payment.domain.pay.model.Payment,
     * com.ymatou.payment.facade.model.PaymentNotifyType)
     */
    @Override
    public String buildResponse(PaymentNotifyMessage notifyMessage, Payment payment, PaymentNotifyType notifyType) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        sb.append("<return_code><![CDATA[SUCCESS]]></return_code>");
        sb.append("<return_msg><![CDATA[OK]]></return_msg>");
        sb.append("</xml>");

        return sb.toString();
    }

}
