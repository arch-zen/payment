package com.ymatou.payment.infrastructure.db.model;

import java.util.Date;

public class CmbAggrementPo {
    /**
     * BIGINT(19) 必填<br>
     * 
     */
    private Long aggId;

    /**
     * BIGINT(19) 必填<br>
     * 
     */
    private Long userId;

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     * 
     */
    private Integer aggStatus;

    /**
     * VARCHAR(14)<br>
     * 
     */
    private String signDateTime;

    /**
     * VARCHAR(10)<br>
     * 
     */
    private String respCode;

    /**
     * VARCHAR(200)<br>
     * 
     */
    private String respMessage;

    /**
     * VARCHAR(40)<br>
     * 
     */
    private String bankSerialNo;

    /**
     * VARCHAR(1)<br>
     * 
     */
    private String userPidType;

    /**
     * VARCHAR(30)<br>
     * 
     */
    private String userPidHash;

    /**
     * VARCHAR(1)<br>
     * 
     */
    private String noPwdPay;

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     * 
     */
    private Integer cancelType;

    /**
     * VARCHAR(30)<br>
     * 
     */
    private String cancelBankSerialNo;

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
     * BIGINT(19) 必填<br>
     */
    public Long getAggId() {
        return aggId;
    }

    /**
     * BIGINT(19) 必填<br>
     */
    public void setAggId(Long aggId) {
        this.aggId = aggId;
    }

    /**
     * BIGINT(19) 必填<br>
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * BIGINT(19) 必填<br>
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public Integer getAggStatus() {
        return aggStatus;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public void setAggStatus(Integer aggStatus) {
        this.aggStatus = aggStatus;
    }

    /**
     * VARCHAR(14)<br>
     */
    public String getSignDateTime() {
        return signDateTime;
    }

    /**
     * VARCHAR(14)<br>
     */
    public void setSignDateTime(String signDateTime) {
        this.signDateTime = signDateTime == null ? null : signDateTime.trim();
    }

    /**
     * VARCHAR(10)<br>
     */
    public String getRespCode() {
        return respCode;
    }

    /**
     * VARCHAR(10)<br>
     */
    public void setRespCode(String respCode) {
        this.respCode = respCode == null ? null : respCode.trim();
    }

    /**
     * VARCHAR(200)<br>
     */
    public String getRespMessage() {
        return respMessage;
    }

    /**
     * VARCHAR(200)<br>
     */
    public void setRespMessage(String respMessage) {
        this.respMessage = respMessage == null ? null : respMessage.trim();
    }

    /**
     * VARCHAR(40)<br>
     */
    public String getBankSerialNo() {
        return bankSerialNo;
    }

    /**
     * VARCHAR(40)<br>
     */
    public void setBankSerialNo(String bankSerialNo) {
        this.bankSerialNo = bankSerialNo == null ? null : bankSerialNo.trim();
    }

    /**
     * VARCHAR(1)<br>
     */
    public String getUserPidType() {
        return userPidType;
    }

    /**
     * VARCHAR(1)<br>
     */
    public void setUserPidType(String userPidType) {
        this.userPidType = userPidType == null ? null : userPidType.trim();
    }

    /**
     * VARCHAR(30)<br>
     */
    public String getUserPidHash() {
        return userPidHash;
    }

    /**
     * VARCHAR(30)<br>
     */
    public void setUserPidHash(String userPidHash) {
        this.userPidHash = userPidHash == null ? null : userPidHash.trim();
    }

    /**
     * VARCHAR(1)<br>
     */
    public String getNoPwdPay() {
        return noPwdPay;
    }

    /**
     * VARCHAR(1)<br>
     */
    public void setNoPwdPay(String noPwdPay) {
        this.noPwdPay = noPwdPay == null ? null : noPwdPay.trim();
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public Integer getCancelType() {
        return cancelType;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public void setCancelType(Integer cancelType) {
        this.cancelType = cancelType;
    }

    /**
     * VARCHAR(30)<br>
     */
    public String getCancelBankSerialNo() {
        return cancelBankSerialNo;
    }

    /**
     * VARCHAR(30)<br>
     */
    public void setCancelBankSerialNo(String cancelBankSerialNo) {
        this.cancelBankSerialNo = cancelBankSerialNo == null ? null : cancelBankSerialNo.trim();
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
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PP_CmbAggrement
     *
     * @mbggenerated Tue Nov 15 20:02:34 CST 2016
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", aggId=").append(aggId);
        sb.append(", userId=").append(userId);
        sb.append(", aggStatus=").append(aggStatus);
        sb.append(", signDateTime=").append(signDateTime);
        sb.append(", respCode=").append(respCode);
        sb.append(", respMessage=").append(respMessage);
        sb.append(", bankSerialNo=").append(bankSerialNo);
        sb.append(", userPidType=").append(userPidType);
        sb.append(", userPidHash=").append(userPidHash);
        sb.append(", noPwdPay=").append(noPwdPay);
        sb.append(", cancelType=").append(cancelType);
        sb.append(", cancelBankSerialNo=").append(cancelBankSerialNo);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", lastUpdatedTime=").append(lastUpdatedTime);
        sb.append("]");
        return sb.toString();
    }
}