/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

/**
 * 
 * @author qianmin 2016年5月30日 下午1:55:16
 *
 */
public class WxCouponRefundData {
    /**
     * 退款代金券批次ID
     */
    private String coupon_refund_batch_id;
    /**
     * 退款代金券ID
     */
    private String coupon_refund_id;
    /**
     * 单个退款代金券支付金额
     */
    private int coupon_refund_fee;

    public String getCoupon_refund_batch_id() {
        return coupon_refund_batch_id;
    }

    public void setCoupon_refund_batch_id(String coupon_refund_batch_id) {
        this.coupon_refund_batch_id = coupon_refund_batch_id;
    }

    public String getCoupon_refund_id() {
        return coupon_refund_id;
    }

    public void setCoupon_refund_id(String coupon_refund_id) {
        this.coupon_refund_id = coupon_refund_id;
    }

    public int getCoupon_refund_fee() {
        return coupon_refund_fee;
    }

    public void setCoupon_refund_fee(int coupon_refund_fee) {
        this.coupon_refund_fee = coupon_refund_fee;
    }
}
