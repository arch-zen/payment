/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.util.HashMap;

import org.hibernate.validator.constraints.NotEmpty;

import com.ymatou.payment.facade.BaseRequest;

/**
 * 执行支付通知请求
 * 
 * @author wangxudong 2016年6月22日 下午6:35:14
 *
 */
public class ExecutePayNotifyReq extends BaseRequest {

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = 6585343843000752324L;

    /**
     * 支付单号
     */
    @NotEmpty(message = "paymentId should not be empty ")
    private String paymentId;

    /**
     * MockHeader
     */
    private HashMap<String, String> mockHeader;


    /**
     * @return the paymentId
     */
    public String getPaymentId() {
        return paymentId;
    }

    /**
     * @param paymentId the paymentId to set
     */
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
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
}
