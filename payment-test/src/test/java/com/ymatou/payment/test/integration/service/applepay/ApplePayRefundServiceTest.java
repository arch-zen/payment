package com.ymatou.payment.test.integration.service.applepay;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.infrastructure.Money;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.model.ApplePayRefundRequest;
import com.ymatou.payment.integration.model.ApplePayRefundResponse;
import com.ymatou.payment.integration.service.applepay.ApplePayRefundService;
import com.ymatou.payment.integration.service.applepay.common.ApplePayConstants;
import com.ymatou.payment.integration.service.applepay.common.ApplePaySignatureUtil;
import com.ymatou.payment.test.RestBaseTest;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by zhangxiaoming on 2017/4/28.
 */
public class ApplePayRefundServiceTest extends RestBaseTest {
    @Resource
    private InstitutionConfigManager instConfigManager;

    @Resource
    private IntegrationConfig integrationConfig;

    @Resource
    private ApplePayRefundService applePayRefundService;
    @Resource
    private SignatureService singatureService;

    @Test
    public void doServiceTest() {
        InstitutionConfig config = instConfigManager.getConfig(PayTypeEnum.ApplePay);
        ApplePayRefundRequest request = new ApplePayRefundRequest();
        HashMap<String, String> header = new HashMap<>();
        String refundBatchNo = UUID.randomUUID().toString().replace("-", "");
        String instPaymentId = UUID.randomUUID().toString().replace("-", "");
        BigDecimal amount = new BigDecimal("20.56");
        String url = new StringBuilder().append(integrationConfig.getYmtPaymentBaseUrl(header))
                .append("/RefundNotify/").append(PayTypeEnum.ApplePay.getCode()).toString();
        String txnTime = new DateTime().toString(ApplePayConstants.time_format);
        ApplePayRefundRequest applePayRefundRequest = new ApplePayRefundRequest();
        applePayRefundRequest.setMerId(config.getMerchantId());
        applePayRefundRequest.setBackUrl(url);
        applePayRefundRequest.setOrderId(refundBatchNo);
        applePayRefundRequest.setOrigQryId(instPaymentId);
        applePayRefundRequest.setTxnTime(txnTime);
        Money money = new Money(amount);
        applePayRefundRequest.setTxnAmt(String.valueOf(money.getCent()));

        String privateKey = config.getInstYmtPrivateKey();
        if (integrationConfig.isMock(header)) {
            privateKey = singatureService.getMockPrivateKey();
        }

        String sign = ApplePaySignatureUtil.sign(applePayRefundRequest.genMap(), privateKey);
        applePayRefundRequest.setSignature(sign);

        header.put("mock", "1");
        header.put("MockResult-ApplePay-respCode", "00");
        ApplePayRefundResponse response = applePayRefundService.doService(request, header);
        Assert.assertEquals("00", response.getRespCode());

        header.put("MockResult-ApplePay-respCode", "01");
        response = applePayRefundService.doService(request, header);
        Assert.assertEquals("01", response.getRespCode());
    }
}
