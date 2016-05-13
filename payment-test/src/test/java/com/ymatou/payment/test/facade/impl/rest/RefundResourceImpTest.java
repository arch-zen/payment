/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.facade.impl.rest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.ymatou.payment.facade.impl.rest.RefundResource;
import com.ymatou.payment.facade.model.AcquireRefundRequest;
import com.ymatou.payment.facade.model.AcquireRefundResponse;
import com.ymatou.payment.facade.model.FastRefundRequest;
import com.ymatou.payment.facade.model.FastRefundResponse;
import com.ymatou.payment.facade.model.TradeDetail;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 
 * @author qianmin 2016年5月11日 下午6:05:48
 * 
 */
public class RefundResourceImpTest extends RestBaseTest {

    @Autowired
    private RefundResource refundResource;

    // @Test
    public void testFastRefundSuccess() {
        FastRefundRequest fastRefundRequest = new FastRefundRequest();
        fastRefundRequest.setAppId("100001");
        fastRefundRequest.setPaymentId("20151107202902646000000000053774");
        fastRefundRequest.setTradingId("PP20151107300104764");
        fastRefundRequest.setTradeType(3);
        ArrayList<String> a = new ArrayList<String>();
        a.add("PP20151107300104764");
        fastRefundRequest.setOrderIdList(a);
        fastRefundRequest.setTraceId("1000001");

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        FastRefundResponse resp = refundResource.fastRefund(fastRefundRequest, servletRequest);

        Assert.assertEquals(0, resp.getErrorCode());
    }

    @Test
    public void testFastRefundInvalidArg() {
        FastRefundRequest fastRefundRequest = new FastRefundRequest();
        fastRefundRequest.setAppId("100001");
        fastRefundRequest.setPaymentId("20151107202902646000000000053774");
        fastRefundRequest.setTradingId("PP20151107300104764");
        fastRefundRequest.setTradeType(3);
        ArrayList<String> a = new ArrayList<String>();
        a.add("PP20151107300104764");
        fastRefundRequest.setOrderIdList(a);
        fastRefundRequest.setTraceId("1000001");

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        FastRefundResponse resp = refundResource.fastRefund(fastRefundRequest, servletRequest);

        Assert.assertEquals(1000, resp.getErrorCode());
    }


    @Test
    public void testSubmitRefundSuccess() {
        AcquireRefundRequest request = new AcquireRefundRequest();
        request.setAppId("11111111");
        request.setOrderId("32132143");
        request.setTraceId("12321321321");
        List<TradeDetail> tradeDetails = new ArrayList<>();
        TradeDetail tradeDetail = new TradeDetail();
        tradeDetail.setTradeNo("PP20151104300104729");
        tradeDetail.setTradeType(1);
        tradeDetails.add(tradeDetail);
        request.setTradeDetails(tradeDetails);

        AcquireRefundResponse response = refundResource.submitRefund(request, new MockHttpServletRequest());
        Assert.assertEquals(0, response.getErrorCode());
        Assert.assertEquals(1, response.getDetails().size());
        Assert.assertEquals("PP20151104300104729", response.getDetails().get(0).getTradeNo());
    }
}
