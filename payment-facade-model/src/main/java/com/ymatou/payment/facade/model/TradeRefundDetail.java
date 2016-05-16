/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.math.BigDecimal;

import com.ymatou.payment.facade.PrintFriendliness;

/**
 * 是否可以退款detailModel
 * 
 * @author qianmin 2016年5月12日 下午2:54:07
 * 
 */
public class TradeRefundDetail extends PrintFriendliness {

    private static final long serialVersionUID = -6599328508785983461L;

    private String tradeNo;
    private BigDecimal payAmount;
    private String currencyType;
    private boolean isRefundable;
    private String payChannel;
    private String paymentId;
    private String payType;
    private String instPaymentId;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public boolean isRefundable() {
        return isRefundable;
    }

    public void setRefundable(boolean isRefundable) {
        this.isRefundable = isRefundable;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

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

    public String getInstPaymentId() {
        return instPaymentId;
    }

    public void setInstPaymentId(String instPaymentId) {
        this.instPaymentId = instPaymentId;
    }
}
