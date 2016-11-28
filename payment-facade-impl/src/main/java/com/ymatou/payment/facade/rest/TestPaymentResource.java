/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import com.ymatou.payment.facade.model.CmbCancelAggrementReq;

/**
 * 提供测试
 * 
 * @author qianmin 2016年6月17日 下午4:29:23
 *
 */
public interface TestPaymentResource {


    /**
     * 支付宝 PC DEMO
     * 
     * @param servletRequest
     * @return
     */
    String alipc(HttpServletRequest servletRequest);

    /**
     * 支付宝 WAP DEMO
     * 
     * @param servletRequest
     * @return
     */
    Response aliwap(HttpServletRequest servletRequest);

    /**
     * 一网通 DEMO
     * 
     * @param servletRequest
     * @return
     */
    String cmbpay(HttpServletRequest servletRequest);

    /**
     * 一网通 取消协议DEMO
     * 
     * @param servletRequest
     * @return
     */
    String cmbCancelAggrement(CmbCancelAggrementReq req, HttpServletRequest servletRequest);
}
