/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.util.HashMap;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.ymatou.payment.facade.BaseRequest;

/**
 * 提交退款request
 * 
 * @author qianmin 2016年5月11日 下午2:47:20
 * 
 */
public class AcquireRefundRequest extends BaseRequest {

    private static final long serialVersionUID = -5683980381688847121L;

    /**
     * 订单号
     */
    @NotBlank(message = "orderId is empty")
    private String orderId;
    /**
     * 交易信息
     */
    @NotEmpty(message = "tradeDetails is empty")
    private List<TradeDetail> tradeDetails;
    /**
     * 应用编号
     */
    @NotBlank(message = "appId is empty")
    private String appId;
    /**
     * 跟踪Id
     */
    @Length(min = 1, max = 32, message = "traceid not valid")
    @NotBlank(message = "traceId is empty")
    private String traceId;
    /**
     * 签名
     */
    private String sign;

    /**
     * 退款凭据，交易传入退款单号
     */
    private String bizNo;

    private HashMap<String, String> header;

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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

}
