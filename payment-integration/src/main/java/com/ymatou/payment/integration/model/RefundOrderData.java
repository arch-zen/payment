/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

import java.util.List;

/**
 * 微信支付退款查询中退款订单model
 * 
 * @author qianmin 2016年5月9日 上午10:42:18
 *
 */
public class RefundOrderData {
    /**
     * 商户退款单号
     */
    private String outRefundNo;
    /**
     * 微信退款单号
     */
    private String refundID;
    /**
     * 退款渠道 IGINAL-原路退款;BALANCE-退回到余额
     */
    private String refundChannel;
    /**
     * 退款金额
     */
    private int refundFee;
    /**
     * 代金券或立减优惠退款金额
     */
    private int couponRefundFee;
    /**
     * 代金券或立减优惠使用数量
     */
    private int couponRefundCount;
    /**
     * 单个代金券或立减优惠信息
     */
    private List<CouponRefundData> couponRefundData;
    /**
     * 退款入账账户
     */
    private String refundRecvAccout;
    /**
     * 退款状态
     */
    private String refundStatus;


    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String getRefundID() {
        return refundID;
    }

    public void setRefundID(String refundID) {
        this.refundID = refundID;
    }

    public String getRefundChannel() {
        return refundChannel;
    }

    public void setRefundChannel(String refundChannel) {
        this.refundChannel = refundChannel;
    }

    public int getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(int refundFee) {
        this.refundFee = refundFee;
    }

    public int getCouponRefundFee() {
        return couponRefundFee;
    }

    public void setCouponRefundFee(int couponRefundFee) {
        this.couponRefundFee = couponRefundFee;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public int getCouponRefundCount() {
        return couponRefundCount;
    }

    public void setCouponRefundCount(int couponRefundCount) {
        this.couponRefundCount = couponRefundCount;
    }

    public String getRefundRecvAccout() {
        return refundRecvAccout;
    }

    public List<CouponRefundData> getCouponRefundData() {
        return couponRefundData;
    }

    public void setCouponRefundData(List<CouponRefundData> couponRefundData) {
        this.couponRefundData = couponRefundData;
    }

    public void setRefundRecvAccout(String refundRecvAccout) {
        this.refundRecvAccout = refundRecvAccout;
    }
}
