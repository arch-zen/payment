/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.math.BigDecimal;
import java.util.List;

import com.ymatou.payment.facade.BaseResponse;

/**
 * 退款收单应答(支持部分退款)
 * 
 * @author qianmin 2016年6月3日 下午5:48:58
 *
 */
public class AcquireRefundPlusResponse extends BaseResponse {

    private static final long serialVersionUID = -4695469631024625302L;

    /**
     * 结果
     */
    private List<RefundDetail> details;

    public static class RefundDetail {
        /**
         * 交易号
         */
        private String tradeNo;
        /**
         * 是否可以退款
         */
        private boolean isRefundable;
        /**
         * 可退款金额
         */
        private BigDecimal refundableAmt;

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

        public boolean isRefundable() {
            return isRefundable;
        }

        public void setRefundable(boolean isRefundable) {
            this.isRefundable = isRefundable;
        }

        public BigDecimal getRefundableAmt() {
            return refundableAmt;
        }

        public void setRefundableAmt(BigDecimal refundableAmt) {
            this.refundableAmt = refundableAmt;
        }
    }

    public List<RefundDetail> getDetails() {
        return details;
    }

    public void setDetails(List<RefundDetail> details) {
        this.details = details;
    }
}
