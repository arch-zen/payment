/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

/**
 * 支付宝查询时间戳应答model
 * 
 * @author qianmin 2016年5月9日 上午10:42:18
 *
 */
public class QueryTimestampResponse {

    /**
     * 应答中带回的请求参数service
     */
    private String service;
    /**
     * 应答中带回的请求参数partner
     */
    private String partner;

    /**
     * 请求是否成功(T/F)
     */
    private String isSuccess;

    /**
     * 时间戳(isSunccess=T)
     */
    private String timestampEncryptKey;
    /**
     * 加签值(isSunccess=T)
     */
    private String sign;
    /**
     * 加签方式(isSunccess=T)
     */
    private String singType;

    /**
     * 出错信息(isSunccess=F)
     */
    private String error;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getTimestampEncryptKey() {
        return timestampEncryptKey;
    }

    public void setTimestampEncryptKey(String timestampEncryptKey) {
        this.timestampEncryptKey = timestampEncryptKey;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSingType() {
        return singType;
    }

    public void setSingType(String singType) {
        this.singType = singType;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
