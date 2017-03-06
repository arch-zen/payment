/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.domain.channel.service.paymentquery;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.payment.domain.channel.model.PaymentQueryResp;
import com.ymatou.payment.domain.channel.service.paymentquery.AliPayPaymentQueryServiceImpl;
import com.ymatou.payment.facade.model.CheckPaymentRequset;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 
 * @author qianmin 2016年5月23日 上午11:09:58
 *
 */
public class AliPayPaymentQueryServiceImplTest extends RestBaseTest {

    @Autowired
    private AliPayPaymentQueryServiceImpl service;

    @Test
    public void testPaymentQuery() {
        CheckPaymentRequset requset = new CheckPaymentRequset();
        requset.setFinalCheck(false);
        requset.setHeader(null);
        requset.setPaymentId("16112719404074649");
        requset.setPayType("13");
        requset.setRequestId("");
        PaymentQueryResp response = service.queryPayment(requset.getPaymentId(), requset.getPayType(), null);
        Assert.assertEquals(new BigDecimal("48.00"), response.getActualPayPrice());
    }

    @Test
    public void testPaymentQueryMD5() {
        CheckPaymentRequset requset = new CheckPaymentRequset();
        requset.setFinalCheck(false);
        requset.setHeader(null);
        requset.setPaymentId("21936067900872864");
        requset.setPayType("10");
        requset.setRequestId("");
        PaymentQueryResp response = service.queryPayment(requset.getPaymentId(), requset.getPayType(), null);

        Assert.assertEquals(new BigDecimal("409.00"), response.getActualPayPrice());
    }
}
