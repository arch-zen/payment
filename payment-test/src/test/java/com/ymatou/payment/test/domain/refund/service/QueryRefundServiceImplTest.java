/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.domain.refund.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.payment.domain.refund.service.QueryRefundServiceImpl;
import com.ymatou.payment.facade.model.QueryRefundByRefundNoReq;
import com.ymatou.payment.facade.model.QueryRefundDetail;
import com.ymatou.payment.facade.model.QueryRefundRequest;
import com.ymatou.payment.infrastructure.Money;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 
 * @author qianmin 2016年5月16日 上午11:06:09
 * 
 */
public class QueryRefundServiceImplTest extends RestBaseTest {

    @Autowired
    private QueryRefundServiceImpl queryRefundServiceImpl;

    @Test
    public void testqueryRefundRequestSuccess() {

        QueryRefundRequest req = new QueryRefundRequest();
        req.setApproveStatus(9);
        req.setPageIndex(3);
        queryRefundServiceImpl.queryRefundRequest(req);
    }

    @Test
    public void testQueryRefundByRefundNo() {
        QueryRefundByRefundNoReq req = new QueryRefundByRefundNoReq();
        List<String> refundNoList = new ArrayList<>();
        refundNoList.add("58772bd8ef2e884d59ef76ce");
        refundNoList.add("58772b23ef2e884d59ef750d");

        req.setRefundNoList(refundNoList);
        List<QueryRefundDetail> queryRefundByRefundNo = queryRefundServiceImpl.queryRefundByRefundNo(req);

        assertEquals(2, queryRefundByRefundNo.size());

        QueryRefundDetail detail1 = queryRefundByRefundNo.get(0);
        assertEquals("1703220127", detail1.getPaymentId());
        assertEquals("14158239", detail1.getTradeNo());
        assertEquals(new Money(63), new Money(detail1.getRefundAmount()));
        assertEquals(9, detail1.getApproveStatus());
        assertEquals(false, detail1.getIsIntercept());

        QueryRefundDetail detail2 = queryRefundByRefundNo.get(1);
        assertEquals("17011215100820136", detail2.getPaymentId());
        assertEquals(4, detail2.getRefundStatus());
    }
}
