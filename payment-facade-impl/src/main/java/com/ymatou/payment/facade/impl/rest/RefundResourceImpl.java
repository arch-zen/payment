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
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.facade.RefundFacade;
import com.ymatou.payment.facade.model.FastRefundRequest;
import com.ymatou.payment.facade.model.FastRefundResponse;

/**
 * 退款REST实现
 * 
 * @author qianmin 2016年5月11日 上午10:52:35
 * 
 */
@Path("/api")
@Component("refundResource")
public class RefundResourceImpl implements RefundResource {

    @Autowired
    private RefundFacade refundFacade;

    @POST
    @Path("/Refund/FastRefund")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public FastRefundResponse fastRefund(FastRefundRequest req, @Context HttpServletRequest servletRequest) {
        // generate http header
        Enumeration<String> headerNames = servletRequest.getHeaderNames();
        HashMap<String, String> header = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerName = (String) headerNames.nextElement();
            header.put(headerName, servletRequest.getHeader(headerName));
        }
        req.setHeader(header);

        return refundFacade.fastRefund(req);
    }

}
