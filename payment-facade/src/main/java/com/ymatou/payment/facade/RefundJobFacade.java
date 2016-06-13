/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade;

import java.util.HashMap;

/**
 * 
 * @author qianmin 2016年6月8日 上午10:43:51
 *
 */
public interface RefundJobFacade {

    /**
     * 定时任务，根据RefundNo来处理退款申请单
     * 
     * @param refundNo
     */
    public void excuteRefund(String refundNo, HashMap<String, String> header);
}
