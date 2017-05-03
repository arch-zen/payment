/**
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.facade.impl.rest;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.service.PayService;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.constants.PaymentNotifyStatusEnum;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireOrderResp;
import com.ymatou.payment.facade.model.CheckCmbPaymentReq;
import com.ymatou.payment.facade.model.CheckPaymentRequset;
import com.ymatou.payment.facade.rest.CheckPaymentResource;
import com.ymatou.payment.facade.rest.PaymentResource;
import com.ymatou.payment.infrastructure.db.mapper.PaymentCheckMapper;
import com.ymatou.payment.infrastructure.db.model.PaymentCheckExample;
import com.ymatou.payment.infrastructure.db.model.PaymentCheckPo;
import com.ymatou.payment.test.RestBaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 支付对账接口-测试用例
 * 
 * @author wangxudong 2017年1月17日 下午2:00:34
 *
 */
public class CheckPaymentResourceTest extends RestBaseTest {

    @Resource
    private CheckPaymentResource checkPaymentResource;

    @Autowired
    private PaymentCheckMapper paymentCheckMapper;
    @Resource
    private PaymentResource paymentResource;
    @Resource
    private PayService payService;

    @Test
    public void testCheckCmbPayment() {
        CheckCmbPaymentReq req = new CheckCmbPaymentReq();
        req.setDate("2016-12-01");
        req.setNo("4");

        // 清理数据
        String checkTitle = String.format("招行一网通自动对账_%s_%s", req.getDate(), req.getNo());
        PaymentCheckPo paymentCheckByUploadFile = getPaymentCheckByUploadFile(checkTitle);
        if (paymentCheckByUploadFile != null) {
            paymentCheckMapper.deleteByPrimaryKey(paymentCheckByUploadFile.getPaymentCheckId());
        }

        // 确认数据已经清理
        paymentCheckByUploadFile = getPaymentCheckByUploadFile(checkTitle);
        assertNull(paymentCheckByUploadFile);

        // 调用对账服务
        String response = checkPaymentResource.checkCmbPayment(req);
        assertEquals("ok", response);

        // 确认生成了对账的数据
        paymentCheckByUploadFile = getPaymentCheckByUploadFile(checkTitle);
        assertNotNull(paymentCheckByUploadFile);
    }


    @Test
    public void testCheckApplePay(){
        //创建一个消费单
        AcquireOrderReq acquireOrderReq = new AcquireOrderReq();

        PaymentResourceImplTest paymentResourceImplTest = new PaymentResourceImplTest();
        paymentResourceImplTest.buildBaseRequest(acquireOrderReq);

        acquireOrderReq.setPayType(PayTypeEnum.ApplePay.getCode());
        acquireOrderReq.setPayPrice("1.01");
        acquireOrderReq.setNotifyUrl("http://mockforpay.iapi.ymatou.com/api/Trading/NotifyTradingEvent");

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();

        AcquireOrderResp acquireOrderResp = this.paymentResource.acquireOrder(acquireOrderReq, servletRequest);
        Assert.assertNotNull(acquireOrderResp.getResult());
        System.out.println("result:" + acquireOrderResp.getResult());
        Assert.assertEquals(true, acquireOrderResp.getIsSuccess());

        BussinessOrder bo = payService.getBussinessOrderByOrderId(acquireOrderReq.orderId);
        assertNotNull("验证商户订单", bo);
        System.out.println(bo.getOrderId());
        System.out.println(bo.getBussinessOrderId());

        Payment payment = payService.getPaymentByBussinessOrderId(bo.getBussinessOrderId());
        Assert.assertNotNull(payment);

        //查询消费单
        CheckPaymentRequset checkPaymentRequset = new CheckPaymentRequset();
        checkPaymentRequset.setPaymentId(payment.getPaymentId());
        checkPaymentRequset.setFinalCheck(false);
        checkPaymentRequset.setPayType(PayTypeEnum.ApplePay.getCode());

        servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader("mock", "1");
        servletRequest.addHeader("MockResult-ApplePay-origRespCode", "00");
        servletRequest.addHeader("MockResult-ApplePay-settleAmt", "101");
        servletRequest.addHeader("MockResult-ApplePay-orderId", payment.getPaymentId());

        String result = this.checkPaymentResource.checkPayment(checkPaymentRequset, servletRequest);
        Assert.assertEquals("ok", result);

        bo = payService.getBussinessOrderByOrderId(acquireOrderReq.orderId);
        Assert.assertEquals(PayStatusEnum.Paied.getIndex(), bo.getOrderStatus().intValue());

        payment = payService.getPaymentByBussinessOrderId(bo.getBussinessOrderId());
        Assert.assertEquals(PayStatusEnum.Paied.getIndex(), payment.getPayStatus().getIndex());
        Assert.assertEquals(PaymentNotifyStatusEnum.NOTIFIED, payment.getNotifyStatus());
    }




    private PaymentCheckPo getPaymentCheckByUploadFile(String checkTitle) {
        PaymentCheckExample paymentCheckExample = new PaymentCheckExample();
        paymentCheckExample.createCriteria().andUploadFileEqualTo(checkTitle);

        List<PaymentCheckPo> selectByExample = paymentCheckMapper.selectByExample(paymentCheckExample);
        if (selectByExample == null || selectByExample.size() == 0) {
            return null;
        } else {
            return selectByExample.get(0);
        }

    }
}















































