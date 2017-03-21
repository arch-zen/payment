/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.impl;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.messagebus.client.Message;
import com.ymatou.messagebus.client.MessageBusClient;
import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.service.PayService;
import com.ymatou.payment.domain.refund.service.RefundJobService;
import com.ymatou.payment.facade.RefundJobFacade;
import com.ymatou.payment.facade.constants.AccountingStatusEnum;
import com.ymatou.payment.facade.constants.ApproveStatusEnum;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.facade.model.ExecuteRefundRequest;
import com.ymatou.payment.facade.model.ExecuteRefundResponse;
import com.ymatou.payment.facade.model.RefundSuccessNotifyReq;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;
import com.ymatou.payment.integration.service.ymatou.EmailService;

/**
 * 
 * @author qianmin 2016年6月8日 上午10:44:29
 *
 */
@Component
public class RefundJobFacadeImpl implements RefundJobFacade {

    private static final Logger logger = LoggerFactory.getLogger(RefundJobFacadeImpl.class);

    @Autowired
    private RefundJobService refundJobService;

    @Autowired
    private PayService payService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MessageBusClient messageBusClient;

    @Override
    public ExecuteRefundResponse executeRefund(ExecuteRefundRequest request) {
        Integer refundId = request.getRefundId();
        HashMap<String, String> header = request.getHeader();
        ExecuteRefundResponse response = new ExecuteRefundResponse();

        // 根据RefundRequest.RefundStatus判断是否要继续退款流程
        logger.info("Step 1: query refundRequest, payment, businessOrder. {}", refundId);
        RefundRequestPo refundRequest = refundJobService.getRefundRequestByRefundId(refundId);
        boolean isContinue = refundJobService.isContinueExecute(refundRequest);
        if (!isContinue) {
            response.setRefundResult("ok");
            return response;
        }

        Payment payment = payService.getPaymentByPaymentId(refundRequest.getPaymentId());
        BussinessOrder bussinessOrder = payment.getBussinessOrder();
        refundJobService.updateRetryCount(refundId); // 更新重试次数

        RefundStatusEnum refundStatus = RefundStatusEnum.withCode(refundRequest.getRefundStatus());
        response.setRefundResult(String.valueOf(refundStatus.getCode()));
        if (refundStatus.equals(RefundStatusEnum.INIT)) {// 提交第三方退款申请
            AccountingStatusEnum accountingStatus = AccountingStatusEnum.SUCCESS;
            if (refundRequest.getApproveStatus().equals(ApproveStatusEnum.FAST_REFUND.getCode())
                    && !AccountingStatusEnum.SUCCESS.code().equals(refundRequest.getAccoutingStatus())) {
                logger.info("Step 2: dedcut user balance.");
                accountingStatus = refundJobService.dedcutBalance(payment, bussinessOrder, refundRequest, header);
            }

            if (AccountingStatusEnum.SUCCESS.equals(accountingStatus)) {// 扣除码头余额成功/非快速退款
                logger.info("Step 3: submit third party refund.");
                refundStatus = refundJobService.submitRefund(refundRequest, payment, header);
                response.setRefundResult(String.valueOf(refundStatus.getCode()));
            }

            // 微信退款发现用户账户异常或已注销，调用交易系统告知原路退回失败，交易系统返钱至用户码头余额
            if (RefundStatusEnum.COMPLETE_FAILED_WX_USER_ABNORMAL.equals(refundStatus)) {

                logger.info("Step 4: callback trading system refund fail.");
                if (refundJobService.callbackTradingSystem(refundRequest, payment, false, header)) {

                    logger.info("Step 5: update refundRequest to return to trading.");
                    refundJobService.updateRefundRequestToReturnToTrading(refundRequest);
                    response.setRefundResult(String.valueOf(RefundStatusEnum.RETURN_TRANSACTION.getCode()));

                    logger.info("Step 6: send email to customer service.");
                    emailService.sendWxRefundFailEmail(refundRequest.getOrderId());
                }
            }


        } else {// 提交第三方退款查询
            if (!RefundStatusEnum.THIRDPART_REFUND_SUCCESS.equals(refundStatus)) {
                logger.info("Step 4: submit third party refund query.");
                refundStatus = refundJobService.queryRefund(refundRequest, payment, header);
                response.setRefundResult(String.valueOf(refundStatus.getCode()));

                logger.info("Step 5: update refund status, refund amount");
                refundJobService.updateRefundRequestAndPayment(refundRequest, payment, refundStatus);
            }

            if (RefundStatusEnum.THIRDPART_REFUND_SUCCESS.equals(refundStatus)) {
                logger.info("Step 6: callback trading system refund success.");
                if (refundJobService.callbackTradingSystem(refundRequest, payment, true, header)) {

                    logger.info("Step 7: update refundRequest to completed suceess.");
                    refundJobService.updateRefundRequestToCompletedSuccess(refundRequest);
                    response.setRefundResult(String.valueOf(RefundStatusEnum.COMPLETE_SUCCESS.getCode()));
                }


                logger.info("Step 8: call message bus when payment refund success.");
                sendRefundNotifyMessage(refundRequest, payment);
            }
        }

        return response;
    }

    /**
     * 发送退款通知到总线
     * 
     * @param refundRequestPo
     * @param payment
     */
    private void sendRefundNotifyMessage(RefundRequestPo refundRequestPo, Payment payment) {
        RefundSuccessNotifyReq refundSuccessNotifyReq = new RefundSuccessNotifyReq();
        refundSuccessNotifyReq.setRefundId(refundRequestPo.getRefundId());
        refundSuccessNotifyReq.setRefundAmount(refundRequestPo.getRefundAmount());
        refundSuccessNotifyReq.setPaymentId(payment.getPaymentId());
        refundSuccessNotifyReq.setInstPaymentId(payment.getInstitutionPaymentId());
        refundSuccessNotifyReq.setOrderId(refundRequestPo.getOrderId());

        Message message =
                new Message("payment", "refund_notify", String.valueOf(refundRequestPo.getRefundId()),
                        refundSuccessNotifyReq);
        try {
            messageBusClient.sendMessage(message);
        } catch (Exception e) {
            logger.error("call messagebus faild when send refundId:" + refundRequestPo.getRefundId(), e);
        }
    }

}
