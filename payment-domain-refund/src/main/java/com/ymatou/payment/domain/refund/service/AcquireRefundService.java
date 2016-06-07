/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import java.util.List;

import com.ymatou.payment.facade.model.AcquireRefundPlusRequest;
import com.ymatou.payment.facade.model.AcquireRefundPlusRequest.TradeDetail;
import com.ymatou.payment.facade.model.AcquireRefundPlusResponse.RefundDetail;
import com.ymatou.payment.facade.model.TradeRefundDetail;

/**
 * 提交退款(支持部分退款)
 * 
 * @author qianmin 2016年6月3日 下午6:02:26
 *
 */
public interface AcquireRefundService {

    /**
     * 获取可以退款的交易
     * 
     * @param traderDetails
     * @return
     */
    public List<TradeRefundDetail> generateTradeRefundDetailList(List<TradeDetail> tradeDetails);

    /**
     * 检查RefundRequest是否存在， 若不存在则插入
     * 
     * @param tradeRefundDetail
     * @param refund
     */
    public List<RefundDetail> checkAndSaveRefundRequest(List<TradeRefundDetail> refundableTrades,
            AcquireRefundPlusRequest req);
}
