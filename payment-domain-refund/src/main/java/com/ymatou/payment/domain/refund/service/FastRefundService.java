/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import java.math.BigDecimal;
import java.util.HashMap;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.refund.model.Refund;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;

/**
 * 快速退款
 * 
 * @author qianmin 2016年5月13日 上午10:59:08
 * 
 */
public interface FastRefundService {

    /**
     * 退款申请，发货信息落地
     * 
     * @param isJavaSystem
     * @param refundAmt
     * @param payment
     * @param bussinessorder
     * @param refundInfo
     * @return
     */
    public RefundRequestPo saveRefundRequest(boolean isJavaSystem, BigDecimal refundAmt, Payment payment,
            BussinessOrder bussinessorder, Refund refundInfo);

    /**
     * 发送交易信息给用户
     * 
     * @param userId
     * @param orderId
     * @param header
     * @return
     */
    public void sendFastRefundTradingMessage(String userId, String orderId, HashMap<String, String> header);
}
