/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.service.PayService;
import com.ymatou.payment.domain.refund.model.Refund;
import com.ymatou.payment.domain.refund.service.AcquireRefundService;
import com.ymatou.payment.domain.refund.service.ApproveRefundService;
import com.ymatou.payment.domain.refund.service.CheckRefundableService;
import com.ymatou.payment.domain.refund.service.FastRefundService;
import com.ymatou.payment.domain.refund.service.QueryRefundService;
import com.ymatou.payment.domain.refund.service.RefundJobService;
import com.ymatou.payment.domain.refund.service.SubmitRefundService;
import com.ymatou.payment.domain.refund.service.SysApproveRefundService;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.RefundFacade;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.facade.model.AcquireRefundDetail;
import com.ymatou.payment.facade.model.AcquireRefundPlusRequest;
import com.ymatou.payment.facade.model.AcquireRefundPlusResponse;
import com.ymatou.payment.facade.model.AcquireRefundRequest;
import com.ymatou.payment.facade.model.AcquireRefundResponse;
import com.ymatou.payment.facade.model.ApproveRefundRequest;
import com.ymatou.payment.facade.model.ApproveRefundResponse;
import com.ymatou.payment.facade.model.FastRefundRequest;
import com.ymatou.payment.facade.model.FastRefundResponse;
import com.ymatou.payment.facade.model.QueryRefundDetail;
import com.ymatou.payment.facade.model.QueryRefundRequest;
import com.ymatou.payment.facade.model.QueryRefundResponse;
import com.ymatou.payment.facade.model.SysApproveRefundReq;
import com.ymatou.payment.facade.model.SysApproveRefundResp;
import com.ymatou.payment.facade.model.TradeDetail;
import com.ymatou.payment.facade.model.TradeRefundDetail;
import com.ymatou.payment.facade.model.TradeRefundableRequest;
import com.ymatou.payment.facade.model.TradeRefundableResponse;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;

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

    @Autowired
    private AcquireRefundService acquireRefundService;

    @Autowired
    private RefundJobService refundJobService;

    @Autowired
    private SysApproveRefundService sysApproveRefundService;

    @Override
    public FastRefundResponse fastRefund(FastRefundRequest req) {
        if (StringUtils.isBlank(req.getRefundNo()) && StringUtils.isBlank(req.getTraceId())) {
            throw new BizException(ErrorCode.ILLEGAL_ARGUMENT, "RefundNo and TraceId cannot be empty.");
        }

        // query and verify payment
        Payment payment = payService.getPaymentByPaymentId(req.getPaymentId());
        if (payment == null) {
            throw new BizException(ErrorCode.NOT_EXIST_PAYMENTID, "Payment not exist");
        }
        logger.info("Payment result: {}", payment);

        // query and verify bussinessorder
        BussinessOrder bussinessOrder = payService.getBussinessOrderById(payment.getBussinessOrderId());
        if (bussinessOrder == null) {
            throw new BizException(ErrorCode.NOT_EXIST_BUSSINESS_ORDERID, "businessOrderId not exist");
        }
        logger.info("BussinessOrder result: {}", bussinessOrder);

        if (!req.getTradingId().equals(bussinessOrder.getOrderId())) {
            throw new BizException(ErrorCode.INCONSISTENT_PAYMENTID_AND_TRADINGID,
                    "inconsistent paymentId and tradingId");
        }
        if (payment.getPayStatus() != PayStatusEnum.Paied) {
            throw new BizException(ErrorCode.INVALID_PAYMENT_STATUS, "invalid payment status");
        }

        Refund refundInfo = new Refund();
        refundInfo.setAppId(req.getAppId());
        refundInfo.setTradingId(req.getTradingId());
        refundInfo.setTradeType(req.getTradeType());
        refundInfo.setOrderIdList(req.getOrderIdList());
        refundInfo.setPaymentId(req.getPaymentId());
        refundInfo.setTraceId(StringUtils.isBlank(req.getTraceId())
                ? req.getRefundNo() : req.getTraceId()); // .net,java请求参数不同

        // Save RefundRequest
        RefundRequestPo refundRequest =
                fastRefundService.saveRefundRequest(req.getRefundAmt(), payment, bussinessOrder, refundInfo);

        // send trading message
        for (String orderId : req.getOrderIdList()) {
            fastRefundService.sendFastRefundTradingMessage(bussinessOrder.getUserId().toString(), orderId,
                    req.getHeader());
        }

        // 扣除账户码头余额
        boolean accountingSuccess =
                refundJobService.dedcutBalance(payment, bussinessOrder, refundRequest, req.getHeader());

        // notify refund service
        if (accountingSuccess) {
            refundJobService.submitRefund(refundRequest, payment, req.getHeader());
        }

        FastRefundResponse response = new FastRefundResponse();
        response.setSuccess(true);
        response.setErrorMessage("操作成功！");

        return response;
    }

    @Override
    public AcquireRefundResponse submitRefund(AcquireRefundRequest req) {
        if (StringUtils.isEmpty(req.getOrderId())) {
            throw new BizException(ErrorCode.INVALID_ORDER_ID, "order id is empty.");
        }



        // 获取退款的相关的交易信息
        List<TradeDetail> tradeDetails = req.getTradeDetails();
        if (tradeDetails == null || tradeDetails.size() == 0)
            throw new BizException(ErrorCode.ILLEGAL_ARGUMENT, "TradeDetail值不能为 null");

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
        response.setSuccess(true);

        return response;
    }

    @Override
    public ApproveRefundResponse approveRefund(ApproveRefundRequest req) {
        // 更新RefundRequest审核状态， 获取需要通知退款单
        List<RefundRequestPo> refunds = approveRefundService.approveRefund(req.getRefundNos(), req.getApproveUser());

        // 提交第三方退款
        for (RefundRequestPo refundRequest : refunds) {
            Payment payment = payService.getPaymentByPaymentId(refundRequest.getPaymentId());
            refundJobService.submitRefund(refundRequest, payment, req.getHeader());
        }

        ApproveRefundResponse response = new ApproveRefundResponse();
        response.setSuccess(true);
        return response;
    }

    @Override
    public TradeRefundableResponse checkRefundable(TradeRefundableRequest req) {
        // 兼容新老接口
        boolean isDotNet = false;
        if (req.getTradeDetails() == null || req.getTradeDetails().size() == 0) {
            isDotNet = true;
            List<TradeRefundableRequest.TradeDetail> tradeDetails = new ArrayList<>();
            for (String tradeNo : req.getTradeNos()) {
                TradeRefundableRequest.TradeDetail tradeDetail = new TradeRefundableRequest.TradeDetail();
                tradeDetail.setTradeNo(tradeNo);
                tradeDetail.setRefundAmt(null);
                tradeDetails.add(tradeDetail);
            }
            req.setTradeDetails(tradeDetails);
        }

        // 获取是否可以退款信息
        List<TradeRefundDetail> tradeRefundDetails =
                checkRefundableService.generateTradeRefundDetailList(req.getTradeDetails());

        // 剔除不可退款交易
        if (isDotNet) {
            tradeRefundDetails = checkRefundableService.removeNonRefundable(tradeRefundDetails);
        }

        TradeRefundableResponse response = new TradeRefundableResponse();
        response.setDetails(tradeRefundDetails);
        response.setSuccess(true);

        return response;
    }

    @Override
    public QueryRefundResponse query(QueryRefundRequest req) {
        // 查询退款单详情
        List<QueryRefundDetail> details = queryRefundService.queryRefundRequest(req);

        QueryRefundResponse response = new QueryRefundResponse();
        response.setDetails(details);
        response.setCount(details.size());
        response.setSuccess(true);

        return response;
    }

    @Override
    public AcquireRefundPlusResponse acquireRefund(AcquireRefundPlusRequest req) {
        AcquireRefundPlusResponse response = new AcquireRefundPlusResponse();
        if (acquireRefundService.acquireRefund(req)) {
            response.setSuccess(true);
        } else {
            response.setSuccess(false);
            response.setErrorCode(ErrorCode.UNABLE_REFUND);
            response.setErrorMessage("拒绝退款");
        }
        return response;
    }

    @Override
    public SysApproveRefundResp sysApproveRefund(SysApproveRefundReq req) {
        SysApproveRefundResp resp = new SysApproveRefundResp();
        Date scheduleTime = null;
        try {
            scheduleTime = DateUtils.parseDate(req.getBizId(), "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            String errMsg = "system approve refund receive invalid schedule time: " + req.getBizId();
            logger.error(errMsg, e);
            throw new BizException(errMsg, e);
        }

        int approveNum = sysApproveRefundService.batchApproveRefundReq(scheduleTime);
        logger.info("system approved refund req num: {}", approveNum);

        resp.setSuccess(true);
        return resp;
    }
}
