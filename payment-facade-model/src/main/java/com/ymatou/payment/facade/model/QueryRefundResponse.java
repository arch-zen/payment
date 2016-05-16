/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.util.List;

import com.ymatou.payment.facade.BaseResponse;

/**
 * 查询退款单Response
 * 
 * @author qianmin 2016年5月13日 下午3:22:18
 * 
 */
public class QueryRefundResponse extends BaseResponse {

    private static final long serialVersionUID = -3449361524195611454L;

    /**
     * 总记录数
     */
    private int count;
    /**
     * 结果
     */
    private List<QueryRefundDetail> details;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<QueryRefundDetail> getDetails() {
        return details;
    }

    public void setDetails(List<QueryRefundDetail> details) {
        this.details = details;
    }
}
