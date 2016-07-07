/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

import com.ymatou.payment.facade.constants.AccountingStatusEnum;

/**
 * 资金出入账接口Response
 * 
 * @author qianmin 2016年5月26日 下午4:28:33
 *
 */
public class AccountingResponse {
    public static final String ACCOUNTING_SUCCESS = "0"; // 成功
    public static final String ACCOUNTING_IDEMPOTENTE = "4"; // 幂等
    public static final String AccountingCode_SYSTEMERROR = "3"; // 系统异常
    public static final String BALANCE_LIMIT = "9002";

    private String exceptionId;
    private String message;
    private String stackTrace;
    private String statusCode;

    public String getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(String exceptionId) {
        this.exceptionId = exceptionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public AccountingStatusEnum getAccountingStatus() {
        AccountingStatusEnum accountingStatus;
        switch (statusCode) {
            case ACCOUNTING_SUCCESS:
            case ACCOUNTING_IDEMPOTENTE:
                accountingStatus = AccountingStatusEnum.SUCCESS;
                break;
            case AccountingCode_SYSTEMERROR:
                accountingStatus = AccountingStatusEnum.UNKNOW;
                break;
            default:
                accountingStatus = AccountingStatusEnum.FAIL;
                break;
        }
        return accountingStatus;
    }

    public int isAccoutingSuccess() {
        if (ACCOUNTING_SUCCESS.equals(statusCode) || ACCOUNTING_IDEMPOTENTE.equals(statusCode)) {
            return 1;
        } else {
            return 0;
        }
    }
}
