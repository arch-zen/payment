/**
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.refund.service.QueryRefundService;
import com.ymatou.payment.facade.RefundQueryFacade;
import com.ymatou.payment.facade.model.QueryRefundByBizNoReq;
import com.ymatou.payment.facade.model.QueryRefundByBizNoResp;
import com.ymatou.payment.facade.model.QueryRefundByRefundNoReq;
import com.ymatou.payment.facade.model.QueryRefundByRefundNoResp;
import com.ymatou.payment.facade.model.QueryRefundDetail;

@Component("refundQueryFacade")
public class RefundQueryFacadeImpl implements RefundQueryFacade {

    @Resource
    private QueryRefundService queryRefundService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ymatou.payment.facade.RefundQueryFacade#queryRefundByRefundNo(com.ymatou.payment.facade.
     * model.QueryRefundByRefundNoReq)
     */
    @Override
    public QueryRefundByRefundNoResp queryRefundByRefundNo(QueryRefundByRefundNoReq req) {
        QueryRefundByRefundNoResp resp = new QueryRefundByRefundNoResp();
        List<QueryRefundDetail> result = queryRefundService.queryRefundByRefundNo(req);

        resp.setResult(result);
        resp.setSuccess(true);

        return resp;
    }

    @Override
    public QueryRefundByBizNoResp queryRefundByBizNo(QueryRefundByBizNoReq req) {
        QueryRefundByBizNoResp resp = new QueryRefundByBizNoResp();
        List<QueryRefundDetail> result = queryRefundService.queryRefundByBizNo(req);

        resp.setResult(result);
        resp.setSuccess(true);

        return resp;
    }

}
