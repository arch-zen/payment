/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.integration.service.ymatou;

import java.io.IOException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.payment.integration.model.PaymentCallbackRequest;
import com.ymatou.payment.integration.service.ymatou.PaymentCallbackService;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 
 * @author qianmin 2016年6月1日 上午10:53:37
 *
 */
public class PaymentCallbackServiceTest extends RestBaseTest {

    private static final String callbackUrl =
            "http://api.trading.operate.ymatou.com/api/Trading/TradingCompletedNotify";

    @Autowired
    private PaymentCallbackService paymentCallbackService;

    /**
     * {"TradingId":"27095948001237","BizCode":3,"PayPrice":"1.00",
     * "Currency":"CNY","Version":1,"AppId":2,
     * "TraceId":"fc04be7b-bd80-4d4d-81b1-814af832d749",
     * "PayTime":"20160531173454","PaymentId":"17333995000768041",
     * "PayChannel":"Alipay","ExternalUserId":"Yr2x@qq.com",
     * "InstPaymentId":"b431a33a-11bf-4452-85b6-4510bcbaaf91",
     * "Memo":"备注:036","SignMethod":"MD5",
     * "Sign":"106D0AD312CF69DAEDB4EA18D2310117"}
     * 
     * @throws IOException
     */
    @Test
    public void testDoService() throws IOException {
        PaymentCallbackRequest request = new PaymentCallbackRequest();

        request.setTradingId("8178254");
        request.setBizCode("3");
        request.setPayPrice("1.00");
        request.setCurrency("CNY");
        request.setAppId("2");
        request.setTraceId("fc04be7b-bd80-4d4d-81b1-814af832d749");
        request.setPayTime(new Date());
        request.setPaymentId("17333995000768041");
        request.setPayChannel("Alipay");
        request.setExternalUserId("Yr2x@qq.com");
        request.setInstPaymentId("b431a33a-11bf-4452-85b6-4510bcbaaf91");
        request.setInternalUserId("");
        request.setMemo("备注:036");
        request.setPayType("");
        request.setSignMethod("MD5");
        boolean flag = paymentCallbackService.doService(callbackUrl, request, null);

        Assert.assertEquals(true, flag);
    }


}
