/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import com.ymatou.payment.facade.model.AliPayRefundNotifyRequest;

/**
 * 
 * @author qianmin 2016年5月17日 下午2:54:15
 * 
 */
public interface RefundNotifyService {

    /**
     * 处理支付宝的退款回调
     * 
     * @param req
     * @return
     */
    public void processRefundCallback(AliPayRefundNotifyRequest req);
}
