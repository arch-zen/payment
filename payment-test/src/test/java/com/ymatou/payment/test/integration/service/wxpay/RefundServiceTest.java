/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.integration.service.wxpay;

import java.util.Random;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.ymatou.payment.integration.common.Signature;
import com.ymatou.payment.integration.model.WxRefundRequest;
import com.ymatou.payment.integration.model.WxRefundResponse;
import com.ymatou.payment.integration.service.wxpay.WxRefundService;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 
 * @author qianmin 2016年5月30日 下午2:54:29
 *
 */
public class RefundServiceTest extends RestBaseTest {

    @Autowired
    private WxRefundService refundService;

    @Test
    public void testDoService() throws Exception {
        WxRefundRequest request = new WxRefundRequest();
        request.setAppid("wxa06ebe9f39751792");
        request.setMch_id("1278350701");
        request.setDevice_info("WEB");
        request.setNonce_str("weixin" + String.valueOf(new Random().nextInt(10)));
        request.setOut_trade_no("4073008428812002463261");
        request.setTransaction_id("294be28a62a34aa38b0e38e0cdcbfd71");
        request.setOut_refund_no("121213324343");
        request.setTotal_fee(1000);
        request.setRefund_fee(1000);
        request.setOp_user_id("1278350701");

        // 加签
        String sign = Signature.getSign(request, "es839gnc8451lp0s943n568xzskjgdbv");
        request.setSign(sign);

        WxRefundResponse response = refundService.doService(request, null);
        System.out.println(JSONObject.toJSONString(response));
    }

}
