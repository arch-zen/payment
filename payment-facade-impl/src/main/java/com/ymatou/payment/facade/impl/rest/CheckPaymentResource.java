/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.impl.rest;

import javax.servlet.http.HttpServletRequest;

import com.ymatou.payment.facade.model.CheckPaymentRequset;

/**
 * 
 * @author qianmin 2016年5月19日 下午4:16:10
 *
 */
public interface CheckPaymentResource {

    /**
     * 支付对账
     * 
     * @param req
     * @param servletRequest
     * @return
     */
    public String checkPayment(CheckPaymentRequset req, HttpServletRequest servletRequest);
}
