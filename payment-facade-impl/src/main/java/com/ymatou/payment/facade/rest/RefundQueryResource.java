/**
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.rest;

import com.ymatou.payment.facade.model.QueryRefundByRefundNoReq;
import com.ymatou.payment.facade.model.QueryRefundByRefundNoResp;

/**
 * 退款查询接口-REST
 * 
 * @author wangxudong 2017年1月13日 下午2:07:47
 *
 */
public interface RefundQueryResource {
    /**
     * 根据退款申请号查询退款信息
     * 
     * @param req
     * @return
     */
    QueryRefundByRefundNoResp queryRefundByRefundNo(QueryRefundByRefundNoReq req);
}
