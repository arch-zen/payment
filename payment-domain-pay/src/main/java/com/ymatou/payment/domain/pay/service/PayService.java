package com.ymatou.payment.domain.pay.service;

import com.ymatou.payment.domain.pay.model.Payment;

/**
 * 支付服务接口
 * @author wangxudong
 *
 */
public interface PayService {
	
	/**
	 * 根据PaymentId获取到支付单信息
	 * @param paymentId
	 * @return
	 */
	Payment GetByPaymentId(String paymentId);
}
