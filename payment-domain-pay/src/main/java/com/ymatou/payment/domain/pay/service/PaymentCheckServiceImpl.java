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
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.model.ThirdPartyPayment;
import com.ymatou.payment.domain.pay.repository.AlipayNotifyLogRespository;
import com.ymatou.payment.domain.pay.repository.BussinessOrderRepository;
import com.ymatou.payment.domain.pay.repository.PaymentRepository;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.constants.CheckStatusEnum;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.infrastructure.Money;
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

        // 如果第三方返回系统未知的状态
        if (thirdPartyPayment.getPayStatus() == PayStatusEnum.UNKNOW.getIndex()) {
            throw new BizException(ErrorCode.FAIL,
                    "check payment failed because query statues unknow with paymentid: " + paymentId);
        }

        // 根据paymentId查询Payment,BussinessOrder
        Payment payment = paymentRepository.getByPaymentId(paymentId);
        if (payment == null) {
            throw new BizException(ErrorCode.NOT_EXIST_PAYMENT_ID, "can not find paymentid " + paymentId);
        }
        BussinessOrder bussinessOrder = bussinessOrderRepository.getBussinessOrderById(payment.getBussinessOrderId());
        if (bussinessOrder == null) {
            throw new BizException(ErrorCode.NOT_EXIST_BUSINESS_ORDER_ID,
                    "can not find order " + payment.getBussinessOrderId());
        }

        // 对账逻辑
        if (PayStatusEnum.Paied.getIndex() == thirdPartyPayment.getPayStatus()) {
            if (PayStatusEnum.Paied == payment.getPayStatus()
                    || PayStatusEnum.Refunded == payment.getPayStatus()) {
                paymentRepository.updatePaymentCheckStatus(CheckStatusEnum.SUCCESS.getCode(), paymentId); // 对账成功
            } else {
                try {
                    payment.setCheckStatus(CheckStatusEnum.REPAIR_SUCCESS.getCode()); // 补单成功
                    payment.setInstitutionPaymentId(thirdPartyPayment.getInstitutionPaymentId());
                    payment.setPayStatus(PayStatusEnum.parse(thirdPartyPayment.getPayStatus()));
                    payment.setActualPayPrice(new Money(thirdPartyPayment.getActualPayPrice()));
                    payment.setActualPayCurrencyType(thirdPartyPayment.getActualPayCurrency());
                    payment.setBankId(thirdPartyPayment.getBankId());
                    payment.setCardType(thirdPartyPayment.getCardType());
                    payment.setPayTime(thirdPartyPayment.getPayTime());
                    payment.setPayerId(thirdPartyPayment.getPayerId());
                    payment.setExchangeRate(payment.getExchangeRate() == null ? 1 : payment.getExchangeRate());

                    payService.setPaymentOrderPaid(payment, thirdPartyPayment.getTraceId());

                    // 通知发货服务
                    try {
                        notifyPaymentService.doService(payment.getPaymentId(), thirdPartyPayment.getTraceId(),
                                header);
                    } catch (Exception e) {
                        logger.error("notify deliver service failed with paymentid :" + payment.getPaymentId(), e);
                    }

                } catch (Exception e) {
                    paymentRepository.updatePaymentCheckStatus(CheckStatusEnum.AMOUNT_NOT_MATCH.getCode(), paymentId); // 金额不一致
                    throw e;
                }
            }
        } else {

            if (payment.getPayStatus() == PayStatusEnum.Paied
                    || payment.getPayStatus() == PayStatusEnum.Refunded) {
                paymentRepository.updatePaymentCheckStatus(CheckStatusEnum.THIRD_PART_NOT_PAID.getCode(), paymentId); // 第三方未付，YMT已付

                logger.error("{} pay check failed, but paystatus is successful!", paymentId);
            } else {
                int checkStatus = checkFailed(finalCheck, payment.getCheckStatus());
                paymentRepository.updatePaymentCheckStatus(checkStatus, paymentId);
            }
        }
    }

    private int checkFailed(boolean isFinalCheck, Integer originalCheckStatus) {
        int checkStaus = 0;
        if (isFinalCheck) {
            checkStaus = CheckStatusEnum.FINAL_CHECK_FAIL_INDEX.getCode(); // 直接设为最大的对账失败次数
            return checkStaus;
        }

        if (originalCheckStatus == null) {
            checkStaus = CheckStatusEnum.INIT_CHECK_FAIL_INDEX.getCode(); // 第一次对账失败
        } else if (originalCheckStatus > CheckStatusEnum.FINAL_CHECK_FAIL_INDEX.getCode()) { // 需要大于最大的对账失败次数
            checkStaus = originalCheckStatus - 1; // 对账次数+1
        }
        return checkStaus;
    }

}
