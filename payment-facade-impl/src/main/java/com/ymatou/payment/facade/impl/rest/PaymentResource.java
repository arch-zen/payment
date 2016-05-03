package com.ymatou.payment.facade.impl.rest;


import javax.servlet.http.HttpServletRequest;

import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireOrderResp;

/**
 * 支付REST接口
 * @author wangxudong
 *
 */
public interface PaymentResource {
	/**
	 * 支付收单
	 * @param req
	 * @param servletRequest
	 * @return
	 */
	AcquireOrderResp acquireOrder(AcquireOrderReq req, HttpServletRequest servletRequest);
}
