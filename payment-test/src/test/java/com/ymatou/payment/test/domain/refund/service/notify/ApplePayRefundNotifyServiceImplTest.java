package com.ymatou.payment.test.domain.refund.service.notify;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.repository.PaymentRepository;
import com.ymatou.payment.domain.refund.service.notify.RefundNotifyService;
import com.ymatou.payment.domain.refund.service.notify.RefundNotifyServiceFactory;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.facade.model.RefundNotifyRequest;
import com.ymatou.payment.infrastructure.Money;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.service.applepay.common.ApplePayConstants;
import com.ymatou.payment.integration.service.applepay.common.ApplePayMessageUtil;
import com.ymatou.payment.integration.service.applepay.common.ApplePaySignatureUtil;
import com.ymatou.payment.test.RestBaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxiaoming on 2017/5/3.
 */
public class ApplePayRefundNotifyServiceImplTest extends RestBaseTest {
    @Resource
    private RefundNotifyServiceFactory refundNotifyServiceFactory;

    @Resource
    private InstitutionConfigManager instConfigManager;

    @Resource
    private IntegrationConfig integrationConfig;

    @Resource
    private SignatureService signatureService;
    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    public void processRefundCallbackTest() throws UnsupportedEncodingException {
        RefundRequestPo refundRequestPo = getRefundRequestBy(RefundStatusEnum.REFUND_FAILED);
        Payment payment = paymentRepository.getByPaymentId(refundRequestPo.getPaymentId());

        InstitutionConfig config = instConfigManager.getConfig(PayTypeEnum.ApplePay);
        RefundNotifyRequest request = new RefundNotifyRequest();
        request.setPayType(PayTypeEnum.ApplePay.getCode());
        request.setMockHeader(new HashMap<>());
        request.getMockHeader().put("mock", "1");

        Map<String, String> parameters = new HashMap<>();
        parameters.put("merId", "afds");
        parameters.put("orderId", "fdsaf");
        parameters.put("txnAmt", "fdsa");
        parameters.put("currencyCode", "fdsa");
        parameters.put("respCode", "00");
        String privateKey = config.getInstYmtPrivateKey();
        if (integrationConfig.isMock(request.getMockHeader())) {
            privateKey = this.signatureService.getMockPrivateKey();
        }
        String sign = ApplePaySignatureUtil.sign(parameters, privateKey);
        parameters.put("signature", sign);
        String body = ApplePayMessageUtil.genRequestMessage(parameters);
        body = URLDecoder.decode(body, ApplePayConstants.encoding);
        request.setBody(body);

        RefundNotifyService refundNotifyService = refundNotifyServiceFactory.createRefundNotifyService(request);
        Assert.assertNotNull(refundNotifyService);

        try {
            refundNotifyService.processRefundCallback(request);
            Assert.fail("此处应该有异常！");
        } catch (BizException ex) {
            System.out.print("\n" + ex.getMessage() + "\n");
        }

        parameters.put("merId", config.getMerchantId());

        sign = ApplePaySignatureUtil.sign(parameters, privateKey);
        parameters.put("signature", sign);
        body = ApplePayMessageUtil.genRequestMessage(parameters);
        body = URLDecoder.decode(body, ApplePayConstants.encoding);
        request.setBody(body);

        try {
            refundNotifyService.processRefundCallback(request);
            Assert.fail("此处应该有异常！");
        } catch (BizException ex) {
            System.out.print("\n" + ex.getMessage() + "\n");
        }

        parameters.put("orderId", refundRequestPo.getRefundBatchNo());

        sign = ApplePaySignatureUtil.sign(parameters, privateKey);
        parameters.put("signature", sign);
        body = ApplePayMessageUtil.genRequestMessage(parameters);
        body = URLDecoder.decode(body, ApplePayConstants.encoding);
        request.setBody(body);

        try {
            refundNotifyService.processRefundCallback(request);
            Assert.fail("此处应该有异常！");
        } catch (BizException ex) {
            System.out.print("\n" + ex.getMessage() + "\n");
        }

        Money money = new Money(refundRequestPo.getRefundAmount());
        parameters.put("txnAmt", String.valueOf(money.getCent()));

        sign = ApplePaySignatureUtil.sign(parameters, privateKey);
        parameters.put("signature", sign);
        body = ApplePayMessageUtil.genRequestMessage(parameters);
        body = URLDecoder.decode(body, ApplePayConstants.encoding);
        request.setBody(body);

        try {
            refundNotifyService.processRefundCallback(request);
            Assert.fail("此处应该有异常！");
        } catch (BizException ex) {
            System.out.print("\n" + ex.getMessage() + "\n");
        }

        parameters.put("currencyCode", ApplePayConstants.currency_code);
        sign = ApplePaySignatureUtil.sign(parameters, privateKey);
        parameters.put("signature", sign);
        body = ApplePayMessageUtil.genRequestMessage(parameters);
        body = URLDecoder.decode(body, ApplePayConstants.encoding);
        request.setBody(body);

        try {
            refundNotifyService.processRefundCallback(request);

        } catch (BizException ex) {
            Assert.fail("此处不应该有异常！");
        }
    }
}
