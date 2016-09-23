/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.integration.service.alipay;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.integration.model.AliPayRefundQueryRequest;
import com.ymatou.payment.integration.model.AliPayRefundQueryResponse;
import com.ymatou.payment.integration.service.alipay.AliPayRefundQueryService;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 
 * @author qianmin 2016年5月31日 下午3:00:03
 *
 */
public class AliPayRefundQueryServiceTest extends RestBaseTest {

    @Autowired
    private AliPayRefundQueryService refundQueryService;

    @Autowired
    private SignatureService signatureService;

    @Autowired
    private InstitutionConfigManager institutionConfigManager;

    @Test
    public void testDoService() throws Exception {
        AliPayRefundQueryRequest request = new AliPayRefundQueryRequest();
        request.setPartner("2088701734809577");
        request.setSignType("RSA");
        request.setBatchNo("201605310000353946");
        request.setTradeNo("2016053121001004390240047883");
        InstitutionConfig instConfig = institutionConfigManager.getConfig(PayTypeEnum.AliPayApp);
        HashMap<String, String> map = request.mapForSign();
        String sign = signatureService.signMessage(map, instConfig, null);

        request.setSign(sign);

        AliPayRefundQueryResponse response = refundQueryService.doService(request, null);
        Assert.assertEquals("T", response.getIsSuccess());
    }

    @Test
    public void testDoServiceWithMock() throws Exception {
        AliPayRefundQueryRequest request = new AliPayRefundQueryRequest();
        request.setPartner("2088701734809577");
        request.setSignType("RSA");
        request.setBatchNo("201605310000353946");
        request.setTradeNo("2016053121001004390240047883");
        InstitutionConfig instConfig = institutionConfigManager.getConfig(PayTypeEnum.AliPayApp);
        HashMap<String, String> map = request.mapForSign();
        String sign = signatureService.signMessage(map, instConfig, null);

        request.setSign(sign);

        HashMap<String, String> mockHeader = buildMockHeader();
        mockHeader.put("MockResult-AliPay-Result",
                "is_success=T&result_details=201510160000000007^2015101621001004480043768719^0.01^SUCCESS^true^S");

        AliPayRefundQueryResponse response = refundQueryService.doService(request, mockHeader);
        Assert.assertEquals("T", response.getIsSuccess());
        Assert.assertEquals("201510160000000007^2015101621001004480043768719^0.01^SUCCESS^true^S",
                response.getResultDetails());
    }

}
