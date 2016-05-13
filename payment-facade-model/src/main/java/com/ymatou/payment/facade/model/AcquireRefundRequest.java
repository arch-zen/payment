/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.util.HashMap;
import java.util.List;

import org.hibernate.validator.constraints.Length;

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
    /**
     * 签名
     */
    private String sign;

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

}
