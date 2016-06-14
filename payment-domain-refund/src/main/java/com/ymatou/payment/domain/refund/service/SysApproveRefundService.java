/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import java.util.Date;

public interface SysApproveRefundService {

    /**
     * 批量审批调度前一个小时的退款申请
     * 
     * @param scheduleTime
     * @return
     */
    public int batchApproveRefundReq(Date scheduleTime);
}
