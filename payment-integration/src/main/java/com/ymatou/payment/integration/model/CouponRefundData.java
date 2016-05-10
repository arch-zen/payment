/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

/**
 * 微信支付退款查询中的优惠券使用信息model
 * 
 * @author qianmin 2016年5月9日 上午10:42:18
 *
 */
public class CouponRefundData {
    /**
     * 代金券或立减优惠批次ID
     */
    private String couponRefundBatchId;
    /**
     * 代金券或立减优惠ID
     */
    private String couponRefundId;
    /**
     * 单个代金券或立减优惠支付金额
     */
    private int couponRefundFee;

    public String getCouponRefundBatchId() {
        return couponRefundBatchId;
    }

    public void setCouponRefundBatchId(String couponRefundBatchId) {
        this.couponRefundBatchId = couponRefundBatchId;
    }

    public String getCouponRefundId() {
        return couponRefundId;
    }

    public void setCouponRefundId(String couponRefundId) {
        this.couponRefundId = couponRefundId;
    }

    public int getCouponRefundFee() {
        return couponRefundFee;
    }

    public void setCouponRefundFee(int couponRefundFee) {
        this.couponRefundFee = couponRefundFee;
    }
}
