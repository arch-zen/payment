package com.ymatou.payment.test.domain.channel.service.refundquery;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.service.RefundQueryService;
import com.ymatou.payment.domain.channel.service.refundquery.RefundQueryServiceFactory;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.repository.PaymentRepository;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.infrastructure.Money;
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
public class ApplePayRefundQueryServiceImplTest extends RestBaseTest {
    @Resource
    private RefundQueryServiceFactory refundQueryServiceFactory;
    @Autowired
    private PaymentRepository paymentRepository;
    @Resource
    private InstitutionConfigManager configManager;

    @Test
    public void queryRefundTest() {
        RefundQueryService refundQueryService = refundQueryServiceFactory.getInstanceByPayType(PayTypeEnum.ApplePay);
        Assert.assertNotNull(refundQueryService);

        RefundRequestPo refundRequestPo = getRefundRequestBy(RefundStatusEnum.REFUND_FAILED);
        Payment payment = paymentRepository.getByPaymentId(refundRequestPo.getPaymentId());
        HashMap<String, String> header = new HashMap<>();
        header.put("mock", "1");
        header.put("MockResult-ApplePay-respCode", "00");
        header.put("MockResult-ApplePay-origRespCode", "00");
        RefundStatusEnum refundStatusEnum = refundQueryService.queryRefund(refundRequestPo, payment, header);
        Assert.assertEquals(RefundStatusEnum.REFUND_FAILED, refundStatusEnum);


        InstitutionConfig config = configManager.getConfig(payment.getPayType());
        Money money = new Money(refundRequestPo.getRefundAmount());
        header.put("MockResult-ApplePay-orderId", refundRequestPo.getRefundBatchNo());
        header.put("MockResult-ApplePay-txnAmt", String.valueOf(money.getCent()));
        header.put("MockResult-ApplePay-currencyCode", "156");
        header.put("MockResult-ApplePay-merId", config.getMerchantId());

        refundStatusEnum = refundQueryService.queryRefund(refundRequestPo, payment, header);
        Assert.assertEquals(RefundStatusEnum.THIRDPART_REFUND_SUCCESS, refundStatusEnum);

    }
}
