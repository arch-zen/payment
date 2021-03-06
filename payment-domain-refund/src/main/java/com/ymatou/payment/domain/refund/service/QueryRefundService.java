/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import java.util.List;

import com.ymatou.payment.facade.model.QueryRefundByBizNoReq;
import com.ymatou.payment.facade.model.QueryRefundByRefundNoReq;
import com.ymatou.payment.facade.model.QueryRefundDetail;
import com.ymatou.payment.facade.model.QueryRefundRequest;

/**
 * 退款查询
 * 
 * @author qianmin 2016年5月13日 下午3:38:05
 * 
 */
public interface QueryRefundService {

    /**
     * 查询退款单
     * 
     * @param req
     * @return
     */
    public List<QueryRefundDetail> queryRefundRequest(QueryRefundRequest req);

    /**
     * 根据RefundNo查询退款列表
     * 
     * @param req
     * @return
     */
    public List<QueryRefundDetail> queryRefundByRefundNo(QueryRefundByRefundNoReq req);

    /**
     * 根据BizNo查询退款列表
     * 
     * @param req
     * @return
     */
    public List<QueryRefundDetail> queryRefundByBizNo(QueryRefundByBizNoReq req);
}
