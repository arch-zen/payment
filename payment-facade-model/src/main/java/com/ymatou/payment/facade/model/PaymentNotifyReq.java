/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.util.HashMap;

import com.ymatou.payment.facade.BaseRequest;
import com.ymatou.payment.facade.constants.PaymentNotifyType;

/**
 * 支付回调请求
 * 
 * @author wangxudong 2016年5月17日 下午8:11:21
 *
 */
public class PaymentNotifyReq extends BaseRequest {

    /**
     * 序列化版本
     */
    private static final long serialVersionUID = -3699489461058687559L;

    /**
     * 支付渠道
     */
    private String payType;

    /**
     * 原始的HTTP请求
     */
    private String rawString;

    /**
     * MockHeader
     */
    private HashMap<String, String> mockHeader;

    /**
     * 支付回调请求类型
     */
    private PaymentNotifyType notifyType;

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
     * @return the notifyType
     */
    public PaymentNotifyType getNotifyType() {
        return notifyType;
    }

    /**
     * @param notifyType the notifyType to set
     */
    public void setNotifyType(PaymentNotifyType notifyType) {
        this.notifyType = notifyType;
    }
}
