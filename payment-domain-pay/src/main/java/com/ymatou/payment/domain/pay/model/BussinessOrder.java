/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.pay.model;

import java.math.BigDecimal;
import java.util.Date;

import com.ymatou.payment.facade.PrintFriendliness;
import com.ymatou.payment.infrastructure.db.model.BussinessOrderPo;

/**
 * 商户订单模型
 * 
 * @author wangxudong 2016年5月11日 下午4:19:36
 *
 */
public class BussinessOrder extends PrintFriendliness {

    private static final long serialVersionUID = 5438330052260748278L;

    private String bussinessOrderId;
    private String appId;
    private String originAppId;
    private String orderId;
    private String payType;
    private BigDecimal orderPrice;
    private String currencyType;
    private Integer userId;
    private Integer version;
    private String traceId;
    private String orderTime;
    private String thirdPartyUserId;
    private Integer thirdPartyUserType;
    private String clientIp;
    private String callbackUrl;
    private String notifyUrl;
    private String productName;
    private String productDesc;
    private String productUrl;
    private Integer codePage;
    private String ext;
    private String memo;
    private String signMethod;
    private Integer bizCode;
    private Date createdTime;
    private Date lastUpdatedTime;
    private Integer orderStatus;
    private Integer notifyStatus;
    private Date notifyTime;


    public String getBussinessOrderId() {
        return bussinessOrderId;
    }

    public void setBussinessOrderId(String bussinessOrderId) {
        this.bussinessOrderId = bussinessOrderId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOriginAppId() {
        return originAppId;
    }

    public void setOriginAppId(String originAppId) {
        this.originAppId = originAppId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getThirdPartyUserId() {
        return thirdPartyUserId;
    }

    public void setThirdPartyUserId(String thirdPartyUserId) {
        this.thirdPartyUserId = thirdPartyUserId;
    }

    public Integer getThirdPartyUserType() {
        return thirdPartyUserType;
    }

    public void setThirdPartyUserType(Integer thirdPartyUserType) {
        this.thirdPartyUserType = thirdPartyUserType;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public Integer getCodePage() {
        return codePage;
    }

    public void setCodePage(Integer codePage) {
        this.codePage = codePage;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getSignMethod() {
        return signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

    public Integer getBizCode() {
        return bizCode;
    }

    public void setBizCode(Integer bizCode) {
        this.bizCode = bizCode;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getNotifyStatus() {
        return notifyStatus;
    }

    public void setNotifyStatus(Integer notifyStatus) {
        this.notifyStatus = notifyStatus;
    }

    public Date getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", bussinessOrderId=").append(bussinessOrderId);
        sb.append(", appId=").append(appId);
        sb.append(", originAppId=").append(originAppId);
        sb.append(", orderId=").append(orderId);
        sb.append(", payType=").append(payType);
        sb.append(", orderPrice=").append(orderPrice);
        sb.append(", currencyType=").append(currencyType);
        sb.append(", userId=").append(userId);
        sb.append(", version=").append(version);
        sb.append(", traceId=").append(traceId);
        sb.append(", orderTime=").append(orderTime);
        sb.append(", thirdPartyUserId=").append(thirdPartyUserId);
        sb.append(", thirdpartyUserType=").append(thirdPartyUserType);
        sb.append(", clientIp=").append(clientIp);
        sb.append(", callbackUrl=").append(callbackUrl);
        sb.append(", notifyUrl=").append(notifyUrl);
        sb.append(", productName=").append(productName);
        sb.append(", productDesc=").append(productDesc);
        sb.append(", productUrl=").append(productUrl);
        sb.append(", codePage=").append(codePage);
        sb.append(", ext=").append(ext);
        sb.append(", memo=").append(memo);
        sb.append(", signMethod=").append(signMethod);
        sb.append(", bizCode=").append(bizCode);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", lastUpdatedTime=").append(lastUpdatedTime);
        sb.append(", orderStatus=").append(orderStatus);
        sb.append(", notifyStatus=").append(notifyStatus);
        sb.append(", notifyTime=").append(notifyTime);
        sb.append("]");
        return sb.toString();
    }

    public static BussinessOrder convertFromPo(BussinessOrderPo po) {
        BussinessOrder model = new BussinessOrder();
        model.setBussinessOrderId(po.getBussinessOrderId());
        model.setAppId(po.getAppId());
        model.setOriginAppId(po.getOriginAppId());
        model.setOrderId(po.getOrderId());
        model.setPayType(po.getPayType());
        model.setOrderPrice(po.getOrderPrice());
        model.setCurrencyType(po.getCurrencyType());
        model.setUserId(po.getUserId());
        model.setVersion(po.getVersion());
        model.setTraceId(po.getTraceId());
        model.setOrderTime(po.getOrderTime());
        model.setThirdPartyUserId(po.getThirdPartyUserId());
        model.setThirdPartyUserType(po.getThirdPartyUserType());
        model.setClientIp(po.getClientIp());
        model.setCallbackUrl(po.getCallbackUrl());
        model.setNotifyUrl(po.getNotifyUrl());
        model.setProductName(po.getProductName());
        model.setProductDesc(po.getProductDesc());
        model.setProductUrl(po.getProductUrl());
        model.setCodePage(po.getCodePage());
        model.setExt(po.getExt());
        model.setMemo(po.getMemo());
        model.setSignMethod(po.getSignMethod());
        model.setBizCode(po.getBizCode());
        model.setCreatedTime(po.getCreatedTime());
        model.setLastUpdatedTime(po.getLastUpdatedTime());
        model.setOrderStatus(po.getOrderStatus());
        model.setNotifyStatus(po.getNotifyStatus());
        model.setNotifyTime(po.getNotifyTime());

        return model;
    }

    /**
     * 返回商品主题
     * 
     * @return
     */
    public String getSubject() {
        if (productName != null && !productName.isEmpty())
            return productName;

        if (productDesc != null && !productDesc.isEmpty())
            return productDesc;

        return null;
    }

    /**
     * 返回商品描述
     * 
     * @return
     */
    public String getBody() {
        if (productDesc != null && !productDesc.isEmpty())
            return productDesc;

        if (productName != null && !productName.isEmpty())
            return productName;

        return null;
    }
}
