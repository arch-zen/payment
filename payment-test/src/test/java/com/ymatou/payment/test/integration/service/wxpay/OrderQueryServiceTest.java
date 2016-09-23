/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.integration.service.wxpay;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.integration.model.QueryOrderRequest;
import com.ymatou.payment.integration.model.QueryOrderResponse;
import com.ymatou.payment.integration.service.wxpay.OrderQueryService;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 
 * @author qianmin 2016年5月23日 上午10:25:38
 *
 */
public class OrderQueryServiceTest extends RestBaseTest {

    @Autowired
    private SignatureService signatureService;

    @Autowired
    private InstitutionConfigManager institutionConfigManager;

    @Autowired
    private OrderQueryService orderQueryService;

    @Test
    public void testDoService() throws Exception {
        QueryOrderRequest orderQueryRequest = new QueryOrderRequest();
        orderQueryRequest.setAppid("wxf51a439c0416f182");
        orderQueryRequest.setMch_id("1234079001");
        orderQueryRequest.setNonce_str("95f71d8d84a641209e9345788cab5c58");
        orderQueryRequest.setOut_trade_no("21935472000015038");
        orderQueryRequest.setTransaction_id("");



        InstitutionConfig institutionConfig = institutionConfigManager.getConfig(PayTypeEnum.parse("15"));

        @SuppressWarnings("unchecked")
        String sign = signatureService.signMessage(new ObjectMapper().convertValue(orderQueryRequest, HashMap.class),
                institutionConfig, null); // 加签
        orderQueryRequest.setSign(sign);

        QueryOrderResponse response = orderQueryService.doService(orderQueryRequest, null);

        Assert.assertEquals(2900, response.getTotal_fee());
    }
}
