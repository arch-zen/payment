package com.ymatou.payment.test.integration.service.applepay;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.infrastructure.util.StringUtil;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.model.ApplePayConsumeQueryRequest;
import com.ymatou.payment.integration.model.ApplePayTradeQueryResponse;
import com.ymatou.payment.integration.service.applepay.ApplePayTradeQueryService;
import com.ymatou.payment.integration.service.applepay.common.ApplePayConstants;
import com.ymatou.payment.integration.service.applepay.common.ApplePaySignatureUtil;
import com.ymatou.payment.test.RestBaseTest;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Random;

/**
 * Created by gejianhua on 2017/4/28.
 */
public class ApplePayTradeQueryServiceTest extends RestBaseTest {
    @Resource
    private ApplePayTradeQueryService applePayTradeQueryService;

    @Resource
    private InstitutionConfigManager instConfigManager;

    @Resource
    private IntegrationConfig integrationConfig;

    @Test
    public void testTradeQueryOrderNotExists() {
        InstitutionConfig config = this.instConfigManager.getConfig(PayTypeEnum.ApplePay);

        ApplePayConsumeQueryRequest request = new ApplePayConsumeQueryRequest();
        request.setTxnTime(StringUtil.getDateFormatString());
        request.setOrderId(String.valueOf(10000000 + new Random().nextInt(100000000)));
        request.setMerId(config.getMerchantId());
        request.setCertId(config.getCertId());

        String sign = ApplePaySignatureUtil.sign(request.genMap(), config.getInstYmtPrivateKey());
        request.setSignature(sign);

        ApplePayTradeQueryResponse response =
                this.applePayTradeQueryService.doPost(request, null);

        System.out.println("respcode:" + response.getRespCode());
        System.out.println("respmsg:" + response.getRespMsg());
        System.out.println("originrespcode:" + response.getOrigRespCode());
        System.out.println("originrespmsg:" + response.getOrigRespMsg());

        Assert.assertNotEquals(ApplePayConstants.response_success_code, response.getRespCode());

        //验签
        boolean flag = ApplePaySignatureUtil.validate(response.getOriginMap(), config.getInstPublicKey());
        Assert.assertEquals(true, flag);
    }
}













































