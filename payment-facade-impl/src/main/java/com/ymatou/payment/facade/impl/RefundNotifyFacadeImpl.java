/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.impl;

import com.ymatou.payment.domain.refund.service.notify.RefundNotifyService;
import com.ymatou.payment.domain.refund.service.notify.RefundNotifyServiceFactory;
import com.ymatou.payment.facade.BaseResponse;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.RefundNotifyFacade;
import com.ymatou.payment.facade.model.RefundNotifyRequest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qianmin 2016年5月23日 下午5:19:53
 */
@Component
public class RefundNotifyFacadeImpl implements RefundNotifyFacade {

//    @Autowired
//    private RefundNotifyService refundNotifyService;

//    @Override
//    public BaseResponse refundNotify(AliPayRefundNotifyRequest req) {
//
//        refundNotifyService.processRefundCallback(req);
//
//        BaseResponse resp = new BaseResponse();
//        resp.setSuccess(true);
//        return resp;
//    }

    @Resource
    private RefundNotifyServiceFactory refundNotifyServiceFactory;


    @Override
    public BaseResponse refundNotify(RefundNotifyRequest request) {
        RefundNotifyService refundNotifyService = refundNotifyServiceFactory.createRefundNotifyService(request);
        if (refundNotifyService == null) {
            throw new BizException(ErrorCode.FAIL, String.format("未能找到PayType=%s的退款通知服务的实现！", request.getPayType()));
        }
        refundNotifyService.processRefundCallback(request);
        BaseResponse resp = new BaseResponse();
        resp.setSuccess(true);
        return resp;
    }
}
