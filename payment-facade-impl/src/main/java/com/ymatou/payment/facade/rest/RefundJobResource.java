/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.ymatou.payment.facade.model.ExecuteRefundRequest;
import com.ymatou.payment.facade.model.RefundSuccessNotifyReq;

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
    public String executeRefund(ExecuteRefundRequest request, @Context HttpServletRequest servletRequest);

    /**
     * 接收总线推送过来的退款消息（用于测试总线的推送请款，不做业务处理直接返回OK）
     * 
     * @param req
     * @return
     */
    public String refundSuccessNotify(RefundSuccessNotifyReq req);
}
