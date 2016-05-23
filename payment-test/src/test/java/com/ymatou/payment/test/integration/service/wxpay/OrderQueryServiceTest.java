/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.integration.service.wxpay;

import com.ymatou.payment.integration.model.OrderQueryRequest;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 
 * @author qianmin 2016年5月23日 上午10:25:38
 *
 */
public class OrderQueryServiceTest extends RestBaseTest {

    public void testDoService() {
        OrderQueryRequest orderQueryRequest = new OrderQueryRequest();
        orderQueryRequest.setAppid("wxf51a439c0416f182");
        orderQueryRequest.setMch_id("1234079001");
        orderQueryRequest.setNonce_str("95f71d8d84a641209e9345788cab5c58");
        orderQueryRequest.setOut_trade_no("21935472000015038");
        orderQueryRequest.setSign("");

    }
}
