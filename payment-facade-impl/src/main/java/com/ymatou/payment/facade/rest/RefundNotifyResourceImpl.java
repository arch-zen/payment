/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.RefundNotifyFacade;
import com.ymatou.payment.facade.model.AliPayRefundNotifyRequest;
import com.ymatou.payment.infrastructure.util.HttpUtil;

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
    @Produces(MediaType.TEXT_HTML)
    public String refundNotify(@PathParam("payType") String payType,
            @Context HttpServletRequest servletRequest) {

        try {
            String body = getHttpBody(servletRequest);
            logger.info("receive alipay refund notify {}:{}", payType, body);

            AliPayRefundNotifyRequest req = parseFromBody(body, payType);
            req.setMockHeader(getMockHttpHeader(servletRequest));
            BaseResponse response = refundNotifyFacade.refundNotify(req);
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

    /**
     * 从Form中解析出报文
     * 
     * @param body
     * @return
     */
    private AliPayRefundNotifyRequest parseFromBody(String body, String payType) {
        Map<String, String> map = new HashMap<String, String>();
        AliPayRefundNotifyRequest notifyRequest = new AliPayRefundNotifyRequest();
        try {
            map = HttpUtil.parseQueryStringToMap(body);
            notifyRequest.setPayType(payType);
            notifyRequest.setBatchNo(map.get("batch_no"));
            notifyRequest.setNotifyId(map.get("notify_id"));
            notifyRequest.setNotifyTime(map.get("notify_time"));
            notifyRequest.setNotifyType(map.get("notify_type"));
            notifyRequest.setResultDetails(map.get("result_details"));
            notifyRequest.setSign(map.get("sign"));
            notifyRequest.setSignType(map.get("sign_type"));
            notifyRequest.setSuccessNum(map.get("success_num"));

            return notifyRequest;
        } catch (Exception e) {
            throw new BizException(ErrorCode.FAIL, "parse post form when receive alipay refund notify.", e);
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
