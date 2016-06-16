/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.facade.impl.rest;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.service.PayService;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.FastRefundRequest;
import com.ymatou.payment.facade.model.FastRefundResponse;
import com.ymatou.payment.facade.rest.PaymentResource;
import com.ymatou.payment.facade.rest.RefundJobResource;
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
 * 
 * @author qianmin 2016年6月15日 下午5:04:39
 *
 */
public class RefundJobResourceImplTest extends RestBaseTest {

    @Autowired
    private RefundJobResource refundJobResource;

    @Autowired
    private RefundResource refundResource;

    @Autowired
    private RefundRequestMapper refundRequestMapper;

    @Autowired
    private PaymentResource paymentResource;

    @Autowired
    private PayService payService;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private BussinessOrderMapper bussinessOrderMapper;

    private HttpServletRequest servletRequest = new MockHttpServletRequest();


    @Test
    public void testSubmitRufund() throws InterruptedException {
        HashMap<String, Object> result = generatePayment();
        BussinessOrderPo bussinessOrderPo = (BussinessOrderPo) result.get("businessOrder");
        PaymentPo paymentPo = (PaymentPo) result.get("payment");

        FastRefundRequest request = new FastRefundRequest();
        request.setPaymentId(paymentPo.getPaymentId());
        request.setAppId(bussinessOrderPo.getAppId());
        request.setOrderIdList(Arrays.asList(new String[] {bussinessOrderPo.getBussinessOrderId()}));
        request.setRequestNo(RandomStringUtils.randomAlphabetic(8));
        request.setTradingId(bussinessOrderPo.getOrderId());
        request.setTradeType(1);
        request.setRefundAmt(new BigDecimal(7.8));

        FastRefundResponse response = refundResource.fastRefund(request, new MockHttpServletRequest());
        Assert.assertEquals(0, response.getErrorCode());

        RefundRequestExample example = new RefundRequestExample();
        example.createCriteria().andPaymentIdEqualTo(paymentPo.getPaymentId());
        List<RefundRequestPo> refundRequestPos = refundRequestMapper.selectByExample(example);
        String refundBatchNo = refundRequestPos.get(0).getRefundBatchNo();

        // 此时同步应答还没回来，从新提交了退款请求
        String respMsg = refundJobResource.excuteRefund(refundBatchNo, servletRequest);
        System.out.println(respMsg);

        Assert.assertEquals("0", respMsg);

        Thread.sleep(1000);

    }


    @Test
    public void testQueryFail() throws InterruptedException {
        HashMap<String, Object> result = generatePayment();
        BussinessOrderPo bussinessOrderPo = (BussinessOrderPo) result.get("businessOrder");
        PaymentPo paymentPo = (PaymentPo) result.get("payment");

        FastRefundRequest request = new FastRefundRequest();
        request.setPaymentId(paymentPo.getPaymentId());
        request.setAppId(bussinessOrderPo.getAppId());
        request.setOrderIdList(Arrays.asList(new String[] {bussinessOrderPo.getBussinessOrderId()}));
        request.setRequestNo(RandomStringUtils.randomAlphabetic(8));
        request.setTradingId(bussinessOrderPo.getOrderId());
        request.setTradeType(1);
        request.setRefundAmt(new BigDecimal(7.8));

        FastRefundResponse response = refundResource.fastRefund(request, new MockHttpServletRequest());
        Assert.assertEquals(0, response.getErrorCode());

        Thread.sleep(1000);

        RefundRequestExample example = new RefundRequestExample();
        example.createCriteria().andPaymentIdEqualTo(paymentPo.getPaymentId());
        List<RefundRequestPo> refundRequestPos = refundRequestMapper.selectByExample(example);
        String refundBatchNo = refundRequestPos.get(0).getRefundBatchNo();

        String respMsg = refundJobResource.excuteRefund(refundBatchNo, servletRequest);
        Thread.sleep(1000);
        System.out.println(respMsg);
        Assert.assertEquals("-1", respMsg);
    }

    @Test
    public void testThirdPartySuccess() throws InterruptedException {
        HashMap<String, Object> result = generatePayment();
        BussinessOrderPo bussinessOrderPo = (BussinessOrderPo) result.get("businessOrder");
        PaymentPo paymentPo = (PaymentPo) result.get("payment");

        FastRefundRequest request = new FastRefundRequest();
        request.setPaymentId(paymentPo.getPaymentId());
        request.setAppId(bussinessOrderPo.getAppId());
        request.setOrderIdList(Arrays.asList(new String[] {RandomStringUtils.randomNumeric(8)}));
        request.setRequestNo(RandomStringUtils.randomAlphabetic(8));
        request.setTradingId(bussinessOrderPo.getOrderId());
        request.setTradeType(1);
        request.setRefundAmt(new BigDecimal(7.8));

        FastRefundResponse response = refundResource.fastRefund(request, new MockHttpServletRequest());
        Assert.assertEquals(0, response.getErrorCode());

        Thread.sleep(1000);

        RefundRequestExample example = new RefundRequestExample();
        example.createCriteria().andPaymentIdEqualTo(paymentPo.getPaymentId());
        List<RefundRequestPo> refundRequestPos = refundRequestMapper.selectByExample(example);
        String refundBatchNo = refundRequestPos.get(0).getRefundBatchNo();

        RefundRequestPo refundRequestPo = new RefundRequestPo();
        refundRequestPo.setRefundTime(new Date());
        refundRequestPo.setRefundStatus(RefundStatusEnum.THIRDPART_REFUND_SUCCESS.getCode());
        RefundRequestExample example2 = new RefundRequestExample();
        example2.createCriteria().andRefundBatchNoEqualTo(refundBatchNo);
        refundRequestMapper.updateByExampleSelective(refundRequestPo, example2);


        String respMsg = refundJobResource.excuteRefund(refundBatchNo, servletRequest);
        Thread.sleep(1000);
        System.out.println(respMsg);
        Assert.assertEquals("-1", respMsg);
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
        req.setUserId(168L);
        req.setUserIp("127.0.0.1");
        req.setBankId("CMB");
    }

    private String getDateFormatString(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(new Date());
    }
}
