/**
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.facade.impl.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.service.PayService;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireRefundPlusRequest;
import com.ymatou.payment.facade.model.AcquireRefundPlusResponse;
import com.ymatou.payment.facade.model.QueryRefundByBizNoReq;
import com.ymatou.payment.facade.model.QueryRefundByBizNoResp;
import com.ymatou.payment.facade.model.QueryRefundByRefundNoReq;
import com.ymatou.payment.facade.model.QueryRefundByRefundNoResp;
import com.ymatou.payment.facade.rest.PaymentResource;
import com.ymatou.payment.facade.rest.RefundQueryResource;
import com.ymatou.payment.facade.rest.RefundResource;
import com.ymatou.payment.infrastructure.db.mapper.BussinessOrderMapper;
import com.ymatou.payment.infrastructure.db.mapper.PaymentMapper;
import com.ymatou.payment.infrastructure.db.mapper.RefundRequestMapper;
import com.ymatou.payment.infrastructure.db.model.BussinessOrderPo;
import com.ymatou.payment.infrastructure.db.model.PaymentExample;
import com.ymatou.payment.infrastructure.db.model.PaymentPo;
import com.ymatou.payment.infrastructure.db.model.RefundRequestExample;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 退款查询接口-测试用例
 * 
 * @author wangxudong 2017年1月13日 下午6:27:57
 *
 */
public class RefundQueryResourceTest extends RestBaseTest {

    @Resource
    private RefundQueryResource refundQueryResource;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private PaymentResource paymentResource;

    @Autowired
    private BussinessOrderMapper bussinessOrderMapper;

    @Autowired
    private RefundResource refundResource;

    @Autowired
    private PayService payService;

    @Autowired
    private RefundRequestMapper refundRequestMapper;

    @Test
    public void testQueryByRefundNo() {
        QueryRefundByRefundNoReq req = new QueryRefundByRefundNoReq();
        List<String> refundNoList = new ArrayList<>();
        refundNoList.add("58772bd8ef2e884d59ef76ce");

        req.setRefundNoList(refundNoList);
        QueryRefundByRefundNoResp resp = refundQueryResource.queryRefundByRefundNo(req);

        assertNotNull(resp);
        assertEquals(true, resp.getIsSuccess());
        assertEquals(1, resp.getResult().size());
    }

    @Test
    public void testQueryByBizNo() {
        HashMap<String, Object> result = generatePayment();
        BussinessOrderPo bussinessOrderPo = (BussinessOrderPo) result.get("businessOrder");

        AcquireRefundPlusRequest request = new AcquireRefundPlusRequest();
        request.setAppId(bussinessOrderPo.getAppId());
        request.setOrderId(bussinessOrderPo.getBussinessOrderId());
        request.setRefundNo(RandomStringUtils.randomAlphabetic(8));
        request.setBizNo(RandomStringUtils.randomAlphabetic(8));
        request.setTradeNo(bussinessOrderPo.getOrderId());
        request.setTradeType(1);
        request.setRefundAmt(new BigDecimal(20));

        AcquireRefundPlusResponse response = refundResource.acquireRefund(request, new MockHttpServletRequest());
        Assert.assertEquals(0, response.getErrorCode());

        RefundRequestExample example = new RefundRequestExample();
        example.createCriteria().andTraceIdEqualTo(request.getRefundNo());
        List<RefundRequestPo> selectByExample = refundRequestMapper.selectByExample(example);

        assertNotNull(selectByExample);
        assertEquals(1, selectByExample.size());
        assertEquals(request.getBizNo(), selectByExample.get(0).getBizNo());

        QueryRefundByBizNoReq req = new QueryRefundByBizNoReq();
        List<String> refundNoList = new ArrayList<>();
        refundNoList.add("58772bd8ef2e884d59ef76ce");

        req.setBizNoList(refundNoList);
        QueryRefundByBizNoResp resp = refundQueryResource.queryRefundByBizNo(req);

        assertNotNull(resp);
        assertEquals(true, resp.getIsSuccess());
        assertEquals(1, resp.getResult().size());
        assertEquals(request.getOrderId(), resp.getResult().get(0).getOrderId());
    }



    private HashMap<String, Object> generatePayment() {
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);
        req.setPayPrice("20.01");

        req.setPayType("11");
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        paymentResource.acquireOrder(req, servletRequest);
        BussinessOrder bo = payService.getBussinessOrderByOrderId(req.orderId);

        PaymentExample example = new PaymentExample();
        example.createCriteria().andBussinessOrderIdEqualTo(bo.getBussinessOrderId());
        PaymentPo paymentPo = paymentMapper.selectByExample(example).get(0);
        paymentPo.setPayStatus(PayStatusEnum.Paied.getIndex());
        paymentPo.setActualPayPrice(paymentPo.getPayPrice());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        paymentPo.setInstitutionPaymentId(simpleDateFormat.format(new Date()) + RandomStringUtils.randomNumeric(2));

        paymentMapper.updateByExampleSelective(paymentPo, example);

        BussinessOrderPo bussinessOrderPo = bussinessOrderMapper.selectByPrimaryKey(bo.getBussinessOrderId());
        bussinessOrderPo.setOrderStatus(1);
        bussinessOrderMapper.updateByPrimaryKey(bussinessOrderPo);

        System.out.println(bo.getOrderId() + " " + bo.getBussinessOrderId());
        HashMap<String, Object> result = new HashMap<>();
        result.put("payment", paymentPo);
        result.put("businessOrder", bussinessOrderPo);

        return result;
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
        req.setUserId(12345L);
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
