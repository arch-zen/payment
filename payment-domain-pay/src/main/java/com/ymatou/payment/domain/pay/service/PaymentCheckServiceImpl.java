/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.pay.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.CheckStatus;
import com.ymatou.payment.domain.pay.model.PayStatus;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.model.ThirdPartyPayment;
import com.ymatou.payment.domain.pay.repository.AlipayNotifyLogRespository;
import com.ymatou.payment.domain.pay.repository.BussinessOrderRepository;
import com.ymatou.payment.domain.pay.repository.PaymentRepository;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.integration.service.ymatou.NotifyPaymentService;

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

    @Autowired
    private PayService payService;

    @Autowired
    private NotifyPaymentService notifyPaymentService;

    @Override
    public void doCheck(ThirdPartyPayment thirdPartyPayment, String paymentId, boolean finalCheck,
            HashMap<String, String> header) {
        // 记录第三方应答
        alipayNotifyLogRespository.saveAlipaynoitfylog(paymentId, thirdPartyPayment.getOriginMessage());

        // 根据paymentId查询Payment,BussinessOrder
        Payment payment = paymentRepository.getByPaymentId(paymentId);
        if (payment == null) {
            throw new BizException(ErrorCode.DATA_NOT_FOUND, "can not find paymentid " + paymentId);
        }
        BussinessOrder bussinessOrder = bussinessOrderRepository.getBussinessOrderById(payment.getBussinessorderid());
        if (bussinessOrder == null) {
            throw new BizException(ErrorCode.DATA_NOT_FOUND,
                    "can not find order " + payment.getBussinessorderid());
        }

        if (PayStatus.Paied.getIndex() == thirdPartyPayment.getPayStatus()) {
            if (PayStatus.Paied.getIndex() == payment.getPaystatus()
                    || PayStatus.Refunded.getIndex() == payment.getPaystatus()) {
                paymentRepository.updatePaymentCheckStatus(CheckStatus.SUCCESS.getCode(), paymentId); // 对账成功
            } else {
                try {
                    payment.setCheckstatus(CheckStatus.REPAIR_SUCCESS.getCode()); // 补单成功
                    payment.setInstitutionpaymentid(thirdPartyPayment.getInstitutionPaymentId());
                    payment.setPaystatus(thirdPartyPayment.getPayStatus());
                    payment.setActualpayprice(thirdPartyPayment.getActualPayPrice());
                    payment.setActualpaycurrencytype(thirdPartyPayment.getActualPayCurrency());
                    payment.setBankid(thirdPartyPayment.getBankId());
                    payment.setCardtype(thirdPartyPayment.getCardType());
                    payment.setPaytime(thirdPartyPayment.getPayTime());
                    payment.setPayerid(thirdPartyPayment.getPayerId());
                    payment.setExchangerate(payment.getExchangerate() == null ? 1 : payment.getExchangerate());

                    payService.setPaymentOrderPaid(payment, thirdPartyPayment.getTraceId());

                    // 通知发货服务
                    try {
                        notifyPaymentService.doService(payment.getPaymentid(), thirdPartyPayment.getTraceId(),
                                header);
                    } catch (Exception e) {
                        logger.error("notify deliver service failed with paymentid :" + payment.getPaymentid(), e);
                    }

                } catch (Exception e) {
                    paymentRepository.updatePaymentCheckStatus(CheckStatus.AMOUNT_NOT_MATCH.getCode(), paymentId); // 金额不一致
                    throw e;
                }
            }
        } else {
            if (PayStatus.Paied.equals(payment.getPaystatus())) {
                paymentRepository.updatePaymentCheckStatus(CheckStatus.THIRD_PART_NOT_PAID.getCode(), paymentId); // 第三方未付，YMT已付
                logger.error("{} pay check failed, but paystatus is successful!", paymentId);
            } else {
                int checkStatus = checkFailed(finalCheck, payment.getCheckstatus());
                paymentRepository.updatePaymentCheckStatus(checkStatus, paymentId);
            }
        }
    }

    private int checkFailed(boolean isFinalCheck, Integer originalCheckStatus) {
        int checkStaus = 0;
        if (isFinalCheck) {
            checkStaus = -10; // 负数为对账次数
            return checkStaus;
        }

        if (originalCheckStatus == null) {
            checkStaus = -1; // 对账次数
        } else if (originalCheckStatus > -10) { // --最多对10次，避免和-20产生冲突
            checkStaus = originalCheckStatus - 1; // 对账次数+1
        }
        return checkStaus;
    }

}
