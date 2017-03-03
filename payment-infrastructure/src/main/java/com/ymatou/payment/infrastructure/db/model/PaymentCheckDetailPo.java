package com.ymatou.payment.infrastructure.db.model;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentCheckDetailPo {
    /**
     * INTEGER(10) 必填<br>
     * 
     */
    private Integer id;

    /**
     * INTEGER(10) 必填<br>
     * 
     */
    private Integer paymentCheckId;

    /**
     * VARCHAR(64) 必填<br>
     * 
     */
    private String institutionPaymentId;

    /**
     * VARCHAR(32) 必填<br>
     * 
     */
    private String paymentId;

    /**
     * VARCHAR(32)<br>
     * 
     */
    private String ymtPaymentId;

    /**
     * VARCHAR(100)<br>
     * 
     */
    private String merchantId;

    /**
     * VARCHAR(200) 必填<br>
     * 
     */
    private String trdUserId;

    /**
     * VARCHAR(200)<br>
     * 
     */
    private String ymtLoginId;

    /**
     * TIMESTAMP(23,3) 必填<br>
     * 
     */
    private Date payTime;

    /**
     * DECIMAL(18,2) 默认值[((0))] 必填<br>
     * 
     */
    private BigDecimal amount;

    /**
     * DECIMAL(18,2) 默认值[((0))] 必填<br>
     * 
     */
    private BigDecimal ymtAmount;

    /**
     * VARCHAR(50)<br>
     * 
     */
    private String payBank;

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     * 
     */
    private Integer checkStatus;

    /**
     * VARCHAR(200)<br>
     * 
     */
    private String remark;

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     * 
     */
    private Integer checkPhase;

    /**
     * DECIMAL(18,2) 默认值[((0))]<br>
     * 
     */
    private BigDecimal discountAmt;

    /**
     * DECIMAL(18,2) 默认值[((0))]<br>
     * 
     */
    private BigDecimal ymtDiscountAmount;

    /**
     * INTEGER(10) 必填<br>
     */
    public Integer getId() {
        return id;
    }

    /**
     * INTEGER(10) 必填<br>
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * INTEGER(10) 必填<br>
     */
    public Integer getPaymentCheckId() {
        return paymentCheckId;
    }

    /**
     * INTEGER(10) 必填<br>
     */
    public void setPaymentCheckId(Integer paymentCheckId) {
        this.paymentCheckId = paymentCheckId;
    }

    /**
     * VARCHAR(64) 必填<br>
     */
    public String getInstitutionPaymentId() {
        return institutionPaymentId;
    }

    /**
     * VARCHAR(64) 必填<br>
     */
    public void setInstitutionPaymentId(String institutionPaymentId) {
        this.institutionPaymentId = institutionPaymentId == null ? null : institutionPaymentId.trim();
    }

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
     * VARCHAR(32)<br>
     */
    public String getYmtPaymentId() {
        return ymtPaymentId;
    }

    /**
     * VARCHAR(32)<br>
     */
    public void setYmtPaymentId(String ymtPaymentId) {
        this.ymtPaymentId = ymtPaymentId == null ? null : ymtPaymentId.trim();
    }

    /**
     * VARCHAR(100)<br>
     */
    public String getMerchantId() {
        return merchantId;
    }

    /**
     * VARCHAR(100)<br>
     */
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId == null ? null : merchantId.trim();
    }

    /**
     * VARCHAR(200) 必填<br>
     */
    public String getTrdUserId() {
        return trdUserId;
    }

    /**
     * VARCHAR(200) 必填<br>
     */
    public void setTrdUserId(String trdUserId) {
        this.trdUserId = trdUserId == null ? null : trdUserId.trim();
    }

    /**
     * VARCHAR(200)<br>
     */
    public String getYmtLoginId() {
        return ymtLoginId;
    }

    /**
     * VARCHAR(200)<br>
     */
    public void setYmtLoginId(String ymtLoginId) {
        this.ymtLoginId = ymtLoginId == null ? null : ymtLoginId.trim();
    }

    /**
     * TIMESTAMP(23,3) 必填<br>
     */
    public Date getPayTime() {
        return payTime;
    }

    /**
     * TIMESTAMP(23,3) 必填<br>
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    /**
     * DECIMAL(18,2) 默认值[((0))] 必填<br>
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * DECIMAL(18,2) 默认值[((0))] 必填<br>
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * DECIMAL(18,2) 默认值[((0))] 必填<br>
     */
    public BigDecimal getYmtAmount() {
        return ymtAmount;
    }

    /**
     * DECIMAL(18,2) 默认值[((0))] 必填<br>
     */
    public void setYmtAmount(BigDecimal ymtAmount) {
        this.ymtAmount = ymtAmount;
    }

    /**
     * VARCHAR(50)<br>
     */
    public String getPayBank() {
        return payBank;
    }

    /**
     * VARCHAR(50)<br>
     */
    public void setPayBank(String payBank) {
        this.payBank = payBank == null ? null : payBank.trim();
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public Integer getCheckStatus() {
        return checkStatus;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    /**
     * VARCHAR(200)<br>
     */
    public String getRemark() {
        return remark;
    }

    /**
     * VARCHAR(200)<br>
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public Integer getCheckPhase() {
        return checkPhase;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public void setCheckPhase(Integer checkPhase) {
        this.checkPhase = checkPhase;
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
     * DECIMAL(18,2) 默认值[((0))]<br>
     */
    public BigDecimal getYmtDiscountAmount() {
        return ymtDiscountAmount;
    }

    /**
     * DECIMAL(18,2) 默认值[((0))]<br>
     */
    public void setYmtDiscountAmount(BigDecimal ymtDiscountAmount) {
        this.ymtDiscountAmount = ymtDiscountAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PP_PaymentCheckDetail
     *
     * @mbggenerated Mon Feb 27 18:24:23 CST 2017
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", paymentCheckId=").append(paymentCheckId);
        sb.append(", institutionPaymentId=").append(institutionPaymentId);
        sb.append(", paymentId=").append(paymentId);
        sb.append(", ymtPaymentId=").append(ymtPaymentId);
        sb.append(", merchantId=").append(merchantId);
        sb.append(", trdUserId=").append(trdUserId);
        sb.append(", ymtLoginId=").append(ymtLoginId);
        sb.append(", payTime=").append(payTime);
        sb.append(", amount=").append(amount);
        sb.append(", ymtAmount=").append(ymtAmount);
        sb.append(", payBank=").append(payBank);
        sb.append(", checkStatus=").append(checkStatus);
        sb.append(", remark=").append(remark);
        sb.append(", checkPhase=").append(checkPhase);
        sb.append(", discountAmt=").append(discountAmt);
        sb.append(", ymtDiscountAmount=").append(ymtDiscountAmount);
        sb.append("]");
        return sb.toString();
    }
}