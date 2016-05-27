/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.integration.service.ymatou;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.payment.integration.model.AccountingItem;
import com.ymatou.payment.integration.model.AccountingRequest;
import com.ymatou.payment.integration.model.AccountingResponse;
import com.ymatou.payment.integration.service.ymatou.AccountService;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 
 * @author qianmin 2016年5月26日 下午3:56:21
 *
 */
public class AccountServiceTest extends RestBaseTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void testAccounting() throws IOException {
        List<AccountingItem> itemList = new ArrayList<>();
        AccountingItem item = new AccountingItem();
        item.setAccountId("01010099735467");
        item.setAccountingDate(new Date());
        item.setAccountOperateType("2");
        item.setAccountType(1);
        item.setAmount("2.30");
        item.setBizCode("100001");
        item.setBizNo("061c68c5-04c8-458a-9859-b71614e6bc75");
        item.setCurrencyType(1);
        item.setMemo("unit test");
        item.setOperator("tester");
        item.setOriginalNo("7238ce81-9862-4b8f-8d91-4d876cf9568G");
        item.setUserId(99735467);
        itemList.add(item);

        AccountingRequest request = new AccountingRequest();
        request.setAccountingItems(itemList);
        request.setAppId("unit test");

        AccountingResponse response = accountService.accounting(request, null);
        System.out.println(response.getExceptionId() + " " + response.getMessage()
                + " " + response.getStackTrace() + " " + response.getStatusCode());
        Assert.assertEquals("4", response.getStatusCode()); // 重复出入账
    }

    @Test
    public void testAccounting2() throws IOException {
        List<AccountingItem> itemList = new ArrayList<>();
        AccountingItem item = new AccountingItem();
        item.setAccountId("01010099735467");
        item.setAccountingDate(new Date());
        item.setAccountOperateType("2");
        item.setAccountType(1);
        item.setAmount("0.01");
        item.setBizCode("100001");
        item.setBizNo(UUID.randomUUID().toString());
        item.setCurrencyType(1);
        item.setMemo("unit test");
        item.setOperator("tester");
        item.setOriginalNo(UUID.randomUUID().toString());
        item.setUserId(99735467);
        itemList.add(item);

        AccountingRequest request = new AccountingRequest();
        request.setAccountingItems(itemList);
        request.setAppId("unit test");

        AccountingResponse response = accountService.accounting(request, null);
        System.out.println(response.getExceptionId() + " " + response.getMessage()
                + " " + response.getStackTrace() + " " + response.getStatusCode());
        Assert.assertEquals("0", response.getStatusCode());
    }
}
