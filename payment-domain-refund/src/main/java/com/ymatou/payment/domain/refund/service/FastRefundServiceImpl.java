/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.refund.model.Refund;
import com.ymatou.payment.domain.refund.repository.RefundPository;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;
import com.ymatou.payment.integration.common.constants.BusinessTypeEnum;
import com.ymatou.payment.integration.model.NotifyUserRequest;
import com.ymatou.payment.integration.service.ymatou.NotifyUserService;

/**
 * 
 * @author qianmin 2016年5月13日 上午10:59:41
 * 
 */
@Component
public class FastRefundServiceImpl implements FastRefundService {

    private static final Logger logger = LoggerFactory.getLogger(FastRefundServiceImpl.class);

    @Autowired
    private RefundPository refundPository;

    @Autowired
    private NotifyUserService notifyUserService;

    public RefundRequestPo saveRefundRequest(BigDecimal refundAmt, Payment payment, BussinessOrder bussinessorder,
            Refund refundInfo) {
        // 根据RequestNo及TradeNo查找RefundRequest， 保证幂等
        List<RefundRequestPo> refundRequests =
                refundPository.getRefundReqestByTraceId(refundInfo.getTraceId());

        if (refundRequests.size() == 0) { // 若不存在RefundRequest，则新增， 更新退款申请金额
            logger.info("Save RefundRequest.");
            setRefundAmt(refundAmt, refundInfo, payment); // 设置退款金额
            refundPository.saveFastRefundrequest(payment, bussinessorder, refundInfo);
            refundRequests = refundPository.getRefundReqestByTraceId(refundInfo.getTraceId());
        } else { // 已存在， 幂等， 返回成功
            logger.info("RefundRequest already exists. RefundBatchNo:{}",
                    refundRequests.get(0).getRefundBatchNo());
        }

        return refundRequests.get(0);
    }

    private void setRefundAmt(BigDecimal refundAmt, Refund refundInfo, Payment payment) {
        // 判断，设置退款金额
        BigDecimal requestedRefundAmt =
                payment.getRefundAmt() == null ? BigDecimal.ZERO : payment.getRefundAmt();
        if (refundAmt == null || refundAmt.compareTo(BigDecimal.ZERO) <= 0) {
            if (requestedRefundAmt.compareTo(BigDecimal.ZERO) > 0) { // 未输入退款申请金额，默认全额退款
                throw new BizException(ErrorCode.FAIL, "refund amt limit.");
            } else {
                refundInfo.setRefundAmt(payment.getPayPrice().getAmount());
            }
        } else {
            if (requestedRefundAmt.add(refundAmt).compareTo(payment.getPayPrice().getAmount()) > 0) {
                throw new BizException(ErrorCode.FAIL, "refund amt limit.");
            } else {
                refundInfo.setRefundAmt(refundAmt);
            }
        }
    }

    @Override
    public void sendFastRefundTradingMessage(String userId, String orderId, HashMap<String, String> header) {
        try {
            NotifyUserRequest request = new NotifyUserRequest();
            request.setBusinessType(BusinessTypeEnum.FAST_REFUND.getCode());
            request.setBuyerId(userId);
            request.setIsShangouOrder(true);
            request.setOrderId(orderId);
            logger.info("async send trading message");
            notifyUserService.sendTradingMessage(request, header);
        } catch (Exception e) {
            // 异步通知，发完通知就行
        }
    }
}
