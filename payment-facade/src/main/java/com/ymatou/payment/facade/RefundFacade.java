/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade;

import com.ymatou.payment.facade.model.AcquireRefundPlusRequest;
import com.ymatou.payment.facade.model.AcquireRefundPlusResponse;
import com.ymatou.payment.facade.model.AcquireRefundRequest;
import com.ymatou.payment.facade.model.AcquireRefundResponse;
import com.ymatou.payment.facade.model.ApproveRefundRequest;
import com.ymatou.payment.facade.model.ApproveRefundResponse;
import com.ymatou.payment.facade.model.FastRefundRequest;
import com.ymatou.payment.facade.model.FastRefundResponse;
import com.ymatou.payment.facade.model.QueryRefundRequest;
import com.ymatou.payment.facade.model.QueryRefundResponse;
import com.ymatou.payment.facade.model.SysApproveRefundReq;
import com.ymatou.payment.facade.model.SysApproveRefundResp;
import com.ymatou.payment.facade.model.TradeRefundableRequest;
import com.ymatou.payment.facade.model.TradeRefundableResponse;

/**
 * 退款接口
 * 
 * @author qianmin 2016年5月11日 上午10:51:16
 * 
 */
public interface RefundFacade {

    /**
     * 快速退款
     * 
     * @param req
     * @return
     */
    FastRefundResponse fastRefund(FastRefundRequest req);

    /**
     * 退款收单
     * 
     * @param req
     * @return
     */
    AcquireRefundResponse submitRefund(AcquireRefundRequest req);

    /**
     * 退款审核
     * 
     * @param req
     * @return
     */
    ApproveRefundResponse approveRefund(ApproveRefundRequest req);

    /**
     * 检查是否可以退款
     * 
     * @param req
     * @return
     */
    TradeRefundableResponse checkRefundable(TradeRefundableRequest req);

    /**
     * 查询退款单
     * 
     * @param req
     * @return
     */
    QueryRefundResponse query(QueryRefundRequest req);

    /**
     * 退款收单(支持部分退款)
     * 
     * @param req
     * @param header
     * @return
     */
    AcquireRefundPlusResponse acquireRefund(AcquireRefundPlusRequest req);

    /**
     * 系统自动审核退款（供调度使用）
     * 
     * @param req
     * @return
     */
    SysApproveRefundResp sysApproveRefund(SysApproveRefundReq req);
}
