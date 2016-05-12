/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.util.HashMap;
import java.util.List;

import com.ymatou.payment.facade.BaseRequest;

/**
 * 快速退款请求参数
 * 
 * @author qianmin 2016年5月11日 上午11:19:20
 *
 */
public class FastRefundRequest extends BaseRequest {

    private static final long serialVersionUID = -7414443014183140909L;

    private String paymentId;
    private String tradingId;
    private int tradeType;
    private List<String> orderIdList;
    private String appId;
    private String traceId;
    private HashMap<String, String> header;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getTradingId() {
        return tradingId;
    }

    public void setTradingId(String tradingId) {
        this.tradingId = tradingId;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public List<String> getOrderIdList() {
        return orderIdList;
    }

    public void setOrderIdList(List<String> orderIdList) {
        this.orderIdList = orderIdList;
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

    public HashMap<String, String> getHeader() {
        return header;
    }

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }
}
