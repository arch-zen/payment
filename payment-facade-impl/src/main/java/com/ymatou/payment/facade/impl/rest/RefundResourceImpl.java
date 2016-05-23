/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.impl.rest;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.facade.RefundFacade;
import com.ymatou.payment.facade.model.AcquireRefundRequest;
import com.ymatou.payment.facade.model.AcquireRefundResponse;
import com.ymatou.payment.facade.model.ApproveRefundRequest;
import com.ymatou.payment.facade.model.ApproveRefundResponse;
import com.ymatou.payment.facade.model.FastRefundRequest;
import com.ymatou.payment.facade.model.FastRefundResponse;
import com.ymatou.payment.facade.model.QueryRefundRequest;
import com.ymatou.payment.facade.model.QueryRefundResponse;
import com.ymatou.payment.facade.model.TradeRefundableRequest;
import com.ymatou.payment.facade.model.TradeRefundableResponse;

/**
 * 退款REST实现
 * 
 * @author qianmin 2016年5月11日 上午10:52:35
 * 
 */
@Path("/api")
@Component("refundResource")
@Consumes({"application/json; charset=UTF-8"})
@Produces({"application/json; charset=UTF-8"})
public class RefundResourceImpl implements RefundResource {

    @Autowired
    private RefundFacade refundFacade;

    @POST
    @Path("/{Refund:(?i:Refund)}/{FastRefund:(?i:FastRefund)}")
    @Override
    public FastRefundResponse fastRefund(FastRefundRequest req, @Context HttpServletRequest servletRequest) {
        req.setHeader(generateHttpHeader(servletRequest));

        return refundFacade.fastRefund(req);
    }

    @POST
    @Path("/{Refund:(?i:Refund)}/{SubmitRefund:(?i:SubmitRefund)}")
    @Override
    public AcquireRefundResponse submitRefund(AcquireRefundRequest req, @Context HttpServletRequest servletRequest) {
        req.setHeader(generateHttpHeader(servletRequest));

        return refundFacade.submitRefund(req);
    }

    /**
     * 获取请求中的HttpHeader
     * 
     * @param servletRequest
     * @return
     */
    private HashMap<String, String> generateHttpHeader(HttpServletRequest servletRequest) {
        // generate http header
        Enumeration<String> headerNames = servletRequest.getHeaderNames();
        HashMap<String, String> header = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerName = (String) headerNames.nextElement();
            header.put(headerName, servletRequest.getHeader(headerName));
        }
        return header;
    }

    @POST
    @Path("/{Refund:(?i:Refund)}/{ApproveRefund:(?i:ApproveRefund)}")
    @Override
    public ApproveRefundResponse approveRefund(ApproveRefundRequest req, @Context HttpServletRequest servletRequest) {
        req.setHeader(generateHttpHeader(servletRequest));

        return refundFacade.approveRefund(req);
    }

    @POST
    @Path("/{Refund:(?i:Refund)}/{CheckRefundable:(?i:CheckRefundable)}")
    @Override
    public TradeRefundableResponse checkRefundable(TradeRefundableRequest req,
            @Context HttpServletRequest servletRequest) {

        return refundFacade.checkRefundable(req);
    }

    @POST
    @Path("/{Refund:(?i:Refund)}/{Query:(?i:Query)}")
    @Override
    public QueryRefundResponse query(QueryRefundRequest req, @Context HttpServletRequest servletRequest) {

        return refundFacade.query(req);
    }
}
