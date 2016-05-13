/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import java.util.HashMap;
import java.util.List;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.refund.model.Refund;
import com.ymatou.payment.facade.model.AcquireRefundDetail;
import com.ymatou.payment.facade.model.AcquireRefundRequest;
import com.ymatou.payment.facade.model.TradeDetail;
import com.ymatou.payment.facade.model.TradeRefundDetail;

/**
 * 退款服务
 * 
 * @author qianmin 2016年5月11日 上午11:44:12
 *
 */
public interface RefundService {

    /**
     * 退款申请，发货信息落地
     * 
     * @param payment
     * @param bussinessorder
     * @param refundInfo
     */
    public void saveRefundRequest(Payment payment, BussinessOrder bussinessorder, Refund refundInfo);

    /**
     * 通知退款
     * 
     * @param refundInfo
     * @param header
     * @return
     */
    public void notifyRefund(Refund refundInfo, HashMap<String, String> header);

    /**
     * 发送交易信息给用户
     * 
     * @param userId
     * @param orderId
     * @param header
     * @return
     */
    public void sendFastRefundTradingMessage(String userId, String orderId, HashMap<String, String> header);

    /**
     * 获取可以退款的交易
     * 
     * @param traderDetails
     * @return
     */
    public List<TradeRefundDetail> generateTradeRefundDetailList(List<TradeDetail> traderDetails);

    /**
     * 检查RefundRequest是否存在， 若不存在则插入
     * 
     * @param tradeRefundDetail
     * @param refund
     */
    public List<AcquireRefundDetail> checkAndSaveRefundRequest(List<TradeRefundDetail> tradeRefundDetail,
            AcquireRefundRequest req);
}
