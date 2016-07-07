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

    public String refundResult;

    public String getRefundResult() {
        return refundResult;
    }

    public void setRefundResult(String refundResult) {
        this.refundResult = refundResult;
    }
}
