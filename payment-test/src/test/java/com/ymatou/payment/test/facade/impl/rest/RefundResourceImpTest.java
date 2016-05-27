/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.facade.impl.rest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.ymatou.payment.facade.impl.rest.RefundResource;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireRefundRequest;
import com.ymatou.payment.facade.model.AcquireRefundResponse;
import com.ymatou.payment.facade.model.ApproveRefundRequest;
import com.ymatou.payment.facade.model.ApproveRefundResponse;
import com.ymatou.payment.facade.model.FastRefundRequest;
import com.ymatou.payment.facade.model.FastRefundResponse;
import com.ymatou.payment.facade.model.QueryRefundRequest;
import com.ymatou.payment.facade.model.QueryRefundResponse;
import com.ymatou.payment.facade.model.TradeDetail;
import com.ymatou.payment.facade.model.TradeRefundableRequest;
import com.ymatou.payment.facade.model.TradeRefundableResponse;
import com.ymatou.payment.test.RestBaseTest;

/**
 * test
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
        fastRefundRequest.setTradeType(1);
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
        tradeDetail.setTradeNo("4070149626832712");
        tradeDetail.setTradeType(1);
        tradeDetails.add(tradeDetail);
        request.setTradeDetails(tradeDetails);

        AcquireRefundResponse response = refundResource.submitRefund(request, new MockHttpServletRequest());
        Assert.assertEquals(0, response.getErrorCode());
        Assert.assertEquals(1, response.getDetails().size());
        Assert.assertEquals("4070149626832712", response.getDetails().get(0).getTradeNo());
    }

    @Test
    public void testCheckRefundable() {
        TradeRefundableRequest req = new TradeRefundableRequest();
        req.setTradeNos(Arrays
                .asList(new String[] {"20150915133207757", "8178254", "19335806615459", "8526937", "114581437722686",
                        "20160503194109806", "114659944449425", "4070149626832712", "8198025", "4092297615140378"}));
        TradeRefundableResponse response = refundResource.checkRefundable(req, new MockHttpServletRequest());
        Assert.assertEquals(2, response.getDetails().size());
    }

    // @Test
    public void testQueryRefund() {
        QueryRefundRequest req = new QueryRefundRequest();
        req.setKey("");
        req.setPageSize(10);
        req.setPageIndex(5); // 41之后的，
        req.setApproveStatus(9);

        QueryRefundResponse resp = refundResource.query(req, new MockHttpServletRequest());

        Assert.assertEquals("21568176200183545", resp.getDetails().get(0).getPaymentId());
    }

    @Test
    public void testQueryRefundWithOrderId() {
        QueryRefundRequest req = new QueryRefundRequest();
        req.setKey("");
        req.setPageSize(10);
        req.setPageIndex(1);
        req.setApproveStatus(9);
        req.setKey("127822744");

        QueryRefundResponse resp = refundResource.query(req, new MockHttpServletRequest());

        Assert.assertEquals(1, resp.getDetails().size());
        Assert.assertEquals("127822744", resp.getDetails().get(0).getOrderId());
    }

    @Test
    public void testApproveRefund() {
        ApproveRefundRequest req = new ApproveRefundRequest();
        req.setApproveUser("testApproveRefund");
        req.setPaymentIds(Arrays.asList(new String[] {"20151104150448396000000000027244"}));

        ApproveRefundResponse response = refundResource.approveRefund(req, new MockHttpServletRequest());
        Assert.assertEquals(true, response.getDetails().getOkFlag());

    }

    /**
     * 构造请求报文
     * 
     * @param req
     */
    private void buildBaseRequest(AcquireOrderReq req) {
        req.setVersion(1);
        req.setBizCode(3);
        req.setOriginAppId("JavaTest");
        req.setAppId("AutoTest");
        req.setCallbackUrl("http://www.ymatou.com/pay/result");
        req.setTraceId(UUID.randomUUID().toString());
        req.setCurrency("CNY");
        req.setEncoding(65001);
        req.setNotifyUrl("http://api.trading.operate.ymatou.com/api/Trading/TradingCompletedNotify");
        req.setOrderId(getDateFormatString("yyyyMMddHHmmssSSS"));
        req.setOrderTime(getDateFormatString("yyyyMMddHHmmss"));
        req.setPayPrice("0.01");
        req.setPayType("10");
        req.setProductName("测试商品");
        req.setProductDesc("商品描述");
        req.setProductUrl("www.ymatou.com");
        req.setMemo("备注");
        req.setSignMethod("MD5");
        req.setExt("{\"SHOWMODE\":\"2\",\"PAYMETHOD\":\"2\", \"IsHangZhou\":0}");
        req.setUserId(12345);
        req.setUserIp("127.0.0.1");
        req.setBankId("CMB");
    }

    /**
     * 构造日期字符串
     * 
     * @return
     */
    private String getDateFormatString(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(new Date());
    }
}
