/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.math.BigDecimal;
import java.util.HashMap;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

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
     * 应用编号
     */
    @Length(max = 16, message = "appId not valid")
    @NotBlank(message = "appId not be empty")
    private String appId;
    /**
     * 订单号
     */
    @Length(max = 64, message = "orderId not valid")
    @NotBlank(message = "orderId not be empty")
    private String orderId;
    /**
     * 交易号
     */
    @Length(max = 64, message = "tradeNo not valid")
    @NotBlank(message = "tradeNo not be empty")
    private String tradeNo;
    /**
     * 退款申请号
     */
    @Length(max = 32, message = "refundNo not valid")
    @NotBlank(message = "refundNo not be empty")
    private String refundNo;
    /**
     * 退款金额
     */
    @NotNull(message = "refundAmt not valid")
    private BigDecimal refundAmt;
    /**
     * 交易类型
     */
    @Max(2)
    @Min(1)
    private int tradeType;

    private HashMap<String, String> header;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public BigDecimal getRefundAmt() {
        return refundAmt;
    }

    public void setRefundAmt(BigDecimal refundAmt) {
        this.refundAmt = refundAmt;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }
}
