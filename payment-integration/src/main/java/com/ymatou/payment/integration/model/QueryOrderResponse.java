/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 微信支付查询订单Response
 * 
 * @author qianmin 2016年5月19日 下午2:14:41
 *
 */
public class QueryOrderResponse {

    public static final String SUCCESS = "SUCCESS";

    /**
     * 系统错误，未知状态
     * <xml><return_code><![CDATA[SUCCESS]]></return_code>
     * <return_msg><![CDATA[OK]]></return_msg>
     * <appid><![CDATA[wxf51a439c0416f182]]></appid>
     * <mch_id><![CDATA[1234079001]]></mch_id>
     * <nonce_str><![CDATA[7oDOx5D1X0CqUB2Z]]></nonce_str>
     * <sign><![CDATA[EF9A7609C69C35ABB50232CFA184C4B1]]></sign>
     * <result_code><![CDATA[FAIL]]></result_code>
     * <err_code><![CDATA[SYSTEMERROR]]></err_code>
     * <err_code_des><![CDATA[System Error]]></err_code_des>
     * </xml>
     */
    public static final String UNKONW = "SYSTEMERROR";

    /*
     * 退款状态
     */
    public static final String REFUND = "REFUND";

    /**
     * 返回状态码
     */
    private String return_code;
    /**
     * 返回信息
     */
    private String return_msg;


    // 以下字段在return_code为SUCCESS的时候有返回
    /**
     * 公众账号ID
     */
    private String appid;
    /**
     * 商户号
     */
    private String mch_id;
    /**
     * 随机字符串
     */
    private String nonce_str;
    /**
     * 签名
     */
    private String sign;
    /**
     * 业务结果
     */
    private String result_code;
    /**
     * 错误代码
     */
    private String err_code;
    /**
     * 错误代码描述
     */
    private String err_code_des;


    // 以下字段在return_code 和result_code都为SUCCESS的时候有返回
    /**
     * 设备号
     */
    private String device_info;
    /**
     * 用户标识
     */
    private String openid;
    /**
     * 是否关注公众账号
     */
    private String is_subscribe;
    /**
     * 交易类型
     */
    private String trade_type;
    /**
     * 交易状态
     */
    private String trade_state;
    /**
     * 付款银行
     */
    private String bank_type;
    /**
     * 订单金额
     */
    private int total_fee;
    /**
     * 应结订单金额
     */
    private int settlement_total_fee;
    /**
     * 货币种类
     */
    private String fee_type;
    /**
     * 现金支付金额
     */
    private int cash_fee;
    /**
     * 现金支付货币类型
     */
    private String cash_fee_type;
    /**
     * 代金券金额
     */
    private int coupon_fee;
    /**
     * 代金券使用数量
     */
    private int coupon_count;
    /**
     * 代金券信息
     */
    private List<CouponData> couponDatas;
    /**
     * 微信支付订单号
     */
    private String transaction_id;
    /**
     * 商户订单号
     */
    private String out_trade_no;
    /**
     * 附加数据
     */
    private String attach;
    /**
     * 支付完成时间
     */
    private String time_end;
    /**
     * 交易状态描述
     */
    private String trade_state_desc;

    /**
     * 原始应答
     */
    private String responseOriginString;

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

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

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_code_des() {
        return err_code_des;
    }

    public void setErr_code_des(String err_code_des) {
        this.err_code_des = err_code_des;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getIs_subscribe() {
        return is_subscribe;
    }

    public void setIs_subscribe(String is_subscribe) {
        this.is_subscribe = is_subscribe;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getTrade_state() {
        return trade_state;
    }

    public void setTrade_state(String trade_state) {
        this.trade_state = trade_state;
    }

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public int getSettlement_total_fee() {
        return settlement_total_fee;
    }

    public void setSettlement_total_fee(int settlement_total_fee) {
        this.settlement_total_fee = settlement_total_fee;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public int getCash_fee() {
        return cash_fee;
    }

    public void setCash_fee(int cash_fee) {
        this.cash_fee = cash_fee;
    }

    public String getCash_fee_type() {
        return cash_fee_type;
    }

    public void setCash_fee_type(String cash_fee_type) {
        this.cash_fee_type = cash_fee_type;
    }

    public int getCoupon_fee() {
        return coupon_fee;
    }

    public void setCoupon_fee(int coupon_fee) {
        this.coupon_fee = coupon_fee;
    }

    public int getCoupon_count() {
        return coupon_count;
    }

    public void setCoupon_count(int coupon_count) {
        this.coupon_count = coupon_count;
    }

    public List<CouponData> getCouponDatas() {
        return couponDatas;
    }

    public void setCouponDatas(List<CouponData> couponDatas) {
        this.couponDatas = couponDatas;
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

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getTrade_state_desc() {
        return trade_state_desc;
    }

    public void setTrade_state_desc(String trade_state_desc) {
        this.trade_state_desc = trade_state_desc;
    }

    public String getResponseOriginString() {
        return responseOriginString;
    }

    public void setResponseOriginString(String responseOriginString) {
        this.responseOriginString = responseOriginString;
    }

    public boolean isValid() {
        boolean flag = total_fee > 0
                && SUCCESS.equalsIgnoreCase(result_code)
                && SUCCESS.equalsIgnoreCase(return_code)
                && (SUCCESS.equalsIgnoreCase(trade_state) || "REFUND".equalsIgnoreCase(trade_state))
                && (!StringUtils.isBlank(transaction_id))
                && (!StringUtils.isBlank(out_trade_no));

        return flag;
    }
}
