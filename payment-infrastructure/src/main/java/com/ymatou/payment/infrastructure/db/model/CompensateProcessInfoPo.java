package com.ymatou.payment.infrastructure.db.model;

import java.util.Date;

public class CompensateProcessInfoPo {
    /**
     * BIGINT(19) 必填<br>
     * 
     */
    private Long compensateId;

    /**
     * VARCHAR(64) 必填<br>
     * 
     */
    private String correlateId;

    /**
     * VARCHAR(16) 必填<br>
     * 
     */
    private String appId;

    /**
     * VARCHAR(16)<br>
     * 
     */
    private String payType;

    /**
     * VARCHAR(32) 必填<br>
     * 
     */
    private String methodName;

    /**
     * VARCHAR(256) 必填<br>
     * 
     */
    private String requestUrl;

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     * 
     */
    private Integer retryCount;

    /**
     * TIMESTAMP(23,3) 默认值[(getdate())] 必填<br>
     * 
     */
    private Date createdTime;

    /**
     * VARCHAR(64)<br>
     * 
     */
    private String processMachineName;

    /**
     * TIMESTAMP(23,3)<br>
     * 
     */
    private Date lastProcessedTime;

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     * 
     */
    private Integer processStatus;

    /**
     * INTEGER(10)<br>
     * 
     */
    private Integer compensateType;

    /**
     * CLOB(2147483647) 必填<br>
     * 
     */
    private String requestData;

    /**
     * BIGINT(19) 必填<br>
     */
    public Long getCompensateId() {
        return compensateId;
    }

    /**
     * BIGINT(19) 必填<br>
     */
    public void setCompensateId(Long compensateId) {
        this.compensateId = compensateId;
    }

    /**
     * VARCHAR(64) 必填<br>
     */
    public String getCorrelateId() {
        return correlateId;
    }

    /**
     * VARCHAR(64) 必填<br>
     */
    public void setCorrelateId(String correlateId) {
        this.correlateId = correlateId == null ? null : correlateId.trim();
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
     * VARCHAR(16)<br>
     */
    public String getPayType() {
        return payType;
    }

    /**
     * VARCHAR(16)<br>
     */
    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    /**
     * VARCHAR(32) 必填<br>
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * VARCHAR(32) 必填<br>
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName == null ? null : methodName.trim();
    }

    /**
     * VARCHAR(256) 必填<br>
     */
    public String getRequestUrl() {
        return requestUrl;
    }

    /**
     * VARCHAR(256) 必填<br>
     */
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl == null ? null : requestUrl.trim();
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public Integer getRetryCount() {
        return retryCount;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
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
     * VARCHAR(64)<br>
     */
    public String getProcessMachineName() {
        return processMachineName;
    }

    /**
     * VARCHAR(64)<br>
     */
    public void setProcessMachineName(String processMachineName) {
        this.processMachineName = processMachineName == null ? null : processMachineName.trim();
    }

    /**
     * TIMESTAMP(23,3)<br>
     */
    public Date getLastProcessedTime() {
        return lastProcessedTime;
    }

    /**
     * TIMESTAMP(23,3)<br>
     */
    public void setLastProcessedTime(Date lastProcessedTime) {
        this.lastProcessedTime = lastProcessedTime;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public Integer getProcessStatus() {
        return processStatus;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }

    /**
     * INTEGER(10)<br>
     */
    public Integer getCompensateType() {
        return compensateType;
    }

    /**
     * INTEGER(10)<br>
     */
    public void setCompensateType(Integer compensateType) {
        this.compensateType = compensateType;
    }

    /**
     * CLOB(2147483647) 必填<br>
     */
    public String getRequestData() {
        return requestData;
    }

    /**
     * CLOB(2147483647) 必填<br>
     */
    public void setRequestData(String requestData) {
        this.requestData = requestData == null ? null : requestData.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PP_CompensateProcessInfo
     *
     * @mbggenerated Wed Jul 06 15:05:05 CST 2016
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", compensateId=").append(compensateId);
        sb.append(", correlateId=").append(correlateId);
        sb.append(", appId=").append(appId);
        sb.append(", payType=").append(payType);
        sb.append(", methodName=").append(methodName);
        sb.append(", requestUrl=").append(requestUrl);
        sb.append(", retryCount=").append(retryCount);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", processMachineName=").append(processMachineName);
        sb.append(", lastProcessedTime=").append(lastProcessedTime);
        sb.append(", processStatus=").append(processStatus);
        sb.append(", compensateType=").append(compensateType);
        sb.append(", requestData=").append(requestData);
        sb.append("]");
        return sb.toString();
    }
}