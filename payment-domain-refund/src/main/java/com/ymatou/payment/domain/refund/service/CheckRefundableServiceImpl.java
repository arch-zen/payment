/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.repository.BussinessOrderRepository;
import com.ymatou.payment.domain.pay.repository.PaymentRepository;
import com.ymatou.payment.domain.refund.DomainConfig;
import com.ymatou.payment.facade.constants.OrderStatusEnum;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.model.TradeRefundDetail;
import com.ymatou.payment.facade.model.TradeRefundableRequest.TradeDetail;

/**
 * 
 * @author qianmin 2016年5月13日 下午2:59:40
 * 
 */
@Component
public class CheckRefundableServiceImpl implements CheckRefundableService {

    private static final Logger logger = LoggerFactory.getLogger(CheckRefundableServiceImpl.class);

    @Autowired
    private DomainConfig domainConfig;

    @Autowired
    private BussinessOrderRepository bussinessOrderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public List<TradeRefundDetail> generateTradeRefundDetailList(List<TradeDetail> tradeDetails) {
        List<TradeRefundDetail> tradeRefundDetails = new ArrayList<>();

        String refundSupportDaysStr = domainConfig.getRefundSupportDays(); // 获取退款允许的天数
        int refundSupportDays = StringUtils.isBlank(refundSupportDaysStr) ? 90 : Integer.valueOf(refundSupportDaysStr);
        LocalDate validDate = LocalDate.now().minusDays(refundSupportDays); // 获取有效的订单日期
        String[] notSupportRefundPayTypes = domainConfig.getNotSupportRefundPayType().split(","); // 不支持退款的

        logger.info("generate tradeRefundDetailList.");
        for (TradeDetail tradeDetail : tradeDetails) {
            BussinessOrder bussinessOrder = bussinessOrderRepository.getBussinessOrderCanRefund(
                    tradeDetail.getTradeNo(), OrderStatusEnum.Paied.getIndex(), Date.valueOf(validDate));
            if (bussinessOrder != null) {
                Payment payment = paymentRepository.getPaymentCanPartRefund(bussinessOrder.getBussinessOrderId());
                BigDecimal paymentRefundAmt = // 已申请退款金额
                        payment.getRefundAmt() == null ? BigDecimal.ZERO : payment.getRefundAmt();
                BigDecimal refundableAmt = payment.getPayPrice().getAmount().subtract(paymentRefundAmt);
                BigDecimal refundAmt = tradeDetail.getRefundAmt() == null ? payment.getPayPrice().getAmount()
                        : tradeDetail.getRefundAmt(); // 退款金额
                logger.info("tradeNo:{}, paymentRefundAmt:{}, refundableAmt:{}, refundAmt:{}", tradeDetail.getTradeNo(),
                        paymentRefundAmt, refundableAmt, refundAmt);

                TradeRefundDetail tradeRefundDetail = new TradeRefundDetail();
                tradeRefundDetail.setCurrencyType(payment.getPayCurrencyType());
                tradeRefundDetail.setPayAmount(payment.getPayPrice().getAmount());
                tradeRefundDetail.setTradeNo(tradeDetail.getTradeNo());
                tradeRefundDetail.setPayChannel(PayTypeEnum.getChannelType(payment.getPayType()).getCode());
                tradeRefundDetail.setPaymentId(payment.getPaymentId());
                tradeRefundDetail.setPayType(payment.getPayType().getCode());
                tradeRefundDetail.setInstPaymentId(payment.getInstitutionPaymentId());
                tradeRefundDetail.setRefundableAmt(refundableAmt);

                if (refundableAmt.compareTo(refundAmt) >= 0) {// 退款金额小于可申请退款金额
                    tradeRefundDetail.setRefundableAmt(refundableAmt);
                    tradeRefundDetail.setRefundable(true); // 可退款
                    if (ArrayUtils.contains(notSupportRefundPayTypes, tradeRefundDetail.getPayType())) { // 不可退款类型
                        tradeRefundDetail.setRefundable(false); // 不可退款
                        tradeRefundDetail.setRefundableAmt(BigDecimal.ZERO);
                    }
                } else {
                    if (refundableAmt.compareTo(BigDecimal.ZERO) > 0) {
                        tradeRefundDetail.setRefundableAmt(refundableAmt);
                    } else {
                        tradeRefundDetail.setRefundableAmt(BigDecimal.ZERO);
                    }
                    tradeRefundDetail.setRefundable(false); // 可退款
                }

                tradeRefundDetails.add(tradeRefundDetail);
            } else {
                // 不能退款的交易信息
                logger.info("Payment also did not pay. TradeNo:{}", tradeDetail.getTradeNo());
                TradeRefundDetail tradeRefundDetail = new TradeRefundDetail();
                tradeRefundDetail.setTradeNo(tradeDetail.getTradeNo());
                tradeRefundDetail.setRefundable(false);

                tradeRefundDetails.add(tradeRefundDetail);
            }
        }

        return tradeRefundDetails;
    }

    @Override
    public List<TradeRefundDetail> removeNonRefundable(List<TradeRefundDetail> tradeRefundDetails) {
        List<TradeRefundDetail> refundableList = new ArrayList<>();
        for (TradeRefundDetail tradeRefundDetail : tradeRefundDetails) {
            if (tradeRefundDetail.isRefundable()) {
                refundableList.add(tradeRefundDetail);
            }
        }
        return refundableList;
    }
}
