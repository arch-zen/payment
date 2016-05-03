package com.ymatou.payment.domain.pay.model;

import com.ymatou.payment.facade.PrintFriendliness;

/**
 * 支付单
 * @author wangxudong
 *
 */
public class Payment extends PrintFriendliness {

	/**
	 * 序列化版本号
	 */
	private static final long serialVersionUID = 2524661669277166299L;

	/**
	 * 支付单号
	 */
	private String paymentId;

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	
}
