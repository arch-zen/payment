/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

/**
 * 代金券信息Detail
 * 
 * @author qianmin 2016年5月19日 下午2:27:48
 *
 */
public class CouponData {
    /**
     * 代金券批次ID
     */
    private String coupon_batch_id;
    /**
     * 代金券类型
     */
    private String coupon_type;
    /**
     * 代金券ID
     */
    private String coupon_id;
    /**
     * 单个代金券支付金额
     */
    private int coupon_fee;


    public String getCoupon_batch_id() {
        return coupon_batch_id;
    }

    public void setCoupon_batch_id(String coupon_batch_id) {
        this.coupon_batch_id = coupon_batch_id;
    }

    public String getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(String coupon_type) {
        this.coupon_type = coupon_type;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public int getCoupon_fee() {
        return coupon_fee;
    }

    public void setCoupon_fee(int coupon_fee) {
        this.coupon_fee = coupon_fee;
    }
}
