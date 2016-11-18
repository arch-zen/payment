/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.util.HashMap;

import com.ymatou.payment.facade.BaseRequest;

/**
 * 招行一网通签约回调报文
 * 
 * @author wangxudong 2016年11月18日 下午8:04:20
 *
 */
public class SignNotifyReq extends BaseRequest {

    /**
     * 序列化版本
     */
    private static final long serialVersionUID = -6609671077868440743L;

    /**
     * 原始的HTTP请求
     */
    private String rawString;

    /**
     * 支付渠道
     */
    private String payType;

    /**
     * MockHeader
     */
    private HashMap<String, String> mockHeader;

    /**
     * @return the rawString
     */
    public String getRawString() {
        return rawString;
    }

    /**
     * @param rawString the rawString to set
     */
    public void setRawString(String rawString) {
        this.rawString = rawString;
    }

    /**
     * @return the mockHeader
     */
    public HashMap<String, String> getMockHeader() {
        return mockHeader;
    }

    /**
     * @param mockHeader the mockHeader to set
     */
    public void setMockHeader(HashMap<String, String> mockHeader) {
        this.mockHeader = mockHeader;
    }

    /**
     * @return the payType
     */
    public String getPayType() {
        return payType;
    }

    /**
     * @param payType the payType to set
     */
    public void setPayType(String payType) {
        this.payType = payType;
    }

}
