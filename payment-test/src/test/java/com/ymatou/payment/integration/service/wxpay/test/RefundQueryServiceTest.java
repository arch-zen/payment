package com.ymatou.payment.integration.service.wxpay.test;

import java.util.HashMap;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ymatou.payment.integration.common.Signature;
import com.ymatou.payment.integration.model.RefundQueryRequest;
import com.ymatou.payment.integration.model.RefundQueryResponse;
import com.ymatou.payment.integration.service.wxpay.RefundQueryService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class RefundQueryServiceTest {

    @Autowired
    private RefundQueryService refundQueryService;

    @Test
    public void testDoServiceSuccess() throws Exception {
        RefundQueryRequest request = new RefundQueryRequest();
        request.setAppid("wxf51a439c0416f182");
        request.setMch_id("1234079001");
        request.setDevice_info("WEB");
        request.setNonce_str("weixin" + String.valueOf(new Random().nextInt(10)));
        request.setOut_trade_no("407300842881200246326");
        request.setTransaction_id("294be28a62a34aa38b0e38e0cdcbfd7");

        // 加签
        String sign = Signature.getSign(request, "es839gnc8451lp0s943n568xzskjgdbv");
        request.setSign(sign);

        HashMap<String, String> header = new HashMap<>();
        header.put("Mock", "1");
        header.put("MockId", "888888");
        RefundQueryResponse response = refundQueryService.doService(request, header);
        Assert.assertNotNull(response);
        Assert.assertEquals(400, response.getTotal_fee());
    }
}
