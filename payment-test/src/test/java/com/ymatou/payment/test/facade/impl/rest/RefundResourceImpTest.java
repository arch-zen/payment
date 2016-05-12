/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.facade.impl.rest;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ymatou.payment.facade.impl.rest.RefundResource;
import com.ymatou.payment.facade.model.FastRefundRequest;
import com.ymatou.payment.facade.model.FastRefundResponse;

/**
 * 
 * @author qianmin 2016年5月11日 下午6:05:48
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class RefundResourceImpTest {

    @Autowired
    private RefundResource refundResource;

    @Test
    public void testSuccess() {
        FastRefundRequest fastRefundRequest = new FastRefundRequest();
        fastRefundRequest.setAppId("100001");
        fastRefundRequest.setPaymentId("20151107202902646000000000053774");
        fastRefundRequest.setTradingId("PP20151107300104764");
        fastRefundRequest.setTradeType(1);
        ArrayList<String> a = new ArrayList<String>();
        a.add("PP20151107300104764");
        fastRefundRequest.setOrderIdList(a);
        fastRefundRequest.setTraceId("1000001");

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        FastRefundResponse resp = refundResource.fastRefund(fastRefundRequest, servletRequest);

        Assert.assertEquals(0, resp.getErrorCode());
    }
}
