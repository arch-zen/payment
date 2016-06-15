/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.math.BigDecimal;
import java.util.HashMap;

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
     * 应用编号
     */
    private String appId;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 交易号
     */
    @Length(min = 1, max = 32, message = "tradeNo not valid")
    private String tradeNo;
    /**
     * 退款申请号
     */
    @Length(min = 1, max = 32, message = "requestNo not valid")
    private String requestNo;
    /**
     * 退款金额
     */
    private BigDecimal refundAmt;
    /**
     * 退款金额
     */
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

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
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
