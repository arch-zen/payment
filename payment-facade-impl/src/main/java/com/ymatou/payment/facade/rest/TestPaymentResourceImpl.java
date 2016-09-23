/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.rest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.stereotype.Component;

import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireOrderResp;

/**
 * 
 * @author qianmin 2016年6月17日 下午4:29:58
 *
 */
@Component("testPaymentResource")
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class TestPaymentResourceImpl implements TestPaymentResource {


    @Resource
    private PaymentResource paymentResource;

    @Override
    @GET
    @Path("/alipc")
    public String alipc(@Context HttpServletRequest servletRequest) {
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);

        req.setPayType("10");
        req.setPayPrice("0.02");

        AcquireOrderResp res = paymentResource.acquireOrder(req, servletRequest);

        return res.getResult();
    }

    @Override
    @GET
    @Path("/aliwap")
    public Response aliwap(@Context HttpServletRequest servletRequest) {
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);

        req.setPayType("11");
        req.setPayPrice("0.02");

        AcquireOrderResp res = paymentResource.acquireOrder(req, servletRequest);

        return Response.status(Status.FOUND).header("location", res.getResult()).build();
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
        req.setUserId(12345L);
        req.setUserIp("127.0.0.1");
        req.setBankId("ICBCB2C");
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
