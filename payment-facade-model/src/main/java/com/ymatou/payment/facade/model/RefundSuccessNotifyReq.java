/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.math.BigDecimal;

import com.ymatou.payment.facade.BaseRequest;

/**
 * 退款成功时发送到总线的消息
 * 
 * @author wangxudong 2016年9月14日 下午6:07:49
 *
 */
public class RefundSuccessNotifyReq extends BaseRequest {

    private static final long serialVersionUID = -7765167008583574182L;

    /**
     * 退款单Id
     */
    private int refundId;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 支付单号
     */
    private String paymentId;

    /**
     * 第三方支付单号
     */
    private String instPaymentId;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * @return the refundId
     */
    public int getRefundId() {
        return refundId;
    }

    /**
     * @param refundId the refundId to set
     */
    public void setRefundId(int refundId) {
        this.refundId = refundId;
    }

    /**
     * @return the refundAmount
     */
    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    /**
     * @param refundAmount the refundAmount to set
     */
    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    /**
     * @return the paymentId
     */
    public String getPaymentId() {
        return paymentId;
    }

    /**
     * @param paymentId the paymentId to set
     */
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    /**
     * @return the instPaymentId
     */
    public String getInstPaymentId() {
        return instPaymentId;
    }

    /**
     * @param instPaymentId the instPaymentId to set
     */
    public void setInstPaymentId(String instPaymentId) {
        this.instPaymentId = instPaymentId;
    }

    /**
     * @return the orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
