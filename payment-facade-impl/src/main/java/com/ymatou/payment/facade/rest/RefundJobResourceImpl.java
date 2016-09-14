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
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.facade.RefundJobFacade;
import com.ymatou.payment.facade.model.ExecuteRefundRequest;
import com.ymatou.payment.facade.model.ExecuteRefundResponse;
import com.ymatou.payment.facade.model.RefundSuccessNotifyReq;

/**
 * 
 * @author qianmin 2016年6月8日 上午10:42:24
 *
 */
@Path("/{api:(?i:api)}")
@Component("refundJobResource")
@Consumes({"application/json; charset=UTF-8"})
// @Produces({"text/html; charset=UTF-8"})
public class RefundJobResourceImpl implements RefundJobResource {

    private static Logger logger = LoggerFactory.getLogger(RefundJobResourceImpl.class);

    @Autowired
    private RefundJobFacade refundJobFacade;

    @POST
    @Path("/{Refund:(?i:Refund)}/{ExecuteRefund:(?i:ExecuteRefund)}")
    @Override
    public String executeRefund(ExecuteRefundRequest request, @Context HttpServletRequest servletRequest) {
        HashMap<String, String> header = generateHttpHeader(servletRequest);
        request.setHeader(header);

        ExecuteRefundResponse response = refundJobFacade.executeRefund(request);
        String refundResult = response.getRefundResult();
        if (refundResult == null) {
            return String.valueOf(response.getErrorCode());
        } else {
            return String.valueOf(response.getRefundResult());
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

    /*
     * (non-Javadoc)
     * 
     * @see com.ymatou.payment.facade.rest.RefundJobResource#refundSuccessNotify(com.ymatou.payment.
     * facade.model.RefundSuccessNotifyReq)
     */
    @POST
    @Path("/{Refund:(?i:Refund)}/{RefundSuccessNotify:(?i:RefundSuccessNotify)}")
    @Override
    public String refundSuccessNotify(RefundSuccessNotifyReq req) {
        logger.info("receive refund success notify from messagebus, req:{}", req);

        return "ok";
    }
}
