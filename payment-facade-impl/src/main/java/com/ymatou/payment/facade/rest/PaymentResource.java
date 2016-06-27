package com.ymatou.payment.facade.rest;


import javax.servlet.http.HttpServletRequest;

import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireOrderResp;
import com.ymatou.payment.facade.model.ExecutePayNotifyReq;

/**
 * 支付REST接口
 * 
 * @author wangxudong
 *
 */
public interface PaymentResource {
    /**
     * 支付收单
     * 
     * @param req
     * @param servletRequest
     * @return
     */
    AcquireOrderResp acquireOrder(AcquireOrderReq req, HttpServletRequest servletRequest);

    /**
     * 执行支付通知
     * 
     * @param req
     * @param servletRequest
     * @return
     */
    String executePayNotify(ExecutePayNotifyReq req, HttpServletRequest servletRequest);
}
