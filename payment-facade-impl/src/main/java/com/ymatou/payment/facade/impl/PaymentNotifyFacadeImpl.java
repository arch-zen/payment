/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.impl;

import org.springframework.stereotype.Component;

import com.ymatou.payment.facade.PaymentNotifyFacade;
import com.ymatou.payment.facade.model.PaymentNotifyRequest;

/**
 * 支付回调接口
 * 
 * @author wangxudong 2016年5月17日 下午8:19:16
 *
 */
@Component("paymentNotifyFacade")
public class PaymentNotifyFacadeImpl implements PaymentNotifyFacade {

    @Override
    public String callback(PaymentNotifyRequest req) {
        return null;
    }

    @Override
    public String notify(PaymentNotifyRequest req) {
        return null;
    }
}
