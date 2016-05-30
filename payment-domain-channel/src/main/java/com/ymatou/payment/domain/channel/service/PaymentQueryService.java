/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.channel.service;

import java.util.HashMap;

import com.ymatou.payment.domain.channel.model.PaymentQueryResp;

/**
 * 调用第三方的支付单查询接口
 * 
 * @author qianmin 2016年5月19日 下午4:39:43
 *
 */
public interface PaymentQueryService {

    /**
     * 调用第三方的支付单查询接口
     * 
     * @param paymentId
     * @param payType
     * @param finalCheck
     * @return
     */
    public PaymentQueryResp paymentQuery(String payType, String paymentId, HashMap<String, String> header);
}
