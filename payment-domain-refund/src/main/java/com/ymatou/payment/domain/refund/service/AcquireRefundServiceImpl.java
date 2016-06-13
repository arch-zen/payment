/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
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
import com.ymatou.payment.facade.model.AcquireRefundPlusRequest.TradeDetail;
import com.ymatou.payment.facade.model.AcquireRefundPlusResponse.RefundDetail;
import com.ymatou.payment.facade.model.TradeRefundDetail;
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
    public List<TradeRefundDetail> generateTradeRefundDetailList(List<TradeDetail> tradeDetails) {
        List<TradeRefundDetail> tradeRefundDetails = new ArrayList<>();

        String refundSupportDaysStr = domainConfig.getRefundSupportDays(); // 获取退款允许的天数
        int refundSupportDays = StringUtils.isBlank(refundSupportDaysStr) ? 90 : Integer.valueOf(refundSupportDaysStr);
        LocalDate validDate = LocalDate.now().minusDays(refundSupportDays); // 获取有效的订单日期

        logger.info("generate tradeRefundDetailList begin.");
        for (TradeDetail tradeDetail : tradeDetails) {
            String tradeNo = tradeDetail.getTradeNo();
            BigDecimal refundAmt = tradeDetail.getRefundAmt();
            int tradeType = tradeDetail.getTradeType();

            // 根据tradeNo获取已支付，退款有效期内的BussinessOrder
            BussinessOrder bussinessOrder = bussinessOrderRepository.getBussinessOrderCanRefund(
                    tradeNo, OrderStatusEnum.Paied.getIndex(), Date.valueOf(validDate));

            if (bussinessOrder != null) {

                // 根据bussinessorderid找到支付单Payment
                Payment payment = paymentRepository.getPaymentCanRefund(bussinessOrder.getBussinessOrderId(),
                        OrderStatusEnum.Paied.getIndex());
                BigDecimal paymentRefundAmt =
                        payment.getRefundAmt() == null ? new BigDecimal(0.00) : payment.getRefundAmt();

                if (payment.getActualPayPrice().compareTo(refundAmt.add(paymentRefundAmt)) > 0) { // TODO
                    // 组装可退款交易信息
                    TradeRefundDetail tradeRefundDetail = new TradeRefundDetail();
                    tradeRefundDetail.setCurrencyType(payment.getPayCurrencyType());
                    tradeRefundDetail.setRefundable(true); // 可退款
                    tradeRefundDetail.setPayAmount(payment.getPayPrice().getAmount());
                    tradeRefundDetail.setTradeNo(tradeNo);
                    tradeRefundDetail
                            .setPayChannel(refundPository.convertPayTypeToPayChannel(payment.getPayType().getCode()));
                    tradeRefundDetail.setPaymentId(payment.getPaymentId());
                    tradeRefundDetail.setPayType(payment.getPayType().getCode());
                    tradeRefundDetail.setInstPaymentId(payment.getInstitutionPaymentId());
                    tradeRefundDetail.setRefundableAmt(refundAmt); // 设置可退款金额

                    tradeRefundDetails.add(tradeRefundDetail);
                } else {
                    // 不能退款的交易信息
                    TradeRefundDetail tradeRefundDetail = new TradeRefundDetail();
                    tradeRefundDetail.setTradeNo(tradeNo);
                    tradeRefundDetail
                            .setRefundableAmt(payment.getActualPayPrice().getAmount().subtract(paymentRefundAmt)); // TODO

                    tradeRefundDetails.add(tradeRefundDetail);
                }
            } else {
                // 不能退款的交易信息
                TradeRefundDetail tradeRefundDetail = new TradeRefundDetail();
                tradeRefundDetail.setTradeNo(tradeNo);

                tradeRefundDetails.add(tradeRefundDetail);
            }
        }
        logger.info("generate tradeRefundDetailList end.");

        return tradeRefundDetails;
    }

    @Override
    public List<RefundDetail> checkAndSaveRefundRequest(List<TradeRefundDetail> tradeRefundDetails,
            AcquireRefundPlusRequest req) {
        List<RefundDetail> acquireRefundDetails = new ArrayList<>();
        List<RefundRequestPo> refundrequestWithBLOBs = new ArrayList<>();

        logger.info("generate RefundRequest list");
        for (TradeRefundDetail tradeRefundDetail : tradeRefundDetails) {
            // 根据paymentId查找RefundRequest
            RefundRequestPo refundrequestPo =
                    refundPository.getRefundRequestByPaymentId(tradeRefundDetail.getPaymentId());

            // 组装AcquireRefundDetail，可被退款的交易(接口的返回参数)
            RefundDetail acquireRefundDetail = new RefundDetail();
            acquireRefundDetail.setRefundable(true);
            acquireRefundDetail.setTradeNo(tradeRefundDetail.getTradeNo());
            acquireRefundDetails.add(acquireRefundDetail);

            if (refundrequestPo == null) { // 若不存在RefundRequest，则插入
                RefundRequestPo refundrequest = new RefundRequestPo();
                refundrequest.setPaymentId(tradeRefundDetail.getPaymentId());
                refundrequest.setInstPaymentId(tradeRefundDetail.getInstPaymentId());
                refundrequest.setTradeNo(tradeRefundDetail.getTradeNo());
                refundrequest.setOrderId(req.getOrderId());
                refundrequest.setTraceId(req.getTraceId());
                refundrequest.setAppId(req.getAppId());
                refundrequest.setPayType(tradeRefundDetail.getPayType());
                refundrequest.setRefundAmount(tradeRefundDetail.getPayAmount());
                refundrequest.setCurrencyType(tradeRefundDetail.getCurrencyType());
                refundrequest.setRefundStatus(RefundStatusEnum.INIT.getCode());
                for (TradeDetail tradeDetail : req.getTradeDetails()) {
                    if (tradeDetail.getTradeNo().equals(tradeRefundDetail.getTradeNo())) {
                        refundrequest.setTradeType(tradeDetail.getTradeType());
                        break;
                    }
                }
                refundrequest.setRefundBatchNo(refundPository.generateRefundBatchNo());

                refundrequestWithBLOBs.add(refundrequest); // 需要被插入的RefundRequest
            }
        }
        logger.info("{} RefundRequest will be saved.", refundrequestWithBLOBs.size());

        logger.info("batch save RefundRequest begin.");
        refundPository.batchSaveRefundRequest(refundrequestWithBLOBs); // 插入RefundRequest
        logger.info("batch save RefundRequest end.");

        return acquireRefundDetails;
    }


}
