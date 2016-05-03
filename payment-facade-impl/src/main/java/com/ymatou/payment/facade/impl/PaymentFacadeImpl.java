package com.ymatou.payment.facade.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.service.PayService;
import com.ymatou.payment.facade.PaymentFacade;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireOrderResp;

/**
 * 支付接口实现
 * @author wangxudong
 *
 */
@Component("paymentFacade")
public class PaymentFacadeImpl implements PaymentFacade {

	@Resource
	private PayService payService;
	
	@Override
	public AcquireOrderResp acquireOrder(AcquireOrderReq req) {
		AcquireOrderResp resp = new AcquireOrderResp();
		
		Payment payment = payService.GetByPaymentId(req.getOrderId());

		resp.setTraceId(req.getOriginAppId() + ":" + req.getOrderId() + "：" + payment.getPaymentId());
		
		return resp;
	}

}
