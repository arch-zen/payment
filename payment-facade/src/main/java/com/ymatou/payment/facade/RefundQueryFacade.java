/**
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade;

import com.ymatou.payment.facade.model.QueryRefundByBizNoReq;
import com.ymatou.payment.facade.model.QueryRefundByBizNoResp;
import com.ymatou.payment.facade.model.QueryRefundByRefundNoReq;
import com.ymatou.payment.facade.model.QueryRefundByRefundNoResp;

/**
 * 退款查询接口-API
 * 
 * @author wangxudong 2017年1月13日 上午11:21:07
 *
 */
public interface RefundQueryFacade {

    /**
     * 根据退款申请号查询退款信息
     * 
     * @param req
     * @return
     */
    QueryRefundByRefundNoResp queryRefundByRefundNo(QueryRefundByRefundNoReq req);



    /**
     * 根据退款单号查询退款信息
     * 
     * @param req
     * @return
     */
    QueryRefundByBizNoResp queryRefundByBizNo(QueryRefundByBizNoReq req);
}
