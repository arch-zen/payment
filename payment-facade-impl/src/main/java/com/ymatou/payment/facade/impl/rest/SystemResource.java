/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.impl.rest;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统消息接口
 * 
 * @author wangxudong 2016年5月22日 下午10:07:53
 *
 */
public interface SystemResource {
    /**
     * 返回系统状态
     * 
     * @return
     */
    String status();

    /**
     * 返回AppId
     * 
     * @return
     */
    String alipc(HttpServletRequest servletRequest);
}
