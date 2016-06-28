package com.ymatou.payment.infrastructure.db.model;

import java.math.BigDecimal;
import java.util.Date;

public class BussinessOrderPo {
    /**
     * CHAR(36) 默认值[(newid())] 必填<br>
     * 
     */
    private String bussinessOrderId;

    /**
     * VARCHAR(16) 必填<br>
     * 
     */
    private String appId;

    /**
     * VARCHAR(16) 必填<br>
     * 
     */
    private String originAppId;

    /**
     * VARCHAR(32) 必填<br>
     * 
     */
    private String orderId;

    /**
     * VARCHAR(16) 必填<br>
     * 
     */
    private String payType;

    /**
     * DECIMAL(18,4) 必填<br>
     * 
     */
    private BigDecimal orderPrice;

    /**
     * VARCHAR(3) 必填<br>
     * 
     */
    private String currencyType;

    /**
     * INTEGER(10)<br>
     * 
     */
    private Integer userId;

    /**
     * INTEGER(10) 必填<br>
     * 
     */
    private Integer version;

    /**
     * VARCHAR(36) 必填<br>
     * 
     */
    private String traceId;

    /**
     * VARCHAR(16) 必填<br>
     * 
     */
    private String orderTime;

    /**
     * VARCHAR(64)<br>
     * 
     */
    private String thirdPartyUserId;

    /**
     * INTEGER(10)<br>
     * 
     */
    private Integer thirdPartyUserType;

    /**
     * VARCHAR(16) 必填<br>
     * 
     */
    private String clientIp;

    /**
     * VARCHAR(256)<br>
     * 
     */
    private String callbackUrl;

    /**
     * VARCHAR(256) 必填<br>
     * 
     */
    private String notifyUrl;

    /**
     * VARCHAR(64) 必填<br>
     * 
     */
    private String productName;

    /**
     * VARCHAR(512)<br>
     * 
     */
    private String productDesc;

    /**
     * VARCHAR(512)<br>
     * 
     */
    private String productUrl;

    /**
     * INTEGER(10) 必填<br>
     * 
     */
    private Integer codePage;

    /**
     * VARCHAR(512)<br>
     * 
     */
    private String ext;

    /**
     * VARCHAR(512)<br>
     * 
     */
    private String memo;

    /**
     * VARCHAR(8) 必填<br>
     * 
     */
    private String signMethod;

    /**
     * INTEGER(10) 必填<br>
     * 
     */
    private Integer bizCode;

    /**
     * TIMESTAMP(23,3) 默认值[(getdate())] 必填<br>
     * 
     */
    private Date createdTime;

    /**
     * TIMESTAMP(23,3)<br>
     * 
     */
    private Date lastUpdatedTime;

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     * 
     */
    private Integer orderStatus;

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     * 
     */
    private Integer notifyStatus;

    /**
     * TIMESTAMP(23,3)<br>
     * 
     */
    private Date notifyTime;

    /**
     * BINARY(8) 必填<br>
     * 
     */
    private byte[] dataVersion;

    /**
     * CHAR(36) 默认值[(newid())] 必填<br>
     */
    public String getBussinessOrderId() {
        return bussinessOrderId;
    }

    /**
     * CHAR(36) 默认值[(newid())] 必填<br>
     */
    public void setBussinessOrderId(String bussinessOrderId) {
        this.bussinessOrderId = bussinessOrderId == null ? null : bussinessOrderId.trim();
    }

    /**
     * VARCHAR(16) 必填<br>
     */
    public String getAppId() {
        return appId;
    }

    /**
     * VARCHAR(16) 必填<br>
     */
    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    /**
     * VARCHAR(16) 必填<br>
     */
    public String getOriginAppId() {
        return originAppId;
    }

    /**
     * VARCHAR(16) 必填<br>
     */
    public void setOriginAppId(String originAppId) {
        this.originAppId = originAppId == null ? null : originAppId.trim();
    }

    /**
     * VARCHAR(32) 必填<br>
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * VARCHAR(32) 必填<br>
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    /**
     * VARCHAR(16) 必填<br>
     */
    public String getPayType() {
        return payType;
    }

    /**
     * VARCHAR(16) 必填<br>
     */
    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    /**
     * DECIMAL(18,4) 必填<br>
     */
    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    /**
     * DECIMAL(18,4) 必填<br>
     */
    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    /**
     * VARCHAR(3) 必填<br>
     */
    public String getCurrencyType() {
        return currencyType;
    }

    /**
     * VARCHAR(3) 必填<br>
     */
    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType == null ? null : currencyType.trim();
    }

    /**
     * INTEGER(10)<br>
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * INTEGER(10)<br>
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * INTEGER(10) 必填<br>
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * INTEGER(10) 必填<br>
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * VARCHAR(36) 必填<br>
     */
    public String getTraceId() {
        return traceId;
    }

    /**
     * VARCHAR(36) 必填<br>
     */
    public void setTraceId(String traceId) {
        this.traceId = traceId == null ? null : traceId.trim();
    }

    /**
     * VARCHAR(16) 必填<br>
     */
    public String getOrderTime() {
        return orderTime;
    }

    /**
     * VARCHAR(16) 必填<br>
     */
    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime == null ? null : orderTime.trim();
    }

    /**
     * VARCHAR(64)<br>
     */
    public String getThirdPartyUserId() {
        return thirdPartyUserId;
    }

    /**
     * VARCHAR(64)<br>
     */
    public void setThirdPartyUserId(String thirdPartyUserId) {
        this.thirdPartyUserId = thirdPartyUserId == null ? null : thirdPartyUserId.trim();
    }

    /**
     * INTEGER(10)<br>
     */
    public Integer getThirdPartyUserType() {
        return thirdPartyUserType;
    }

    /**
     * INTEGER(10)<br>
     */
    public void setThirdPartyUserType(Integer thirdPartyUserType) {
        this.thirdPartyUserType = thirdPartyUserType;
    }

    /**
     * VARCHAR(16) 必填<br>
     */
    public String getClientIp() {
        return clientIp;
    }

    /**
     * VARCHAR(16) 必填<br>
     */
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp == null ? null : clientIp.trim();
    }

    /**
     * VARCHAR(256)<br>
     */
    public String getCallbackUrl() {
        return callbackUrl;
    }

    /**
     * VARCHAR(256)<br>
     */
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl == null ? null : callbackUrl.trim();
    }

    /**
     * VARCHAR(256) 必填<br>
     */
    public String getNotifyUrl() {
        return notifyUrl;
    }

    /**
     * VARCHAR(256) 必填<br>
     */
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl == null ? null : notifyUrl.trim();
    }

    /**
     * VARCHAR(64) 必填<br>
     */
    public String getProductName() {
        return productName;
    }

    /**
     * VARCHAR(64) 必填<br>
     */
    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    /**
     * VARCHAR(512)<br>
     */
    public String getProductDesc() {
        return productDesc;
    }

    /**
     * VARCHAR(512)<br>
     */
    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc == null ? null : productDesc.trim();
    }

    /**
     * VARCHAR(512)<br>
     */
    public String getProductUrl() {
        return productUrl;
    }

    /**
     * VARCHAR(512)<br>
     */
    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl == null ? null : productUrl.trim();
    }

    /**
     * INTEGER(10) 必填<br>
     */
    public Integer getCodePage() {
        return codePage;
    }

    /**
     * INTEGER(10) 必填<br>
     */
    public void setCodePage(Integer codePage) {
        this.codePage = codePage;
    }

    /**
     * VARCHAR(512)<br>
     */
    public String getExt() {
        return ext;
    }

    /**
     * VARCHAR(512)<br>
     */
    public void setExt(String ext) {
        this.ext = ext == null ? null : ext.trim();
    }

    /**
     * VARCHAR(512)<br>
     */
    public String getMemo() {
        return memo;
    }

    /**
     * VARCHAR(512)<br>
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    /**
     * VARCHAR(8) 必填<br>
     */
    public String getSignMethod() {
        return signMethod;
    }

    /**
     * VARCHAR(8) 必填<br>
     */
    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod == null ? null : signMethod.trim();
    }

    /**
     * INTEGER(10) 必填<br>
     */
    public Integer getBizCode() {
        return bizCode;
    }

    /**
     * INTEGER(10) 必填<br>
     */
    public void setBizCode(Integer bizCode) {
        this.bizCode = bizCode;
    }

    /**
     * TIMESTAMP(23,3) 默认值[(getdate())] 必填<br>
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * TIMESTAMP(23,3) 默认值[(getdate())] 必填<br>
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * TIMESTAMP(23,3)<br>
     */
    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    /**
     * TIMESTAMP(23,3)<br>
     */
    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public Integer getOrderStatus() {
        return orderStatus;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public Integer getNotifyStatus() {
        return notifyStatus;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public void setNotifyStatus(Integer notifyStatus) {
        this.notifyStatus = notifyStatus;
    }

    /**
     * TIMESTAMP(23,3)<br>
     */
    public Date getNotifyTime() {
        return notifyTime;
    }

    /**
     * TIMESTAMP(23,3)<br>
     */
    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    /**
     * BINARY(8) 必填<br>
     */
    public byte[] getDataVersion() {
        return dataVersion;
    }

    /**
     * BINARY(8) 必填<br>
     */
    public void setDataVersion(byte[] dataVersion) {
        this.dataVersion = dataVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PP_BussinessOrder
     *
     * @mbggenerated Tue Jun 28 20:02:01 CST 2016
     */
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
        sb.append(", thirdPartyUserType=").append(thirdPartyUserType);
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
        sb.append(", dataVersion=").append(dataVersion);
        sb.append("]");
        return sb.toString();
    }
}