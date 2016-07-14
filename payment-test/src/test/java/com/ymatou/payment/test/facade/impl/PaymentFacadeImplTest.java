/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.facade.impl;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ymatou.payment.facade.PaymentFacade;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireOrderResp;

/**
 * @author wangxudong 2016年7月14日 下午2:07:30
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContextDubboConsumerTest.xml"})
public class PaymentFacadeImplTest {
    @Resource(name = "paymentFacadeClient")
    private PaymentFacade paymentFacade;

    @Test
    public void acquireOrderTest() {
        AcquireOrderReq req = new AcquireOrderReq();


        AcquireOrderResp resp = paymentFacade.acquireOrder(req);

        assertEquals("验证Success", false, resp.getIsSuccess());
    }
}
