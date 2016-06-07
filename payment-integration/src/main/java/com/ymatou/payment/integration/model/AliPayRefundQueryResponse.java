/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

/**
 * 支付宝退款查询接口Response
 * 
 * @author qianmin 2016年5月31日 下午2:50:35
 *
 */
public class AliPayRefundQueryResponse {
    /**
     * 是否成功(T：查询成功； F：查询失败)
     */
    private String isSuccess;
    /**
     * 退款结果明细
     */
    private String resultDetails;
    /**
     * 解冻结果明细
     */
    private String unfreezedDetails;
    /**
     * 错误代码
     */
    private String errorCode;
    /**
     * 签名方式
     */
    private String signType;
    /**
     * 签名
     */
    private String sign;

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getResultDetails() {
        return resultDetails;
    }

    public void setResultDetails(String resultDetails) {
        this.resultDetails = resultDetails;
    }

    public String getUnfreezedDetails() {
        return unfreezedDetails;
    }

    public void setUnfreezedDetails(String unfreezedDetails) {
        this.unfreezedDetails = unfreezedDetails;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
