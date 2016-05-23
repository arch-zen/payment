/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.model.PaymentQueryResp;
import com.ymatou.payment.domain.channel.service.PaymentQueryService;
import com.ymatou.payment.domain.channel.service.paymentquery.PaymentQueryServiceFactory;
import com.ymatou.payment.domain.pay.model.ThirdPartyPayment;
import com.ymatou.payment.domain.pay.service.PaymentCheckService;
import com.ymatou.payment.facade.BaseResponse;
import com.ymatou.payment.facade.CheckPaymentFacade;
import com.ymatou.payment.facade.model.CheckPaymentRequset;

/**
 * 
 * @author qianmin 2016年5月19日 下午3:42:32
 *
 */
@Component
public class CheckPaymentFacadeImpl implements CheckPaymentFacade {

    @Autowired
    private PaymentQueryServiceFactory paymentQueryServiceFactory;

    @Autowired
    private PaymentCheckService paymentCheckService;

    @Override
    public BaseResponse checkPayment(CheckPaymentRequset req) {

        PaymentQueryService paymentQueryService =
                paymentQueryServiceFactory.getInstanceByPayType(req.getPayType(), req.getPaymentId());

        PaymentQueryResp paymentQueryResp = paymentQueryService.paymentQuery(req);
        paymentCheckService.doCheck(generateModel(paymentQueryResp), req.getPaymentId(), req.isFinalCheck(),
                req.getHeader());

        return new BaseResponse();
    }


    private ThirdPartyPayment generateModel(PaymentQueryResp paymentQueryResp) {
        ThirdPartyPayment payment = new ThirdPartyPayment();

        payment.setActualPayCurrency(paymentQueryResp.getActualPayCurrency());
        payment.setActualPayPrice(paymentQueryResp.getActualPayPrice());
        payment.setBankId(paymentQueryResp.getBankId());
        payment.setCardType(paymentQueryResp.getCardType());
        payment.setInstitutionPaymentId(paymentQueryResp.getInstitutionPaymentId());
        payment.setOriginMessage(paymentQueryResp.getOriginMessage());
        payment.setPayerId(paymentQueryResp.getPayerId());
        payment.setPaymentId(paymentQueryResp.getPaymentId());
        payment.setPayStatus(paymentQueryResp.getPayStatus().getIndex());
        payment.setPayTime(paymentQueryResp.getPayTime());
        payment.setTraceId(paymentQueryResp.getTraceId());

        return payment;
    }

}
