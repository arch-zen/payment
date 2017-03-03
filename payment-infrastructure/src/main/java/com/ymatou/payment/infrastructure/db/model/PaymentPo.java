package com.ymatou.payment.infrastructure.db.model;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentPo {
    /**
     * VARCHAR(32) 必填<br>
     * 
     */
    private String paymentId;

    /**
     * CHAR(36) 必填<br>
     * 
     */
    private String bussinessOrderId;

    /**
     * VARCHAR(64)<br>
     * 
     */
    private String institutionPaymentId;

    /**
     * VARCHAR(16) 必填<br>
     * 
     */
    private String payType;

    /**
     * DECIMAL(18,4) 必填<br>
     * 
     */
    private BigDecimal payPrice;

    /**
     * DECIMAL(18,4)<br>
     * 
     */
    private BigDecimal actualPayPrice;

    /**
     * VARCHAR(3) 必填<br>
     * 
     */
    private String payCurrencyType;

    /**
     * VARCHAR(3)<br>
     * 
     */
    private String actualPayCurrencyType;

    /**
     * DOUBLE(53)<br>
     * 
     */
    private Double exchangeRate;

    /**
     * VARCHAR(32)<br>
     * 
     */
    private String bankId;

    /**
     * INTEGER(10)<br>
     * 
     */
    private Integer cardType;

    /**
     * VARCHAR(64)<br>
     * 
     */
    private String payerId;

    /**
     * TIMESTAMP(23,3) 默认值[(getdate())] 必填<br>
     * 
     */
    private Date createdTime;

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     * 
     */
    private Integer payStatus;

    /**
     * TIMESTAMP(23,3)<br>
     * 
     */
    private Date payTime;

    /**
     * TIMESTAMP(23,3)<br>
     * 
     */
    private Date lastUpdatedTime;

    /**
     * INTEGER(10)<br>
     * 
     */
    private Integer checkStatus;

    /**
     * DECIMAL(18,2)<br>
     * 
     */
    private BigDecimal refundAmt;

    /**
     * DECIMAL(18,2)<br>
     * 
     */
    private BigDecimal completedRefundAmt;

    /**
     * INTEGER(10)<br>
     * 
     */
    private Integer retryCount;

    /**
     * INTEGER(10)<br>
     * 
     */
    private Integer notifyStatus;

    /**
     * VARCHAR(200)<br>
     * 
     */
    private String payerEmail;

    /**
     * DECIMAL(18,2) 默认值[((0))]<br>
     * 
     */
    private BigDecimal discountAmt;

    /**
     * BINARY(8) 必填<br>
     * 
     */
    private byte[] dataVersion;

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
     * CHAR(36) 必填<br>
     */
    public String getBussinessOrderId() {
        return bussinessOrderId;
    }

    /**
     * CHAR(36) 必填<br>
     */
    public void setBussinessOrderId(String bussinessOrderId) {
        this.bussinessOrderId = bussinessOrderId == null ? null : bussinessOrderId.trim();
    }

    /**
     * VARCHAR(64)<br>
     */
    public String getInstitutionPaymentId() {
        return institutionPaymentId;
    }

    /**
     * VARCHAR(64)<br>
     */
    public void setInstitutionPaymentId(String institutionPaymentId) {
        this.institutionPaymentId = institutionPaymentId == null ? null : institutionPaymentId.trim();
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
    public BigDecimal getPayPrice() {
        return payPrice;
    }

    /**
     * DECIMAL(18,4) 必填<br>
     */
    public void setPayPrice(BigDecimal payPrice) {
        this.payPrice = payPrice;
    }

    /**
     * DECIMAL(18,4)<br>
     */
    public BigDecimal getActualPayPrice() {
        return actualPayPrice;
    }

    /**
     * DECIMAL(18,4)<br>
     */
    public void setActualPayPrice(BigDecimal actualPayPrice) {
        this.actualPayPrice = actualPayPrice;
    }

    /**
     * VARCHAR(3) 必填<br>
     */
    public String getPayCurrencyType() {
        return payCurrencyType;
    }

    /**
     * VARCHAR(3) 必填<br>
     */
    public void setPayCurrencyType(String payCurrencyType) {
        this.payCurrencyType = payCurrencyType == null ? null : payCurrencyType.trim();
    }

    /**
     * VARCHAR(3)<br>
     */
    public String getActualPayCurrencyType() {
        return actualPayCurrencyType;
    }

    /**
     * VARCHAR(3)<br>
     */
    public void setActualPayCurrencyType(String actualPayCurrencyType) {
        this.actualPayCurrencyType = actualPayCurrencyType == null ? null : actualPayCurrencyType.trim();
    }

    /**
     * DOUBLE(53)<br>
     */
    public Double getExchangeRate() {
        return exchangeRate;
    }

    /**
     * DOUBLE(53)<br>
     */
    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    /**
     * VARCHAR(32)<br>
     */
    public String getBankId() {
        return bankId;
    }

    /**
     * VARCHAR(32)<br>
     */
    public void setBankId(String bankId) {
        this.bankId = bankId == null ? null : bankId.trim();
    }

    /**
     * INTEGER(10)<br>
     */
    public Integer getCardType() {
        return cardType;
    }

    /**
     * INTEGER(10)<br>
     */
    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    /**
     * VARCHAR(64)<br>
     */
    public String getPayerId() {
        return payerId;
    }

    /**
     * VARCHAR(64)<br>
     */
    public void setPayerId(String payerId) {
        this.payerId = payerId == null ? null : payerId.trim();
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
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public Integer getPayStatus() {
        return payStatus;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    /**
     * TIMESTAMP(23,3)<br>
     */
    public Date getPayTime() {
        return payTime;
    }

    /**
     * TIMESTAMP(23,3)<br>
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
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
     * INTEGER(10)<br>
     */
    public Integer getCheckStatus() {
        return checkStatus;
    }

    /**
     * INTEGER(10)<br>
     */
    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    /**
     * DECIMAL(18,2)<br>
     */
    public BigDecimal getRefundAmt() {
        return refundAmt;
    }

    /**
     * DECIMAL(18,2)<br>
     */
    public void setRefundAmt(BigDecimal refundAmt) {
        this.refundAmt = refundAmt;
    }

    /**
     * DECIMAL(18,2)<br>
     */
    public BigDecimal getCompletedRefundAmt() {
        return completedRefundAmt;
    }

    /**
     * DECIMAL(18,2)<br>
     */
    public void setCompletedRefundAmt(BigDecimal completedRefundAmt) {
        this.completedRefundAmt = completedRefundAmt;
    }

    /**
     * INTEGER(10)<br>
     */
    public Integer getRetryCount() {
        return retryCount;
    }

    /**
     * INTEGER(10)<br>
     */
    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    /**
     * INTEGER(10)<br>
     */
    public Integer getNotifyStatus() {
        return notifyStatus;
    }

    /**
     * INTEGER(10)<br>
     */
    public void setNotifyStatus(Integer notifyStatus) {
        this.notifyStatus = notifyStatus;
    }

    /**
     * VARCHAR(200)<br>
     */
    public String getPayerEmail() {
        return payerEmail;
    }

    /**
     * VARCHAR(200)<br>
     */
    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail == null ? null : payerEmail.trim();
    }

    /**
     * DECIMAL(18,2) 默认值[((0))]<br>
     */
    public BigDecimal getDiscountAmt() {
        return discountAmt;
    }

    /**
     * DECIMAL(18,2) 默认值[((0))]<br>
     */
    public void setDiscountAmt(BigDecimal discountAmt) {
        this.discountAmt = discountAmt;
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
     * This method corresponds to the database table PP_Payment
     *
     * @mbggenerated Mon Feb 27 18:24:23 CST 2017
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", paymentId=").append(paymentId);
        sb.append(", bussinessOrderId=").append(bussinessOrderId);
        sb.append(", institutionPaymentId=").append(institutionPaymentId);
        sb.append(", payType=").append(payType);
        sb.append(", payPrice=").append(payPrice);
        sb.append(", actualPayPrice=").append(actualPayPrice);
        sb.append(", payCurrencyType=").append(payCurrencyType);
        sb.append(", actualPayCurrencyType=").append(actualPayCurrencyType);
        sb.append(", exchangeRate=").append(exchangeRate);
        sb.append(", bankId=").append(bankId);
        sb.append(", cardType=").append(cardType);
        sb.append(", payerId=").append(payerId);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", payStatus=").append(payStatus);
        sb.append(", payTime=").append(payTime);
        sb.append(", lastUpdatedTime=").append(lastUpdatedTime);
        sb.append(", checkStatus=").append(checkStatus);
        sb.append(", refundAmt=").append(refundAmt);
        sb.append(", completedRefundAmt=").append(completedRefundAmt);
        sb.append(", retryCount=").append(retryCount);
        sb.append(", notifyStatus=").append(notifyStatus);
        sb.append(", payerEmail=").append(payerEmail);
        sb.append(", discountAmt=").append(discountAmt);
        sb.append(", dataVersion=").append(dataVersion);
        sb.append("]");
        return sb.toString();
    }
}