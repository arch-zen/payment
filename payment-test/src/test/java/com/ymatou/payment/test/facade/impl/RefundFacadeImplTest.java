/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.facade.impl;

import java.util.Arrays;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ymatou.payment.facade.RefundFacade;
import com.ymatou.payment.facade.model.TradeRefundableRequest;
import com.ymatou.payment.facade.model.TradeRefundableResponse;

/**
 * 
 * @author qianmin 2016年6月22日 下午6:18:33
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContextDubboConsumerTest.xml"})
public class RefundFacadeImplTest {

    @Resource(name = "refundFacadeClient")
    private RefundFacade refundFacade;

    @Test
    public void test() {
        TradeRefundableRequest request = new TradeRefundableRequest();
        request.setTradeNos(Arrays.asList(new String[] {"1111111111"}));
        TradeRefundableResponse response = refundFacade.checkRefundable(request);
        Assert.assertEquals(true, response.getIsSuccess());
    }
}
