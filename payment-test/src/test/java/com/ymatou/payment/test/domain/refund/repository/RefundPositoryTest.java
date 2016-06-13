/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.domain.refund.repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.payment.domain.refund.repository.RefundPository;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 
 * @author qianmin 2016年6月3日 下午5:00:38
 *
 */
public class RefundPositoryTest extends RestBaseTest {

    @Autowired
    private RefundPository refundPository;

    @Test
    public void testGenerateRefundBatchNo() {
        String refundBatchNo = refundPository.generateRefundBatchNo();
        System.out.println(refundBatchNo);
        Assert.assertEquals(18, refundBatchNo.length());
    }

}
