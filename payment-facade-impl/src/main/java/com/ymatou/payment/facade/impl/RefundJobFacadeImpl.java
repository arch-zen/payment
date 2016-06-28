/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.impl;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.service.PayService;
import com.ymatou.payment.domain.refund.service.RefundJobService;
import com.ymatou.payment.facade.RefundJobFacade;
import com.ymatou.payment.facade.constants.AccountingStatusEnum;
import com.ymatou.payment.facade.constants.ApproveStatusEnum;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.facade.model.ExecuteRefundRequest;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;

/**
 * 
 * @author qianmin 2016年6月8日 上午10:44:29
 *
 */
@Component
public class RefundJobFacadeImpl implements RefundJobFacade {

    private static final Logger logger = LoggerFactory.getLogger(RefundJobFacadeImpl.class);

    private static final int SOFT_DELETED = -3;

    @Autowired
    private RefundJobService refundJobService;

    @Autowired
    private PayService payService;

    @Override
    public int executeRefund(ExecuteRefundRequest request) {
        Integer refundId = request.getRefundId();
        HashMap<String, String> header = request.getHeader();

        logger.info("Step 1: query refundRequest, payment, businessOrder. {}", refundId);
        RefundRequestPo refundRequest = refundJobService.getRefundRequestByRefundId(refundId);
        if (refundRequest == null // 退款申请不不存在或未审核
                || refundRequest.getApproveStatus().equals(ApproveStatusEnum.NOT_APPROVED.getCode())) {
            return RefundStatusEnum.INIT.getCode();
        }
        refundJobService.updateRetryCount(refundId); // 更新重试次数

        if (refundRequest.getSoftDeleteFlag()) {
            return SOFT_DELETED;
        }

        Payment payment = payService.getPaymentByPaymentId(refundRequest.getPaymentId());
        BussinessOrder bussinessOrder = payService.getBussinessOrderById(payment.getBussinessOrderId());
        int refundStatusFlag = refundRequest.getRefundStatus(); // 返回给定时调度器，告知当前退款申请的状态
        if (refundRequest.getRefundStatus().equals(RefundStatusEnum.INIT.getCode())) {// 提交退款申请
            boolean accountingSuccess = true;

            if (refundRequest.getApproveStatus().equals(ApproveStatusEnum.FAST_REFUND.getCode())
                    && !AccountingStatusEnum.SUCCESS.code().equals(refundRequest.getAccoutingStatus())) {
                logger.info("Step 2: dedcut user balance.");
                accountingSuccess = refundJobService.dedcutBalance(payment, bussinessOrder, refundRequest, header);
            }

            if (accountingSuccess) {
                logger.info("Step 3: submit third party refund.");
                refundJobService.submitRefund(refundRequest, payment, header);
            }

        } else {// 查询退款结果
            RefundStatusEnum refundStatus = RefundStatusEnum.withCode(refundRequest.getRefundStatus());
            if (!RefundStatusEnum.THIRDPART_REFUND_SUCCESS.equals(refundStatus)) {
                logger.info("Step 4: submit third party refund query.");
                refundStatus = refundJobService.queryRefund(refundRequest, payment, header);
                refundStatusFlag = refundStatus.getCode();

                logger.info("Step 5: update refund status, refund amount and retry count.");
                refundJobService.updateRefundRequestAndPayment(refundRequest, payment, refundStatus);
            }

            if (RefundStatusEnum.THIRDPART_REFUND_SUCCESS.equals(refundStatus)) {
                logger.info("Step 5: callback trading system.");
                boolean isSuccess = refundJobService.callbackTradingSystem(refundRequest, payment, header);

                if (isSuccess) {
                    logger.info("Step 6: update refundrequest to completed suceess.");
                    refundJobService.updateRefundRequestToCompletedSuccess(refundRequest);
                    refundStatusFlag = RefundStatusEnum.COMPLETE_SUCCESS.getCode();
                }
            }
        }

        return refundStatusFlag;
    }

}
