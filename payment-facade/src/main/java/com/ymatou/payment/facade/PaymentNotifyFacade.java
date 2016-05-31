/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade;

import com.ymatou.payment.facade.model.PaymentNotifyReq;
import com.ymatou.payment.facade.model.PaymentNotifyResp;

/**
 * 支付回调接口
 * 
 * @author wangxudong 2016年5月17日 下午7:29:55
 *
 */
public interface PaymentNotifyFacade {

    /**
     * 支付回调接口
     * 
     * @param req
     * @return
     */
    PaymentNotifyResp notify(PaymentNotifyReq req);
}
