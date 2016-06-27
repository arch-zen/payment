/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 支付发货回调Request
 * 
 * @author qianmin 2016年5月31日 下午5:01:31
 *
 */
public class PaymentCallbackRequest {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    /**
     * 应用Id(2)
     */
    private String appId;
    /**
     * 业务代码(1-补款，2-充值，3-交易)
     */
    private String bizCode;
    /**
     * 货币类型
     */
    private String currency;
    /**
     * 备注
     */
    private String memo;
    /**
     * 交易号
     */
    private String tradingId;
    /**
     * 支付单号
     */
    private String paymentId;
    /**
     * 支付金额
     */
    private String payPrice;
    /**
     * 支付时间
     */
    private Date payTime;
    /**
     * 签名方法
     */
    private String signMethod = "MD5";
    /**
     * 跟踪Id
     */
    private String traceId;
    /**
     * 第三方支付单号
     */
    private String instPaymentId;
    /**
     * 用户码头ID
     */
    private String internalUserId;
    /**
     * 用户外部标识
     */
    private String externalUserId;
    /**
     * 支付渠道(Weixin、Alipay)
     */
    private String payChannel;
    /**
     * 支付类型
     */
    private String payType;

    /**
     * 签名
     */
    private String sign;

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTradingId() {
        return tradingId;
    }

    public void setTradingId(String tradingId) {
        this.tradingId = tradingId;
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

    public String getSignMethod() {
        return signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
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

    public String getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayTime() {
        return sdf.format(payTime);
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    /**
     * @return the sign
     */
    public String getSign() {
        return sign;
    }

    /**
     * @param sign the sign to set
     */
    public void setSign(String sign) {
        this.sign = sign;
    }
}
