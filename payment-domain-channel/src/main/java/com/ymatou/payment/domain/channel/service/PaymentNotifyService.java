/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.service;

import com.ymatou.payment.domain.channel.model.PaymentNotifyMessage;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.model.PaymentNotifyRequest;
import com.ymatou.payment.facade.model.PaymentNotifyType;

/**
 * 支付回调通知服务
 * 
 * @author wangxudong 2016年5月19日 下午2:11:02
 *
 */
public interface PaymentNotifyService {
    /**
     * 解析并验证回调报文
     * 
     * @param notifyRequest
     * @return
     */
    PaymentNotifyMessage resloveNotifyMessage(PaymentNotifyRequest notifyRequest);


    /**
     * 构建第三方需要的请求报文
     * 
     * @param notifyMessage
     * @param payment
     * @param notifyType
     * @return
     */
    String buildResponse(PaymentNotifyMessage notifyMessage, Payment payment, PaymentNotifyType notifyType);
}
