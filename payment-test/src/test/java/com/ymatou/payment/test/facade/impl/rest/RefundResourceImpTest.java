/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.facade.impl.rest;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.service.PayService;
import com.ymatou.payment.domain.refund.repository.RefundPository;
import com.ymatou.payment.facade.constants.ApproveStatusEnum;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireRefundPlusRequest;
import com.ymatou.payment.facade.model.AcquireRefundPlusResponse;
import com.ymatou.payment.facade.model.AcquireRefundRequest;
import com.ymatou.payment.facade.model.AcquireRefundResponse;
import com.ymatou.payment.facade.model.ApproveRefundRequest;
import com.ymatou.payment.facade.model.ApproveRefundResponse;
import com.ymatou.payment.facade.model.FastRefundRequest;
import com.ymatou.payment.facade.model.FastRefundResponse;
import com.ymatou.payment.facade.model.QueryRefundRequest;
import com.ymatou.payment.facade.model.QueryRefundResponse;
import com.ymatou.payment.facade.model.SysApproveRefundReq;
import com.ymatou.payment.facade.model.TradeDetail;
import com.ymatou.payment.facade.model.TradeRefundableRequest;
import com.ymatou.payment.facade.model.TradeRefundableResponse;
import com.ymatou.payment.facade.rest.PaymentResource;
import com.ymatou.payment.facade.rest.RefundResource;
import com.ymatou.payment.infrastructure.db.mapper.BussinessOrderMapper;
import com.ymatou.payment.infrastructure.db.mapper.PaymentMapper;
import com.ymatou.payment.infrastructure.db.mapper.PaymentParamMapper;
import com.ymatou.payment.infrastructure.db.mapper.RefundRequestMapper;
import com.ymatou.payment.infrastructure.db.model.BussinessOrderPo;
import com.ymatou.payment.infrastructure.db.model.PaymentExample;
import com.ymatou.payment.infrastructure.db.model.PaymentParamExample;
import com.ymatou.payment.infrastructure.db.model.PaymentParamPo;
import com.ymatou.payment.infrastructure.db.model.PaymentPo;
import com.ymatou.payment.infrastructure.db.model.RefundRequestExample;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;
import com.ymatou.payment.infrastructure.util.StringUtil;
import com.ymatou.payment.test.RestBaseTest;

/**
 * test
 * 
 * @author qianmin 2016年5月11日 下午6:05:48
 * 
 */
public class RefundResourceImpTest extends RestBaseTest {

    private static final Logger logger = LoggerFactory.getLogger(RefundResourceImpTest.class);

    @Autowired
    private RefundResource refundResource;

    @Autowired
    private RefundRequestMapper refundRequestMapper;

    @Autowired
    private RefundPository refundPository;

    @Autowired
    private PaymentParamMapper paymentParamMapper;

    @Autowired
    private PaymentResource paymentResource;

    @Autowired
    private PayService payService;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private BussinessOrderMapper bussinessOrderMapper;

    @Test
    public void testFastRefundInvalidArg() {
        FastRefundRequest fastRefundRequest = new FastRefundRequest();
        fastRefundRequest.setAppId("100001");
        fastRefundRequest.setPaymentId("20151107202902646000000000053774");
        fastRefundRequest.setTradingId("PP20151107300104764");
        fastRefundRequest.setTradeType(3);
        ArrayList<String> a = new ArrayList<String>();
        a.add("PP20151107300104764");
        fastRefundRequest.setOrderIdList(a);
        fastRefundRequest.setTraceId("1000001");

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        FastRefundResponse resp = refundResource.fastRefund(fastRefundRequest, servletRequest);

        Assert.assertEquals(1000, resp.getErrorCode());
    }

    @Test
    public void testSysApproveRefund() {
        RefundRequestPo refund1 = buildRefundRequest();
        RefundRequestPo refund2 = buildRefundRequest();
        RefundRequestPo refund3 = buildRefundRequest();

        // 作废的退款申请不允许审核通过
        refund3.setSoftDeleteFlag(true);

        refundRequestMapper.insertSelective(refund1);
        refundRequestMapper.insertSelective(refund2);
        refundRequestMapper.insertSelective(refund3);

        // 设置风控值
        setRefundMaxNum(Calendar.getInstance().get(Calendar.HOUR_OF_DAY), 200);

        SysApproveRefundReq req = new SysApproveRefundReq();
        Calendar approveTime = Calendar.getInstance();
        approveTime.add(Calendar.HOUR, 1);
        req.setBizId(StringUtil.getDateFormatString("yyyy-MM-dd HH:00:00", approveTime.getTime()));
        String sysApproveRefund = refundResource.sysApproveRefund(req, null);

        Assert.assertEquals("ok", sysApproveRefund);

        RefundRequestPo refundResult1 = refundPository.getRefundRequestByRefundNo(refund1.getRefundBatchNo());

        Assert.assertEquals(1, refundResult1.getApproveStatus().intValue());
        Assert.assertEquals("system", refundResult1.getApprovedUser());
        Assert.assertNotNull(refundResult1.getApprovedTime());


        RefundRequestPo refundResult2 = refundPository.getRefundRequestByRefundNo(refund2.getRefundBatchNo());

        Assert.assertEquals(1, refundResult2.getApproveStatus().intValue());
        Assert.assertEquals("system", refundResult2.getApprovedUser());
        Assert.assertNotNull(refundResult2.getApprovedTime());

        RefundRequestPo refundResult3 = refundPository.getRefundRequestByRefundNo(refund3.getRefundBatchNo());

        Assert.assertEquals(0, refundResult3.getApproveStatus().intValue());
        Assert.assertNull(refundResult3.getApprovedUser());
        Assert.assertNull(refundResult3.getApprovedTime());
    }


    @Test
    public void testSysApproveRefund1() {
        RefundRequestPo refund1 = buildRefundRequest();

        refundRequestMapper.insertSelective(refund1);

        // 不在时间范围的申请不审批
        SysApproveRefundReq req = new SysApproveRefundReq();
        Calendar approveTime = Calendar.getInstance();
        approveTime.add(Calendar.HOUR, 2);
        req.setBizId(StringUtil.getDateFormatString("yyyy-MM-dd HH:00:00", approveTime.getTime()));
        String sysApproveRefund = refundResource.sysApproveRefund(req, null);

        Assert.assertEquals("ok", sysApproveRefund);

        RefundRequestPo refundResult1 = refundPository.getRefundRequestByRefundNo(refund1.getRefundBatchNo());

        Assert.assertEquals(0, refundResult1.getApproveStatus().intValue());
        Assert.assertNull(refundResult1.getApprovedUser());
        Assert.assertNull(refundResult1.getApprovedTime());

        // 时间范围的申请审批通过
        approveTime = Calendar.getInstance();
        approveTime.add(Calendar.HOUR, 1);
        req.setBizId(StringUtil.getDateFormatString("yyyy-MM-dd HH:00:00", approveTime.getTime()));
        sysApproveRefund = refundResource.sysApproveRefund(req, null);
        Assert.assertEquals("ok", sysApproveRefund);

        refundResult1 = refundPository.getRefundRequestByRefundNo(refund1.getRefundBatchNo());
        Assert.assertEquals(1, refundResult1.getApproveStatus().intValue());
        Assert.assertEquals("system", refundResult1.getApprovedUser());
        Assert.assertNotNull(refundResult1.getApprovedTime());
    }

    @Test
    public void testSysApproveRefund2() {
        RefundRequestPo refund1 = buildRefundRequest();
        RefundRequestPo refund2 = buildRefundRequest();
        RefundRequestPo refund3 = buildRefundRequest();

        refundRequestMapper.insertSelective(refund1);
        refundRequestMapper.insertSelective(refund2);
        refundRequestMapper.insertSelective(refund3);

        SysApproveRefundReq req = new SysApproveRefundReq();
        Calendar approveTime = Calendar.getInstance();
        approveTime.add(Calendar.HOUR, 1);
        req.setBizId(StringUtil.getDateFormatString("yyyy-MM-dd HH:00:00", approveTime.getTime()));

        // 设置风控值
        setRefundMaxNum(Calendar.getInstance().get(Calendar.HOUR_OF_DAY), 2);
        String sysApproveRefund = refundResource.sysApproveRefund(req, null);

        Assert.assertNotEquals("ok", sysApproveRefund);

        RefundRequestPo refundResult1 = refundPository.getRefundRequestByRefundNo(refund1.getRefundBatchNo());
        Assert.assertEquals(0, refundResult1.getApproveStatus().intValue());
        Assert.assertNull(refundResult1.getApprovedUser());
        Assert.assertNull(refundResult1.getApprovedTime());


        // 设置风控值
        setRefundMaxNum(Calendar.getInstance().get(Calendar.HOUR_OF_DAY), 200);
        sysApproveRefund = refundResource.sysApproveRefund(req, null);

        refundResult1 = refundPository.getRefundRequestByRefundNo(refund1.getRefundBatchNo());
        Assert.assertEquals(1, refundResult1.getApproveStatus().intValue());
        Assert.assertEquals("system", refundResult1.getApprovedUser());
        Assert.assertNotNull(refundResult1.getApprovedTime());
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


    /**
     * 构建退款请求
     * 
     * @return
     */
    private RefundRequestPo buildRefundRequest() {
        RefundRequestPo refund = new RefundRequestPo();
        refund.setPaymentId(UUID.randomUUID().toString().replace("-", ""));
        refund.setTradeNo(StringUtil.getDateFormatString());
        refund.setOrderId(StringUtil.getDateFormatString());
        refund.setTraceId(UUID.randomUUID().toString().replace("-", ""));
        refund.setAppId("unitTest");
        refund.setPayType("13");
        refund.setRefundAmount(BigDecimal.valueOf(1.01));
        refund.setCurrencyType("CNY");
        refund.setAccoutingStatus(0);
        refund.setTradeType(1);
        refund.setRefundBatchNo(UUID.randomUUID().toString().replace("-", ""));

        return refund;
    }

    /**
     * 设置自动退款最大值
     * 
     * @param hour
     * @param maxNum
     */
    private void setRefundMaxNum(int hour, int maxNum) {
        PaymentParamExample paymentParamExample = new PaymentParamExample();
        paymentParamExample.createCriteria().andParamCatEqualTo("RefundApproveRisk")
                .andParamKeyEqualTo(String.valueOf(hour));

        PaymentParamPo paymentParamPo = new PaymentParamPo();
        paymentParamPo.setParamIntValue(maxNum);

        paymentParamMapper.updateByExampleSelective(paymentParamPo, paymentParamExample);
    }

    @Test
    public void testAcquireRefund() {
        HashMap<String, Object> result = generatePayment();
        BussinessOrderPo bussinessOrderPo = (BussinessOrderPo) result.get("businessOrder");

        AcquireRefundPlusRequest request = new AcquireRefundPlusRequest();
        request.setAppId(bussinessOrderPo.getAppId());
        request.setOrderId(bussinessOrderPo.getBussinessOrderId());
        request.setRequestNo(RandomStringUtils.randomAlphabetic(8));
        request.setTradeNo(bussinessOrderPo.getOrderId());
        request.setTradeType(1);
        request.setRefundAmt(new BigDecimal(20));

        AcquireRefundPlusResponse response = refundResource.acquireRefund(request, new MockHttpServletRequest());
        Assert.assertEquals(0, response.getErrorCode());
    }

    @Test
    public void testAcquireRefundFail() {
        HashMap<String, Object> result = generatePayment();
        BussinessOrderPo bussinessOrderPo = (BussinessOrderPo) result.get("businessOrder");

        AcquireRefundPlusRequest request = new AcquireRefundPlusRequest();
        request.setAppId(bussinessOrderPo.getAppId());
        request.setOrderId(bussinessOrderPo.getBussinessOrderId());
        request.setRequestNo(RandomStringUtils.randomAlphabetic(8));
        request.setTradeNo(bussinessOrderPo.getOrderId());
        request.setTradeType(1);
        request.setRefundAmt(new BigDecimal(32));

        AcquireRefundPlusResponse response = refundResource.acquireRefund(request, new MockHttpServletRequest());
        Assert.assertEquals(5000, response.getErrorCode());
    }

    @Test
    public void testAcquireRefundFail2() {
        HashMap<String, Object> result = generatePayment();
        BussinessOrderPo bussinessOrderPo = (BussinessOrderPo) result.get("businessOrder");


        AcquireRefundPlusRequest request = new AcquireRefundPlusRequest();
        request.setAppId(bussinessOrderPo.getAppId());
        request.setOrderId(bussinessOrderPo.getBussinessOrderId());
        request.setRequestNo(RandomStringUtils.randomAlphabetic(8));
        request.setTradeNo(bussinessOrderPo.getOrderId());
        request.setTradeType(1);
        request.setRefundAmt(new BigDecimal(20));

        AcquireRefundPlusResponse response = refundResource.acquireRefund(request, new MockHttpServletRequest());
        Assert.assertEquals(0, response.getErrorCode());

        AcquireRefundPlusRequest request2 = new AcquireRefundPlusRequest();
        request2.setAppId(bussinessOrderPo.getAppId());
        request2.setOrderId(bussinessOrderPo.getBussinessOrderId());
        request2.setRequestNo(RandomStringUtils.randomAlphabetic(8));
        request2.setTradeNo(bussinessOrderPo.getOrderId());
        request2.setTradeType(1);
        request2.setRefundAmt(new BigDecimal(15));

        AcquireRefundPlusResponse response2 = refundResource.acquireRefund(request2, new MockHttpServletRequest());
        Assert.assertEquals(5000, response2.getErrorCode());
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

    @Test
    public void testApproveRefund() throws InterruptedException {
        HashMap<String, Object> result = generatePayment();
        BussinessOrderPo bussinessOrderPo = (BussinessOrderPo) result.get("businessOrder");
        PaymentPo paymentPo = (PaymentPo) result.get("payment");

        AcquireRefundPlusRequest request = new AcquireRefundPlusRequest();
        request.setAppId(bussinessOrderPo.getAppId());
        request.setOrderId(bussinessOrderPo.getBussinessOrderId());
        request.setRequestNo(RandomStringUtils.randomAlphabetic(8));
        request.setTradeNo(bussinessOrderPo.getOrderId());
        request.setTradeType(1);
        request.setRefundAmt(new BigDecimal(7.8));

        AcquireRefundPlusResponse response = refundResource.acquireRefund(request, new MockHttpServletRequest());
        Assert.assertEquals(0, response.getErrorCode());

        AcquireRefundPlusRequest request2 = new AcquireRefundPlusRequest();
        request2.setAppId(bussinessOrderPo.getAppId());
        request2.setOrderId(bussinessOrderPo.getBussinessOrderId());
        request2.setRequestNo(RandomStringUtils.randomAlphabetic(8));
        request2.setTradeNo(bussinessOrderPo.getOrderId());
        request2.setTradeType(1);
        request2.setRefundAmt(new BigDecimal(3.2));

        AcquireRefundPlusResponse response2 = refundResource.acquireRefund(request2, new MockHttpServletRequest());
        Assert.assertEquals(0, response2.getErrorCode());

        System.out.println(paymentPo.getPaymentId());
        RefundRequestExample example = new RefundRequestExample();
        example.createCriteria().andPaymentIdEqualTo(paymentPo.getPaymentId());
        List<RefundRequestPo> refundRequestPos = refundRequestMapper.selectByExample(example);
        System.out.println(refundRequestPos.size());
        List<String> refundNos = new ArrayList<>();
        for (RefundRequestPo refundRequestPo : refundRequestPos) {
            refundNos.add(refundRequestPo.getRefundBatchNo());
        }

        ApproveRefundRequest approveRefundRequest = new ApproveRefundRequest();
        approveRefundRequest.setApproveUser("test");
        approveRefundRequest.setRefundNos(refundNos);

        ApproveRefundResponse approveRefundResponse =
                refundResource.approveRefund(approveRefundRequest, new MockHttpServletRequest());
        logger.info(JSON.toJSONString(approveRefundResponse));

        Assert.assertEquals(0, approveRefundResponse.getErrorCode());
        Assert.assertEquals(true, approveRefundResponse.getIsSuccess());
        Thread.sleep(1000);
    }

    @Test
    public void testFastRefundFail() throws InterruptedException {
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

        FastRefundRequest request2 = new FastRefundRequest();
        request2.setPaymentId(paymentPo.getPaymentId());
        request2.setAppId(bussinessOrderPo.getAppId());
        request2.setOrderIdList(Arrays.asList(new String[] {bussinessOrderPo.getBussinessOrderId()}));
        request2.setRequestNo(RandomStringUtils.randomAlphabetic(8));
        request2.setTradingId(bussinessOrderPo.getOrderId());
        request2.setTradeType(1);
        request2.setRefundAmt(new BigDecimal(27.8));

        FastRefundResponse response2 = refundResource.fastRefund(request2, new MockHttpServletRequest());
        Assert.assertEquals(5000, response2.getErrorCode());
    }

    @Test
    public void testFastRefund() throws InterruptedException {
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

        FastRefundRequest request2 = new FastRefundRequest();
        request2.setPaymentId(paymentPo.getPaymentId());
        request2.setAppId(bussinessOrderPo.getAppId());
        request2.setOrderIdList(Arrays.asList(new String[] {bussinessOrderPo.getBussinessOrderId()}));
        request2.setRequestNo(RandomStringUtils.randomAlphabetic(8));
        request2.setTradingId(bussinessOrderPo.getOrderId());
        request2.setTradeType(1);
        request2.setRefundAmt(new BigDecimal(7.8));

        FastRefundResponse response2 = refundResource.fastRefund(request2, new MockHttpServletRequest());
        Assert.assertEquals(0, response2.getErrorCode());
    }

    @Test
    public void testFastRefundFail2() throws InterruptedException {
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
        // request.setRefundAmt(new BigDecimal(7.8));

        FastRefundResponse response = refundResource.fastRefund(request, new MockHttpServletRequest());
        Assert.assertEquals(0, response.getErrorCode());

        FastRefundRequest request2 = new FastRefundRequest();
        request2.setPaymentId(paymentPo.getPaymentId());
        request2.setAppId(bussinessOrderPo.getAppId());
        request2.setOrderIdList(Arrays.asList(new String[] {bussinessOrderPo.getBussinessOrderId()}));
        request2.setTraceId(RandomStringUtils.randomAlphabetic(8));
        request2.setTradingId(bussinessOrderPo.getOrderId());
        request2.setTradeType(1);

        FastRefundResponse response2 = refundResource.fastRefund(request2, new MockHttpServletRequest());
        Assert.assertEquals(5000, response2.getErrorCode());
    }

    @Test
    public void testCheckRefundable() {
        HashMap<String, Object> result = generatePayment();
        BussinessOrderPo bussinessOrderPo = (BussinessOrderPo) result.get("businessOrder");
        PaymentPo paymentPo = (PaymentPo) result.get("payment");

        HashMap<String, Object> result2 = generatePayment();
        BussinessOrderPo bussinessOrderPo2 = (BussinessOrderPo) result2.get("businessOrder");

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


        TradeRefundableRequest req = new TradeRefundableRequest();
        List<String> tradeNos = new ArrayList<>();
        tradeNos.add(bussinessOrderPo.getOrderId());
        tradeNos.add(bussinessOrderPo2.getOrderId());
        req.setTradeNos(tradeNos);

        TradeRefundableResponse response2 = refundResource.checkRefundable(req, new MockHttpServletRequest());
        logger.info(JSON.toJSONString(response));
        Assert.assertEquals(true, response2.getDetails().get(0).isRefundable());
        Assert.assertEquals(1, response2.getDetails().size());
    }

    @Test
    public void testCheckRefundable2() {
        HashMap<String, Object> result = generatePayment();
        BussinessOrderPo bussinessOrderPo = (BussinessOrderPo) result.get("businessOrder");

        HashMap<String, Object> result2 = generatePayment();
        BussinessOrderPo bussinessOrderPo2 = (BussinessOrderPo) result2.get("businessOrder");

        TradeRefundableRequest req = new TradeRefundableRequest();
        List<String> tradeNos = new ArrayList<>();
        tradeNos.add(bussinessOrderPo.getOrderId());
        tradeNos.add(bussinessOrderPo2.getOrderId());
        req.setTradeNos(tradeNos);

        TradeRefundableResponse response = refundResource.checkRefundable(req, new MockHttpServletRequest());
        logger.info(JSON.toJSONString(response));
        Assert.assertEquals(true, response.getDetails().get(0).isRefundable());
        Assert.assertEquals(2, response.getDetails().size());
    }

    @Test
    public void testCheckRefundable3() {
        HashMap<String, Object> result = generatePayment();
        BussinessOrderPo bussinessOrderPo = (BussinessOrderPo) result.get("businessOrder");
        PaymentPo paymentPo = (PaymentPo) result.get("payment");

        HashMap<String, Object> result2 = generatePayment();
        BussinessOrderPo bussinessOrderPo2 = (BussinessOrderPo) result2.get("businessOrder");

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

        TradeRefundableRequest req = new TradeRefundableRequest();
        List<TradeRefundableRequest.TradeDetail> tradeDetails = new ArrayList<>();
        TradeRefundableRequest.TradeDetail detail = new TradeRefundableRequest.TradeDetail();
        detail.setTradeNo(bussinessOrderPo.getOrderId());
        detail.setRefundAmt(new BigDecimal(7.8));
        TradeRefundableRequest.TradeDetail detail2 = new TradeRefundableRequest.TradeDetail();
        detail2.setTradeNo(bussinessOrderPo2.getOrderId());
        detail2.setRefundAmt(new BigDecimal(20.1));
        tradeDetails.add(detail);
        tradeDetails.add(detail2);
        req.setTradeDetails(tradeDetails);

        TradeRefundableResponse response2 = refundResource.checkRefundable(req, new MockHttpServletRequest());
        logger.info(JSON.toJSONString(response2));
        Assert.assertEquals(true, response2.getDetails().get(0).isRefundable());
        Assert.assertEquals(false, response2.getDetails().get(1).isRefundable());
        Assert.assertEquals(2, response2.getDetails().size());
    }

    @Test
    public void testQueryRefundWithOrderId() {
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

        QueryRefundRequest req = new QueryRefundRequest();
        req.setKey("");
        req.setPageSize(10);
        req.setPageIndex(1);
        req.setApproveStatus(9);
        req.setKey(refundRequestPos.get(0).getOrderId());
        System.out.println(refundRequestPos.get(0).getOrderId());

        QueryRefundResponse resp = refundResource.query(req, new MockHttpServletRequest());

        Assert.assertEquals(1, resp.getDetails().size());
        Assert.assertEquals(new BigDecimal(7.8).setScale(2, BigDecimal.ROUND_HALF_UP),
                resp.getDetails().get(0).getRefundAmount().setScale(2));
        Assert.assertEquals(ApproveStatusEnum.FAST_REFUND.getCode(), resp.getDetails().get(0).getApproveStatus());
        Assert.assertEquals(bussinessOrderPo.getOrderId(), resp.getDetails().get(0).getTradeNo());
        Assert.assertEquals(refundRequestPos.get(0).getOrderId(), resp.getDetails().get(0).getOrderId());
    }

    @Test
    public void testSubmitRefundSuccess() {
        HashMap<String, Object> result = generatePayment();
        BussinessOrderPo bussinessOrderPo = (BussinessOrderPo) result.get("businessOrder");

        AcquireRefundRequest request = new AcquireRefundRequest();
        request.setAppId("11111111");
        request.setOrderId("32132143");
        request.setTraceId("12321321321");
        List<TradeDetail> tradeDetails = new ArrayList<>();
        TradeDetail tradeDetail = new TradeDetail();
        tradeDetail.setTradeNo(bussinessOrderPo.getOrderId());
        tradeDetail.setTradeType(1);
        tradeDetails.add(tradeDetail);
        request.setTradeDetails(tradeDetails);

        AcquireRefundResponse response = refundResource.submitRefund(request, new MockHttpServletRequest());
        logger.info(JSON.toJSONString(response));
        Assert.assertEquals(0, response.getErrorCode());
        Assert.assertEquals(1, response.getDetails().size());
        Assert.assertEquals(bussinessOrderPo.getOrderId(), response.getDetails().get(0).getTradeNo());
    }
}
