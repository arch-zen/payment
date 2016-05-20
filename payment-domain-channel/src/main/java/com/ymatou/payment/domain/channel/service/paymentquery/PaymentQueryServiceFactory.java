/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.paymentquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.service.PaymentQueryService;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;

/**
 * 
 * @author qianmin 2016年5月20日 下午2:12:36
 *
 */
@Component
public class PaymentQueryServiceFactory {

    @Autowired
    private AliPayPaymentQueryServiceImpl aliPayPaymentQueryServiceImpl;

    @Autowired
    private WeiXinPaymentQueryServiceImpl weiXinPaymentQueryServiceImpl;

    public PaymentQueryService getInstanceByPayType(String payType, String paymentId) {
        if ("10".equals(payType) || "13".equals(payType)) {
            return aliPayPaymentQueryServiceImpl;
        } else if ("14".equals(payType) || "15".equals(payType)) {
            return weiXinPaymentQueryServiceImpl;
        } else {
            throw new BizException(ErrorCode.ILLEGAL_ARGUMENT, "payType error,paymentId:[" + paymentId + "]");
        }
    }
}
