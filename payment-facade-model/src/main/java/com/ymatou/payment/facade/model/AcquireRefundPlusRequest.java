/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.ymatou.payment.facade.BaseRequest;

/**
 * 退款收单请求(支持部分退款)
 * 
 * @author qianmin 2016年6月3日 下午5:44:52
 *
 */
public class AcquireRefundPlusRequest extends BaseRequest {

    private static final long serialVersionUID = -2259236919876681507L;

    /**
     * 订单号
     */
    private String orderId;
    /**
     * 交易信息
     */
    private List<TradeDetail> tradeDetails;
    /**
     * 应用编号
     */
    private String appId;
    /**
     * 跟踪Id
     */
    @Length(min = 1, max = 32, message = "traceid not valid")
    private String traceId;

    public static class TradeDetail {
        /**
         * 交易号
         */
        private String tradeNo;
        /**
         * 交易类型
         */
        private int tradeType;
        /**
         * 退款金额
         */
        private BigDecimal refundAmt;

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

        public int getTradeType() {
            return tradeType;
        }

        public void setTradeType(int tradeType) {
            this.tradeType = tradeType;
        }

        public BigDecimal getRefundAmt() {
            return refundAmt;
        }

        public void setRefundAmt(BigDecimal refundAmt) {
            this.refundAmt = refundAmt;
        }
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<TradeDetail> getTradeDetails() {
        return tradeDetails;
    }

    public void setTradeDetails(List<TradeDetail> tradeDetails) {
        this.tradeDetails = tradeDetails;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
