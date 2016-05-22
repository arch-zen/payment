/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.util.List;

import com.ymatou.payment.facade.BaseResponse;

/**
 * 根据支付单号查询订单结果
 * 
 * @author wangxudong 2016年5月22日 下午5:04:30
 *
 */
public class QueryOrderIdResp extends BaseResponse {

    /**
     * 序列化版本
     */
    private static final long serialVersionUID = -7257167982592070670L;

    /**
     * 订单Id列表
     */
    private List<String> orderIdList;

    public List<String> getOrderIdList() {
        return orderIdList;
    }

    public void setOrderIdList(List<String> orderIdList) {
        this.orderIdList = orderIdList;
    }
}
