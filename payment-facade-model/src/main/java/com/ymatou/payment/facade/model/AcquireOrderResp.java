package com.ymatou.payment.facade.model;

import com.ymatou.payment.facade.BaseResponse;

public class AcquireOrderResp extends BaseResponse {

    /**
     * 序列化版本
     */
    private static final long serialVersionUID = -8592961206186919734L;

    /**
     * 跟踪Id
     */
    private String traceId;

    /**
     * 接口版本
     */
    private int version;

    /**
     * 应用Id
     */
    private String appId;

    /**
     * 返回结果
     */
    private String result;

    /**
     * 返回结果类型
     */
    private String resultType;

    /**
     * @return the traceId
     */
    public String getTraceId() {
        return traceId;
    }

    /**
     * @param traceId the traceId to set
     */
    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * @return the appId
     */
    public String getAppId() {
        return appId;
    }

    /**
     * @param appId the appId to set
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * @return the resultType
     */
    public String getResultType() {
        return resultType;
    }

    /**
     * @param resultType the resultType to set
     */
    public void setResultType(String resultType) {
        this.resultType = resultType;
    }
}
