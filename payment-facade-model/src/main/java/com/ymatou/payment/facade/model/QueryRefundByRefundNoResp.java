/**
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.util.List;

import com.ymatou.payment.facade.BaseResponse;

/**
 * 根据退款申请号列表查询退款单信息
 * 
 * @author wangxudong 2017年1月13日 下午1:04:59
 *
 */
public class QueryRefundByRefundNoResp extends BaseResponse {

    /**
     * 序列化版本
     */
    private static final long serialVersionUID = 96188077481649900L;

    /**
     * 退款结果
     */
    private List<QueryRefundDetail> result;

    /**
     * @return the result
     */
    public List<QueryRefundDetail> getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(List<QueryRefundDetail> result) {
        this.result = result;
    }

}
