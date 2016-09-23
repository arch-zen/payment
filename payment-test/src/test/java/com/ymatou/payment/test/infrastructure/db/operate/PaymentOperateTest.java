/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.infrastructure.db.operate;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.ymatou.payment.infrastructure.db.operate.PaymentOperate;
import com.ymatou.payment.infrastructure.util.StringUtil;
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

    @Test
    public void testGetFullPaymentId() {
        long paymentSuffixId = paymentOperate.genPaymentSuffixId();
        // long paymentSuffixId = 1;
        String prefix = StringUtil.getDateFormatString("yyMMddHHmmss");
        String suffix = String.format("%05d", paymentSuffixId);
        String paymentId = prefix + StringUtils.right(suffix, 5);

        System.out.println(suffix);
        System.out.println(paymentId);
        assertEquals("验证生成的支付单号", 17, paymentId.length());
    }
}
