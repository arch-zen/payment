/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.integration.service.ymatou;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.payment.integration.model.RefundCallbackRequest;
import com.ymatou.payment.integration.service.ymatou.RefundCallbackService;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 
 * @author qianmin 2016年5月31日 下午4:47:57
 *
 */
public class RefundCallbackServiceTest extends RestBaseTest {

    @Autowired
    private RefundCallbackService refundCallbackService;

    @Test
    public void doServiceTest() throws IOException {
        RefundCallbackRequest request = new RefundCallbackRequest();
        request.setActualRefundAmount(new BigDecimal(0.01));
        request.setAuditor("system");
        request.setOptType(10);
        request.setOrderID(111642639);
        request.setPassAuditTime(new Date());
        request.setRequiredRefundAmount(new BigDecimal(0.01));
        request.setThirdPartyName("Alipay");
        request.setThirdPartyTradingNo("2016053021001004580212614604");
        request.setTradeNo("13912628");
        request.setIsFastRefund(true);

        boolean isSuccess = refundCallbackService.doService(request, false, null);

        Assert.assertEquals(true, isSuccess);
    }

    @Test
    public void doServiceTestWithMock() throws IOException {
        RefundCallbackRequest request = new RefundCallbackRequest();
        request.setActualRefundAmount(new BigDecimal(0.01));
        request.setAuditor("system");
        request.setOptType(10);
        request.setOrderID(111642639);
        request.setPassAuditTime(new Date());
        request.setRequiredRefundAmount(new BigDecimal(0.01));
        request.setThirdPartyName("Alipay");
        request.setThirdPartyTradingNo("2016053021001004580212614604");
        request.setTradeNo("13912628");
        request.setIsFastRefund(true);

        HashMap<String, String> mockHeader = buildMockHeader();
        mockHeader.put("MockResult-Trading-Status", "{'code':'200'}");
        boolean isSuccess = refundCallbackService.doService(request, true, mockHeader);

        Assert.assertEquals(true, isSuccess);
    }


    @Test
    public void doServiceTestWithMock2() throws IOException {
        RefundCallbackRequest request = new RefundCallbackRequest();
        request.setActualRefundAmount(new BigDecimal(0.01));
        request.setAuditor("system");
        request.setOptType(10);
        request.setOrderID(111642639);
        request.setPassAuditTime(new Date());
        request.setRequiredRefundAmount(new BigDecimal(0.01));
        request.setThirdPartyName("Alipay");
        request.setThirdPartyTradingNo("2016053021001004580212614604");
        request.setTradeNo("13912628");
        request.setIsFastRefund(true);

        HashMap<String, String> mockHeader = buildMockHeader();
        mockHeader.put("MockResult-Trading-Status", "FAILD");
        boolean isSuccess = refundCallbackService.doService(request, false, mockHeader);

        Assert.assertEquals(false, isSuccess);
    }


}
