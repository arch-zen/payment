/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.channel.model;

import java.math.BigDecimal;
import java.util.Date;

import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.infrastructure.Money;

/**
 * 第三方交易查询结果
 * 
 * @author qianmin 2016年5月19日 下午4:50:49
 *
 */
public class PaymentQueryResp {
    private String paymentId;
    private String institutionPaymentId;
    /**
     * 实际支付金额 = 订单金额 - 优惠金额
     */
    private BigDecimal ActualPayPrice;
    private String ActualPayCurrency;
    private String PayerId;
    private Integer CardType;
    private String BankId;
    private String OriginMessage;
    private Date PayTime;
    private PayStatusEnum PayStatus;
    private String TraceId;
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
        return ActualPayPrice;
    }

    public void setActualPayPrice(BigDecimal actualPayPrice) {
        ActualPayPrice = actualPayPrice;
    }

    public String getActualPayCurrency() {
        return ActualPayCurrency;
    }

    public void setActualPayCurrency(String actualPayCurrency) {
        ActualPayCurrency = actualPayCurrency;
    }

    public String getPayerId() {
        return PayerId;
    }

    public void setPayerId(String payerId) {
        PayerId = payerId;
    }

    public Integer getCardType() {
        return CardType;
    }

    public void setCardType(Integer cardType) {
        CardType = cardType;
    }

    public String getBankId() {
        return BankId;
    }

    public void setBankId(String bankId) {
        BankId = bankId;
    }

    public String getOriginMessage() {
        return OriginMessage;
    }

    public void setOriginMessage(String originMessage) {
        OriginMessage = originMessage;
    }

    public Date getPayTime() {
        return PayTime;
    }

    public void setPayTime(Date payTime) {
        PayTime = payTime;
    }

    public PayStatusEnum getPayStatus() {
        return PayStatus;
    }

    public void setPayStatus(PayStatusEnum payStatus) {
        PayStatus = payStatus;
    }

    public String getTraceId() {
        return TraceId;
    }

    public void setTraceId(String traceId) {
        TraceId = traceId;
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
