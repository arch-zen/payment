/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

/**
 * 支付宝单笔交易查询Request
 * 
 * @author qianmin 2016年5月19日 上午11:11:45
 *
 */
public class SingleTradeQueryRequest {

    /**
     * 接口名称
     */
    private String service;
    /**
     * 合作者身份ID
     */
    private String partner;
    /**
     * 参数编码字符集
     */
    private String _input_charset = "UTF-8";
    /**
     * 签名
     */
    private String sign;
    /**
     * 签名方式
     */
    private String sign_type;


    /**
     * 支付宝交易号(64)
     */
    private String trade_no;
    /**
     * 商户网站唯一订单号(64)
     */
    private String out_trade_no;

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

    public String get_input_charset() {
        return _input_charset;
    }

    /**
     * 默认值UTF-8
     * 
     * @param _input_charset
     */
    public void set_input_charset(String _input_charset) {
        this._input_charset = _input_charset;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }
}
