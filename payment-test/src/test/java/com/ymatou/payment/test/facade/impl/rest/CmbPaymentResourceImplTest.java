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
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
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
import com.ymatou.payment.domain.refund.repository.RefundPository;
import com.ymatou.payment.facade.constants.CmbAggrementStatusEnum;
import com.ymatou.payment.facade.constants.CmbCancelTypeEnum;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireOrderResp;
import com.ymatou.payment.facade.model.CheckPaymentRequset;
import com.ymatou.payment.facade.model.ExecuteRefundRequest;
import com.ymatou.payment.facade.model.FastRefundRequest;
import com.ymatou.payment.facade.model.FastRefundResponse;
import com.ymatou.payment.facade.model.SyncCmbPublicKeyReq;
import com.ymatou.payment.facade.rest.CheckPaymentResource;
import com.ymatou.payment.facade.rest.PaymentNotifyResource;
import com.ymatou.payment.facade.rest.PaymentResource;
import com.ymatou.payment.facade.rest.RefundJobResource;
import com.ymatou.payment.facade.rest.RefundResource;
import com.ymatou.payment.facade.rest.SignNotifyResource;
import com.ymatou.payment.infrastructure.Money;
import com.ymatou.payment.infrastructure.db.extmapper.CmbPublicKeyExtMapper;
import com.ymatou.payment.infrastructure.db.mapper.PaymentMapper;
import com.ymatou.payment.infrastructure.db.model.CmbAggrementPo;
import com.ymatou.payment.infrastructure.db.model.CmbPublicKeyPo;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;
import com.ymatou.payment.infrastructure.security.RSAUtil;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.CmbSignature;
import com.ymatou.payment.integration.model.CmbPayNotifyRequest;
import com.ymatou.payment.integration.model.CmbPayNotifyRequest.PayNoticeData;
import com.ymatou.payment.integration.model.CmbSignNotifyRequest;
import com.ymatou.payment.integration.model.CmbSignNotifyRequest.SignNoticeData;
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
    private CheckPaymentResource checkPaymentResource;

    @Resource
    private RefundJobResource refundJobResource;

    @Resource
    private RefundResource refundResource;

    @Resource
    private SignNotifyResource signNotifyResource;

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
    private RefundPository refundPository;

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

        @SuppressWarnings("unused")
        MockHttpServletRequest servletRequest = payAndBaseAssert(req);

        BussinessOrder bo = payService.getBussinessOrderByOrderId(req.orderId);
        assertNotNull("验证商户订单", bo);

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

        long userId = req.getUserId();
        CmbAggrementPo newAggrement = cmbAggrementRepository.findInitAggrement(userId);
        assertNotNull(newAggrement);

        assertEquals(CmbAggrementStatusEnum.INIT.code(), newAggrement.getAggStatus());
        assertEquals(CmbCancelTypeEnum.NOCANCEL.code(), newAggrement.getCancelType());
        assertEquals(userId, newAggrement.getUserId().longValue());
        assertEquals(true, newAggrement.getAggId() > 0);
    }

    private MockHttpServletRequest payAndBaseAssert(AcquireOrderReq req) {
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

        return servletRequest;
    }

    @Test
    public void testNotify() throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException,
            InvalidKeySpecException, SignatureException {
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);
        req.setPayPrice("1.01");

        MockHttpServletRequest servletRequest = payAndBaseAssert(req);

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
        assertEquals(noticeData.getAmount(), payment.getActualPayPrice().add(payment.getDiscountAmt()).toString());
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

        MockHttpServletRequest servletRequest = payAndBaseAssert(req);

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

        MockHttpServletRequest servletRequest = payAndBaseAssert(req);

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
    public void testSignNotify() throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException,
            InvalidKeySpecException, SignatureException {
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);
        req.setPayPrice("1.01");

        MockHttpServletRequest servletRequest = payAndBaseAssert(req);

        BussinessOrder bo = payService.getBussinessOrderByOrderId(req.orderId);
        assertNotNull("验证商户订单", bo);

        Payment payment = payService.getPaymentByBussinessOrderId(bo.getBussinessOrderId());
        assertNotNull("验证支付单", payment);

        CmbAggrementPo aggrementPo = cmbAggrementRepository.findInitAggrement(req.getUserId());
        assertNotNull("验证签约记录", aggrementPo);

        // 构建回调请求
        servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader("mock", "1");
        servletRequest.setRequestURI("/cmbPayNotify");

        // 获取到第三方机构配置
        InstitutionConfig institutionConfig = institutionConfigManager.getConfig(PayTypeEnum.CmbApp);

        // 构建签约通知
        CmbSignNotifyRequest cmbSignNotifyRequest = new CmbSignNotifyRequest();
        SignNoticeData signNoticeData = cmbSignNotifyRequest.getNoticeData();
        signNoticeData.setRspCode("SUC0000");
        signNoticeData.setRspMsg("签约成功");
        signNoticeData.setNoticeSerialNo(UUID.randomUUID().toString());
        signNoticeData.setUserID(String.valueOf(bo.getUserId()));
        signNoticeData.setAgrNo(String.valueOf(aggrementPo.getAggId()));
        signNoticeData.setBranchNo(institutionConfig.getBranchNo());
        signNoticeData.setMerchantNo(institutionConfig.getMerchantId());
        signNoticeData.setNoPwdPay("N");


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTime = simpleDateFormat.format(new Date());
        signNoticeData.setDateTime(dateTime);
        signNoticeData.setUserPidHash(dateTime);
        signNoticeData.setUserPidType("1");

        // 签名
        String rawString = JSON.toJSONString(cmbSignNotifyRequest);
        String signString = CmbSignature.buildSignString(rawString, "noticeData");
        String sign = RSAUtil.sign(signString, mockYmtPrivateKey);
        cmbSignNotifyRequest.setSign(sign);
        cmbSignNotifyRequest.setSignType("RSA");

        // 构建请求报文
        String jsonRequestData = URLEncoder.encode(JSON.toJSONString(cmbSignNotifyRequest), "UTF-8");
        String requestBody = String.format("jsonRequestData=%s", jsonRequestData);
        servletRequest.setContent(requestBody.getBytes("utf-8"));

        Response cmbSignNotify = signNotifyResource.cmbSignNotify(servletRequest);
        assertEquals(200, cmbSignNotify.getStatus());

        // 验证更新后的协议
        CmbAggrementPo aggrement = cmbAggrementRepository.getByAggId(aggrementPo.getAggId());
        assertNotNull("验证更新后的协议", aggrement);
        assertEquals(CmbAggrementStatusEnum.SIGN.code(), aggrement.getAggStatus());
        assertEquals(dateTime, aggrement.getSignDateTime());
        assertEquals(signNoticeData.getRspCode(), aggrement.getRespCode());
        assertEquals(signNoticeData.getRspMsg(), aggrement.getRespMessage());
        assertEquals(signNoticeData.getNoticeSerialNo(), aggrement.getBankSerialNo());
        assertEquals(signNoticeData.getUserPidType(), aggrement.getUserPidType());
        assertEquals(signNoticeData.getUserPidHash(), aggrement.getUserPidHash());
        assertEquals(signNoticeData.getNoPwdPay(), aggrement.getNoPwdPay());

        // 同个用户再下一单，验证不会生成新的协议
        req.setOrderId(getDateFormatString("yyyyMMddHHmmssSSS"));
        AcquireOrderResp res = paymentResource.acquireOrder(req, servletRequest);

        assertEquals("验证返回码", 0, res.getErrorCode());
        assertEquals("验证TraceId", req.getTraceId(), res.getTraceId());
        assertEquals("验证ResultType", "Form", res.getResultType());

        CmbAggrementPo findInitAggrement2 = cmbAggrementRepository.findInitAggrement(req.getUserId());
        assertNull(findInitAggrement2);
    }

    @Test
    public void testSignNotifyWithWrongSign()
            throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException,
            InvalidKeySpecException, SignatureException {
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);
        req.setPayPrice("1.01");

        MockHttpServletRequest servletRequest = payAndBaseAssert(req);

        BussinessOrder bo = payService.getBussinessOrderByOrderId(req.orderId);
        assertNotNull("验证商户订单", bo);

        Payment payment = payService.getPaymentByBussinessOrderId(bo.getBussinessOrderId());
        assertNotNull("验证支付单", payment);

        CmbAggrementPo aggrementPo = cmbAggrementRepository.findInitAggrement(req.getUserId());
        assertNotNull("验证签约记录", aggrementPo);

        // 构建回调请求
        servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader("mock", "1");
        servletRequest.setRequestURI("/cmbPayNotify");

        // 获取到第三方机构配置
        InstitutionConfig institutionConfig = institutionConfigManager.getConfig(PayTypeEnum.CmbApp);

        // 构建签约通知
        CmbSignNotifyRequest cmbSignNotifyRequest = new CmbSignNotifyRequest();
        SignNoticeData signNoticeData = cmbSignNotifyRequest.getNoticeData();
        signNoticeData.setRspCode("SUC0000");
        signNoticeData.setRspMsg("签约成功");
        signNoticeData.setNoticeSerialNo(UUID.randomUUID().toString());
        signNoticeData.setUserID(String.valueOf(bo.getUserId()));
        signNoticeData.setAgrNo(String.valueOf(aggrementPo.getAggId()));
        signNoticeData.setBranchNo(institutionConfig.getBranchNo());
        signNoticeData.setMerchantNo(institutionConfig.getMerchantId());
        signNoticeData.setNoPwdPay("N");


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTime = simpleDateFormat.format(new Date());
        signNoticeData.setDateTime(dateTime);
        signNoticeData.setUserPidHash(dateTime);
        signNoticeData.setUserPidType("1");

        // 签名
        String sign = RSAUtil.sign(cmbSignNotifyRequest.buildSignString(), mockYmtPrivateKey);
        cmbSignNotifyRequest.setSign(sign + "error sign");
        cmbSignNotifyRequest.setSignType("RSA");

        // 构建请求报文
        String jsonRequestData = URLEncoder.encode(JSON.toJSONString(cmbSignNotifyRequest), "UTF-8");
        String requestBody = String.format("jsonRequestData=%s", jsonRequestData);
        servletRequest.setContent(requestBody.getBytes("utf-8"));

        Response cmbSignNotify = signNotifyResource.cmbSignNotify(servletRequest);
        assertEquals(500, cmbSignNotify.getStatus());

        // 验证更新后的协议
        CmbAggrementPo aggrement = cmbAggrementRepository.getByAggId(aggrementPo.getAggId());
        assertNotNull("验证更新后的协议", aggrement);
        assertEquals(CmbAggrementStatusEnum.INIT.code(), aggrement.getAggStatus());
    }

    @Test
    public void testSyncCmbPublicKeyReq() {
        SyncCmbPublicKeyReq syncCmbPublicKeyReq = new SyncCmbPublicKeyReq();

        String resp = paymentResource.syncCmbPublicKeyReq(syncCmbPublicKeyReq, buildServletRequestWithMock());

        assertEquals("ok", resp);

        CmbPublicKeyPo cmbPublicKeyPo = cmbPublicKeyExtMapper.selectLatestOne();

        assertNotNull(cmbPublicKeyPo);
        assertEquals(true, Math.abs(System.currentTimeMillis() - cmbPublicKeyPo.getCreatedTime().getTime()) < 3000);
    }


    @Test
    public void testCheckPayment()
            throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException,
            InvalidKeySpecException, SignatureException {
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);
        req.setPayPrice("1.01");

        MockHttpServletRequest servletRequest = payAndBaseAssert(req);

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

        // 发起对账请求
        CheckPaymentRequset checkPaymentRequset = new CheckPaymentRequset();
        checkPaymentRequset.setPaymentId(payment.getPaymentId());
        checkPaymentRequset.setFinalCheck(true);
        checkPaymentRequset.setPayType(PayTypeEnum.CmbApp.getCode());

        MockHttpServletRequest servletRequest1 = new MockHttpServletRequest();
        servletRequest1.addHeader("mock", "1");
        checkPaymentResource.checkPayment(checkPaymentRequset, servletRequest1);

        Payment paymentCheck = payService.getPaymentByBussinessOrderId(bo.getBussinessOrderId());
        assertNotNull(payment);
        assertEquals(1, paymentCheck.getCheckStatus().intValue());
    }

    @Test
    public void testCheckPaymentWhenThirdNotPay()
            throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException,
            InvalidKeySpecException, SignatureException {
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);
        req.setPayPrice("1.01");

        MockHttpServletRequest servletRequest = payAndBaseAssert(req);

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

        // 发起对账请求
        CheckPaymentRequset checkPaymentRequset = new CheckPaymentRequset();
        checkPaymentRequset.setPaymentId(payment.getPaymentId());
        checkPaymentRequset.setFinalCheck(true);
        checkPaymentRequset.setPayType(PayTypeEnum.CmbApp.getCode());

        MockHttpServletRequest servletRequest1 = new MockHttpServletRequest();
        servletRequest1.addHeader("mock", "1");
        servletRequest1.addHeader("MockResult-Cmb-rspCode", "ERROR");
        checkPaymentResource.checkPayment(checkPaymentRequset, servletRequest1);

        Payment paymentCheck = payService.getPaymentByBussinessOrderId(bo.getBussinessOrderId());
        assertNotNull(payment);
        assertEquals(-20, paymentCheck.getCheckStatus().intValue());
    }

    @Test
    public void testDoRefundFast()
            throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException,
            InvalidKeySpecException, SignatureException {

        // 由于存在优惠金额，当招行支付 140 ，优惠10，码头余额入账150 发生快速退款
        // 此时支付网关 应该退用户 140， 码头余额出账 150
        // 如果发生部分的快速退款 130， 码头余额出账 (130 / 140) * 10 + 130
        BigDecimal refundAmount = new BigDecimal(140);
        BigDecimal actualAmount = new BigDecimal(140);
        BigDecimal discountAmount = new BigDecimal(10);
        BigDecimal accountAmount = refundAmount.divide(actualAmount, MathContext.DECIMAL64).multiply(discountAmount)
                .add(refundAmount).setScale(2, RoundingMode.DOWN);

        assertEquals(new BigDecimal(150.00).setScale(2, RoundingMode.DOWN), accountAmount);

        refundAmount = new BigDecimal(130);
        accountAmount = refundAmount.divide(actualAmount, MathContext.DECIMAL64).multiply(discountAmount)
                .add(refundAmount).setScale(2, RoundingMode.DOWN);

        assertEquals("139.28", accountAmount.toString());


        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);
        req.setPayPrice("1.01");

        MockHttpServletRequest servletRequest = payAndBaseAssert(req);

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

        Money discontAmount = new Money(0.01);
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
        noticeData.setDiscountFlag("Y");
        noticeData.setMerchantPara("Pay");
        noticeData.setDiscountAmount(discontAmount.toString());

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
        assertEquals(noticeData.getAmount(), payment.getActualPayPrice().add(discontAmount).toString());
        assertEquals(noticeData.getDateTime(), simpleDateFormat.format(payment.getPayTime()));
        assertEquals(discontAmount, payment.getDiscountAmt());
        assertEquals(payment.getPayPrice().subtract(discontAmount), payment.getActualPayPrice());
        assertEquals(PayStatusEnum.Paied, payment.getPayStatus());

        // 构建快速退款
        FastRefundRequest fastRefundRequest = new FastRefundRequest();
        fastRefundRequest.setAppId("100001");
        fastRefundRequest.setPaymentId(payment.getPaymentId());
        fastRefundRequest.setTradingId(bo.getOrderId());
        fastRefundRequest.setTradeType(1);

        ArrayList<String> a = new ArrayList<String>();
        a.add("12345678");
        fastRefundRequest.setOrderIdList(a);
        fastRefundRequest.setTraceId(StringUtils.left(UUID.randomUUID().toString(), 32));

        FastRefundResponse resp = refundResource.fastRefund(fastRefundRequest, servletRequest);
        assertEquals(0, resp.getErrorCode());

        List<RefundRequestPo> refundRequestPos =
                refundPository.getRefundReqestByTraceId(fastRefundRequest.getTraceId());
        assertEquals(1, refundRequestPos.size());
        assertEquals(RefundStatusEnum.THIRDPART_REFUND_SUCCESS.getCode(),
                refundRequestPos.get(0).getRefundStatus().intValue());

        // 构建执行退款
        ExecuteRefundRequest executeRefundRequest = new ExecuteRefundRequest();
        executeRefundRequest.setRefundId(refundRequestPos.get(0).getRefundId());
        executeRefundRequest.setRequestId(UUID.randomUUID().toString());

        String executeRefund = refundJobResource.executeRefund(executeRefundRequest, servletRequest);
        assertEquals("4", executeRefund);

        refundRequestPos =
                refundPository.getRefundReqestByTraceId(fastRefundRequest.getTraceId());
        assertEquals(1, refundRequestPos.size());
        assertEquals(RefundStatusEnum.COMPLETE_SUCCESS.getCode(),
                refundRequestPos.get(0).getRefundStatus().intValue());

        // 验证退款金额 == 实际支付金额
        payment = payService.getPaymentByBussinessOrderId(bo.getBussinessOrderId());
        assertEquals(payment.getRefundAmt(), payment.getActualPayPrice().getAmount());


        // 再次执行退款，返回ok
        executeRefund = refundJobResource.executeRefund(executeRefundRequest, servletRequest);
        assertEquals("ok", executeRefund);

        System.out.println(payment.getPaymentId());
    }

    @Test
    public void testPaymentNotifyFromCMBTest() throws UnsupportedEncodingException {
        String requestBody =
                "jsonRequestData=%7B%22noticeData%22%3A%7B%22branchNo%22%3A%220021%22%2C%22AccountType%22%3A%22DebitCard%22%2C%22merchantPara%22%3A%22Pay%22%2C%22httpMethod%22%3A%22POST%22%2C%22merchantNo%22%3A%22000157%22%2C%22bankDate%22%3A%2220161125%22%2C%22discountFlag%22%3A%22N%22%2C%22noticeSerialNo%22%3A%220021000157201611251603213576%22%2C%22date%22%3A%2220161125%22%2C%22orderNo%22%3A%221603213576%22%2C%22bankSerialNo%22%3A%2216312531000000000020%22%2C%22noticeType%22%3A%22BKPAYRTN%22%2C%22noticeUrl%22%3A%22http%3A%2F%2F116.228.41.4%3A8081%2FcmbPayNotify%22%2C%22amount%22%3A%2297.00%22%2C%22discountAmount%22%3A%220.00%22%2C%22dateTime%22%3A%2220161125183627%22%7D%2C%22signType%22%3A%22RSA%22%2C%22version%22%3A%221.0%22%2C%22charset%22%3A%22UTF-8%22%2C%22sign%22%3A%220Rk6IXyj1g7Ja6nuUPPwWLkZlTad32zJ8mtbSGBkmkaRlVBrUkOZbihYKiwIlI%2BE5Hjd3JIFNM5%2B%2BGwFdztakfJH518BzIEnTkQUyaTcwjXvNwo88Ajr4Ex3aElqFr8ypawGhbSIp6f8ff809O%2FEe1NMatUtMBecdPbTTkgtukU%3D%22%7D";

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setContent(requestBody.getBytes("utf-8"));

        Response cmbPayNotify = paymentNotifyResource.cmbPayNotify(servletRequest);
        assertEquals(200, cmbPayNotify.getStatus());
    }

    @Test
    public void testPaymentNotifyFromCMBTestWithWrongPubKey() throws UnsupportedEncodingException {
        String requestBody =
                "jsonRequestData=%7B%22noticeData%22%3A%7B%22branchNo%22%3A%220021%22%2C%22AccountType%22%3A%22DebitCard%22%2C%22merchantPara%22%3A%22Pay%22%2C%22httpMethod%22%3A%22POST%22%2C%22merchantNo%22%3A%22000157%22%2C%22bankDate%22%3A%2220161125%22%2C%22discountFlag%22%3A%22N%22%2C%22noticeSerialNo%22%3A%220021000157201611251603213576%22%2C%22date%22%3A%2220161125%22%2C%22orderNo%22%3A%221603213576%22%2C%22bankSerialNo%22%3A%2216312531000000000020%22%2C%22noticeType%22%3A%22BKPAYRTN%22%2C%22noticeUrl%22%3A%22http%3A%2F%2F116.228.41.4%3A8081%2FcmbPayNotify%22%2C%22amount%22%3A%2297.00%22%2C%22discountAmount%22%3A%220.00%22%2C%22dateTime%22%3A%2220161125183627%22%7D%2C%22signType%22%3A%22RSA%22%2C%22version%22%3A%221.0%22%2C%22charset%22%3A%22UTF-8%22%2C%22sign%22%3A%220Rk6IXyj1g7Ja6nuUPPwWLkZlTad32zJ8mtbSGBkmkaRlVBrUkOZbihYKiwIlI%2BE5Hjd3JIFNM5%2B%2BGwFdztakfJH518BzIEnTkQUyaTcwjXvNwo88Ajr4Ex3aElqFr8ypawGhbSIp6f8ff809O%2FEe1NMatUtMBecdPbTTkgtukU%3D%22%7D";

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setContent(requestBody.getBytes("utf-8"));
        servletRequest.addHeader("mock", "1"); // 此处使用MOCK会导致使用码头内部的公钥而不是招行测试环境的公钥验签，而报文来自招行测试环境，因此会出现验签失败

        Response cmbPayNotify = paymentNotifyResource.cmbPayNotify(servletRequest);
        assertEquals(500, cmbPayNotify.getStatus());
    }

    @Test
    public void testSignNotifyFromCMBTest() throws UnsupportedEncodingException {
        String requestBody =
                "jsonRequestData=%7B%22noticeData%22%3A%7B%22branchNo%22%3A%220021%22%2C%22userID%22%3A%2220000312%22%2C%22merchantNo%22%3A%22000157%22%2C%22httpMethod%22%3A%22Post%22%2C%22noPwdPay%22%3A%22N%22%2C%22noticeSerialNo%22%3A%22844Y611250000202xCXNTTKRJHRFVYUJSVKEZTL%22%2C%22agrNo%22%3A%22208%22%2C%22userPidHash%22%3A%22129468525030254567040075172085%22%2C%22noticeType%22%3A%22BKQY%22%2C%22rspMsg%22%3A%22%E7%AD%BE%E7%BD%B2%E5%8D%8F%E8%AE%AE%E6%88%90%E5%8A%9F%22%2C%22noticeUrl%22%3A%22http%3A%2F%2F116.228.41.4%3A8081%2FcmbSignNotify%22%2C%22noticePara%22%3A%22Sign%22%2C%22userPidType%22%3A%221%22%2C%22dateTime%22%3A%2220161125173139%22%2C%22rspCode%22%3A%22SUC0000%22%7D%2C%22signType%22%3A%22RSA%22%2C%22version%22%3A%221.0%22%2C%22charset%22%3A%22UTF-8%22%2C%22sign%22%3A%22AGtHxxahOTsK4gJtuK1PwtHa6OcggXUSdVVb%2Fdd1m4fglGQ4HILBlW0qD%2BAsLQMMKh3DAb4UpGIKA8I0vu9fZLoTyxhQAtN%2Bf1qa1ipAojOxOEYzl0%2FPbphEGIVVW6rs%2BBCIWwjq64T%2B1LorEKFeXb72QfYevHYeF3QOCXr0dz8%3D%22%7D";

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setContent(requestBody.getBytes("utf-8"));

        Response cmbSignNotify = signNotifyResource.cmbSignNotify(servletRequest);
        assertEquals(200, cmbSignNotify.getStatus());
    }

    @Test
    public void testSignNotifyFromCMBTestWithWrongPubKey() throws UnsupportedEncodingException {
        String requestBody =
                "jsonRequestData=%7B%22noticeData%22%3A%7B%22branchNo%22%3A%220021%22%2C%22userID%22%3A%2220000312%22%2C%22merchantNo%22%3A%22000157%22%2C%22httpMethod%22%3A%22Post%22%2C%22noPwdPay%22%3A%22N%22%2C%22noticeSerialNo%22%3A%22844Y611250000202xCXNTTKRJHRFVYUJSVKEZTL%22%2C%22agrNo%22%3A%22208%22%2C%22userPidHash%22%3A%22129468525030254567040075172085%22%2C%22noticeType%22%3A%22BKQY%22%2C%22rspMsg%22%3A%22%E7%AD%BE%E7%BD%B2%E5%8D%8F%E8%AE%AE%E6%88%90%E5%8A%9F%22%2C%22noticeUrl%22%3A%22http%3A%2F%2F116.228.41.4%3A8081%2FcmbSignNotify%22%2C%22noticePara%22%3A%22Sign%22%2C%22userPidType%22%3A%221%22%2C%22dateTime%22%3A%2220161125173139%22%2C%22rspCode%22%3A%22SUC0000%22%7D%2C%22signType%22%3A%22RSA%22%2C%22version%22%3A%221.0%22%2C%22charset%22%3A%22UTF-8%22%2C%22sign%22%3A%22AGtHxxahOTsK4gJtuK1PwtHa6OcggXUSdVVb%2Fdd1m4fglGQ4HILBlW0qD%2BAsLQMMKh3DAb4UpGIKA8I0vu9fZLoTyxhQAtN%2Bf1qa1ipAojOxOEYzl0%2FPbphEGIVVW6rs%2BBCIWwjq64T%2B1LorEKFeXb72QfYevHYeF3QOCXr0dz8%3D%22%7D";

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setContent(requestBody.getBytes("utf-8"));
        servletRequest.addHeader("mock", "1"); // 此处使用MOCK会导致使用码头内部的公钥而不是招行测试环境的公钥验签，而报文来自招行测试环境，因此会出现验签失败

        Response cmbSignNotify = signNotifyResource.cmbSignNotify(servletRequest);
        assertEquals(500, cmbSignNotify.getStatus());
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
        req.setPayType("50");
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
