/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

/**
 * 通知用户交易信息的请求参数
 * 
 * @author qianmin 2016年5月11日 下午12:05:01
 * 
 */
public class NotifyUserRequest {

    private String BusinessType;
    private String BuyerId;
    private boolean IsShangouOrder;
    private String OrderId;

    public String getBusinessType() {
        return BusinessType;
    }

    public void setBusinessType(String businessType) {
        BusinessType = businessType;
    }

    public String getBuyerId() {
        return BuyerId;
    }

    public void setBuyerId(String buyerId) {
        BuyerId = buyerId;
    }

    public boolean isIsShangouOrder() {
        return IsShangouOrder;
    }

    public void setIsShangouOrder(boolean isShangouOrder) {
        IsShangouOrder = isShangouOrder;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

}
