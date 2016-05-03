package com.ymatou.payment.facade.model;

import com.ymatou.payment.facade.BaseRequest;

/**
 * 收单请求消息
 * @author wangxudong
 *
 */
public class AcquireOrderReq extends BaseRequest {
	
	/**
	 * 请求版本号
	 */
	private static final long serialVersionUID = -3983050225255199358L;
	
	/**
	 * 原始AppId
	 */
	private String originAppId;
	
	/**
	 * 商户内部订单号（交易号）
	 */
	private String orderId;

	public String getOriginAppId() {
		return originAppId;
	}

	public void setOriginAppId(String originAppId) {
		this.originAppId = originAppId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
}
