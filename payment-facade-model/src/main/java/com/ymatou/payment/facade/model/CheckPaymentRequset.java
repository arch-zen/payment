/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.util.HashMap;

import com.ymatou.payment.facade.BaseRequest;

/**
 * 支付对账Request
 * 
 * @author qianmin 2016年5月19日 下午3:44:33
 *
 */
public class CheckPaymentRequset extends BaseRequest {

    private static final long serialVersionUID = -3583018792649066201L;

    /**
     * 支付单号
     */
    private String paymentId;
    /**
     * 支付渠道(支付宝：10、11、12、13;微信支付：14、15)
     */
    private String payType;
    /**
     * 是否为最终对账(False: 支付补偿; True: 支付对账)
     */
    private boolean finalCheck;

    /**
     * http header， 可不填
     */
    private HashMap<String, String> header;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public boolean isFinalCheck() {
        return finalCheck;
    }

    public void setFinalCheck(boolean finalCheck) {
        this.finalCheck = finalCheck;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }

}
