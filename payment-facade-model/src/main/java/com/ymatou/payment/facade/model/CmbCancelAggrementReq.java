/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.model;

import com.ymatou.payment.facade.BaseRequest;

/**
 * 招行取消协议请求报文
 * 
 * @author wangxudong 2016年11月28日 下午2:34:13
 *
 */
public class CmbCancelAggrementReq extends BaseRequest {

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = -5147999455085068111L;


    /**
     * 用户ID
     */
    private Integer userId;


    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }


    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
