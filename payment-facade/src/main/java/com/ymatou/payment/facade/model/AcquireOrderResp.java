package com.ymatou.payment.facade.model;

import com.ymatou.payment.facade.BaseResponse;

public class AcquireOrderResp extends BaseResponse {
	
	/**
	 * 序列化版本
	 */
	private static final long serialVersionUID = -8592961206186919734L;
	/**
	 * 跟踪Id
	 */
	public String traceId;

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}


}
