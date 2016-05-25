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

import com.ymatou.payment.facade.BaseResponse;
import com.ymatou.payment.facade.CheckPaymentFacade;
import com.ymatou.payment.facade.model.CheckPaymentRequset;

/**
 * 
 * @author qianmin 2016年5月19日 下午4:16:36
 *
 */
@Path("/{api:(?i:api)}")
@Component("checkPaymentResource")
@Consumes({"application/json; charset=UTF-8"})
@Produces({"text/html; charset=UTF-8"})
public class CheckPaymentResourceImpl implements CheckPaymentResource {

    @Autowired
    private CheckPaymentFacade checkPaymentFacade;

    @POST
    @Path("/{CheckPayment:(?i:CheckPayment)}")
    @Override
    public String checkPayment(CheckPaymentRequset req, @Context HttpServletRequest servletRequest) {
        req.setHeader(generateHttpHeader(servletRequest));

        BaseResponse response = checkPaymentFacade.checkPayment(req);
        if (response.getIsSuccess()) {
            return "ok";
        } else {
            return "failed";
        }
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

}
