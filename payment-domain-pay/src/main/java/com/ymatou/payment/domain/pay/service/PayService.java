package com.ymatou.payment.domain.pay.service;

import java.util.HashMap;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.infrastructure.db.model.PaymentPo;

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
    Payment getPaymentByPaymentId(String paymentId);

    /**
     * 根据BussinessOrderId获取到支付单信息
     * 
     * @param bussinessOrderId
     * @return
     */
    Payment getPaymentByBussinessOrderId(String bussinessOrderId);

    /**
     * 根据OrderId获取到商户订单信息
     * 
     * @param orderId
     * @return
     */
    BussinessOrder getBussinessOrderByOrderId(String orderId);

    /**
     * 根据收单请求创建Payment和BussinessOrder
     * 
     * @param req
     * @return
     */
    Payment createPayment(AcquireOrderReq req);

    /**
     * 根据bussinessOrderId获取BussinessOrder
     * 
     * @param bussinessOrderId
     * @return
     */
    BussinessOrder getBussinessOrderById(String bussinessOrderId);

    /**
     * 设置支付单为已支付
     * 
     * @param paymentPo
     * @param traceId
     */
    void setPaymentOrderPaid(Payment payment, String traceId);

    /**
     * 执行支付通知
     * 
     * @param payment
     */
    void executePayNotify(Payment payment, HashMap<String, String> mockHeader);
}
