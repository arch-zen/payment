/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.rest;

import javax.servlet.http.HttpServletRequest;

import com.ymatou.payment.facade.model.AcquireRefundRequest;
import com.ymatou.payment.facade.model.AcquireRefundResponse;
import com.ymatou.payment.facade.model.ApproveRefundRequest;
import com.ymatou.payment.facade.model.ApproveRefundResponse;
import com.ymatou.payment.facade.model.FastRefundRequest;
import com.ymatou.payment.facade.model.FastRefundResponse;
import com.ymatou.payment.facade.model.QueryRefundRequest;
import com.ymatou.payment.facade.model.QueryRefundResponse;
import com.ymatou.payment.facade.model.TradeRefundableRequest;
import com.ymatou.payment.facade.model.TradeRefundableResponse;

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

    /**
     * 退款审核
     * 
     * @param req
     * @param servletRequest
     * @return
     */
    ApproveRefundResponse approveRefund(ApproveRefundRequest req, HttpServletRequest servletRequest);

    /**
     * 检查是否可以退款
     * 
     * @param req
     * @param servletRequest
     * @return
     */
    TradeRefundableResponse checkRefundable(TradeRefundableRequest req, HttpServletRequest servletRequest);

    /**
     * 查询退款单
     * 
     * @param req
     * @return
     */
    QueryRefundResponse query(QueryRefundRequest req, HttpServletRequest servletRequest);
}
