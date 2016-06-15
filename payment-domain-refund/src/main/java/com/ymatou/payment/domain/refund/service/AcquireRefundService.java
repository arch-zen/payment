/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import com.ymatou.payment.facade.model.AcquireRefundPlusRequest;

/**
 * 提交退款(支持部分退款)
 * 
 * @author qianmin 2016年6月3日 下午6:02:26
 *
 */
public interface AcquireRefundService {

    /**
     * 提交退款申请
     * 
     * @param req
     * @return
     */
    public boolean acquireRefund(AcquireRefundPlusRequest req);

}
