package com.ymatou.payment.domain.pay.repository;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.infrastructure.db.mapper.PaymentMapper;
import com.ymatou.payment.infrastructure.db.model.PaymentPo;

/**
 * 支付单仓储
 * 
 * @author wangxudong
 *
 */
@Component
public class PaymentRepository {

	private static final Logger logger = LoggerFactory.getLogger(PaymentRepository.class);

	@Resource
	private PaymentMapper paymentMapper;

	/**
	 * 根据PaymentId获取到支付单信息
	 * @param paymentId
	 * @return
	 */
	public Payment getByPaymentId(String paymentId) {
		PaymentPo payment = paymentMapper.selectByPrimaryKey(paymentId);
		
		logger.info("come in paymentRepository.");
		
		return convert(payment);
	}
	
	/**
	 * PO转换成Model
	 * @param po
	 * @return
	 */
	public Payment convert(PaymentPo po){
		Payment payment = new Payment();
		payment.setPaymentId(po.getPaymentid());
		
		return payment;
	}

}
