/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

/**
 * 支付宝授权接口Response
 * 
 * @author qianmin 2016年5月25日 下午2:13:29
 *
 */
public class CreateTradeResponse {

    private String partner;
    private String req_id;
    private String sec_id;
    private String service;
    private String v;
    private String sign;
    private String res_data;
    private String requestToken;
    private String error;
    private CreateTradeErrorDeatil errorDetail;

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

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getRes_data() {
        return res_data;
    }

    public void setRes_data(String res_data) {
        this.res_data = res_data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public CreateTradeErrorDeatil getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(CreateTradeErrorDeatil errorDetail) {
        this.errorDetail = errorDetail;
    }
}
