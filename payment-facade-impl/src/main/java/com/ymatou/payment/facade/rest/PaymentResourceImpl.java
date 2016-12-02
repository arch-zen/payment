package com.ymatou.payment.facade.rest;

import java.util.Enumeration;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.PaymentFacade;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireOrderResp;
import com.ymatou.payment.facade.model.ExecutePayNotifyReq;
import com.ymatou.payment.facade.model.ExecutePayNotifyResp;
import com.ymatou.payment.facade.model.SyncCmbPublicKeyReq;
import com.ymatou.payment.facade.model.SyncCmbPublicKeyResp;

/**
 * 支付REST接口实现
 * 
 * @author wangxudong
 *
 */
@Path("/{api:(?i:api)}")
@Component("paymentResource")
@Produces({"application/json; charset=UTF-8"})
@Consumes({MediaType.APPLICATION_JSON})
public class PaymentResourceImpl implements PaymentResource {

    private static Logger logger = LoggerFactory.getLogger(PaymentResourceImpl.class);
    /**
     * 支付接口
     */
    @Resource
    private PaymentFacade paymentFacade;

    /**
     * 支付收单
     */
    @Override
    @POST
    @Path("/{AcquireOrder:(?i:AcquireOrder)}")
    public AcquireOrderResp acquireOrder(AcquireOrderReq req, @Context HttpServletRequest servletRequest) {
        req.setMockHeader(getMockHttpHeader(servletRequest));

        String rawString = JSON.toJSONString(req);
        logger.info("receive acquire order request: {}", rawString);

        AcquireOrderResp resp = paymentFacade.acquireOrder(req);
        resp.setAppId(req.getAppId());
        resp.setTraceId(req.getTraceId());

        return resp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ymatou.payment.facade.rest.PaymentResource#executePayNotify(com.ymatou.payment.facade.
     * model.ExecutePayNotifyReq, javax.servlet.http.HttpServletRequest)
     */
    @Override
    @POST
    @Path("/{ExecutePayNotify:(?i:ExecutePayNotify)}")
    @Produces({"text/xml"})
    public String executePayNotify(ExecutePayNotifyReq req, @Context HttpServletRequest servletRequest) {
        req.setMockHeader(getMockHttpHeader(servletRequest));
        ExecutePayNotifyResp resp = paymentFacade.executePayNotify(req);
        if (resp.getErrorCode() == ErrorCode.PAYMENT_NOTIFY_ACCOUNTING_FAILED.getCode()
                || resp.getErrorCode() == ErrorCode.PAYMENT_NOTIFY_INNER_SYSTEM_FAILED.getCode()) {
            return "failed-" + resp.getErrorCode() + "|" + resp.getErrorMessage();
        }

        return "ok";
    }

    @Override
    @POST
    @Path("/{SyncCmbPublicKey:(?i:SyncCmbPublicKey)}")
    @Produces({"text/xml"})
    public String syncCmbPublicKeyReq(SyncCmbPublicKeyReq req, @Context HttpServletRequest servletRequest) {
        req.setMockHeader(getMockHttpHeader(servletRequest));
        SyncCmbPublicKeyResp resp = paymentFacade.syncCmbPublicKey(req);
        if (resp.getIsSuccess()) {
            return "ok";
        } else {
            return "failed-" + resp.getErrorCode() + "|" + resp.getErrorMessage();
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
            if (headerName != null && headerName.toLowerCase().startsWith("mock"))
                header.put(headerName, servletRequest.getHeader(headerName));
        }
        return header;
    }

}
