/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.integration.service.alipay;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.payment.integration.model.CreateTradeRequest;
import com.ymatou.payment.integration.model.CreateTradeResponse;
import com.ymatou.payment.integration.service.alipay.TradeCreateService;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 
 * @author qianmin 2016年5月25日 下午3:11:39
 *
 */
public class TradeCreateServiceTest extends RestBaseTest {

    @Autowired
    private TradeCreateService tradeCreateService;

    @Test
    public void doServiceTest() throws Exception {
        CreateTradeRequest request = new CreateTradeRequest();
        request.setFormat("xml");
        request.setPartner("2088701734809577");
        request.setReq_id("841ded23-1e36-4163-8a0b-0830aa863fde");
        request.setSec_id("MD5");
        request.setService("alipay.wap.trade.create.direct");
        request.setSign("cef339b38505cf52860eea9f9c445476");
        request.setV("2.0");
        request.setReq_data(
                "<direct_trade_create_req><subject>你好</subject><out_trade_no>13514606200642938</out_trade_no><total_fee>10.00</total_fee><seller_account_name>ap.ymt@ymatou.com</seller_account_name><notify_url>http://localhost:12345/notify/11</notify_url><out_user>20124373</out_user><call_back_url>http://localhost:12345/callback/11</call_back_url></direct_trade_create_req>");

        CreateTradeResponse response = tradeCreateService.doService(request, null);

        assertNotNull(response);
    }
}
