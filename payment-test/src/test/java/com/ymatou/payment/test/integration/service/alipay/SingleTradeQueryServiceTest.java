/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.integration.service.alipay;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.payment.integration.model.SingleTradeQueryRequest;
import com.ymatou.payment.integration.model.SingleTradeQueryResponse;
import com.ymatou.payment.integration.service.alipay.SingleTradeQueryService;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 
 * @author qianmin 2016年5月20日 下午7:03:13
 *
 */
public class SingleTradeQueryServiceTest extends RestBaseTest {

    @Autowired
    private SingleTradeQueryService singleTradeQueryService;

    @Test
    public void testDoServiceSuccessRSA() throws Exception {
        SingleTradeQueryRequest request = new SingleTradeQueryRequest();
        request.setService("single_trade_query");
        request.setPartner("2088701734809577");
        request.set_input_charset("utf-8");
        request.setSign_type("RSA");
        request.setOut_trade_no("21923497900951602");
        request.setSign(
                "Sgxa3uTDEghEF9IccXbuebdlCbpDvu/cvj7ybU6cPlNnjifS8x6B8Au+6VFt7IiZJVth0+1wvwjqAd7TK5GOqyalHvyAmrXBP9kYwirTBi0FkwksWEEGRyQv/51sjclgEoPlMI8ixdo3kKevs3Jh8ScGmrKD3vxzZsojjc0Mbr4=");
        SingleTradeQueryResponse response = singleTradeQueryService.doService(request, null);

        Assert.assertEquals("T", response.getIs_success());
        Assert.assertEquals("79.00", response.getPrice());
    }

    @Test
    public void testDoServiceSuccessMD5() throws Exception {
        SingleTradeQueryRequest request = new SingleTradeQueryRequest();
        request.setService("single_trade_query");
        request.setPartner("2088701734809577");
        request.set_input_charset("utf-8");
        request.setSign_type("MD5");
        request.setOut_trade_no("21923281900628365");
        request.setSign("3f277e5ff720db27127808fca23a4a39");

        SingleTradeQueryResponse response = singleTradeQueryService.doService(request, null);
        Assert.assertEquals("T", response.getIs_success());
        Assert.assertEquals("521.50", response.getPrice());
    }
}
