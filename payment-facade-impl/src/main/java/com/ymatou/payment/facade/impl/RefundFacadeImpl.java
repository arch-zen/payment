/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.impl;

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
import com.ymatou.payment.facade.model.FastRefundRequest;
import com.ymatou.payment.facade.model.FastRefundResponse;

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
        Payment payment = payService.GetPaymentByPaymentId(req.getPaymentId());
        if (payment == null) {
            throw new BizException(ErrorCode.NOT_EXIST_PAYMENTID, "Payment not exist");
        }
        logger.info("Payment result: {}", payment);

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

        refundService.saveRefundRequest(payment, bussinessorder, refundInfo); // 退款请求落地

        refundService.notifyRefund(refundInfo, req.getHeader());// 通知退款

        for (String orderId : req.getOrderIdList()) {
            refundService.sendFastRefundTradingMessage(bussinessorder.getUserid().toString(), orderId,
                    req.getHeader()); // 发送交易信息
        }

        return new FastRefundResponse();
    }

}
