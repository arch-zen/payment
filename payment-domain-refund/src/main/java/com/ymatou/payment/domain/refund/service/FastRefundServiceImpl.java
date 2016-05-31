/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.refund.model.Refund;
import com.ymatou.payment.domain.refund.repository.RefundPository;
import com.ymatou.payment.integration.common.constants.BusinessTypeEnum;
import com.ymatou.payment.integration.model.NotifyUserRequest;
import com.ymatou.payment.integration.service.ymatou.NotifyRefundService;
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
    private NotifyRefundService notifyRefundService;

    @Autowired
    private NotifyUserService notifyUserService;

    public void saveRefundRequest(Payment payment, BussinessOrder bussinessorder, Refund refundInfo) {
        
        logger.info("Save RefundRequest And Compensateprocessinfo begin");
        refundPository.addRefundrequestAndCompensateprocessinfo(payment, bussinessorder, refundInfo);
        logger.info("Save RefundRequest And Compensateprocessinfo end");
    }

    public void notifyRefund(Refund refundInfo, HashMap<String, String> header) {
        try {
            logger.info("async notify refund begin");
            notifyRefundService.doService(refundInfo.getPaymentId(), refundInfo.getTraceId(), header);
        } catch (Exception e) {
            // 异步通知，发完通知就行
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
