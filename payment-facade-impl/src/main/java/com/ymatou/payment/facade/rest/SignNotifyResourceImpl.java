/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.SignNotifyFacade;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.model.SignNotifyReq;
import com.ymatou.payment.facade.model.SignNotifyResp;

/**
 * 签约通知
 * 
 * @author wangxudong 2016年11月18日 下午8:22:46
 *
 */
@Component("signNotifyResource")
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class SignNotifyResourceImpl implements SignNotifyResource {

    private static Logger logger = LoggerFactory.getLogger(SignNotifyResourceImpl.class);

    @Resource
    private SignNotifyFacade signNotifyFacade;

    @Override
    @POST
    @Path("{cmbSignNotify:(?i:cmbSignNotify)}")
    public Response cmbSignNotify(@Context HttpServletRequest servletRequest) {
        try {
            SignNotifyReq notifyReq = new SignNotifyReq();
            notifyReq.setPayType(PayTypeEnum.CmbApp.getCode()); // 招行一网通渠道号
            notifyReq.setRawString(getHttpBody(servletRequest));
            notifyReq.setMockHeader(getMockHttpHeader(servletRequest));

            SignNotifyResp notifyResp = signNotifyFacade.signNotify(notifyReq);
            if (notifyResp.getIsSuccess()) {
                return Response.ok("success", "text/plain").build();
            } else {
                return Response.serverError().build();
            }
        } catch (Exception e) {
            logger.error("process sign notify failed with paytype:20", e);
            return Response.serverError().build();
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
