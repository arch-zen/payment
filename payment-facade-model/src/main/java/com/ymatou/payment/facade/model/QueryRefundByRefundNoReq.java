/**
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.ymatou.payment.facade.BaseRequest;

/**
 * 根据RefundNo查询退款
 * 
 * @author wangxudong 2017年1月13日 下午1:00:46
 *
 */
public class QueryRefundByRefundNoReq extends BaseRequest {

    /**
     * 序列化版本
     */
    private static final long serialVersionUID = 5234832548469988269L;

    /**
     * 退款申请号列表
     */
    @NotEmpty(message = "退款申请号不可为空")
    private List<String> refundNoList;

    /**
     * @return the refundNoList
     */
    public List<String> getRefundNoList() {
        return refundNoList;
    }

    /**
     * @param refundNoList the refundNoList to set
     */
    public void setRefundNoList(List<String> refundNoList) {
        this.refundNoList = refundNoList;
    }

}
