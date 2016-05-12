/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.impl.rest;

import javax.servlet.http.HttpServletRequest;

import com.ymatou.payment.facade.model.AcquireRefundRequest;
import com.ymatou.payment.facade.model.AcquireRefundResponse;
import com.ymatou.payment.facade.model.FastRefundRequest;
import com.ymatou.payment.facade.model.FastRefundResponse;

/**
 * 退款REST接口
 * 
 * @author qianmin 2016年5月11日 上午10:52:20
 * 
 */
public interface RefundResource {

    /**
     * 快速退款
     * 
     * @param req
     * @param servletRequest
     * @return
     */
    FastRefundResponse fastRefund(FastRefundRequest req, HttpServletRequest servletRequest);

    /**
     * 退款收单
     * 
     * @param req
     * @param servletRequest
     * @return
     */
    AcquireRefundResponse submitRefund(AcquireRefundRequest req, HttpServletRequest servletRequest);
}
