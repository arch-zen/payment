/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import com.ymatou.payment.facade.BaseResponse;

/**
 * 退款审核response
 * 
 * @author qianmin 2016年5月13日 上午11:20:05
 * 
 */
public class ApproveRefundResponse extends BaseResponse {

    private static final long serialVersionUID = -481912820519871846L;

    /**
     * 结果
     */
    private ApproveRefundDetail details;

    public ApproveRefundDetail getDetails() {
        return details;
    }

    public void setDetails(ApproveRefundDetail details) {
        this.details = details;
    }

}
