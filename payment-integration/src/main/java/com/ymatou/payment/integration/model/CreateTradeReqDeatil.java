/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

/**
 * 
 * @author qianmin 2016年5月25日 下午4:25:14
 *
 */
public class CreateTradeReqDeatil {
    private String subject;
    private String out_trade_no;
    private String total_fee;
    private String seller_account_name;
    private String call_back_url;
    private String notify_url;
    private String out_user;
    private String merchant_url;
    private String pay_exprie;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getSeller_account_name() {
        return seller_account_name;
    }

    public void setSeller_account_name(String seller_account_name) {
        this.seller_account_name = seller_account_name;
    }

    public String getCall_back_url() {
        return call_back_url;
    }

    public void setCall_back_url(String call_back_url) {
        this.call_back_url = call_back_url;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getOut_user() {
        return out_user;
    }

    public void setOut_user(String out_user) {
        this.out_user = out_user;
    }

    public String getMerchant_url() {
        return merchant_url;
    }

    public void setMerchant_url(String merchant_url) {
        this.merchant_url = merchant_url;
    }

    public String getPay_exprie() {
        return pay_exprie;
    }

    public void setPay_exprie(String pay_exprie) {
        this.pay_exprie = pay_exprie;
    }
}
