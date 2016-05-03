package com.ymatou.payment.domain.pay.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.repository.PaymentRepository;
import com.ymatou.payment.infrastructure.db.mapper.PaymentMapper;

@Component
public class PayServiceImpl implements PayService {

	@Resource
	private PaymentMapper paymentMapper;
	
	@Resource
	private PaymentRepository paymentRepository;
	
	
	/* (non-Javadoc)
	 * @see com.payment.domain.pay.service.PayService#GetByPaymentId(java.lang.String)
	 */
	@Override
	public Payment GetByPaymentId(String paymentId) {
		return paymentRepository.getByPaymentId(paymentId);
	}

}
