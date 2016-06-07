/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import java.util.HashMap;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;

/**
 * 账户相关操作
 * 
 * @author qianmin 2016年6月6日 下午7:10:14
 *
 */
public interface AccountOperateService {

    /**
     * 扣除码头账户余额
     * 
     * @param payment
     * @param bussinessOrder
     * @param header
     * @return
     */
    public boolean deductBalance(Payment payment, BussinessOrder bussinessOrder, HashMap<String, String> header);
}
