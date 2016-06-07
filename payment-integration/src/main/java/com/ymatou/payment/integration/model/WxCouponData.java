/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

import java.util.List;

/**
 * 
 * @author qianmin 2016年5月30日 下午1:54:58
 *
 */
public class WxCouponData {
    /**
     * 退款金额
     */
    private int settlement_refund_fee;
    /**
     * 代金券类型
     */
    private String coupon_type;
    /**
     * 代金券退款金额
     */
    private int coupon_refund_fee;
    /**
     * 退款代金券使用数量
     */
    private int coupon_refund_count;

    /**
     * 退款代金券信息
     */
    private List<WxCouponRefundData> couponRefundDatas;

    public int getSettlement_refund_fee() {
        return settlement_refund_fee;
    }

    public void setSettlement_refund_fee(int settlement_refund_fee) {
        this.settlement_refund_fee = settlement_refund_fee;
    }

    public String getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(String coupon_type) {
        this.coupon_type = coupon_type;
    }

    public int getCoupon_refund_fee() {
        return coupon_refund_fee;
    }

    public void setCoupon_refund_fee(int coupon_refund_fee) {
        this.coupon_refund_fee = coupon_refund_fee;
    }

    public int getCoupon_refund_count() {
        return coupon_refund_count;
    }

    public void setCoupon_refund_count(int coupon_refund_count) {
        this.coupon_refund_count = coupon_refund_count;
    }

    public List<WxCouponRefundData> getCouponRefundDatas() {
        return couponRefundDatas;
    }

    public void setCouponRefundDatas(List<WxCouponRefundData> couponRefundDatas) {
        this.couponRefundDatas = couponRefundDatas;
    }

}
