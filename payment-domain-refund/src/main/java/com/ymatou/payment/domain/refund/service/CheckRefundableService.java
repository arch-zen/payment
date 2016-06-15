/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import java.util.List;

import com.ymatou.payment.facade.model.TradeRefundDetail;
import com.ymatou.payment.facade.model.TradeRefundableRequest.TradeDetail;

/**
 * 查看是否可以退款
 * 
 * @author qianmin 2016年5月13日 下午2:59:08
 * 
 */
public interface CheckRefundableService {

    /**
     * 获取是否可退款交易的信息
     * 
     * @param tradeDetails
     * @return
     */
    public List<TradeRefundDetail> generateTradeRefundDetailList(List<TradeDetail> tradeDetails);

    /**
     * 剔除不可退款的交易信息
     * 
     * @param tradeDetails
     * @return
     */
    public List<TradeRefundDetail> removeNonRefundable(List<TradeRefundDetail> tradeRefundDetails);
}
