/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade;

import com.ymatou.payment.facade.model.QueryOrderIdResp;

/**
 * 支付查询接口
 * 
 * @author wangxudong 2016年5月22日 下午4:51:51
 *
 */
public interface PaymentQueryFacade {

    /**
     * 根据支付单Id查询订单Id列表
     * 
     * @param paymentId
     * @return
     */
    QueryOrderIdResp queryOrderIdListByPaymentId(String paymentId);
}
