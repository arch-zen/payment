/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 * 退款定时任务
 * 
 * @author qianmin 2016年6月8日 上午10:41:09
 *
 */
public interface RefundJobResource {

    /**
     * 定时任务，根据RefundNo来处理退款申请单
     * 
     * @param refundNo
     * @param servletRequest
     */
    public String executeRefund(String refundNo, @Context HttpServletRequest servletRequest);
}
