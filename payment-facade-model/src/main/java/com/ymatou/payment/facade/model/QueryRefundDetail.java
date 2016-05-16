/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.math.BigDecimal;
import java.util.Date;

import com.ymatou.payment.facade.PrintFriendliness;

/**
 * 查询退款单应答detail
 * 
 * @author qianmin 2016年5月13日 下午3:23:33
 * 
 */
public class QueryRefundDetail extends PrintFriendliness {

    private static final long serialVersionUID = -4637858498397115322L;

    /**
     * 交易号
     */
    private String tradeNo;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 应用编号
     */
    private String appId;
    /**
     * 支付类型
     */
    private String payType;
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    /**
     * 货币类型
     */
    private String currencyType;
    /**
     * 审核状态
     */
    private int approveStatus;
    /**
     * 审核时间
     */
    private Date approvedTime;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 审核人
     */
    private String approvedUser;
    /**
     * 退款状态
     */
    private int refundStatus;
    /**
     * 退款时间
     */
    private Date refundTime;
    /**
     * 退款批次号
     */
    private String refundBatchNo;
    /**
     * 支付单号
     */
    private String paymentId;
    /**
     * 支付渠道
     */
    private int payChannel;
    /**
     * 交易类型
     */
    private int tradeType;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public int getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(int approveStatus) {
        this.approveStatus = approveStatus;
    }

    public Date getApprovedTime() {
        return approvedTime;
    }

    public void setApprovedTime(Date approvedTime) {
        this.approvedTime = approvedTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getApprovedUser() {
        return approvedUser;
    }

    public void setApprovedUser(String approvedUser) {
        this.approvedUser = approvedUser;
    }

    public int getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public String getRefundBatchNo() {
        return refundBatchNo;
    }

    public void setRefundBatchNo(String refundBatchNo) {
        this.refundBatchNo = refundBatchNo;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public int getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(int payChannel) {
        this.payChannel = payChannel;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }
}
