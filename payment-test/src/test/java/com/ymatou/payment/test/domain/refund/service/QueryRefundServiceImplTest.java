/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.domain.refund.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.payment.domain.refund.service.QueryRefundServiceImpl;
import com.ymatou.payment.facade.model.QueryRefundRequest;
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
}
