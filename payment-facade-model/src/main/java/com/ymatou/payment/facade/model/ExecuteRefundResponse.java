/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import com.ymatou.payment.facade.BaseResponse;

/**
 * 
 * @author qianmin 2016年6月29日 下午2:22:53
 *
 */
public class ExecuteRefundResponse extends BaseResponse {

    private static final long serialVersionUID = 3753674889862363798L;

    public Integer refundResult;

    public Integer getRefundResult() {
        return refundResult;
    }

    public void setRefundResult(Integer refundResult) {
        this.refundResult = refundResult;
    }
}
