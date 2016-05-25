/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.facade.impl.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.service.PayService;
import com.ymatou.payment.facade.impl.rest.PaymentResource;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireOrderResp;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.test.RestBaseTest;


/**
 * 
 * @author wangxudong 2016年5月10日 下午12:10:19
 *
 */
public class PaymentResourceImplTest extends RestBaseTest {

    @Resource
    private PaymentResource paymentResource;

    @Resource
    private PayService payService;

    @Resource
    private IntegrationConfig config;

    @Test
    public void testAcquireOrderPC() {
        System.out.println(config);
        System.out.println(config.getAliPayBaseUrl());
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);

        req.setPayType("10");
        req.setPayPrice("1.01");

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        AcquireOrderResp res = paymentResource.acquireOrder(req, servletRequest);

        assertEquals("验证返回码", 0, res.getErrorCode());
        assertEquals("验证TraceId", req.getTraceId(), res.getTraceId());
        assertEquals("验证ResultType", "Form", res.getResultType());

        BussinessOrder bo = payService.getBussinessOrderByOrderId(req.orderId);
        assertNotNull("验证商户订单", bo);

        System.out.println(bo.getOrderId());
        System.out.println(bo.getBussinessOrderId());

        assertEquals("验证PayType", req.getPayType(), bo.getPayType());
        assertEquals("验证OrderPrice", new BigDecimal(req.getPayPrice()).doubleValue(), bo.getOrderPrice().doubleValue(),
                0.00001);
        assertEquals("验证CurrencyType", req.getCurrency(), bo.getCurrencyType());
        assertEquals("验证Version", req.getVersion(), bo.getVersion());
        assertEquals("验证AppId", req.getAppId(), bo.getAppId());
        assertEquals("验证TraceId", req.getTraceId(), bo.getTraceId());
        assertEquals("验证OrderTime", req.getOrderTime(), bo.getOrderTime());
        assertEquals("验证ClientIP", req.getUserIp(), bo.getClientIp());
        assertEquals("验证CallbackUrl", req.getCallbackUrl(), bo.getCallbackUrl());
        assertEquals("验证NotifyUrl", req.getNotifyUrl(), bo.getNotifyUrl());
        assertEquals("验证ProductName", req.getProductName(), bo.getProductName());
        assertEquals("验证ProductDesc", req.getProductDesc(), bo.getProductDesc());
        assertEquals("验证ProductUrl", req.getProductUrl(), bo.getProductUrl());
        assertEquals("验证CodePage", req.getEncoding(), bo.getCodePage());
        assertEquals("验证Ext", req.getExt(), bo.getExt());
        assertEquals("验证Memo", req.getMemo(), bo.getMemo());
        assertEquals("验证SignMethod", req.getSignMethod(), bo.getSignMethod());
        assertEquals("验证BizCode", req.getBizCode(), bo.getBizCode());
        assertEquals("验证OrderStatus", new Integer(0), bo.getOrderStatus());
        assertEquals("验证NotifyStatus", new Integer(0), bo.getNotifyStatus());
    }

    @Test
    public void testAcquireOrderApp() {
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);

        req.setPayType("13");
        req.setPayPrice("1.01");

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        AcquireOrderResp res = paymentResource.acquireOrder(req, servletRequest);

        assertEquals("验证返回码", 0, res.getErrorCode());
        assertEquals("验证TraceId", req.getTraceId(), res.getTraceId());
        assertEquals("验证ResultType", "JSON", res.getResultType());
        assertNotNull("验证Result", res.getResult());

        BussinessOrder bo = payService.getBussinessOrderByOrderId(req.orderId);
        assertNotNull("验证商户订单", bo);

        System.out.println(bo.getOrderId());
        System.out.println(bo.getBussinessOrderId());

        assertEquals("验证PayType", req.getPayType(), bo.getPayType());

        Payment payment = payService.getPaymentByBussinessOrderId(bo.getBussinessOrderId());
        assertNotNull("验证支付单不为空", payment);

        assertEquals("验证PayType", req.getPayType(), payment.getPayType());
        assertEquals("验证PayPrice", new BigDecimal(req.getPayPrice()).doubleValue(), payment.getPayPrice().doubleValue(),
                0.000001);
        assertEquals("验证PayStatus", new Integer(0), payment.getPayStatus());
    }

    @Test
    public void testAcquireOrderFailedWhenOrderIdDuplicate() {
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);

        req.setOrderId("20160510180328297");

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        AcquireOrderResp res = paymentResource.acquireOrder(req, servletRequest);

        assertEquals("验证返回码", -2106, res.getErrorCode());
    }

    @Test
    public void testAcquireOrderFailedWhenInvalidArgument() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);

        req.setVersion(2);
        AcquireOrderResp res = paymentResource.acquireOrder(req, servletRequest);
        assertEquals("验证无效版本返回码", 1000, res.getErrorCode());
        System.out.println(res.getErrorMessage());

        req.setVersion(1);
        req.setPayType("99");
        res = paymentResource.acquireOrder(req, servletRequest);
        assertEquals("验证无效PayType返回码", 1005, res.getErrorCode());
        System.out.println(res.getErrorMessage());
    }

    @Test
    public void testAcquireOrderWeixinApp() {
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);

        req.setPayType("15");
        req.setPayPrice("1.01");

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        AcquireOrderResp res = paymentResource.acquireOrder(req, servletRequest);

        assertEquals("验证返回码", 0, res.getErrorCode());
        assertEquals("验证TraceId", req.getTraceId(), res.getTraceId());
        assertEquals("验证ResultType", "JSON", res.getResultType());
        assertNotNull("验证Result", res.getResult());

        BussinessOrder bo = payService.getBussinessOrderByOrderId(req.orderId);
        assertNotNull("验证商户订单", bo);

        System.out.println(bo.getOrderId());
        System.out.println(bo.getBussinessOrderId());

        assertEquals("验证PayType", req.getPayType(), bo.getPayType());

        Payment payment = payService.getPaymentByBussinessOrderId(bo.getBussinessOrderId());
        assertNotNull("验证支付单不为空", payment);

        assertEquals("验证PayType", req.getPayType(), payment.getPayType());
        assertEquals("验证PayPrice", new BigDecimal(req.getPayPrice()).doubleValue(), payment.getPayPrice().doubleValue(),
                0.000001);
        assertEquals("验证PayStatus", new Integer(0), payment.getPayStatus());
    }

    @Test
    public void testAcquireOrderWeixinJSAPI() {
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);

        req.setPayType("14");
        req.setPayPrice("1.01");
        req.setUserId(3790800);

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        AcquireOrderResp res = paymentResource.acquireOrder(req, servletRequest);

        assertEquals("验证返回码", 0, res.getErrorCode());
        assertEquals("验证TraceId", req.getTraceId(), res.getTraceId());
        assertEquals("验证ResultType", "JSON", res.getResultType());
        assertNotNull("验证Result", res.getResult());

        BussinessOrder bo = payService.getBussinessOrderByOrderId(req.orderId);
        assertNotNull("验证商户订单", bo);

        System.out.println(bo.getOrderId());
        System.out.println(bo.getBussinessOrderId());

        assertEquals("验证PayType", req.getPayType(), bo.getPayType());

        Payment payment = payService.getPaymentByBussinessOrderId(bo.getBussinessOrderId());
        assertNotNull("验证支付单不为空", payment);

        assertEquals("验证PayType", req.getPayType(), payment.getPayType());
        assertEquals("验证PayPrice", new BigDecimal(req.getPayPrice()).doubleValue(), payment.getPayPrice().doubleValue(),
                0.000001);
        assertEquals("验证PayStatus", new Integer(0), payment.getPayStatus());
    }

    /**
     * 构造请求报文
     * 
     * @param req
     */
    private void buildBaseRequest(AcquireOrderReq req) {
        req.setVersion(1);
        req.setBizCode(3);
        req.setOriginAppId("JavaTest");
        req.setAppId("AutoTest");
        req.setCallbackUrl("http://www.ymatou.com/pay/result");
        req.setTraceId(UUID.randomUUID().toString());
        req.setCurrency("CNY");
        req.setEncoding(65001);
        req.setNotifyUrl("http://api.trading.operate.ymatou.com/api/Trading/TradingCompletedNotify");
        req.setOrderId(getDateFormatString("yyyyMMddHHmmssSSS"));
        req.setOrderTime(getDateFormatString("yyyyMMddHHmmss"));
        req.setPayPrice("0.01");
        req.setPayType("10");
        req.setProductName("测试商品");
        req.setProductDesc("商品描述");
        req.setProductUrl("www.ymatou.com");
        req.setMemo("备注");
        req.setSignMethod("MD5");
        req.setExt("{\"SHOWMODE\":\"2\",\"PAYMETHOD\":\"2\", \"IsHangZhou\":0}");
        req.setUserId(12345);
        req.setUserIp("127.0.0.1");
        req.setBankId("CMB");
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
