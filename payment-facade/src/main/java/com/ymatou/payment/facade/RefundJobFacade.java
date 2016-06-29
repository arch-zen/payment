/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade;

import com.ymatou.payment.facade.model.ExecuteRefundRequest;
import com.ymatou.payment.facade.model.ExecuteRefundResponse;

/**
 * 
 * @author qianmin 2016年6月8日 上午10:43:51
 *
 */
public interface RefundJobFacade {

    /**
     * 定时任务，根据RefundNo来处理退款申请单
     * 
     * @param request
     */
    public ExecuteRefundResponse executeRefund(ExecuteRefundRequest request);
}
