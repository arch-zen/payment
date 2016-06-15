/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import java.util.HashMap;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;

/**
 * 退款定时任务相关服务
 * 
 * @author qianmin 2016年6月8日 上午10:48:01
 *
 */
public interface RefundJobService {

    /**
     * 根据退款单号获取退款申请单
     * 
     * @param refundNo
     * @return
     */
    public RefundRequestPo getRefundRequestById(String refundNo);

    /**
     * 扣除码头账户余额
     * 
     * @param refundRequest
     */
    public boolean dedcutBalance(Payment payment, BussinessOrder bussinessOrder, RefundRequestPo refundRequest,
            HashMap<String, String> header);

    /**
     * 提交第三方退款
     * 
     * @param refundRequest
     */
    public void submitRefund(RefundRequestPo refundRequest, Payment payment, HashMap<String, String> header);

    /**
     * 查询第三方退款并做处理
     * 
     * @param refundRequest
     * @return RefundStatusEnum
     */
    public RefundStatusEnum queryRefund(RefundRequestPo refundRequest, Payment payment, HashMap<String, String> header);

    /**
     * 更新退款申请金额， 完成金额，重试次数及退款状态
     * 
     * @param refundRequest
     * @param payment
     * @param refundStatus
     */
    public void updateRefundRequestAndPayment(RefundRequestPo refundRequest, Payment payment,
            RefundStatusEnum refundStatus);

    /**
     * 通知交易系统
     * 
     * @param refundRequest
     * @param payment
     */
    public boolean callbackTradingSystem(RefundRequestPo refundRequest, Payment payment,
            HashMap<String, String> header);

    /**
     * 更新退款状态为完全完成
     * 
     * @param refundRequest
     */
    public void updateRefundRequestToCompletedSuccess(RefundRequestPo refundRequest);
}
