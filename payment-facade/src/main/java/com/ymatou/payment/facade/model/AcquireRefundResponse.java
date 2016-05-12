/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.util.List;

import com.ymatou.payment.facade.BaseResponse;

/**
 * SubmitRefund应答
 * 
 * @author qianmin 2016年5月12日 上午11:26:18
 * 
 */
public class AcquireRefundResponse extends BaseResponse {

    private static final long serialVersionUID = -166635294703070369L;

    private List<AcquireRefundDetail> details;

    public List<AcquireRefundDetail> getDetails() {
        return details;
    }

    public void setDetails(List<AcquireRefundDetail> details) {
        this.details = details;
    }
}
