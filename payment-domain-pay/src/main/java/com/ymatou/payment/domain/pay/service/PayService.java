package com.ymatou.payment.domain.pay.service;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.model.AcquireOrderReq;

/**
 * 支付服务接口
 * 
 * @author wangxudong
 *
 */
public interface PayService {

    /**
     * 根据PaymentId获取到支付单信息
     * 
     * @param paymentId
     * @return
     */
    Payment GetPaymentByPaymentId(String paymentId);

    /**
     * 根据BussinessOrderId获取到支付单信息
     * 
     * @param bussinessOrderId
     * @return
     */
    Payment GetPaymentByBussinessOrderId(String bussinessOrderId);

    /**
     * 根据OrderId获取到商户订单信息
     * 
     * @param orderId
     * @return
     */
    BussinessOrder GetBussinessOrderByOrderId(String orderId);

    /**
     * 根据收单请求创建Payment和BussinessOrder
     * 
     * @param req
     * @return
     */
    Payment CreatePayment(AcquireOrderReq req);
}
