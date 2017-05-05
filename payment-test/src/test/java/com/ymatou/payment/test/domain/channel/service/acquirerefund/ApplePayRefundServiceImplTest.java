package com.ymatou.payment.test.domain.channel.service.acquirerefund;

import com.ymatou.payment.domain.channel.service.AcquireRefundService;
import com.ymatou.payment.domain.channel.service.acquirerefund.RefundServiceFactory;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.repository.PaymentRepository;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;
import com.ymatou.payment.test.RestBaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * Created by zhangxiaoming on 2017/5/3.
 */
public class ApplePayRefundServiceImplTest extends RestBaseTest {
    @Resource
    private RefundServiceFactory refundServiceFactory;
    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    public void notifyRefundMockTest() {
        AcquireRefundService acquireRefundService = refundServiceFactory.getInstanceByPayType(PayTypeEnum.ApplePay);
        Assert.assertNotNull(acquireRefundService);

        RefundRequestPo refundRequestPo = getRefundRequestBy(RefundStatusEnum.REFUND_FAILED);
        Payment payment = paymentRepository.getByPaymentId(refundRequestPo.getPaymentId());
        HashMap<String, String> header = new HashMap<>();
        header.put("mock", "1");
        header.put("MockResult-ApplePay-respCode", "00");
        RefundStatusEnum refundStatusEnum = acquireRefundService.notifyRefund(refundRequestPo, payment, header);
        Assert.assertEquals(RefundStatusEnum.COMMIT, refundStatusEnum);

        header.put("MockResult-ApplePay-respCode", "01");
        refundStatusEnum = acquireRefundService.notifyRefund(refundRequestPo, payment, header);
        Assert.assertEquals(RefundStatusEnum.REFUND_FAILED, refundStatusEnum);
    }

    @Test
    public void notifyRefundTest() {
        AcquireRefundService acquireRefundService = refundServiceFactory.getInstanceByPayType(PayTypeEnum.ApplePay);
        Assert.assertNotNull(acquireRefundService);

        RefundRequestPo refundRequestPo = getRefundRequestBy(RefundStatusEnum.REFUND_FAILED);
        Payment payment = paymentRepository.getByPaymentId(refundRequestPo.getPaymentId());
        HashMap<String, String> header = new HashMap<>();
        refundRequestPo.setPayType(PayTypeEnum.ApplePay.getCode());
        payment.setPayType(PayTypeEnum.ApplePay);
        RefundStatusEnum refundStatusEnum = acquireRefundService.notifyRefund(refundRequestPo, payment, header);
        Assert.assertEquals(RefundStatusEnum.REFUND_FAILED, refundStatusEnum);

    }
}
