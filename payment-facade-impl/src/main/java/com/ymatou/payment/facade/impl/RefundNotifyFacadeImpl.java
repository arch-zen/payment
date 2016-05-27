/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.refund.service.RefundNotifyService;
import com.ymatou.payment.facade.BaseResponse;
import com.ymatou.payment.facade.RefundNotifyFacade;
import com.ymatou.payment.facade.model.AliPayRefundNotifyRequest;

/**
 * 
 * @author qianmin 2016年5月23日 下午5:19:53
 *
 */
@Component
public class RefundNotifyFacadeImpl implements RefundNotifyFacade {

    @Autowired
    private RefundNotifyService refundNotifyService;

    @Override
    public BaseResponse refundNotify(AliPayRefundNotifyRequest req) {

        refundNotifyService.processRefundCallback(req);

        return new BaseResponse();
    }

}
