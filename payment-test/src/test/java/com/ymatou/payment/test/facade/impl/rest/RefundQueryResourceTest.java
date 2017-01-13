/**
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.facade.impl.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.ymatou.payment.facade.model.QueryRefundByRefundNoReq;
import com.ymatou.payment.facade.model.QueryRefundByRefundNoResp;
import com.ymatou.payment.facade.rest.RefundQueryResource;
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
}
