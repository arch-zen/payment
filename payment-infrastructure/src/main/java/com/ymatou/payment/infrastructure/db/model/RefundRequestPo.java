package com.ymatou.payment.infrastructure.db.model;

import java.math.BigDecimal;
import java.util.Date;

public class RefundRequestPo {
    /**
     * VARCHAR(32) 必填<br>
     * 
     */
    private String paymentId;

    /**
     * VARCHAR(64) 必填<br>
     * 
     */
    private String tradeNo;

    /**
     * VARCHAR(64)<br>
     * 
     */
    private String orderId;

    /**
     * VARCHAR(32) 必填<br>
     * 
     */
    private String traceId;

    /**
     * VARCHAR(16) 必填<br>
     * 
     */
    private String appId;

    /**
     * VARCHAR(16) 必填<br>
     * 
     */
    private String payType;

    /**
     * DECIMAL(18,4) 必填<br>
     * 
     */
    private BigDecimal refundAmount;

    /**
     * VARCHAR(8) 必填<br>
     * 
     */
    private String currencyType;

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     * 
     */
    private Integer approveStatus;

    /**
     * TIMESTAMP(23,3)<br>
     * 
     */
    private Date approvedTime;

    /**
     * BIT(1) 默认值[((0))] 必填<br>
     * 
     */
    private Boolean softDeleteFlag;

    /**
     * TIMESTAMP(23,3) 默认值[(getdate())] 必填<br>
     * 
     */
    private Date createdTime;

    /**
     * VARCHAR(128)<br>
     * 
     */
    private String approvedUser;

    /**
     * INTEGER(10) 默认值[((0))]<br>
     * 
     */
    private Integer refundStatus;

    /**
     * TIMESTAMP(23,3)<br>
     * 
     */
    private Date refundTime;

    /**
     * VARCHAR(32)<br>
     * 
     */
    private String refundBatchNo;

    /**
     * VARCHAR(64)<br>
     * 
     */
    private String instPaymentId;

    /**
     * INTEGER(10)<br>
     * 
     */
    private Integer tradeType;

    /**
     * CLOB(2147483647)<br>
     * 
     */
    private String memo;

    /**
     * VARCHAR(32) 必填<br>
     */
    public String getPaymentId() {
        return paymentId;
    }

    /**
     * VARCHAR(32) 必填<br>
     */
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId == null ? null : paymentId.trim();
    }

    /**
     * VARCHAR(64) 必填<br>
     */
    public String getTradeNo() {
        return tradeNo;
    }

    /**
     * VARCHAR(64) 必填<br>
     */
    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
    }

    /**
     * VARCHAR(64)<br>
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * VARCHAR(64)<br>
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    /**
     * VARCHAR(32) 必填<br>
     */
    public String getTraceId() {
        return traceId;
    }

    /**
     * VARCHAR(32) 必填<br>
     */
    public void setTraceId(String traceId) {
        this.traceId = traceId == null ? null : traceId.trim();
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
    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    /**
     * DECIMAL(18,4) 必填<br>
     */
    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    /**
     * VARCHAR(8) 必填<br>
     */
    public String getCurrencyType() {
        return currencyType;
    }

    /**
     * VARCHAR(8) 必填<br>
     */
    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType == null ? null : currencyType.trim();
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public Integer getApproveStatus() {
        return approveStatus;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public void setApproveStatus(Integer approveStatus) {
        this.approveStatus = approveStatus;
    }

    /**
     * TIMESTAMP(23,3)<br>
     */
    public Date getApprovedTime() {
        return approvedTime;
    }

    /**
     * TIMESTAMP(23,3)<br>
     */
    public void setApprovedTime(Date approvedTime) {
        this.approvedTime = approvedTime;
    }

    /**
     * BIT(1) 默认值[((0))] 必填<br>
     */
    public Boolean getSoftDeleteFlag() {
        return softDeleteFlag;
    }

    /**
     * BIT(1) 默认值[((0))] 必填<br>
     */
    public void setSoftDeleteFlag(Boolean softDeleteFlag) {
        this.softDeleteFlag = softDeleteFlag;
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
     * VARCHAR(128)<br>
     */
    public String getApprovedUser() {
        return approvedUser;
    }

    /**
     * VARCHAR(128)<br>
     */
    public void setApprovedUser(String approvedUser) {
        this.approvedUser = approvedUser == null ? null : approvedUser.trim();
    }

    /**
     * INTEGER(10) 默认值[((0))]<br>
     */
    public Integer getRefundStatus() {
        return refundStatus;
    }

    /**
     * INTEGER(10) 默认值[((0))]<br>
     */
    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    /**
     * TIMESTAMP(23,3)<br>
     */
    public Date getRefundTime() {
        return refundTime;
    }

    /**
     * TIMESTAMP(23,3)<br>
     */
    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    /**
     * VARCHAR(32)<br>
     */
    public String getRefundBatchNo() {
        return refundBatchNo;
    }

    /**
     * VARCHAR(32)<br>
     */
    public void setRefundBatchNo(String refundBatchNo) {
        this.refundBatchNo = refundBatchNo == null ? null : refundBatchNo.trim();
    }

    /**
     * VARCHAR(64)<br>
     */
    public String getInstPaymentId() {
        return instPaymentId;
    }

    /**
     * VARCHAR(64)<br>
     */
    public void setInstPaymentId(String instPaymentId) {
        this.instPaymentId = instPaymentId == null ? null : instPaymentId.trim();
    }

    /**
     * INTEGER(10)<br>
     */
    public Integer getTradeType() {
        return tradeType;
    }

    /**
     * INTEGER(10)<br>
     */
    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    /**
     * CLOB(2147483647)<br>
     */
    public String getMemo() {
        return memo;
    }

    /**
     * CLOB(2147483647)<br>
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundRequest
     *
     * @mbggenerated Tue Jun 07 15:47:31 CST 2016
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", paymentId=").append(paymentId);
        sb.append(", tradeNo=").append(tradeNo);
        sb.append(", orderId=").append(orderId);
        sb.append(", traceId=").append(traceId);
        sb.append(", appId=").append(appId);
        sb.append(", payType=").append(payType);
        sb.append(", refundAmount=").append(refundAmount);
        sb.append(", currencyType=").append(currencyType);
        sb.append(", approveStatus=").append(approveStatus);
        sb.append(", approvedTime=").append(approvedTime);
        sb.append(", softDeleteFlag=").append(softDeleteFlag);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", approvedUser=").append(approvedUser);
        sb.append(", refundStatus=").append(refundStatus);
        sb.append(", refundTime=").append(refundTime);
        sb.append(", refundBatchNo=").append(refundBatchNo);
        sb.append(", instPaymentId=").append(instPaymentId);
        sb.append(", tradeType=").append(tradeType);
        sb.append(", memo=").append(memo);
        sb.append("]");
        return sb.toString();
    }
}