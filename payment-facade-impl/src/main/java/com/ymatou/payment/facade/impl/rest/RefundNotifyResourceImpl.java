/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.impl.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.facade.BaseResponse;
import com.ymatou.payment.facade.RefundNotifyFacade;
import com.ymatou.payment.facade.model.AliPayRefundNotifyRequest;

/**
 * 
 * @author qianmin 2016年5月16日 下午5:10:16
 * 
 */

@Path("/{RefundNotify:(?i:RefundNotify)}")
@Component("refundNotifyResource")
public class RefundNotifyResourceImpl implements RefundNotifyResource {

    private static final Logger logger = LoggerFactory.getLogger(RefundNotifyResourceImpl.class);

    @Autowired
    private RefundNotifyFacade refundNotifyFacade;

    @POST
    @Path("/{payType}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_HTML)
    public String refundNotify(AliPayRefundNotifyRequest req, @PathParam("payType") String payType,
            @Context HttpServletRequest servletRequest) {

        try {
            BaseResponse response = refundNotifyFacade.refundNotify(req, payType);
            if (response.getIsSuccess()) {
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "fail";
        }
    }
}
