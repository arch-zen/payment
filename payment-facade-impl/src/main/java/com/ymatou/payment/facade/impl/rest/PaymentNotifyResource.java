/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.impl.rest;

import javax.servlet.http.HttpServletRequest;

import com.ymatou.payment.facade.model.PaymentNotifyRequest;

/**
 * 支付回调REST接口
 * 
 * @author wangxudong 2016年5月17日 下午7:34:26
 *
 */
public interface PaymentNotifyResource {

    /**
     * 客户端回调
     * 
     * @param payType
     * @param servletRequest
     * @return
     */
    String callback(String payType, HttpServletRequest servletRequest);

    /**
     * 服务端回调
     * 
     * @param payType
     * @param servletRequest
     * @return
     */
    String notify(String payType, HttpServletRequest servletRequest);
}
