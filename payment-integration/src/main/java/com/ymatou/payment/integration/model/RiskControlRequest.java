/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

/**
 * 风控Request
 * 
 * @author qianmin 2016年5月26日 下午5:08:04
 *
 */
public class RiskControlRequest {
    private String appId;
    private String bizCode;
    private String externalUserId;
    private String instPaymentId;
    private String internalUserId;
    private String ordeId;
    private String paymentId;
    private String payPrice;
    private String payChannel;
    private String traceId;
    private String userIp;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    public String getInstPaymentId() {
        return instPaymentId;
    }

    public void setInstPaymentId(String instPaymentId) {
        this.instPaymentId = instPaymentId;
    }

    public String getInternalUserId() {
        return internalUserId;
    }

    public void setInternalUserId(String internalUserId) {
        this.internalUserId = internalUserId;
    }

    public String getOrdeId() {
        return ordeId;
    }

    public void setOrdeId(String ordeId) {
        this.ordeId = ordeId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }
}
