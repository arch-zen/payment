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
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.ymatou.payment.domain.refund.repository.RefundPository;
import com.ymatou.payment.facade.model.AcquireOrderReq;
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
import com.ymatou.payment.facade.rest.RefundResource;
import com.ymatou.payment.infrastructure.db.mapper.PaymentParamMapper;
import com.ymatou.payment.infrastructure.db.mapper.RefundRequestMapper;
import com.ymatou.payment.infrastructure.db.model.PaymentParamExample;
import com.ymatou.payment.infrastructure.db.model.PaymentParamPo;
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

    @Autowired
    private RefundResource refundResource;

    @Autowired
    private RefundRequestMapper refundRequestMapper;

    @Autowired
    private RefundPository refundPository;

    @Autowired
    private PaymentParamMapper paymentParamMapper;

    // @Test
    public void testFastRefundSuccess() {
        FastRefundRequest fastRefundRequest = new FastRefundRequest();
        fastRefundRequest.setAppId("100001");
        fastRefundRequest.setPaymentId("20151107202902646000000000053774");
        fastRefundRequest.setTradingId("PP20151107300104764");
        fastRefundRequest.setTradeType(1);
        ArrayList<String> a = new ArrayList<String>();
        a.add("PP20151107300104764");
        fastRefundRequest.setOrderIdList(a);
        fastRefundRequest.setTraceId("1000001");

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        FastRefundResponse resp = refundResource.fastRefund(fastRefundRequest, servletRequest);

        Assert.assertEquals(0, resp.getErrorCode());
    }

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
    public void testSubmitRefundSuccess() {
        AcquireRefundRequest request = new AcquireRefundRequest();
        request.setAppId("11111111");
        request.setOrderId("32132143");
        request.setTraceId("12321321321");
        List<TradeDetail> tradeDetails = new ArrayList<>();
        TradeDetail tradeDetail = new TradeDetail();
        tradeDetail.setTradeNo("4070149626832712");
        tradeDetail.setTradeType(1);
        tradeDetails.add(tradeDetail);
        request.setTradeDetails(tradeDetails);

        AcquireRefundResponse response = refundResource.submitRefund(request, new MockHttpServletRequest());
        Assert.assertEquals(0, response.getErrorCode());
        Assert.assertEquals(1, response.getDetails().size());
        Assert.assertEquals("4070149626832712", response.getDetails().get(0).getTradeNo());
    }

    @Test
    public void testCheckRefundable() {
        TradeRefundableRequest req = new TradeRefundableRequest();
        req.setTradeNos(Arrays
                .asList(new String[] {"20150915133207757", "8178254", "19335806615459", "8526937", "114581437722686",
                        "20160503194109806", "114659944449425", "4070149626832712", "8198025", "4092297615140378"}));
        TradeRefundableResponse response = refundResource.checkRefundable(req, new MockHttpServletRequest());
        Assert.assertEquals(2, response.getDetails().size());
    }

    // @Test
    public void testQueryRefund() {
        QueryRefundRequest req = new QueryRefundRequest();
        req.setKey("");
        req.setPageSize(10);
        req.setPageIndex(5); // 41之后的，
        req.setApproveStatus(9);

        QueryRefundResponse resp = refundResource.query(req, new MockHttpServletRequest());

        Assert.assertEquals("21568176200183545", resp.getDetails().get(0).getPaymentId());
    }

    @Test
    public void testQueryRefundWithOrderId() {
        QueryRefundRequest req = new QueryRefundRequest();
        req.setKey("");
        req.setPageSize(10);
        req.setPageIndex(1);
        req.setApproveStatus(9);
        req.setKey("127822744");

        QueryRefundResponse resp = refundResource.query(req, new MockHttpServletRequest());

        Assert.assertEquals(1, resp.getDetails().size());
        Assert.assertEquals("127822744", resp.getDetails().get(0).getOrderId());
    }

    @Test
    public void testApproveRefund() {
        ApproveRefundRequest req = new ApproveRefundRequest();
        req.setApproveUser("testApproveRefund");
        req.setRefundNos(Arrays.asList(new String[] {"20151104150448396000000000027244"}));

        ApproveRefundResponse response = refundResource.approveRefund(req, new MockHttpServletRequest());
        Assert.assertEquals(true, response.getIsSuccess());

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


        SysApproveRefundReq req = new SysApproveRefundReq();
        Calendar approveTime = Calendar.getInstance();
        approveTime.add(Calendar.HOUR, 1);
        req.setBizId(StringUtil.getDateFormatString("yyyy-MM-dd HH:00:00", approveTime.getTime()));
        String sysApproveRefund = refundResource.sysApproveRefund(req, null);

        Assert.assertEquals("ok", sysApproveRefund);

        RefundRequestPo refundResult1 = refundPository.getRefundRequestByPaymentId(refund1.getPaymentId());

        Assert.assertEquals(1, refundResult1.getApproveStatus().intValue());
        Assert.assertEquals("system", refundResult1.getApprovedUser());
        Assert.assertNotNull(refundResult1.getApprovedTime());


        RefundRequestPo refundResult2 = refundPository.getRefundRequestByPaymentId(refund2.getPaymentId());

        Assert.assertEquals(1, refundResult2.getApproveStatus().intValue());
        Assert.assertEquals("system", refundResult2.getApprovedUser());
        Assert.assertNotNull(refundResult2.getApprovedTime());

        RefundRequestPo refundResult3 = refundPository.getRefundRequestByPaymentId(refund3.getPaymentId());

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

        RefundRequestPo refundResult1 = refundPository.getRefundRequestByPaymentId(refund1.getPaymentId());

        Assert.assertEquals(0, refundResult1.getApproveStatus().intValue());
        Assert.assertNull(refundResult1.getApprovedUser());
        Assert.assertNull(refundResult1.getApprovedTime());

        // 时间范围的申请审批通过
        approveTime = Calendar.getInstance();
        approveTime.add(Calendar.HOUR, 1);
        req.setBizId(StringUtil.getDateFormatString("yyyy-MM-dd HH:00:00", approveTime.getTime()));
        sysApproveRefund = refundResource.sysApproveRefund(req, null);
        Assert.assertEquals("ok", sysApproveRefund);

        refundResult1 = refundPository.getRefundRequestByPaymentId(refund1.getPaymentId());
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

        RefundRequestPo refundResult1 = refundPository.getRefundRequestByPaymentId(refund1.getPaymentId());
        Assert.assertEquals(0, refundResult1.getApproveStatus().intValue());
        Assert.assertNull(refundResult1.getApprovedUser());
        Assert.assertNull(refundResult1.getApprovedTime());


        // 设置风控值
        setRefundMaxNum(Calendar.getInstance().get(Calendar.HOUR_OF_DAY), 200);
        sysApproveRefund = refundResource.sysApproveRefund(req, null);

        refundResult1 = refundPository.getRefundRequestByPaymentId(refund1.getPaymentId());
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
}
