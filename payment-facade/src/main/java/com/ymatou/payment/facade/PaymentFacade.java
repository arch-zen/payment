package com.ymatou.payment.facade;

import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireOrderResp;

/**
 * 支付接口
 * @author wangxudong
 *
 */
public interface PaymentFacade {
	
	/**
	 * 支付收单
	 * @param req
	 * @return
	 */
	AcquireOrderResp acquireOrder(AcquireOrderReq req);
}
