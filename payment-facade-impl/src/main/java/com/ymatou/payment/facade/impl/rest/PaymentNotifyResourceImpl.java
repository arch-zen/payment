/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.impl.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.PaymentNotifyFacade;
import com.ymatou.payment.facade.constants.PaymentNotifyType;
import com.ymatou.payment.facade.model.PaymentNotifyReq;
import com.ymatou.payment.facade.model.PaymentNotifyResp;

/**
 * 支付回调接口实现
 * 
 * @author wangxudong 2016年5月17日 下午8:20:36
 *
 */
@Component("paymentNotifyResource")
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class PaymentNotifyResourceImpl implements PaymentNotifyResource {

    private static Logger logger = LoggerFactory.getLogger(PaymentNotifyResourceImpl.class);

    /**
     * 支付接口
     */
    @Resource
    private PaymentNotifyFacade paymentNotifyFacade;

    @Override
    @GET
    @Path("{callback:(?i:callback)}/{payType}")
    public Response callback(@PathParam("payType") String payType, @Context HttpServletRequest servletRequest) {
        Response response;
        try {
            PaymentNotifyReq notifyReq = new PaymentNotifyReq();
            notifyReq.setPayType(payType);
            notifyReq.setNotifyType(PaymentNotifyType.Client);
            notifyReq.setRawString(servletRequest.getQueryString());
            notifyReq.setMockHeader(getMockHttpHeader(servletRequest));

            PaymentNotifyResp notifyResp = paymentNotifyFacade.notify(notifyReq);
            if (notifyResp.getIsSuccess()) {
                String url = notifyResp.getResult();
                response = Response.status(Status.FOUND).header("location", url).build();
            } else {
                logger.error("process callback failed with paytype: " + payType);
                response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            logger.error("process callback failed with paytype: " + payType, e);
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        return response;
    }

    @Override
    @POST
    @Path("{notify:(?i:notify)}/{payType}")
    public String notify(@PathParam("payType") String payType, @Context HttpServletRequest servletRequest) {
        try {
            PaymentNotifyReq notifyReq = new PaymentNotifyReq();
            notifyReq.setPayType(payType);
            notifyReq.setNotifyType(PaymentNotifyType.Server);
            notifyReq.setRawString(getHttpBody(servletRequest));
            notifyReq.setMockHeader(getMockHttpHeader(servletRequest));

            PaymentNotifyResp notifyResp = paymentNotifyFacade.notify(notifyReq);
            if (notifyResp.getIsSuccess()) {
                return notifyResp.getResult();
            } else {
                return "failed";
            }
        } catch (Exception e) {
            logger.error("process notify failed with paytype: " + payType, e);
            return "failed";
        }
    }

    /**
     * 获取请求中的HttpMockHeader
     * 
     * @param servletRequest
     * @return
     */
    private HashMap<String, String> getMockHttpHeader(HttpServletRequest servletRequest) {
        Enumeration<String> headerNames = servletRequest.getHeaderNames();
        HashMap<String, String> header = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerName = (String) headerNames.nextElement();
            if (headerName != null && headerName.startsWith("mock"))
                header.put(headerName, servletRequest.getHeader(headerName));
        }
        return header;
    }

    /**
     * 获取到HTTP Body
     * 
     * @param servletRequest
     * @return
     */
    private String getHttpBody(HttpServletRequest servletRequest) {
        try {
            servletRequest.setCharacterEncoding("UTF-8");
            int size = servletRequest.getContentLength();
            InputStream is = servletRequest.getInputStream();

            byte[] reqBodyBytes = readBytes(is, size);

            String res = new String(reqBodyBytes);

            return res;
        } catch (Exception e) {
            logger.error("parse http body failed when process alipay notify", e);
            throw new BizException(ErrorCode.FAIL, "parse http body failed", e);
        }
    }

    /**
     * 从InputStream获取到字节流
     * 
     * @param is
     * @param contentLen
     * @return
     */
    public final byte[] readBytes(InputStream is, int contentLen) {
        if (contentLen > 0) {
            int readLen = 0;
            int readLengthThisTime = 0;
            byte[] message = new byte[contentLen];

            try {
                while (readLen != contentLen) {
                    readLengthThisTime = is.read(message, readLen, contentLen - readLen);

                    if (readLengthThisTime == -1) {// Should not happen.
                        break;
                    }
                    readLen += readLengthThisTime;
                }
                return message;
            } catch (IOException e) {
                logger.error("parse http body when read bytes failed", e);
                throw new BizException(ErrorCode.FAIL, "parse http body where read bytes failed", e);
            }
        }
        return new byte[] {};
    }
}
