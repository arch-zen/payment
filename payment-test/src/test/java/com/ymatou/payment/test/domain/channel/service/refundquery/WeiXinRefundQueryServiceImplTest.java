/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.domain.channel.service.refundquery;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.payment.domain.channel.service.refundquery.WeiXinRefundQueryServiceImpl;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.repository.PaymentRepository;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.infrastructure.Money;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;
import com.ymatou.payment.test.RestBaseTest;


public class WeiXinRefundQueryServiceImplTest extends RestBaseTest {

    @Autowired
    private WeiXinRefundQueryServiceImpl service;

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    public void testQueryRefundWhenSystemError() {

        RefundRequestPo refundRequestPo = getRefundRequestBy(RefundStatusEnum.REFUND_FAILED);
        Payment payment = paymentRepository.getByPaymentId(refundRequestPo.getPaymentId());

        HashMap<String, String> header = buildMockHeader();
        header.put("MockResult-WeiXin-return_code", "FAIL");
        header.put("MockResult-WeiXin-return_msg", "System Error");

        RefundStatusEnum refundStatusEnum = service.queryRefund(refundRequestPo, payment, header);

        assertEquals(RefundStatusEnum.REFUND_FAILED, refundStatusEnum);
    }

    @Test
    public void testQueryRefundMultiProccess() {

        RefundRequestPo refundRequestPo = getRefundRequestBy(RefundStatusEnum.REFUND_FAILED);
        Payment payment = paymentRepository.getByPaymentId(refundRequestPo.getPaymentId());

        HashMap<String, String> header = buildMockHeader();
        header.put("MockResult-WeiXin-refund_status", "PROCESSING");
        header.put("MockResult-WeiXin-refund_fee",
                String.valueOf(new Money(refundRequestPo.getRefundAmount()).getCent()));
        header.put("MockResult-WeiXin-refund_count", "2");

        RefundStatusEnum refundStatusEnum = service.queryRefund(refundRequestPo, payment, header);

        assertEquals(RefundStatusEnum.WAIT_THIRDPART_REFUND, refundStatusEnum);
    }
}
