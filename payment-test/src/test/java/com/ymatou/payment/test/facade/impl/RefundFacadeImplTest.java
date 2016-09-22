/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.facade.impl;

import java.util.Arrays;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import com.ymatou.messagebus.client.Message;
import com.ymatou.messagebus.client.MessageBusClient;
import com.ymatou.messagebus.client.MessageBusException;
import com.ymatou.payment.facade.RefundFacade;
import com.ymatou.payment.facade.model.TradeRefundableRequest;
import com.ymatou.payment.facade.model.TradeRefundableResponse;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 
 * @author qianmin 2016年6月22日 下午6:18:33
 *
 */
public class RefundFacadeImplTest extends RestBaseTest {

    @Resource(name = "refundFacadeClient")
    private RefundFacade refundFacade;

    @Resource
    private MessageBusClient messageBusClient;

    @Test
    public void checkRefundableTest() {
        TradeRefundableRequest request = new TradeRefundableRequest();
        request.setTradeNos(Arrays.asList(new String[] {"1111111111"}));
        TradeRefundableResponse response = refundFacade.checkRefundable(request);
        Assert.assertEquals(true, response.getIsSuccess());
    }

    @Test
    public void messageBusClientTest() throws MessageBusException {
        Message req = new Message();
        req.setAppId("payment");
        req.setCode("refund_notify");
        req.setMessageId("payment-000001");
        req.setBody("messagebody");
        messageBusClient.sendMessage(req);
    }
}
