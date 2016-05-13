/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.PayStatus;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.service.PayService;
import com.ymatou.payment.domain.refund.model.Refund;
import com.ymatou.payment.domain.refund.service.RefundService;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.RefundFacade;
import com.ymatou.payment.facade.model.AcquireRefundDetail;
import com.ymatou.payment.facade.model.AcquireRefundRequest;
import com.ymatou.payment.facade.model.AcquireRefundResponse;
import com.ymatou.payment.facade.model.FastRefundRequest;
import com.ymatou.payment.facade.model.FastRefundResponse;
import com.ymatou.payment.facade.model.TradeDetail;
import com.ymatou.payment.facade.model.TradeRefundDetail;

/**
 * 
 * @author qianmin 2016年5月11日 上午10:56:11
 * 
 */
@Component
public class RefundFacadeImpl implements RefundFacade {

    private static final Logger logger = LoggerFactory.getLogger(RefundFacadeImpl.class);

    @Autowired
    private PayService payService;

    @Autowired
    private RefundService refundService;

    @Override
    public FastRefundResponse fastRefund(FastRefundRequest req) {
        // query and verify payment
        Payment payment = payService.GetPaymentByPaymentId(req.getPaymentId());
        if (payment == null) {
            throw new BizException(ErrorCode.NOT_EXIST_PAYMENTID, "Payment not exist");
        }
        logger.info("Payment result: {}", payment);

        // query and verify bussinessorder
        BussinessOrder bussinessorder = payService.getBussinessOrderById(payment.getBussinessorderid());
        if (bussinessorder == null) {
            throw new BizException(ErrorCode.NOT_EXIST_BUSSINESS_ORDERID, "businessOrderId not exist");
        }
        logger.info("BussinessOrder result: {}", bussinessorder);

        if (!req.getTradingId().equals(bussinessorder.getOrderid())) {
            throw new BizException(ErrorCode.INCONSISTENT_PAYMENTID_AND_TRADINGID,
                    "inconsistent paymentId and tradingId");
        }
        if (!payment.getPaystatus().equals(PayStatus.Paied.getIndex())) {
            throw new BizException(ErrorCode.INVALID_PAYMENT_STATUS, "invalid payment status");
        }

        Refund refundInfo = new Refund();
        refundInfo.setAppId(req.getAppId());
        refundInfo.setTradingId(req.getTradingId());
        refundInfo.setTradeType(req.getTradeType());
        refundInfo.setOrderIdList(req.getOrderIdList());
        refundInfo.setPaymentId(req.getPaymentId());
        refundInfo.setTraceId(req.getTraceId());

        // Save RefundRequest And CompensateProcessInfo
        refundService.saveRefundRequest(payment, bussinessorder, refundInfo);

        // notify refund service
        refundService.notifyRefund(refundInfo, req.getHeader());

        // send trading message
        for (String orderId : req.getOrderIdList()) {
            refundService.sendFastRefundTradingMessage(bussinessorder.getUserid().toString(), orderId,
                    req.getHeader());
        }

        return new FastRefundResponse();
    }

    @Override
    public AcquireRefundResponse submitRefund(AcquireRefundRequest req) {
        if (StringUtils.isEmpty(req.getOrderId())) {
            throw new BizException(ErrorCode.INVALID_ORDER_ID, "order id is empty.");
        }

        AcquireRefundResponse response = new AcquireRefundResponse();
        List<TradeDetail> tradeDetails = req.getTradeDetails();
        List<TradeRefundDetail> refundableTrades = new ArrayList<>();

        // 获取退款的相关的交易信息
        List<TradeRefundDetail> tradeRefundDetails = refundService.generateTradeRefundDetailList(tradeDetails);
        for (TradeRefundDetail tradeRefundDetail : tradeRefundDetails) {
            if (tradeRefundDetail.isRefundable()) { // 筛选出可退款的交易信息
                refundableTrades.add(tradeRefundDetail);
            }
        }

        if (refundableTrades.size() != tradeDetails.size()) { // 若有不能被退款的，报错
            logger.info("request refund trades size {}", tradeDetails.size());
            logger.info("refundableTrades size {} ", refundableTrades.size());
            throw new BizException(ErrorCode.NOT_ALL_TRADE_CAN_REFUND, "not all trade can be refunded.");
        }

        // 检查是否已经生成RefundRequest，若未生成则生成RefundRequest，并生成相应应答
        List<AcquireRefundDetail> acquireRefundDetails = refundService.checkAndSaveRefundRequest(refundableTrades, req);

        response.setDetails(acquireRefundDetails);

        return response;
    }

}
