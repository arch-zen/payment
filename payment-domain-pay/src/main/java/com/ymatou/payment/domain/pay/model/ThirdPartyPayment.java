/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.pay.model;

import java.math.BigDecimal;
import java.util.Date;

import com.ymatou.payment.infrastructure.Money;

/**
 * 
 * @author qianmin 2016年5月20日 下午3:04:23
 *
 */
public class ThirdPartyPayment {
    private String paymentId;
    private String institutionPaymentId;
    /**
     * 实际支付金额 = 订单金额 - 优惠金额
     */
    private BigDecimal actualPayPrice;
    private String actualPayCurrency;
    private String payerId;
    private Integer cardType;
    private String bankId;
    private String originMessage;
    private Date payTime;
    private int payStatus;
    private String traceId;
    /**
     * 优惠金额
     */
    private Money discountAmount;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getInstitutionPaymentId() {
        return institutionPaymentId;
    }

    public void setInstitutionPaymentId(String institutionPaymentId) {
        this.institutionPaymentId = institutionPaymentId;
    }

    public BigDecimal getActualPayPrice() {
        return actualPayPrice;
    }

    public void setActualPayPrice(BigDecimal actualPayPrice) {
        this.actualPayPrice = actualPayPrice;
    }

    public String getActualPayCurrency() {
        return actualPayCurrency;
    }

    public void setActualPayCurrency(String actualPayCurrency) {
        this.actualPayCurrency = actualPayCurrency;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getOriginMessage() {
        return originMessage;
    }

    public void setOriginMessage(String originMessage) {
        this.originMessage = originMessage;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    /**
     * @return the discountAmount
     */
    public Money getDiscountAmount() {
        return discountAmount;
    }

    /**
     * @param discountAmount the discountAmount to set
     */
    public void setDiscountAmount(Money discountAmount) {
        this.discountAmount = discountAmount;
    }
}
