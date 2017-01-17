/**
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.facade.impl.rest;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.payment.facade.model.CheckCmbPaymentReq;
import com.ymatou.payment.facade.rest.CheckPaymentResource;
import com.ymatou.payment.infrastructure.db.mapper.PaymentCheckMapper;
import com.ymatou.payment.infrastructure.db.model.PaymentCheckExample;
import com.ymatou.payment.infrastructure.db.model.PaymentCheckPo;
import com.ymatou.payment.test.RestBaseTest;

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
