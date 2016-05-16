/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import java.util.List;

import com.ymatou.payment.facade.model.AcquireRefundDetail;
import com.ymatou.payment.facade.model.AcquireRefundRequest;
import com.ymatou.payment.facade.model.TradeRefundDetail;

/**
 * 退款收单
 * 
 * @author qianmin 2016年5月13日 上午11:01:37
 * 
 */
public interface SubmitRefundService {

    /**
     * 获取可以退款的交易
     * 
     * @param traderDetails
     * @return
     */
    public List<TradeRefundDetail> generateTradeRefundDetailList(List<String> tradeNos);

    /**
     * 检查RefundRequest是否存在， 若不存在则插入
     * 
     * @param tradeRefundDetail
     * @param refund
     */
    public List<AcquireRefundDetail> checkAndSaveRefundRequest(List<TradeRefundDetail> tradeRefundDetail,
            AcquireRefundRequest req);
}
