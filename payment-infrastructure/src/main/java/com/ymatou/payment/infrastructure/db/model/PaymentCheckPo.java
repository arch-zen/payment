package com.ymatou.payment.infrastructure.db.model;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentCheckPo {
    /**
     * INTEGER(10) 必填<br>
     * 
     */
    private Integer paymentCheckId;

    /**
     * INTEGER(10) 必填<br>
     * 
     */
    private Integer paymentChannel;

    /**
     * TIMESTAMP(23,3) 默认值[(getdate())] 必填<br>
     * 
     */
    private Date uploadTime;

    /**
     * VARCHAR(100) 必填<br>
     * 
     */
    private String uploadFile;

    /**
     * VARCHAR(200) 必填<br>
     * 
     */
    private String originalFileName;

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     * 
     */
    private Integer ymtPayNum;

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     * 
     */
    private Integer trdPayNum;

    /**
     * DECIMAL(18,2) 默认值[((0))] 必填<br>
     * 
     */
    private BigDecimal ymtPayAmount;

    /**
     * DECIMAL(18,2) 默认值[((0))] 必填<br>
     * 
     */
    private BigDecimal trdPayAmount;

    /**
     * TIMESTAMP(23,3)<br>
     * 
     */
    private Date finishTime;

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     * 
     */
    private Integer checkStatus;

    /**
     * VARCHAR(50) 必填<br>
     * 
     */
    private String checkAccount;

    /**
     * VARCHAR(50) 必填<br>
     * 
     */
    private String serverIp;

    /**
     * VARCHAR(200)<br>
     * 
     */
    private String remark;

    /**
     * DECIMAL(18,2) 默认值[((0))]<br>
     * 
     */
    private BigDecimal ymtDiscountAmount;

    /**
     * DECIMAL(18,2) 默认值[((0))]<br>
     * 
     */
    private BigDecimal trdDiscountAmount;

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
     * INTEGER(10) 必填<br>
     */
    public Integer getPaymentChannel() {
        return paymentChannel;
    }

    /**
     * INTEGER(10) 必填<br>
     */
    public void setPaymentChannel(Integer paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    /**
     * TIMESTAMP(23,3) 默认值[(getdate())] 必填<br>
     */
    public Date getUploadTime() {
        return uploadTime;
    }

    /**
     * TIMESTAMP(23,3) 默认值[(getdate())] 必填<br>
     */
    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    /**
     * VARCHAR(100) 必填<br>
     */
    public String getUploadFile() {
        return uploadFile;
    }

    /**
     * VARCHAR(100) 必填<br>
     */
    public void setUploadFile(String uploadFile) {
        this.uploadFile = uploadFile == null ? null : uploadFile.trim();
    }

    /**
     * VARCHAR(200) 必填<br>
     */
    public String getOriginalFileName() {
        return originalFileName;
    }

    /**
     * VARCHAR(200) 必填<br>
     */
    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName == null ? null : originalFileName.trim();
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public Integer getYmtPayNum() {
        return ymtPayNum;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public void setYmtPayNum(Integer ymtPayNum) {
        this.ymtPayNum = ymtPayNum;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public Integer getTrdPayNum() {
        return trdPayNum;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public void setTrdPayNum(Integer trdPayNum) {
        this.trdPayNum = trdPayNum;
    }

    /**
     * DECIMAL(18,2) 默认值[((0))] 必填<br>
     */
    public BigDecimal getYmtPayAmount() {
        return ymtPayAmount;
    }

    /**
     * DECIMAL(18,2) 默认值[((0))] 必填<br>
     */
    public void setYmtPayAmount(BigDecimal ymtPayAmount) {
        this.ymtPayAmount = ymtPayAmount;
    }

    /**
     * DECIMAL(18,2) 默认值[((0))] 必填<br>
     */
    public BigDecimal getTrdPayAmount() {
        return trdPayAmount;
    }

    /**
     * DECIMAL(18,2) 默认值[((0))] 必填<br>
     */
    public void setTrdPayAmount(BigDecimal trdPayAmount) {
        this.trdPayAmount = trdPayAmount;
    }

    /**
     * TIMESTAMP(23,3)<br>
     */
    public Date getFinishTime() {
        return finishTime;
    }

    /**
     * TIMESTAMP(23,3)<br>
     */
    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
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
     * VARCHAR(50) 必填<br>
     */
    public String getCheckAccount() {
        return checkAccount;
    }

    /**
     * VARCHAR(50) 必填<br>
     */
    public void setCheckAccount(String checkAccount) {
        this.checkAccount = checkAccount == null ? null : checkAccount.trim();
    }

    /**
     * VARCHAR(50) 必填<br>
     */
    public String getServerIp() {
        return serverIp;
    }

    /**
     * VARCHAR(50) 必填<br>
     */
    public void setServerIp(String serverIp) {
        this.serverIp = serverIp == null ? null : serverIp.trim();
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
     * DECIMAL(18,2) 默认值[((0))]<br>
     */
    public BigDecimal getTrdDiscountAmount() {
        return trdDiscountAmount;
    }

    /**
     * DECIMAL(18,2) 默认值[((0))]<br>
     */
    public void setTrdDiscountAmount(BigDecimal trdDiscountAmount) {
        this.trdDiscountAmount = trdDiscountAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PP_PaymentCheck
     *
     * @mbggenerated Mon Feb 27 18:24:23 CST 2017
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", paymentCheckId=").append(paymentCheckId);
        sb.append(", paymentChannel=").append(paymentChannel);
        sb.append(", uploadTime=").append(uploadTime);
        sb.append(", uploadFile=").append(uploadFile);
        sb.append(", originalFileName=").append(originalFileName);
        sb.append(", ymtPayNum=").append(ymtPayNum);
        sb.append(", trdPayNum=").append(trdPayNum);
        sb.append(", ymtPayAmount=").append(ymtPayAmount);
        sb.append(", trdPayAmount=").append(trdPayAmount);
        sb.append(", finishTime=").append(finishTime);
        sb.append(", checkStatus=").append(checkStatus);
        sb.append(", checkAccount=").append(checkAccount);
        sb.append(", serverIp=").append(serverIp);
        sb.append(", remark=").append(remark);
        sb.append(", ymtDiscountAmount=").append(ymtDiscountAmount);
        sb.append(", trdDiscountAmount=").append(trdDiscountAmount);
        sb.append("]");
        return sb.toString();
    }
}