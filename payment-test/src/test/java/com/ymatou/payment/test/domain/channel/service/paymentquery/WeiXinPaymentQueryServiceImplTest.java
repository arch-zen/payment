/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.domain.channel.service.paymentquery;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.payment.domain.channel.model.PaymentQueryResp;
import com.ymatou.payment.domain.channel.service.paymentquery.WeiXinPaymentQueryServiceImpl;
import com.ymatou.payment.facade.model.CheckPaymentRequset;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 
 * @author qianmin 2016年5月23日 上午11:10:13
 *
 */
public class WeiXinPaymentQueryServiceImplTest extends RestBaseTest {

    @Autowired
    private WeiXinPaymentQueryServiceImpl service;

    @Test
    public void testPaymentQuery() {
        CheckPaymentRequset request = new CheckPaymentRequset();
        request.setFinalCheck(true);
        request.setHeader(null);
        request.setPaymentId("21935472000015038");
        request.setPayType("15");
        request.setRequestId("4234823654239504397990");
        PaymentQueryResp resp = service.paymentQuery(request);

        Assert.assertEquals(new BigDecimal(29), resp.getActualPayPrice());
    }

}
