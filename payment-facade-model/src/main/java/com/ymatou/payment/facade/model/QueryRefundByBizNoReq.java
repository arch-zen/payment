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
 * 根据BizNo查询退款
 * 
 * @author wangxudong 2017年1月13日 下午1:00:46
 *
 */
public class QueryRefundByBizNoReq extends BaseRequest {

    /**
     * 序列化版本
     */
    private static final long serialVersionUID = 5234832548469988269L;

    /**
     * 退款申请号列表
     */
    @NotEmpty(message = "退款凭据号不可为空")
    private List<String> bizNoList;

    public List<String> getBizNoList() {
        return bizNoList;
    }

    public void setBizNoList(List<String> bizNoList) {
        this.bizNoList = bizNoList;
    }
}
