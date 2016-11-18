/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.facade.impl.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.repository.CmbAggrementRepository;
import com.ymatou.payment.domain.pay.service.PayService;
import com.ymatou.payment.facade.constants.CmbAggrementStatusEnum;
import com.ymatou.payment.facade.constants.CmbCancelTypeEnum;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireOrderResp;
import com.ymatou.payment.facade.model.SyncCmbPublicKeyReq;
import com.ymatou.payment.facade.rest.PaymentNotifyResource;
import com.ymatou.payment.facade.rest.PaymentResource;
import com.ymatou.payment.infrastructure.Money;
import com.ymatou.payment.infrastructure.db.extmapper.CmbPublicKeyExtMapper;
import com.ymatou.payment.infrastructure.db.mapper.PaymentMapper;
import com.ymatou.payment.infrastructure.db.model.CmbAggrementPo;
import com.ymatou.payment.infrastructure.db.model.CmbPublicKeyPo;
import com.ymatou.payment.infrastructure.security.RSAUtil;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.model.CmbPayNotifyRequest;
import com.ymatou.payment.integration.model.CmbPayNotifyRequest.PayNoticeData;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 一网通支付收单测试用例
 * 
 * @author wangxudong 2016年11月14日 下午9:47:02
 *
 */
public class CmbPaymentResourceImplTest extends RestBaseTest {
    @Resource
    private PaymentResource paymentResource;

    @Resource
    private PaymentNotifyResource paymentNotifyResource;

    @Resource
    private PayService payService;

    @Resource
    private IntegrationConfig config;

    @Resource
    private InstitutionConfigManager institutionConfigManager;

    @Resource
    private PaymentMapper paymentMapper;

    @Resource
    private CmbPublicKeyExtMapper cmbPublicKeyExtMapper;

    @Resource
    private CmbAggrementRepository cmbAggrementRepository;

    @Resource
    private SqlSession sqlSession;

    /**
     * Mock 支付宝私钥
     */
    private final String mockYmtPrivateKey =
            "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAOY/57sgqIjHEZjNhmXlLapuYJuSACN4zzWaDCnujAEd2dxulxp57ajrnNgUjRJcm7O7mtkF7cE8u5y5l6De3sWM1/YLuPyB8Nhy+IxqY7AjtW5Zn5kS0IdIeQ6FSXBy6XnEsxeXac93VjMvrMwJ7ZUArvlwegEqd34OYRVI5CKlAgMBAAECgYBApPCKuUCYJkvqesmhEhcgIp09EGC5lNGYWwfPPgpQxfDE0sfZxyHSq1P91sdEwHt2mtV+2QtHlaWW+wR3RhuFEuGM1z8fsvongAk9bNDPvaPz07HF1YwXuviakDYk1bWwqCS+9VFJ82fGae4+ftUQOmJYSH+LV89RRqWdCP5GgQJBAP/nwVbgw/bBR04UDfUK2Bdr+Op+6WFdFoyzK7Kvr5sjO0T5ewswHJ34+B26X50kGqkIU2h2AXh8/AX1ZJUB5vsCQQDmVbglUXIrjLG5zraxstNlDnJvDL3WmYtZJxbKWq9QgSWYzf4iCaAVqsjZHfAHAV2iMGf+x55QGuHk7hZ0SGrfAkEAlVRE0xCX6c8BcANt3Zc1X/2GpDfosgMjHHmVP1Eb1RirBmXasj2iBWD6UEaocsdVs1uDaIqr8wZj/ooi5nzUrwJBAM5R2jETU4FO9aPKVju2Q0UyO67dau7fesLREMkRkhg6lsLZQdqbZJoD8QUKnAaqYoT1dzHw/Q4kBlRaMCLY+2ECQQDVAbyCAAte4LH9EndxkisOZXKMLbjhlmORpyKBUwSwW6Hk4If4hlKTIOUCuwXJzb08BK40AGD+pw6P35e+B3Dh";


    @Test
    public void testCmbAcquireOrder() {
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);
        req.setPayPrice("1.01");

        long userId = req.getUserId();
        // 删除用户的签约记录
        cmbAggrementRepository.deleteByUserId(userId);

        // 确认记录已经删除
        CmbAggrementPo findInitAggrement = cmbAggrementRepository.findInitAggrement(userId);
        assertNull(findInitAggrement);

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
        assertEquals("验证BizCode", req.getBizCode(), bo.getBizCode().code());
        assertEquals("验证OrderStatus", new Integer(0), bo.getOrderStatus());
        assertEquals("验证NotifyStatus", new Integer(0), bo.getNotifyStatus());

        CmbAggrementPo newAggrement = cmbAggrementRepository.findInitAggrement(userId);
        assertNotNull(newAggrement);

        assertEquals(CmbAggrementStatusEnum.INIT.code(), newAggrement.getAggStatus());
        assertEquals(CmbCancelTypeEnum.NOCANCEL.code(), newAggrement.getCancelType());
        assertEquals(userId, newAggrement.getUserId().longValue());
        assertEquals(true, newAggrement.getAggId() > 0);
    }

    @Test
    public void testNotify() throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException,
            InvalidKeySpecException, SignatureException {
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);
        req.setPayPrice("1.01");

        long userId = req.getUserId();
        // 删除用户的签约记录
        cmbAggrementRepository.deleteByUserId(userId);

        // 确认记录已经删除
        CmbAggrementPo findInitAggrement = cmbAggrementRepository.findInitAggrement(userId);
        assertNull(findInitAggrement);

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        AcquireOrderResp res = paymentResource.acquireOrder(req, servletRequest);

        assertEquals("验证返回码", 0, res.getErrorCode());
        assertEquals("验证TraceId", req.getTraceId(), res.getTraceId());
        assertEquals("验证ResultType", "Form", res.getResultType());

        BussinessOrder bo = payService.getBussinessOrderByOrderId(req.orderId);
        assertNotNull("验证商户订单", bo);

        Payment payment = payService.getPaymentByBussinessOrderId(bo.getBussinessOrderId());
        assertNotNull(payment);


        // 构建回调请求
        servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader("mock", "1");
        servletRequest.setRequestURI("/cmbPayNotify");

        // 获取到第三方机构配置
        InstitutionConfig institutionConfig = institutionConfigManager.getConfig(PayTypeEnum.CmbApp);

        CmbPayNotifyRequest cmbPayNotifyRequest = new CmbPayNotifyRequest();
        PayNoticeData noticeData = cmbPayNotifyRequest.getNoticeData();
        noticeData.setAmount(req.getPayPrice());
        noticeData.setDiscountAmount("0.01");
        noticeData.setNoticeUrl("noticeurl");
        noticeData.setHttpMethod("POST");
        noticeData.setBranchNo(institutionConfig.getBranchNo());
        noticeData.setMerchantNo(institutionConfig.getMerchantId());
        noticeData.setNoticeType("BKPAYRTN");
        noticeData.setNoticeSerialNo(UUID.randomUUID().toString());
        noticeData.setDate("20160624");
        noticeData.setOrderNo(payment.getPaymentId());
        noticeData.setBankDate("20160624");
        noticeData.setBankSerialNo(UUID.randomUUID().toString());
        noticeData.setDiscountFlag("Y");
        noticeData.setMerchantPara("Pay");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTime = simpleDateFormat.format(new Date());
        noticeData.setDateTime(dateTime);

        // 签名
        String sign = RSAUtil.sign(cmbPayNotifyRequest.buildSignString(), mockYmtPrivateKey);
        cmbPayNotifyRequest.setSign(sign);
        cmbPayNotifyRequest.setSignType("RSA");

        // 构建请求报文
        String jsonRequestData = URLEncoder.encode(JSON.toJSONString(cmbPayNotifyRequest), "UTF-8");
        String requestBody = String.format("jsonRequestData=%s", jsonRequestData);
        servletRequest.setContent(requestBody.getBytes("utf-8"));

        Response cmbPayNotify = paymentNotifyResource.cmbPayNotify(servletRequest);
        assertEquals(200, cmbPayNotify.getStatus());


        payment = payService.getPaymentByBussinessOrderId(bo.getBussinessOrderId());
        assertNotNull(payment);
        assertEquals(noticeData.getBankSerialNo(), payment.getInstitutionPaymentId());
        assertEquals(noticeData.getOrderNo(), payment.getPaymentId());
        assertEquals(noticeData.getAmount(), payment.getActualPayPrice().toString());
        assertEquals(noticeData.getDateTime(), simpleDateFormat.format(payment.getPayTime()));
        assertEquals(noticeData.getDiscountAmount(), payment.getDiscountAmt().toString());
        assertEquals(PayStatusEnum.Paied, payment.getPayStatus());

    }

    @Test
    public void testNotifyNoDiscont()
            throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException,
            InvalidKeySpecException, SignatureException {
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);
        req.setPayPrice("1.01");

        long userId = req.getUserId();
        // 删除用户的签约记录
        cmbAggrementRepository.deleteByUserId(userId);

        // 确认记录已经删除
        CmbAggrementPo findInitAggrement = cmbAggrementRepository.findInitAggrement(userId);
        assertNull(findInitAggrement);

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        AcquireOrderResp res = paymentResource.acquireOrder(req, servletRequest);

        assertEquals("验证返回码", 0, res.getErrorCode());
        assertEquals("验证TraceId", req.getTraceId(), res.getTraceId());
        assertEquals("验证ResultType", "Form", res.getResultType());

        BussinessOrder bo = payService.getBussinessOrderByOrderId(req.orderId);
        assertNotNull("验证商户订单", bo);

        Payment payment = payService.getPaymentByBussinessOrderId(bo.getBussinessOrderId());
        assertNotNull(payment);


        // 构建回调请求
        servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader("mock", "1");
        servletRequest.setRequestURI("/cmbPayNotify");

        // 获取到第三方机构配置
        InstitutionConfig institutionConfig = institutionConfigManager.getConfig(PayTypeEnum.CmbApp);

        CmbPayNotifyRequest cmbPayNotifyRequest = new CmbPayNotifyRequest();
        PayNoticeData noticeData = cmbPayNotifyRequest.getNoticeData();
        noticeData.setAmount(req.getPayPrice());
        noticeData.setNoticeUrl("noticeurl");
        noticeData.setHttpMethod("POST");
        noticeData.setBranchNo(institutionConfig.getBranchNo());
        noticeData.setMerchantNo(institutionConfig.getMerchantId());
        noticeData.setNoticeType("BKPAYRTN");
        noticeData.setNoticeSerialNo(UUID.randomUUID().toString());
        noticeData.setDate("20160624");
        noticeData.setOrderNo(payment.getPaymentId());
        noticeData.setBankDate("20160624");
        noticeData.setBankSerialNo(UUID.randomUUID().toString());
        noticeData.setDiscountFlag("N");
        noticeData.setMerchantPara("Pay");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTime = simpleDateFormat.format(new Date());
        noticeData.setDateTime(dateTime);

        // 签名
        String sign = RSAUtil.sign(cmbPayNotifyRequest.buildSignString(), mockYmtPrivateKey);
        cmbPayNotifyRequest.setSign(sign);
        cmbPayNotifyRequest.setSignType("RSA");

        // 构建请求报文
        String jsonRequestData = URLEncoder.encode(JSON.toJSONString(cmbPayNotifyRequest), "UTF-8");
        String requestBody = String.format("jsonRequestData=%s", jsonRequestData);
        servletRequest.setContent(requestBody.getBytes("utf-8"));

        Response cmbPayNotify = paymentNotifyResource.cmbPayNotify(servletRequest);
        assertEquals(200, cmbPayNotify.getStatus());

        // 验证支付单的状态
        payment = payService.getPaymentByBussinessOrderId(bo.getBussinessOrderId());
        assertNotNull(payment);
        assertEquals(noticeData.getBankSerialNo(), payment.getInstitutionPaymentId());
        assertEquals(noticeData.getOrderNo(), payment.getPaymentId());
        assertEquals(noticeData.getAmount(), payment.getActualPayPrice().toString());
        assertEquals(noticeData.getDateTime(), simpleDateFormat.format(payment.getPayTime()));
        assertEquals(new Money(0), payment.getDiscountAmt());
        assertEquals(PayStatusEnum.Paied, payment.getPayStatus());
    }

    @Test
    public void testNotifyWithWrongSign()
            throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException,
            InvalidKeySpecException, SignatureException {
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);
        req.setPayPrice("1.01");

        long userId = req.getUserId();
        // 删除用户的签约记录
        cmbAggrementRepository.deleteByUserId(userId);

        // 确认记录已经删除
        CmbAggrementPo findInitAggrement = cmbAggrementRepository.findInitAggrement(userId);
        assertNull(findInitAggrement);

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        AcquireOrderResp res = paymentResource.acquireOrder(req, servletRequest);

        assertEquals("验证返回码", 0, res.getErrorCode());
        assertEquals("验证TraceId", req.getTraceId(), res.getTraceId());
        assertEquals("验证ResultType", "Form", res.getResultType());

        BussinessOrder bo = payService.getBussinessOrderByOrderId(req.orderId);
        assertNotNull("验证商户订单", bo);

        Payment payment = payService.getPaymentByBussinessOrderId(bo.getBussinessOrderId());
        assertNotNull(payment);


        // 构建回调请求
        servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/cmbPayNotify");

        // 获取到第三方机构配置
        InstitutionConfig institutionConfig = institutionConfigManager.getConfig(PayTypeEnum.CmbApp);

        CmbPayNotifyRequest cmbPayNotifyRequest = new CmbPayNotifyRequest();
        PayNoticeData noticeData = cmbPayNotifyRequest.getNoticeData();
        noticeData.setAmount(req.getPayPrice());
        noticeData.setNoticeUrl("noticeurl");
        noticeData.setHttpMethod("POST");
        noticeData.setBranchNo(institutionConfig.getBranchNo());
        noticeData.setMerchantNo(institutionConfig.getMerchantId());
        noticeData.setNoticeType("BKPAYRTN");
        noticeData.setNoticeSerialNo(UUID.randomUUID().toString());
        noticeData.setDate("20160624");
        noticeData.setOrderNo(payment.getPaymentId());
        noticeData.setBankDate("20160624");
        noticeData.setBankSerialNo(UUID.randomUUID().toString());
        noticeData.setDiscountFlag("N");
        noticeData.setMerchantPara("Pay");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTime = simpleDateFormat.format(new Date());
        noticeData.setDateTime(dateTime);

        // 签名
        String sign = RSAUtil.sign(cmbPayNotifyRequest.buildSignString(), mockYmtPrivateKey);
        cmbPayNotifyRequest.setSign(sign);
        cmbPayNotifyRequest.setSignType("RSA");

        // 构建请求报文
        String jsonRequestData = URLEncoder.encode(JSON.toJSONString(cmbPayNotifyRequest), "UTF-8");
        String requestBody = String.format("jsonRequestData=%s", jsonRequestData);
        servletRequest.setContent(requestBody.getBytes("utf-8"));

        Response cmbPayNotify = paymentNotifyResource.cmbPayNotify(servletRequest);
        assertEquals(500, cmbPayNotify.getStatus());
    }

    @Test
    public void testSyncCmbPublicKeyReq() {
        SyncCmbPublicKeyReq syncCmbPublicKeyReq = new SyncCmbPublicKeyReq();

        String resp = paymentResource.syncCmbPublicKeyReq(syncCmbPublicKeyReq, buildServletRequestWithMock());

        assertEquals("ok", resp);

        CmbPublicKeyPo cmbPublicKeyPo = cmbPublicKeyExtMapper.selectLatestOne();

        assertNotNull(cmbPublicKeyPo);
        assertEquals(true, Math.abs(System.currentTimeMillis() - cmbPublicKeyPo.getCreatedTime().getTime()) < 2000);
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
        req.setPayType("20");
        req.setProductName("测试商品");
        req.setProductDesc("商品描述");
        req.setProductUrl("www.ymatou.com");
        req.setMemo("备注");
        req.setSignMethod("MD5");
        req.setUserId(12345L);
        req.setUserIp("127.0.0.1");
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
