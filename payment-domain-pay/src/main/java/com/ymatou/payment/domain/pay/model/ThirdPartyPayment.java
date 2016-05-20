/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.pay.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author qianmin 2016年5月20日 下午3:04:23
 *
 */
public class ThirdPartyPayment {
    private String paymentId;
    private String institutionPaymentId;
    private BigDecimal ActualPayPrice;
    private String ActualPayCurrency;
    private String PayerId;
    private Integer CardType;
    private String BankId;
    private String OriginMessage;
    private Date PayTime;
    private PayStatus PayStatus;
    private String TraceId;

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

    public PayStatus getPayStatus() {
        return PayStatus;
    }

    public void setPayStatus(PayStatus payStatus) {
        PayStatus = payStatus;
    }

    public String getTraceId() {
        return TraceId;
    }

    public void setTraceId(String traceId) {
        TraceId = traceId;
    }
}
