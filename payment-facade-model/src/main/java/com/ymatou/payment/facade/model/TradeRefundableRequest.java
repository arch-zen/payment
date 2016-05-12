/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.util.List;

import com.ymatou.payment.facade.BaseRequest;

/**
 * 检查是否可以退款request
 * 
 * @author qianmin 2016年5月11日 下午3:30:03
 * 
 */
public class TradeRefundableRequest extends BaseRequest {

    private static final long serialVersionUID = -545822325664485996L;

    private List<String> tradeNos;

    public List<String> getTradeNos() {
        return tradeNos;
    }

    public void setTradeNos(List<String> tradeNos) {
        this.tradeNos = tradeNos;
    }
}
