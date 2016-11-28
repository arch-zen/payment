/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.refundquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.service.RefundQueryService;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.constants.PayTypeEnum;

/**
 * 
 * @author qianmin 2016年6月12日 上午11:36:41
 *
 */
@Component
public class RefundQueryServiceFactory {

    @Autowired
    private AliPayRefundQueryServiceImpl aliPayRefundQueryServiceImpl;

    @Autowired
    private WeiXinRefundQueryServiceImpl weiXinRefundQueryServiceImpl;

    @Autowired
    private CmbRefundQueryServiceImpl cmbRefundQueryServiceImpl;

    public RefundQueryService getInstanceByType(String payType) {
        return getInstanceByPayType(PayTypeEnum.parse(payType));
    }

    public RefundQueryService getInstanceByPayType(PayTypeEnum payType) {

        switch (payType) {
            case AliPayPc:
                return aliPayRefundQueryServiceImpl;
            case AliPayApp:
                return aliPayRefundQueryServiceImpl;
            case AliPayWap:
                return aliPayRefundQueryServiceImpl;
            case WeiXinJSAPI:
                return weiXinRefundQueryServiceImpl;
            case WeiXinApp:
                return weiXinRefundQueryServiceImpl;
            case WeiXinPc:
                return weiXinRefundQueryServiceImpl;
            case CmbApp:
                return cmbRefundQueryServiceImpl;
            default:
                throw new BizException(ErrorCode.INVALID_PAY_TYPE, payType.getCode());

        }
    }
}
