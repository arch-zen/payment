/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.domain.channel.service.paymentnotify;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.Test;

import com.ymatou.payment.domain.channel.service.PaymentNotifyService;
import com.ymatou.payment.domain.channel.service.paymentnotify.AliPayAppPaymentNotifyServiceImpl;
import com.ymatou.payment.domain.channel.service.paymentnotify.AliPayPcPaymentNotifyServiceImpl;
import com.ymatou.payment.domain.channel.service.paymentnotify.PaymentNotifyMessageResolverFactory;
import com.ymatou.payment.domain.channel.service.paymentnotify.WeiXinAppPaymentNotifyServiceImpl;
import com.ymatou.payment.domain.channel.service.paymentnotify.WeiXinJSAPIPaymentNotifyServiceImpl;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 支付回调工厂
 * 
 * @author wangxudong 2016年5月20日 下午2:30:47
 *
 */
public class PaymentNotifyMessageResolverFactoryTest extends RestBaseTest {

    @Resource
    PaymentNotifyMessageResolverFactory paymentNotifyMessageResolverFactory;

    @Test
    public void testGetInstance() {
        PaymentNotifyService paymentNotifyService1 = paymentNotifyMessageResolverFactory.getInstance("10");
        assertEquals("10", AliPayPcPaymentNotifyServiceImpl.class, paymentNotifyService1.getClass());

        PaymentNotifyService paymentNotifyService2 = paymentNotifyMessageResolverFactory.getInstance("13");
        assertEquals("13", AliPayAppPaymentNotifyServiceImpl.class, paymentNotifyService2.getClass());

        PaymentNotifyService paymentNotifyService3 = paymentNotifyMessageResolverFactory.getInstance("14");
        assertEquals("14", WeiXinJSAPIPaymentNotifyServiceImpl.class, paymentNotifyService3.getClass());

        PaymentNotifyService paymentNotifyService4 = paymentNotifyMessageResolverFactory.getInstance("15");
        assertEquals("15", WeiXinAppPaymentNotifyServiceImpl.class, paymentNotifyService4.getClass());
    }

    @Test(expected = BizException.class)
    public void testGetInstanceFailedWhenPaytypeIsNull() {
        paymentNotifyMessageResolverFactory.getInstance(null);
    }

    @Test(expected = BizException.class)
    public void testGetInstanceFailedWhenPaytypeIsInvalidate() {
        paymentNotifyMessageResolverFactory.getInstance("99");
    }
}
