/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test;

import java.math.BigDecimal;

public class AccountEntry {

    private String bizCode;

    private String bizNo;

    private BigDecimal amount;

    private String accountId;

    /**
     * @return the bizCode
     */
    public String getBizCode() {
        return bizCode;
    }

    /**
     * @param bizCode the bizCode to set
     */
    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    /**
     * @return the bizNo
     */
    public String getBizNo() {
        return bizNo;
    }

    /**
     * @param bizNo the bizNo to set
     */
    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the accountId
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AccountEntry [bizCode=" + bizCode + ", bizNo=" + bizNo + ", amount=" + amount + ", accountId="
                + accountId + "]";
    }
}
