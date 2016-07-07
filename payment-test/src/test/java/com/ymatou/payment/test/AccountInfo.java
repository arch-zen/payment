/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test;

/**
 * 账户信息
 * 
 * @author wangxudong 2016年7月7日 上午11:46:51
 *
 */
public class AccountInfo {

    private int iCurrencyType;

    private int iAccountType;

    private String sAccountInfoId;

    /**
     * @return the iCurrencyType
     */
    public int getiCurrencyType() {
        return iCurrencyType;
    }

    /**
     * @param iCurrencyType the iCurrencyType to set
     */
    public void setiCurrencyType(int iCurrencyType) {
        this.iCurrencyType = iCurrencyType;
    }

    /**
     * @return the iAccountType
     */
    public int getiAccountType() {
        return iAccountType;
    }

    /**
     * @param iAccountType the iAccountType to set
     */
    public void setiAccountType(int iAccountType) {
        this.iAccountType = iAccountType;
    }

    /**
     * @return the sAccountInfoId
     */
    public String getsAccountInfoId() {
        return sAccountInfoId;
    }

    /**
     * @param sAccountInfoId the sAccountInfoId to set
     */
    public void setsAccountInfoId(String sAccountInfoId) {
        this.sAccountInfoId = sAccountInfoId;
    }
}
