/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

/**
 * 用户服务应答model
 * 
 * @author qianmin 2016年5月9日 上午10:42:18
 *
 */
public class UserServiceResponse {

    private String ResponseCode;
    private String Success;
    private String Result;

    private String SuccessMessage;
    private String LastErrorMessage;

    public String getSuccessMessage() {
        return SuccessMessage;
    }

    public void setSuccessMessage(String successMessage) {
        SuccessMessage = successMessage;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String success) {
        Success = success;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getLastErrorMessage() {
        return LastErrorMessage;
    }

    public void setLastErrorMessage(String lastErrorMessage) {
        LastErrorMessage = lastErrorMessage;
    }
}
