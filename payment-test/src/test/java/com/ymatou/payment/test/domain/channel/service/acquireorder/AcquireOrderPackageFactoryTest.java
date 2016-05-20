/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.domain.channel.service.acquireorder;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.Test;

import com.ymatou.payment.domain.channel.service.AcquireOrderService;
import com.ymatou.payment.domain.channel.service.acquireorder.AcquireOrderPackageFactory;
import com.ymatou.payment.domain.channel.service.acquireorder.AliPayAppAcquireOrderServiceImpl;
import com.ymatou.payment.domain.channel.service.acquireorder.AliPayPcAcquireOrderServiceImpl;
import com.ymatou.payment.domain.channel.service.acquireorder.WeiXinAppAcquireOrderServiceImpl;
import com.ymatou.payment.domain.channel.service.acquireorder.WeiXinJSAPIAcquireOrderServiceImpl;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.test.RestBaseTest;

public class AcquireOrderPackageFactoryTest extends RestBaseTest {
    @Resource
    AcquireOrderPackageFactory acquireOrderPackageFactory;

    @Test(expected = BizException.class)
    public void testGetInstanceThrowExecptionWhenPayTypeIsNull() {
        String payType = null;

        acquireOrderPackageFactory.getInstance(payType);
    }

    @Test(expected = BizException.class)
    public void testGetInstanceThrowExecptionWhenPayTypeIsInvalidate() {
        String payType = "99";

        acquireOrderPackageFactory.getInstance(payType);
    }

    @Test
    public void testGetInstance() {
        AcquireOrderService aoService = acquireOrderPackageFactory.getInstance("10");
        assertEquals("10", AliPayPcAcquireOrderServiceImpl.class, aoService.getClass());

        AcquireOrderService aoService1 = acquireOrderPackageFactory.getInstance("13");
        assertEquals("13", AliPayAppAcquireOrderServiceImpl.class, aoService1.getClass());

        AcquireOrderService aoService2 = acquireOrderPackageFactory.getInstance("14");
        assertEquals("14", WeiXinJSAPIAcquireOrderServiceImpl.class, aoService2.getClass());

        AcquireOrderService aoService3 = acquireOrderPackageFactory.getInstance("15");
        assertEquals("15", WeiXinAppAcquireOrderServiceImpl.class, aoService3.getClass());
    }

}
