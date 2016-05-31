/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.paymentquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.service.PaymentQueryService;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.constants.PayTypeEnum;

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

    //FIXME: paymentId无用
    public PaymentQueryService getInstanceByPayType(String payType, String paymentId) {

        switch (PayTypeEnum.parse(payType)) {
            case AliPayPc:
                return aliPayPaymentQueryServiceImpl;
            case AliPayApp:
                return aliPayPaymentQueryServiceImpl;
            case WeiXinJSAPI:
                return weiXinPaymentQueryServiceImpl;
            case WeiXinApp:
                return weiXinPaymentQueryServiceImpl;
            default:
                throw new BizException(ErrorCode.INVALID_PAYTYPE, payType);

        }
    }
}
