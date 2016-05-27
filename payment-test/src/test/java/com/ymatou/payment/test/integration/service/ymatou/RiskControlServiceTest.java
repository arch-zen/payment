/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.integration.service.ymatou;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.payment.integration.model.RiskControlRequest;
import com.ymatou.payment.integration.service.ymatou.RiskControlService;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 
 * @author qianmin 2016年5月26日 下午5:25:23
 *
 */
public class RiskControlServiceTest extends RestBaseTest {

    @Autowired
    private RiskControlService riskControlService;

    @Test
    public void doServiceTest() throws IOException {
        RiskControlRequest request = new RiskControlRequest();
        request.setAppId("12321");
        request.setBizCode("100001");
        request.setExternalUserId("324324");
        request.setInstPaymentId("32432432");
        request.setInternalUserId("324324543");
        request.setOrdeId("5346457658658");
        request.setPayChannel("11");
        request.setPaymentId("24432432432");
        request.setPayPrice("2.30");
        request.setTraceId("wqewqrwrew");
        request.setUserIp("172.16.22.137");

        boolean resp = riskControlService.doService(request, null);
        Assert.assertEquals(true, resp);
    }
}
