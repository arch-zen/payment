/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

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
    Response callback(String payType, HttpServletRequest servletRequest);

    /**
     * 服务端回调
     * 
     * @param payType
     * @param servletRequest
     * @return
     */
    String notify(String payType, HttpServletRequest servletRequest);

    /**
     * 招行一网通 支付回调
     * 
     * @param payType
     * @param servletRequest
     * @return
     */
    Response cmbPayNotify(HttpServletRequest servletRequest);

    /**
     * 招行一网通 签约回调
     * 
     * @param servletRequest
     * @return
     */
    Response cmbSignNotify(HttpServletRequest servletRequest);
}
