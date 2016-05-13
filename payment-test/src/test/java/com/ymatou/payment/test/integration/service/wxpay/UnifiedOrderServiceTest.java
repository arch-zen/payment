package com.ymatou.payment.test.integration.service.wxpay;

import java.util.HashMap;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.payment.integration.common.Signature;
import com.ymatou.payment.integration.common.constants.WxReturnCodeEnum;
import com.ymatou.payment.integration.model.UnifiedOrderRequest;
import com.ymatou.payment.integration.model.UnifiedOrderResponse;
import com.ymatou.payment.integration.service.wxpay.UnifiedOrderService;
import com.ymatou.payment.test.RestBaseTest;

public class UnifiedOrderServiceTest extends RestBaseTest {

    @Autowired
    private UnifiedOrderService unifiedOrderService;

    @Test
    public void testDoServiceSuceess() throws Exception {
        UnifiedOrderRequest request = new UnifiedOrderRequest();
        request.setAppid("wxa06ebe9f39751792");
        request.setMch_id("1278350701");
        request.setDevice_info("WEB");
        request.setNonce_str("weixin" + String.valueOf(new Random().nextInt(10)));
        request.setBody("Ipad mini 16G");
        request.setOut_trade_no("test20160503125346");
        request.setTotal_fee(888);
        request.setSpbill_create_ip("192.168.1.1");
        request.setNotify_url("www.qq.com");
        request.setTrade_type("JSAPI");
        request.setOpenid("oR5W7jg8-T62LtAvEShpuL-vCxXE");

        // 加签
        String sign = Signature.getSign(request, "es839gnc8451lp0s943n568xzskjgdbv");
        request.setSign(sign);

        UnifiedOrderResponse response = unifiedOrderService.doService(request, new HashMap<String, String>());
        Assert.assertNotNull(response);
        Assert.assertNotNull("appid不存在", response.getReturn_msg());
        Assert.assertEquals(WxReturnCodeEnum.SUCCESS.toString(), response.getReturn_code());
        Assert.assertNotNull(response.getPrepay_id());
    }

    @Test
    public void testDoServiceFail() throws Exception {
        UnifiedOrderRequest request = new UnifiedOrderRequest();
        request.setAppid("wxa06ebe9f397517921");
        request.setMch_id("1278350701");
        request.setDevice_info("WEB");
        request.setNonce_str("weixin" + String.valueOf(new Random().nextInt(10)));
        request.setBody("Ipad mini 16G");
        request.setOut_trade_no("test20160503125346");
        request.setTotal_fee(888);
        request.setSpbill_create_ip("192.168.1.1");
        request.setNotify_url("www.qq.com");
        request.setTrade_type("JSAPI");
        request.setOpenid("oR5W7jg8-T62LtAvEShpuL-vCxXE");

        // 加签
        String sign = Signature.getSign(request, "es839gnc8451lp0s943n568xzskjgdbv");
        request.setSign(sign);

        UnifiedOrderResponse response = unifiedOrderService.doService(request, new HashMap<String, String>());
        Assert.assertNotNull(response);
        Assert.assertEquals(WxReturnCodeEnum.FAIL.toString(), response.getReturn_code());
        Assert.assertNotNull("appid不存在", response.getReturn_msg());
        Assert.assertNull(response.getPrepay_id());
    }
}
