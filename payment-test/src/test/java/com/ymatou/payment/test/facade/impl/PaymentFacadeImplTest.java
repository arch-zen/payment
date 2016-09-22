/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.facade.impl;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.Test;

import com.ymatou.payment.facade.PaymentFacade;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireOrderResp;
import com.ymatou.payment.test.RestBaseTest;

/**
 * @author wangxudong 2016年7月14日 下午2:07:30
 *
 */
public class PaymentFacadeImplTest extends RestBaseTest {
    @Resource(name = "paymentFacadeClient")
    private PaymentFacade paymentFacade;

    @Test
    public void acquireOrderTest() {
        AcquireOrderReq req = new AcquireOrderReq();


        AcquireOrderResp resp = paymentFacade.acquireOrder(req);

        assertEquals("验证Success", false, resp.getIsSuccess());
    }
}
