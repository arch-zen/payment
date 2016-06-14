/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.rest;

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

import com.ymatou.payment.facade.RefundJobFacade;
import com.ymatou.payment.facade.constants.RefundStatusEnum;

/**
 * 
 * @author qianmin 2016年6月8日 上午10:42:24
 *
 */
@Path("/api")
@Component("refundJobResource")
@Consumes({"application/json; charset=UTF-8"})
@Produces({"text/html; charset=UTF-8"})
public class RefundJobResourceImpl implements RefundJobResource {

    @Autowired
    private RefundJobFacade refundJobFacade;

    @POST
    @Path("/excuteRefund")
    @Override
    public String excuteRefund(String refundNo, @Context HttpServletRequest servletRequest) {
        HashMap<String, String> header = generateHttpHeader(servletRequest);

        int flag = refundJobFacade.excuteRefund(refundNo, header);

        if (flag == RefundStatusEnum.COMPLETE_SUCCESS.getCode()) {
            return "ok";
        } else {
            return String.valueOf(flag);
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
