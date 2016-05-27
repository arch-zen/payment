/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

import java.util.List;

/**
 * 资金出入账接口Request
 * 
 * @author qianmin 2016年5月26日 下午3:33:43
 *
 */
public class AccountingRequest {

    /**
     * 项数上限为10，即size<=10
     */
    private List<AccountingItem> accountingItems;
    private String appId;

    public List<AccountingItem> getAccountingItems() {
        return accountingItems;
    }

    public void setAccountingItems(List<AccountingItem> accountingItems) {
        this.accountingItems = accountingItems;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
