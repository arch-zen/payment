/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.pay.service;

import java.util.HashMap;

import com.ymatou.payment.domain.pay.model.ThirdPartyPayment;

/**
 * 对账服务
 * 
 * @author qianmin 2016年5月20日 下午2:48:28
 *
 */
public interface PaymentCheckService {

    /**
     * 对账
     */
    public void doCheck(ThirdPartyPayment thirdPartyPayment, String paymentId, boolean finlCheck,
            HashMap<String, String> header);
}
