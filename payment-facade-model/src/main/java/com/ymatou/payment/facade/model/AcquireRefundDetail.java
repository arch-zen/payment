/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

/**
 * SubmitRefund接口应答的子模型
 * 
 * @author qianmin 2016年5月12日 上午11:27:57
 * 
 */
public class AcquireRefundDetail {
    /**
     * 交易号
     */
    private String tradeNo;
    /**
     * 是否可以退款
     */
    private boolean isRefundable;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public boolean isRefundable() {
        return isRefundable;
    }

    public void setRefundable(boolean isRefundable) {
        this.isRefundable = isRefundable;
    }
}
