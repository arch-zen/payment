/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade;

import com.ymatou.payment.facade.model.PaymentNotifyRequest;

/**
 * 支付回调接口
 * 
 * @author wangxudong 2016年5月17日 下午7:29:55
 *
 */
public interface PaymentNotifyFacade {

    /**
     * 客户端回调
     * 
     * @param req
     * @return
     */
    String callback(PaymentNotifyRequest req);


    /**
     * 服务端回调
     * 
     * @param req
     * @return
     */
    String notify(PaymentNotifyRequest req);
}
