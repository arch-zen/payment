/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import java.util.List;

import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;

/**
 * 退款审核
 * 
 * @author qianmin 2016年5月13日 上午11:59:22
 * 
 */
public interface ApproveRefundService {

    /**
     * 更新RefundRequest审核状态， 保存CompensateProcessInfo
     * 
     * @param paymentIds
     */
    public List<RefundRequestPo> approveRefund(List<Integer> paymentIds, String approveUser);
}
