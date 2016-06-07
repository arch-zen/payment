package com.ymatou.payment.test.integration.service.wxpay;

import java.util.HashMap;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.payment.integration.common.Signature;
import com.ymatou.payment.integration.model.RefundQueryRequest;
import com.ymatou.payment.integration.model.RefundQueryResponse;
import com.ymatou.payment.integration.service.wxpay.RefundQueryService;
import com.ymatou.payment.test.RestBaseTest;

public class RefundQueryServiceTest extends RestBaseTest {

    @Autowired
    private RefundQueryService refundQueryService;

    @Test
    public void testDoServiceSuccess() throws Exception {
        RefundQueryRequest request = new RefundQueryRequest();
        request.setAppid("wxa06ebe9f39751792");
        request.setMch_id("1278350701");
        request.setDevice_info("WEB");
        request.setNonce_str("weixin" + String.valueOf(new Random().nextInt(10)));
        request.setOut_trade_no("407300842881200246326");
        request.setTransaction_id("294be28a62a34aa38b0e38e0cdcbfd7");

        // 加签
        String sign = Signature.getSign(request, "es839gnc8451lp0s943n568xzskjgdbv");
        request.setSign(sign);

        HashMap<String, String> header = new HashMap<>();
        RefundQueryResponse response = refundQueryService.doService(request, header);
        Assert.assertNotNull(response);
        Assert.assertEquals("REFUNDNOTEXIST", response.getErr_code());
    }
}
