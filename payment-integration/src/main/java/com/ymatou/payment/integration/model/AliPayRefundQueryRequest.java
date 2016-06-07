/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

/**
 * 支付宝退款查询接口Request
 * 
 * @author qianmin 2016年5月31日 下午2:30:59
 *
 */
public class AliPayRefundQueryRequest {
    /**
     * 接口名称
     */
    private String service = "refund_fastpay_query";
    /**
     * 合作者身份ID
     */
    private String partner;
    /**
     * 参数编码字符集
     */
    private String inputCharset = "UTF-8";
    /**
     * 签名方式
     */
    private String signType;
    /**
     * 签名
     */
    private String sign;
    /**
     * 批量退款批次号
     */
    private String batchNo;
    /**
     * 支付宝交易号
     */
    private String tradeNo;

    public String getService() {
        return service;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getInputCharset() {
        return inputCharset;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }


}
