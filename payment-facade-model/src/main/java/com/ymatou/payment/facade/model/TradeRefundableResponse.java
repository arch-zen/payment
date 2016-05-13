/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.util.List;

import com.ymatou.payment.facade.BaseResponse;

/**
 * 是否可以退款response
 * 
 * @author qianmin 2016年5月12日 下午2:52:45
 * 
 */
public class TradeRefundableResponse extends BaseResponse {

    private static final long serialVersionUID = 221568335446806357L;

    private List<TradeRefundDetail> details;

    public List<TradeRefundDetail> getDetails() {
        return details;
    }

    public void setDetails(List<TradeRefundDetail> details) {
        this.details = details;
    }
}
