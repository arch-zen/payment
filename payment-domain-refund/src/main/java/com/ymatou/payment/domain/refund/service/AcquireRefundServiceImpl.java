/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

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
import com.ymatou.payment.domain.refund.repository.RefundPository;
import com.ymatou.payment.facade.constants.OrderStatusEnum;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.facade.model.AcquireRefundPlusRequest;
import com.ymatou.payment.infrastructure.Money;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;

/**
 * 
 * @author qianmin 2016年6月3日 下午6:02:55
 *
 */
@Component
public class AcquireRefundServiceImpl implements AcquireRefundService {

    private static final Logger logger = LoggerFactory.getLogger(AcquireRefundServiceImpl.class);

    @Autowired
    private RefundPository refundPository;

    @Autowired
    private BussinessOrderRepository bussinessOrderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private DomainConfig domainConfig;

    @Override
    public boolean acquireRefund(AcquireRefundPlusRequest req) {
        String refundSupportDaysStr = domainConfig.getRefundSupportDays(); // 获取退款允许的天数
        int refundSupportDays = StringUtils.isBlank(refundSupportDaysStr) ? 90 : Integer.valueOf(refundSupportDaysStr);
        LocalDate validDate = LocalDate.now().minusDays(refundSupportDays); // 获取有效的订单日期

        // 根据tradeNo获取已支付，退款有效期内的BussinessOrder
        BussinessOrder bussinessOrder = bussinessOrderRepository.getBussinessOrderCanRefund(
                req.getTradeNo(), OrderStatusEnum.Paied.getIndex(), Date.valueOf(validDate));
        if (bussinessOrder != null) {
            Payment payment = paymentRepository.getPaymentCanPartRefund(bussinessOrder.getBussinessOrderId());

            // 根据RequestNo查找RefundRequest， 保证幂等
            List<RefundRequestPo> refundRequests = refundPository.getRefundReqestByTraceId(req.getRefundNo());
            if (refundRequests.size() == 0) { // 若不存在RefundRequest，则新增

                BigDecimal paymentRefundAmt = payment.getRefundAmt() == null ? BigDecimal.ZERO : payment.getRefundAmt();

                // 申请金额小于实际支付金额（用户实际付款）
                // 存在第三方机构贴钱促销的情况，所以如果订单支付现金 100，第三方补贴 10，则最多只能退90
                if (payment.getActualPayPrice().compareTo(new Money(req.getRefundAmt().add(paymentRefundAmt))) >= 0) {
                    RefundRequestPo refundrequest = new RefundRequestPo();
                    refundrequest.setPaymentId(payment.getPaymentId());
                    refundrequest.setInstPaymentId(payment.getInstitutionPaymentId());
                    refundrequest.setTradeNo(req.getTradeNo());
                    refundrequest.setOrderId(req.getOrderId());
                    refundrequest.setTraceId(req.getRefundNo());
                    refundrequest.setAppId(req.getAppId());
                    refundrequest.setPayType(payment.getPayType().getCode());
                    refundrequest.setRefundAmount(req.getRefundAmt());
                    refundrequest.setCurrencyType(payment.getPayCurrencyType());
                    refundrequest.setRefundStatus(RefundStatusEnum.INIT.getCode());
                    refundrequest.setTradeType(req.getTradeType());
                    // refundrequest.setRefundBatchNo(refundPository.generateRefundBatchNo());

                    // 新增退款申请， 更新退款申请金额
                    logger.info("Save RefundRequest and update RefundAmt. RefundNo:{}", refundrequest.getTraceId());
                    refundPository.saveRefundRequestAndUpdateRefundAmt(refundrequest, payment);
                    return true;
                } else {
                    logger.info("Refundable Amount limited.");
                }

            } else { // 已存在， 幂等， 返回成功
                logger.info("RefundRequest already exists. RefundNo:{}", refundRequests.get(0).getTraceId());
                return true;
            }

        } else {
            logger.info("Paied BusinessOrder not exists. TradeNo{}", req.getTradeNo());
        }
        return false;
    }
}
