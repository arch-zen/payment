/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(RefundNotifyFacadeImpl.class);

    @Autowired
    private RefundNotifyService refundNotifyService;

    @Override
    public BaseResponse refundNotify(AliPayRefundNotifyRequest req) {
        // if (req.getNotify_time() == null || StringUtils.isBlank(req.getNotify_type())
        // || StringUtils.isBlank(req.getNotify_id()) || StringUtils.isBlank(req.getSign_type())
        // || StringUtils.isBlank(req.getSign()) || StringUtils.isBlank(req.getBatch_no())
        // || StringUtils.isBlank(req.getSuccess_num())) {
        // logger.info("request data invalid.");
        // throw new BizException(ErrorCode.ILLEGAL_ARGUMENT, "request data invalid.");
        // }

        refundNotifyService.processRefundCallback(req);

        return new BaseResponse();
    }

}
