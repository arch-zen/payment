/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.facade.impl;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;

import com.ymatou.payment.facade.PaymentFacade;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireOrderResp;
import com.ymatou.payment.test.RestBaseTest;

/**
 * @author wangxudong 2016年7月14日 下午2:07:30
 *
 */
public class PaymentFacadeImplTest extends RestBaseTest {
    @Resource
    private PaymentFacade paymentFacade;

    @Test
    public void acquireOrderTest() {
        AcquireOrderReq req = new AcquireOrderReq();
        req.setVersion(1);
        req.setBizCode(3);
        req.setOriginAppId("1");
        req.setAppId("AutoTest");
        req.setCallbackUrl("http://www.ymatou.com/shopping/payup");
        req.setTraceId(UUID.randomUUID().toString());
        req.setCurrency("CNY");
        req.setEncoding(65001);
        req.setNotifyUrl("http://operate.trading.iapi.ymatou.com/api/trading/orderPayCallBack");
        req.setOrderId(getDateFormatString("yyyyMMddHHmmssSSS"));
        req.setOrderTime(getDateFormatString("yyyyMMddHHmmss"));
        req.setPayPrice("29.00");
        req.setPayType("10");
        req.setProductName("tonywang_efun的订单");
        req.setProductDesc("");
        req.setProductUrl("www.ymatou.com");
        req.setMemo("备注");
        req.setSignMethod("MD5");
        req.setExt("{\"IsHangZhou\":0}");
        req.setUserId(12864011L);
        req.setUserIp("180.166.117.42");
        AcquireOrderResp resp = paymentFacade.acquireOrder(req);

        assertEquals("验证Success", true, resp.getIsSuccess());
    }

    /**
     * 构造日期字符串
     * 
     * @return
     */
    private String getDateFormatString(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(new Date());
    }
}
