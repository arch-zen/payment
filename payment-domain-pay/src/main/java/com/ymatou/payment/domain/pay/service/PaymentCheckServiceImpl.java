/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.pay.service;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.PayStatus;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.model.ThirdPartyPayment;
import com.ymatou.payment.domain.pay.repository.AlipayNotifyLogRespository;
import com.ymatou.payment.domain.pay.repository.BussinessOrderRepository;
import com.ymatou.payment.domain.pay.repository.PaymentRepository;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;

/**
 * 
 * @author qianmin 2016年5月20日 下午2:48:39
 *
 */
@Component
public class PaymentCheckServiceImpl implements PaymentCheckService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentCheckServiceImpl.class);

    @Autowired
    private AlipayNotifyLogRespository alipayNotifyLogRespository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BussinessOrderRepository bussinessOrderRepository;

    @Override
    public void doCheck(ThirdPartyPayment thirdPartyPayment, String paymentId, boolean finalCheck) {
        alipayNotifyLogRespository.saveAlipaynoitfylog(paymentId, thirdPartyPayment.getOriginMessage());

        Payment payment = paymentRepository.getByPaymentId(paymentId);
        if (payment == null) {
            throw new BizException(ErrorCode.DATA_NOT_FOUND, String.format("can not find paymentid {0}", paymentId));
        }
        BussinessOrder bussinessOrder = bussinessOrderRepository.getBussinessOrderById(payment.getBussinessorderid());
        if (bussinessOrder == null) {
            throw new BizException(ErrorCode.DATA_NOT_FOUND,
                    String.format("can not find order {0}", payment.getBussinessorderid()));
        }

        if (PayStatus.Paied.equals(thirdPartyPayment.getPayStatus())) {
            if (PayStatus.Paied.getIndex() == payment.getPaystatus().intValue()
                    || PayStatus.Refunded.getIndex() == payment.getPaystatus().intValue()) {
                payment.setCheckstatus(1); // TODO

                // TODO
            } else {
                try {
                    payment.setCheckstatus(2);

                } catch (Exception e) {

                }
            }
        }
    }


    private void IsPaid(String institutionPaymentId, BigDecimal actualPayAmount, String payCurrency, String bankId,
            Integer cardType, Date payTime, String payerId, double exchangeRate) {

    }

}
