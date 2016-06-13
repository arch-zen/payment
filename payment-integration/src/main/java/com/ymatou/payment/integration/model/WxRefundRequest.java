/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Weixin退款申请Request
 * 
 * @author qianmin 2016年5月30日 下午12:03:11
 *
 */
public class WxRefundRequest {
    private String appid;
    private String mch_id;
    private String device_info;
    private String nonce_str;
    private String sign;
    private String transaction_id;
    private String out_trade_no;
    private String out_refund_no;
    private int total_fee;
    private int refund_fee;
    private String refund_fee_type;
    private String op_user_id;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getOut_refund_no() {
        return out_refund_no;
    }

    public void setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public int getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(int refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getRefund_fee_type() {
        return refund_fee_type;
    }

    public void setRefund_fee_type(String refund_fee_type) {
        this.refund_fee_type = refund_fee_type;
    }

    public String getOp_user_id() {
        return op_user_id;
    }

    public void setOp_user_id(String op_user_id) {
        this.op_user_id = op_user_id;
    }

    public HashMap<String, String> mapForSign() {
        HashMap<String, String> map = new HashMap<>();
        map.put("appid", this.appid);
        map.put("mch_id", this.mch_id);
        map.put("device_info", this.device_info);
        map.put("nonce_str", this.nonce_str);
        map.put("out_trade_no", this.out_trade_no);
        map.put("transaction_id", this.transaction_id);
        map.put("out_refund_no", this.out_refund_no);
        map.put("total_fee", String.valueOf(this.total_fee));
        map.put("refund_fee", String.valueOf(this.refund_fee));
        map.put("refund_fee_type", this.refund_fee_type);
        map.put("op_user_id", this.op_user_id);

        return map;
    }

    public String getRequestData() {
        HashMap<String, String> map = mapForSign();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.append("sign").append("=").append(this.getSign()).append("&");

        return StringUtils.removeEnd(sb.toString(), "&");
    }
}
