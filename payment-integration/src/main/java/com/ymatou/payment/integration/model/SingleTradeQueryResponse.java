/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

import java.util.Date;

/**
 * 支付宝单笔交易查询Response
 * 
 * @author qianmin 2016年5月19日 上午11:17:14
 *
 */
public class SingleTradeQueryResponse {

    public static final String SUCCESS = "T";

    // 基本参数
    /**
     * 请求是否成功
     */
    private String is_success;
    /**
     * 签名
     */
    private String sign;
    /**
     * 签名方式
     */
    private String sign_type;
    /**
     * 查询交易信息失败的错误代码
     */
    private String error;


    // 业务参数
    /**
     * 买家支付宝账号
     */
    private String buyer_email;
    /**
     * 买家支付宝账号对应的支付宝唯一用户号
     */
    private String buyer_id;
    /**
     * 交易状态
     */
    private String trade_status;
    /**
     * 交易金额是否调整过
     */
    private String is_total_fee_adjust;
    /**
     * 商户网站唯一订单号
     */
    private String out_trade_no;
    /**
     * 支付宝交易号
     */
    private String trade_no;
    /**
     * 商品名称
     */
    private String subject;
    /**
     * 交易冻结状态
     */
    private String flag_trade_locked;
    /**
     * 商品描述
     */
    private String body;

    /**
     * 交易创建时间
     */
    private Date gmt_create;
    /**
     * 卖家的支付宝账号
     */
    private String seller_email;
    /**
     * 卖家支付宝账号对应的支付宝唯一用户号
     */
    private String seller_id;
    /**
     * 交易总金额
     */
    private String total_fee;
    /**
     * 商品单价
     */
    private String price;
    /**
     * 购买数量
     */
    private String quantity;
    /**
     * 邮费
     */
    private String logistics_fee;
    /**
     * 红包折扣
     */
    private String coupon_discount;
    /**
     * 是否使用过红包
     */
    private String use_coupon;
    /**
     * 折扣
     */
    private String discount;
    /**
     * 退款状态
     */
    private String refund_status;
    /**
     * 物流状态
     */
    private String logistics_status;
    /**
     * 交易附加状态
     */
    private String additional_trade_status;
    /**
     * 交易最近一次修改时间
     */
    private Date gmt_last_modified_time;
    /**
     * 付款时间
     */
    private Date gmt_payment;
    /**
     * 卖家发货时间
     */
    private Date gmt_send_goods;
    /**
     * 退款时间
     */
    private Date gmt_refund;
    /**
     * 主超时时间
     */
    private Date time_out;
    /**
     * 交易关闭时间
     */
    private Date gmt_close;
    /**
     * 物流状态更新时间
     */
    private Date gmt_logistics_modify;
    /**
     * 主超时间类型
     */
    private String time_out_type;
    /**
     * 退款金额
     */
    private String refund_fee;
    /**
     * 退款流程
     */
    private String refund_flow_type;
    /**
     * 退款ID
     */
    private String refund_id;
    /**
     * 退现金金额
     */
    private String refund_cash_fee;
    /**
     * 退红包金额
     */
    private String refund_coupon_fee;
    /**
     * 退积分金额
     */
    private String refund_agent_pay_fee;
    /**
     * 使用红包的金额
     */
    private String coupon_used_fee;
    /**
     * 累计的已经退款金额
     */
    private String to_buyer_fee;
    /**
     * 累计的已打款给卖家的金额
     */
    private String to_seller_fee;
    /**
     * 资金单据列表
     */
    private String fund_bill_list;
    /**
     * 收款类型
     */
    private String payment_type;
    /**
     * 交易的创建人角色
     */
    private String operator_role;

    /**
     * 原始应答
     */
    private String responseOriginString;

    public String getIs_success() {
        return is_success;
    }

    public void setIs_success(String is_success) {
        this.is_success = is_success;
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getBuyer_email() {
        return buyer_email;
    }

    public void setBuyer_email(String buyer_email) {
        this.buyer_email = buyer_email;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public String getIs_total_fee_adjust() {
        return is_total_fee_adjust;
    }

    public void setIs_total_fee_adjust(String is_total_fee_adjust) {
        this.is_total_fee_adjust = is_total_fee_adjust;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFlag_trade_locked() {
        return flag_trade_locked;
    }

    public void setFlag_trade_locked(String flag_trade_locked) {
        this.flag_trade_locked = flag_trade_locked;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(Date gmt_create) {
        this.gmt_create = gmt_create;
    }

    public String getSeller_email() {
        return seller_email;
    }

    public void setSeller_email(String seller_email) {
        this.seller_email = seller_email;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getLogistics_fee() {
        return logistics_fee;
    }

    public void setLogistics_fee(String logistics_fee) {
        this.logistics_fee = logistics_fee;
    }

    public String getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(String coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public String getUse_coupon() {
        return use_coupon;
    }

    public void setUse_coupon(String use_coupon) {
        this.use_coupon = use_coupon;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(String refund_status) {
        this.refund_status = refund_status;
    }

    public String getLogistics_status() {
        return logistics_status;
    }

    public void setLogistics_status(String logistics_status) {
        this.logistics_status = logistics_status;
    }

    public String getAdditional_trade_status() {
        return additional_trade_status;
    }

    public void setAdditional_trade_status(String additional_trade_status) {
        this.additional_trade_status = additional_trade_status;
    }

    public Date getGmt_last_modified_time() {
        return gmt_last_modified_time;
    }

    public void setGmt_last_modified_time(Date gmt_last_modified_time) {
        this.gmt_last_modified_time = gmt_last_modified_time;
    }

    public Date getGmt_payment() {
        return gmt_payment;
    }

    public void setGmt_payment(Date gmt_payment) {
        this.gmt_payment = gmt_payment;
    }

    public Date getGmt_send_goods() {
        return gmt_send_goods;
    }

    public void setGmt_send_goods(Date gmt_send_goods) {
        this.gmt_send_goods = gmt_send_goods;
    }

    public Date getGmt_refund() {
        return gmt_refund;
    }

    public void setGmt_refund(Date gmt_refund) {
        this.gmt_refund = gmt_refund;
    }

    public Date getTime_out() {
        return time_out;
    }

    public void setTime_out(Date time_out) {
        this.time_out = time_out;
    }

    public Date getGmt_close() {
        return gmt_close;
    }

    public void setGmt_close(Date gmt_close) {
        this.gmt_close = gmt_close;
    }

    public Date getGmt_logistics_modify() {
        return gmt_logistics_modify;
    }

    public void setGmt_logistics_modify(Date gmt_logistics_modify) {
        this.gmt_logistics_modify = gmt_logistics_modify;
    }

    public String getTime_out_type() {
        return time_out_type;
    }

    public void setTime_out_type(String time_out_type) {
        this.time_out_type = time_out_type;
    }

    public String getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(String refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getRefund_flow_type() {
        return refund_flow_type;
    }

    public void setRefund_flow_type(String refund_flow_type) {
        this.refund_flow_type = refund_flow_type;
    }

    public String getRefund_id() {
        return refund_id;
    }

    public void setRefund_id(String refund_id) {
        this.refund_id = refund_id;
    }

    public String getRefund_cash_fee() {
        return refund_cash_fee;
    }

    public void setRefund_cash_fee(String refund_cash_fee) {
        this.refund_cash_fee = refund_cash_fee;
    }

    public String getRefund_coupon_fee() {
        return refund_coupon_fee;
    }

    public void setRefund_coupon_fee(String refund_coupon_fee) {
        this.refund_coupon_fee = refund_coupon_fee;
    }

    public String getRefund_agent_pay_fee() {
        return refund_agent_pay_fee;
    }

    public void setRefund_agent_pay_fee(String refund_agent_pay_fee) {
        this.refund_agent_pay_fee = refund_agent_pay_fee;
    }

    public String getCoupon_used_fee() {
        return coupon_used_fee;
    }

    public void setCoupon_used_fee(String coupon_used_fee) {
        this.coupon_used_fee = coupon_used_fee;
    }

    public String getTo_buyer_fee() {
        return to_buyer_fee;
    }

    public void setTo_buyer_fee(String to_buyer_fee) {
        this.to_buyer_fee = to_buyer_fee;
    }

    public String getTo_seller_fee() {
        return to_seller_fee;
    }

    public void setTo_seller_fee(String to_seller_fee) {
        this.to_seller_fee = to_seller_fee;
    }

    public String getFund_bill_list() {
        return fund_bill_list;
    }

    public void setFund_bill_list(String fund_bill_list) {
        this.fund_bill_list = fund_bill_list;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getOperator_role() {
        return operator_role;
    }

    public void setOperator_role(String operator_role) {
        this.operator_role = operator_role;
    }

    public String getResponseOriginString() {
        return responseOriginString;
    }

    public void setResponseOriginString(String responseOriginString) {
        this.responseOriginString = responseOriginString;
    }

}
