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
import com.ymatou.payment.domain.refund.service.ApproveRefundService;
import com.ymatou.payment.domain.refund.service.CheckRefundableService;
import com.ymatou.payment.domain.refund.service.FastRefundService;
import com.ymatou.payment.domain.refund.service.QueryRefundService;
import com.ymatou.payment.domain.refund.service.SubmitRefundService;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.RefundFacade;
import com.ymatou.payment.facade.model.AcquireRefundDetail;
import com.ymatou.payment.facade.model.AcquireRefundRequest;
import com.ymatou.payment.facade.model.AcquireRefundResponse;
import com.ymatou.payment.facade.model.ApproveRefundDetail;
import com.ymatou.payment.facade.model.ApproveRefundRequest;
import com.ymatou.payment.facade.model.ApproveRefundResponse;
import com.ymatou.payment.facade.model.FastRefundRequest;
import com.ymatou.payment.facade.model.FastRefundResponse;
import com.ymatou.payment.facade.model.QueryRefundDetail;
import com.ymatou.payment.facade.model.QueryRefundRequest;
import com.ymatou.payment.facade.model.QueryRefundResponse;
import com.ymatou.payment.facade.model.TradeDetail;
import com.ymatou.payment.facade.model.TradeRefundDetail;
import com.ymatou.payment.facade.model.TradeRefundableRequest;
import com.ymatou.payment.facade.model.TradeRefundableResponse;

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
    private FastRefundService fastRefundService;

    @Autowired
    private SubmitRefundService submitRefundService;

    @Autowired
    private ApproveRefundService approveRefundService;

    @Autowired
    private CheckRefundableService checkRefundableService;

    @Autowired
    private QueryRefundService queryRefundService;

    @Override
    public FastRefundResponse fastRefund(FastRefundRequest req) {
        // query and verify payment
        Payment payment = payService.getPaymentByPaymentId(req.getPaymentId());
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
        fastRefundService.saveRefundRequest(payment, bussinessorder, refundInfo);

        // notify refund service
        fastRefundService.notifyRefund(refundInfo, req.getHeader());

        // send trading message
        for (String orderId : req.getOrderIdList()) {
            fastRefundService.sendFastRefundTradingMessage(bussinessorder.getUserid().toString(), orderId,
                    req.getHeader());
        }

        return new FastRefundResponse();
    }

    @Override
    public AcquireRefundResponse submitRefund(AcquireRefundRequest req) {
        if (StringUtils.isEmpty(req.getOrderId())) {
            throw new BizException(ErrorCode.INVALID_ORDER_ID, "order id is empty.");
        }



        // 获取退款的相关的交易信息
        List<TradeDetail> tradeDetails = req.getTradeDetails();
        List<String> tradeNos = new ArrayList<>();
        for (TradeDetail tradeDetail : tradeDetails) {
            tradeNos.add(tradeDetail.getTradeNo());
        }
        List<TradeRefundDetail> tradeRefundDetails = submitRefundService.generateTradeRefundDetailList(tradeNos);

        // 筛选出可退款的交易信息
        List<TradeRefundDetail> refundableTrades = new ArrayList<>();
        for (TradeRefundDetail tradeRefundDetail : tradeRefundDetails) {
            if (tradeRefundDetail.isRefundable()) {
                refundableTrades.add(tradeRefundDetail);
            }
        }

        // 若有不能被退款的，报错
        if (refundableTrades.size() != tradeDetails.size()) {
            logger.info("request refund trades size {}", tradeDetails.size());
            logger.info("refundableTrades size {} ", refundableTrades.size());
            throw new BizException(ErrorCode.NOT_ALL_TRADE_CAN_REFUND, "not all trade can be refunded.");
        }

        // 检查是否已经生成RefundRequest，若未生成则生成RefundRequest，并生成相应应答
        List<AcquireRefundDetail> acquireRefundDetails =
                submitRefundService.checkAndSaveRefundRequest(refundableTrades, req);

        AcquireRefundResponse response = new AcquireRefundResponse();
        response.setDetails(acquireRefundDetails);

        return response;
    }

    @Override
    public ApproveRefundResponse approveRefund(ApproveRefundRequest req) {
        // 更新RefundRequest审核状态， 保存CompensateProcessInfo， 获取需要通知退款的PaymentIds
        List<String> paymentIds = approveRefundService.approveRefund(req.getPaymentIds(), req.getApproveUser());

        // 通知退款
        approveRefundService.notifyRefund(paymentIds, req.getHeader());

        ApproveRefundResponse response = new ApproveRefundResponse();
        ApproveRefundDetail approveRefundDetail = new ApproveRefundDetail(true);
        response.setDetails(approveRefundDetail);

        return response;
    }

    @Override
    public TradeRefundableResponse checkRefundable(TradeRefundableRequest req) {
        // 获取是否可以退款信息
        List<TradeRefundDetail> tradeRefundDetails = checkRefundableService.generateRefundableTrades(req.getTradeNos());

        TradeRefundableResponse response = new TradeRefundableResponse();
        response.setDetails(tradeRefundDetails);

        return response;
    }

    @Override
    public QueryRefundResponse query(QueryRefundRequest req) {
        // 查询退款单详情
        List<QueryRefundDetail> details = queryRefundService.queryRefundRequest(req);

        QueryRefundResponse response = new QueryRefundResponse();
        response.setDetails(details);
        response.setCount(details.size());

        return response;
    }

}
