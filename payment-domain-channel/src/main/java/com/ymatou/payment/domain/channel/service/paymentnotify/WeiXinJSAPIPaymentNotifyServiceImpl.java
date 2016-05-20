/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.paymentnotify;

import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.model.PaymentNotifyMessage;
import com.ymatou.payment.domain.channel.service.PaymentNotifyService;
import com.ymatou.payment.facade.model.PaymentNotifyRequest;

/**
 * 微信 JSAPI 回调报文解析器
 * 
 * @author wangxudong 2016年5月19日 下午2:16:06
 *
 */
@Component
public class WeiXinJSAPIPaymentNotifyServiceImpl implements PaymentNotifyService {

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
     * @see
     * com.ymatou.payment.domain.channel.service.PaymentNotifyService#BuildInstNeedResponse(com.
     * ymatou.payment.domain.channel.model.PaymentNotifyMessage)
     */
    @Override
    public String buildInstNeedResponse(PaymentNotifyMessage notifyMessage) {
        return "success";
    }

}
