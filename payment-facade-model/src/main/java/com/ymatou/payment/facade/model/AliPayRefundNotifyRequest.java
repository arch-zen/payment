/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.ymatou.payment.facade.BaseRequest;

/**
 * 支付宝异步退款通知request
 * 
 * @author qianmin 2016年5月16日 下午5:26:07
 * 
 */
public class AliPayRefundNotifyRequest extends BaseRequest {

    private static final long serialVersionUID = 6547133957669831614L;

    @NotNull
    private String notifyTime;

    @NotEmpty
    private String notifyType;

    @NotEmpty
    private String notifyId;

    @NotEmpty
    private String signType;

    @NotEmpty
    private String sign;

    @NotEmpty
    private String batchNo;

    @NotEmpty
    private String successNum;

    @NotEmpty
    private String resultDetails;

    private String payType;

    private HashMap<String, String> mockHeader;

    /**
     * @return the notifyTime
     */
    public String getNotifyTime() {
        return notifyTime;
    }

    /**
     * @param notifyTime the notifyTime to set
     */
    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    /**
     * @return the notifyType
     */
    public String getNotifyType() {
        return notifyType;
    }

    /**
     * @param notifyType the notifyType to set
     */
    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    /**
     * @return the notifyId
     */
    public String getNotifyId() {
        return notifyId;
    }

    /**
     * @param notifyId the notifyId to set
     */
    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    /**
     * @return the signType
     */
    public String getSignType() {
        return signType;
    }

    /**
     * @param signType the signType to set
     */
    public void setSignType(String signType) {
        this.signType = signType;
    }

    /**
     * @return the sign
     */
    public String getSign() {
        return sign;
    }

    /**
     * @param sign the sign to set
     */
    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * @return the batchNo
     */
    public String getBatchNo() {
        return batchNo;
    }

    /**
     * @param batchNo the batchNo to set
     */
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    /**
     * @return the successNum
     */
    public String getSuccessNum() {
        return successNum;
    }

    /**
     * @param successNum the successNum to set
     */
    public void setSuccessNum(String successNum) {
        this.successNum = successNum;
    }

    /**
     * @return the resultDetails
     */
    public String getResultDetails() {
        return resultDetails;
    }

    /**
     * @param resultDetails the resultDetails to set
     */
    public void setResultDetails(String resultDetails) {
        this.resultDetails = resultDetails;
    }

    /**
     * @return the payType
     */
    public String getPayType() {
        return payType;
    }

    /**
     * @param payType the payType to set
     */
    public void setPayType(String payType) {
        this.payType = payType;
    }

    /**
     * @return the mockHeader
     */
    public HashMap<String, String> getMockHeader() {
        return mockHeader;
    }

    /**
     * @param mockHeader the mockHeader to set
     */
    public void setMockHeader(HashMap<String, String> mockHeader) {
        this.mockHeader = mockHeader;
    }


}
