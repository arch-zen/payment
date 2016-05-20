/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.facade.impl.rest;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.ymatou.payment.facade.impl.rest.PaymentNotifyResource;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 支付回调测试
 * 
 * @author wangxudong 2016年5月19日 上午11:54:27
 *
 */
public class PaymentNotifyResourceTest extends RestBaseTest {

    @Resource
    private PaymentNotifyResource paymentNotifyResource;

    @Test
    public void testAliPayPCNotify() throws UnsupportedEncodingException {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/notify/10");

        String reqBody =
                "discount=0.00&payment_type=1&subject=yeyingcao%E7%9A%84%E8%AE%A2%E5%8D%95&trade_no=2016051921001004250269230618&buyer_email=yeyingcao88%40163.com&gmt_create=2016-05-19+12%3A56%3A41&notify_type=trade_status_sync&quantity=1&out_trade_no=21916168000593994&seller_id=2088701734809577&notify_time=2016-05-19+12%3A56%3A47&trade_status=TRADE_SUCCESS&is_total_fee_adjust=N&total_fee=387.00&gmt_payment=2016-05-19+12%3A56%3A47&seller_email=ap.ymt%40ymatou.com&price=387.00&buyer_id=2088002495313254&notify_id=39a92c986d7097b332939697525f793hxi&use_coupon=N&sign_type=MD5&sign=c40ed3acbe82c45cfabf70b0157e6ccf";

        servletRequest.addHeader("Mock", "1");
        servletRequest.setContent(reqBody.getBytes("utf-8"));

        String response = paymentNotifyResource.notify("10", servletRequest);

        assertEquals("验证返回值", "success", response);
    }
}
