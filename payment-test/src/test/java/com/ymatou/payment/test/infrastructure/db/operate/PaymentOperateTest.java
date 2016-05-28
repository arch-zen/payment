/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.infrastructure.db.operate;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.Test;

import com.ymatou.payment.infrastructure.db.operate.PaymentOperate;
import com.ymatou.payment.test.RestBaseTest;

public class PaymentOperateTest extends RestBaseTest {

    @Resource
    private PaymentOperate paymentOperate;

    @Test
    public void testGetPaymentSuffixId() {
        long paymentSuffixId = paymentOperate.genPaymentSuffixId();

        assertEquals("验证生成的支付单号", true, paymentSuffixId > 0);
        System.out.println(paymentSuffixId);
    }
}
