/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.rest;

import javax.servlet.http.HttpServletRequest;

import com.ymatou.payment.facade.model.AliPayRefundNotifyRequest;

/**
 * 接收回调
 * 
 * @author qianmin 2016年5月16日 下午5:09:35
 * 
 */
public interface RefundNotifyResource {

    /**
     * 处理支付宝退款回调
     * 
     * @param req
     * @param payType
     * @param servletRequest
     * @return
     */
    public String refundNotify(String payType, HttpServletRequest servletRequest);
}
