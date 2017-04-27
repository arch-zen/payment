package com.ymatou.payment.integration.model;

/**
 * Created by gejianhua on 2017/4/25.
 * applypay消费请求信息
 */
public class ApplePayConsumeRequest extends ApplePayBaseRequest {

    ///////////////基本信息////////////////////////
    //其它信息在ApplyPayBaseRequest类中

    /**
     * 渠道类型
     */
    private String channelType;



///////////////商户信息////////////////////////

    //其它信息在ApplyPayBaseRequest类中
    /**
     * 后台通知地址
     */
    private String backUrl;


    /////订单信息//////////////////////

    /**
     * 商户订单号
     */
    private String orderId;

    /**
     * 交易币种
     */
    private String currencyCode;

    /**
     * 交易金额
     */
    private String txnAmt;
    /**
     * 订单发送时间
     */
    private String txnTime;

    /**
     * 支付超时时间
     */
    private String payTimeout;

    /**
     * 账号
     */
    private String accNo;
    /**
     * 请求方自定义域
     */
    private String reqReserved;

    /**
     * 订单描述
     */
    private String orderDesc;



    /////////机构信息/////////////////

    /**
     * 收单机构代码
     */
    private String acqInsCode;

    /**
     * 商户类别
     */
    private String merCatCode;

    /**
     * 商户名称
     */
    private String merName;

    /**
     * 商户简称
     */
    private String merAbbr;


    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getTxnAmt() {
        return txnAmt;
    }

    public void setTxnAmt(String txnAmt) {
        this.txnAmt = txnAmt;
    }

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }

    public String getPayTimeout() {
        return payTimeout;
    }

    public void setPayTimeout(String payTimeout) {
        this.payTimeout = payTimeout;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getReqReserved() {
        return reqReserved;
    }

    public void setReqReserved(String reqReserved) {
        this.reqReserved = reqReserved;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getAcqInsCode() {
        return acqInsCode;
    }

    public void setAcqInsCode(String acqInsCode) {
        this.acqInsCode = acqInsCode;
    }

    public String getMerCatCode() {
        return merCatCode;
    }

    public void setMerCatCode(String merCatCode) {
        this.merCatCode = merCatCode;
    }

    public String getMerName() {
        return merName;
    }

    public void setMerName(String merName) {
        this.merName = merName;
    }

    public String getMerAbbr() {
        return merAbbr;
    }

    public void setMerAbbr(String merAbbr) {
        this.merAbbr = merAbbr;
    }


}















































