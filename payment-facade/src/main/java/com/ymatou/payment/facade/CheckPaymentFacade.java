/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade;

import com.ymatou.payment.facade.model.CheckPaymentRequset;

/**
 * 对账接口
 * 
 * @author qianmin 2016年5月19日 下午3:42:46
 *
 */
public interface CheckPaymentFacade {

    /**
     * 支付对账
     * 
     * @param req
     * @return
     */
    public BaseResponse checkPayment(CheckPaymentRequset req);
}
