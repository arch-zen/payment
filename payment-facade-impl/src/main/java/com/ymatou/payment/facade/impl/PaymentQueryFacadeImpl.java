/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.pay.service.PayService;
import com.ymatou.payment.domain.pay.service.PaymentCheckServiceImpl;
import com.ymatou.payment.facade.PaymentQueryFacade;
import com.ymatou.payment.facade.model.QueryOrderIdResp;

/**
 * 支付查询实现
 * 
 * @author wangxudong 2016年5月22日 下午5:11:26
 *
 */
@Component("paymentQueryFacade")
public class PaymentQueryFacadeImpl implements PaymentQueryFacade {

    private static final Logger logger = LoggerFactory.getLogger(PaymentCheckServiceImpl.class);

    @Resource
    private PayService payService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ymatou.payment.facade.PaymentQueryFacade#queryOrderIdListByPaymentId(java.lang.String)
     */
    @Override
    public QueryOrderIdResp queryOrderIdListByPaymentId(String paymentId) {
        return null;
    }

}
