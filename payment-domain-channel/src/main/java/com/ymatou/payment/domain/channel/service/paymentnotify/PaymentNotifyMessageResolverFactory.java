/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.paymentnotify;

import com.ymatou.payment.domain.channel.service.PaymentNotifyService;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 支付回调解析器工厂
 * 
 * @author wangxudong 2016年5月19日 下午2:23:15
 *
 */
@Component
public class PaymentNotifyMessageResolverFactory {

    @Resource
    private AliPayPaymentNotifyServiceImpl aliPayPaymentNotifyServiceImpl;

    @Resource
    private WeiXinPaymentNotifyServiceImpl weiXinPaymentNotifyServiceImpl;

    @Resource
    private AliPayWapPaymentNotifyServiceImpl aliPayWapPaymentNotifyServiceImpl;

    @Resource
    private CmbPaymentNotifyServiceImpl cmbPaymentNotifyServiceImpl;

    @Resource
    private ApplePayPaymentNotifyServiceImpl applePayPaymentNotifyService;

    /**
     * 获取到支付回调解析器
     * 
     * @param payType
     * @return
     */
    public PaymentNotifyService getInstance(String payType) {
        switch (PayTypeEnum.parse(payType)) {
            case AliPayPc:
                return aliPayPaymentNotifyServiceImpl;
            case AliPayApp:
                return aliPayPaymentNotifyServiceImpl;
            case AliPayWap:
                return aliPayWapPaymentNotifyServiceImpl;
            case WeiXinJSAPI:
                return weiXinPaymentNotifyServiceImpl;
            case WeiXinApp:
                return weiXinPaymentNotifyServiceImpl;
            case WeiXinPc:
                return weiXinPaymentNotifyServiceImpl;
            case CmbApp:
                return cmbPaymentNotifyServiceImpl;
            case ApplePay:
                return applePayPaymentNotifyService;
            default:
                throw new BizException(ErrorCode.INVALID_PAY_TYPE, payType);
        }
    }
}
