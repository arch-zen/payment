/**
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.rest;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

import com.ymatou.payment.facade.RefundQueryFacade;
import com.ymatou.payment.facade.model.QueryRefundByBizNoReq;
import com.ymatou.payment.facade.model.QueryRefundByBizNoResp;
import com.ymatou.payment.facade.model.QueryRefundByRefundNoReq;
import com.ymatou.payment.facade.model.QueryRefundByRefundNoResp;

/**
 * 退款查询接口-REST实现
 * 
 * @author wangxudong 2017年1月13日 下午2:09:01
 *
 */
@Path("/refund")
@Produces({"application/json; charset=UTF-8"})
@Component("refundQueryResource")
public class RefundQueryResourceImpl implements RefundQueryResource {

    @Resource
    private RefundQueryFacade refundQueryFacade;

    @POST
    @Path("/queryRefundByRefundNo")
    @Override
    public QueryRefundByRefundNoResp queryRefundByRefundNo(QueryRefundByRefundNoReq req) {
        return refundQueryFacade.queryRefundByRefundNo(req);
    }

    @POST
    @Path("/queryRefundByBizNo")
    @Override
    public QueryRefundByBizNoResp queryRefundByBizNo(QueryRefundByBizNoReq req) {
        return refundQueryFacade.queryRefundByBizNo(req);
    }

}
