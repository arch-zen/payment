/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

/**
 * 
 * 支付宝授权接口Request
 * field命名不规范是为了解析报文
 * 
 * @author qianmin 2016年5月25日 下午2:13:13
 *
 */
public class CreateTradeRequest {

    public static final String SEC_ID = "MD5";
    public static final String VERSION = "2.0";
    public static final String SERVICE = "alipay.wap.trade.create.direct";
    public static final String FORMAT = "xml";

    private String service = SERVICE;
    private String format = FORMAT;
    private String v = VERSION;
    private String partner;
    private String req_id;
    private String sec_id = SEC_ID;
    private String sign;
    private String req_data;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getReq_id() {
        return req_id;
    }

    public void setReq_id(String req_id) {
        this.req_id = req_id;
    }

    public String getSec_id() {
        return sec_id;
    }

    public void setSec_id(String sec_id) {
        this.sec_id = sec_id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getReq_data() {
        return req_data;
    }

    public void setReq_data(String req_data) {
        this.req_data = req_data;
    }
}
