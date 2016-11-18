/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

/**
 * 签约通知
 * 
 * @author wangxudong 2016年11月18日 下午8:21:24
 *
 */
public interface SignNotifyResource {
    /**
     * @param payType
     * @param servletRequest
     * @return
     */
    public Response cmbSignNotify(HttpServletRequest servletRequest);
}
