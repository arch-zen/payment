/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.model;

import java.math.BigDecimal;
import java.util.Date;

import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.infrastructure.Money;

public class PaymentNotifyMessage {
    /**
     * 支付单Id
     */
    private String paymentId;

    /**
     * 第三方支付Id
     */
    private String institutionPaymentId;

    /**
     * 实际支付金额(支付订单金额)
     */
    private Money actualPayPrice;

    /**
     * 实际支付币种
     */
    private String actualPayCurrency;

    /**
     * 优惠金额
     */
    private Money discountAmt;

    /**
     * 第三方用户标识
     */
    private String payerId;

    /**
     * 第三方用户邮箱
     */
    private String payerEmail;

    /**
     * 银行卡种类
     */
    private Integer cardType;

    /**
     * 银行Id
     */
    private String bankId;

    /**
     * 原始消息
     */
    private String originMessage;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 支付单状态
     */
    private PayStatusEnum payStatus;

    /**
     * 跟踪Id
     */
    private String traceId;

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
     * @return the institutionPaymentId
     */
    public String getInstitutionPaymentId() {
        return institutionPaymentId;
    }

    /**
     * @param institutionPaymentId the institutionPaymentId to set
     */
    public void setInstitutionPaymentId(String institutionPaymentId) {
        this.institutionPaymentId = institutionPaymentId;
    }

    /**
     * @return the actualPayPrice
     */
    public Money getActualPayPrice() {
        return actualPayPrice;
    }

    /**
     * @param actualPayPrice the actualPayPrice to set
     */
    public void setActualPayPrice(Money actualPayPrice) {
        this.actualPayPrice = actualPayPrice;
    }

    /**
     * @return the actualPayCurrency
     */
    public String getActualPayCurrency() {
        return actualPayCurrency;
    }

    /**
     * @param actualPayCurrency the actualPayCurrency to set
     */
    public void setActualPayCurrency(String actualPayCurrency) {
        this.actualPayCurrency = actualPayCurrency;
    }

    /**
     * @return the payerId
     */
    public String getPayerId() {
        return payerId;
    }

    /**
     * @param payerId the payerId to set
     */
    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    /**
     * @return the cardType
     */
    public Integer getCardType() {
        return cardType;
    }

    /**
     * @param cardType the cardType to set
     */
    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    /**
     * @return the bankId
     */
    public String getBankId() {
        return bankId;
    }

    /**
     * @param bankId the bankId to set
     */
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    /**
     * @return the originMessage
     */
    public String getOriginMessage() {
        return originMessage;
    }

    /**
     * @param originMessage the originMessage to set
     */
    public void setOriginMessage(String originMessage) {
        this.originMessage = originMessage;
    }

    /**
     * @return the payTime
     */
    public Date getPayTime() {
        return payTime;
    }

    /**
     * @param payTime the payTime to set
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    /**
     * @return the payStatus
     */
    public PayStatusEnum getPayStatus() {
        return payStatus;
    }

    /**
     * @param payStatus the payStatus to set
     */
    public void setPayStatus(PayStatusEnum payStatus) {
        this.payStatus = payStatus;
    }

    /**
     * @return the traceId
     */
    public String getTraceId() {
        return traceId;
    }

    /**
     * @param traceId the traceId to set
     */
    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    /**
     * @return the discountAmt
     */
    public Money getDiscountAmt() {
        if (discountAmt == null) {
            return new Money(0);
        } else {
            return discountAmt;
        }
    }

    /**
     * @param discountAmt the discountAmt to set
     */
    public void setDiscountAmt(Money discountAmt) {
        this.discountAmt = discountAmt;
    }

}
