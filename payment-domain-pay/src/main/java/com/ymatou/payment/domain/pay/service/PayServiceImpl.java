package com.ymatou.payment.domain.pay.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.OrderStatus;
import com.ymatou.payment.domain.pay.model.PayStatus;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.repository.BussinessOrderRepository;
import com.ymatou.payment.domain.pay.repository.PaymentRepository;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.infrastructure.db.model.BussinessorderPo;
import com.ymatou.payment.infrastructure.db.model.PaymentPo;

@Component
public class PayServiceImpl implements PayService {

    @Resource
    private PaymentRepository paymentRepository;

    @Resource
    private BussinessOrderRepository bussinessOrderRepository;


    /*
     * (non-Javadoc)
     * 
     * @see com.payment.domain.pay.service.PayService#GetByPaymentId(java.lang.String)
     */
    @Override
    public Payment getPaymentByPaymentId(String paymentId) {
        return paymentRepository.getByPaymentId(paymentId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ymatou.payment.domain.pay.service.PayService#GetPaymentByBussinessOrderId(java.lang.
     * String)
     */
    @Override
    public Payment getPaymentByBussinessOrderId(String bussinessOrderId) {
        return paymentRepository.getByBussinessOrderId(bussinessOrderId);
    }

    @Override
    public BussinessOrder getBussinessOrderByOrderId(String orderId) {
        return bussinessOrderRepository.getByOrderId(orderId);
    }

    @Override
    public Payment createPayment(AcquireOrderReq req) {
        BussinessorderPo bussinessOrderPo = buildBussinessOrder(req);
        PaymentPo paymentPo = buildPayment(req, bussinessOrderPo);

        paymentRepository.acquireOrder(paymentPo, bussinessOrderPo);

        Payment payment = Payment.convertFromPo(paymentPo);
        payment.setBussinessOrder(BussinessOrder.convertFromPo(bussinessOrderPo));

        // 将请求带入到模型中
        payment.setAcquireOrderReq(req);

        return payment;
    }

    /**
     * 构建支付单
     * 
     * @param req
     * @param bussinessOrder
     * @return
     */
    private PaymentPo buildPayment(AcquireOrderReq req, BussinessorderPo bussinessOrder) {
        PaymentPo payment = new PaymentPo();
        payment.setBankid(req.getBankId());
        payment.setBussinessorderid(bussinessOrder.getBussinessorderid());
        payment.setPaystatus(PayStatus.Init.getIndex());
        payment.setPaycurrencytype(req.getCurrency());
        payment.setPayprice(bussinessOrder.getOrderprice());
        payment.setPaytype(req.getPayType());

        return payment;
    }


    /**
     * 构建商户订单信息
     * 
     * @param req
     * @return
     */
    private BussinessorderPo buildBussinessOrder(AcquireOrderReq req) {
        BussinessorderPo bo = new BussinessorderPo();
        bo.setBussinessorderid(UUID.randomUUID().toString());
        bo.setAppid(req.getAppId());
        bo.setTraceid(req.getTraceId());
        bo.setBizcode(req.getBizCode());
        bo.setCallbackurl(req.getCallbackUrl());
        bo.setClientip(req.getUserIp());
        bo.setCodepage(req.getEncoding());
        bo.setCurrencytype(req.getCurrency());
        bo.setExt(req.getExt());
        bo.setMemo(req.getMemo());
        bo.setNotifyurl(req.getNotifyUrl());
        bo.setOrderid(req.getOrderId());
        bo.setOrderprice(new BigDecimal(req.getPayPrice()));
        bo.setOrderstatus(OrderStatus.Init.getIndex());
        bo.setOrdertime(req.getOrderTime());
        bo.setOriginappid(req.getOriginAppId());
        bo.setPaytype(req.getPayType());
        bo.setProductdesc(req.getProductDesc());
        bo.setProducturl(req.getProductUrl());
        bo.setProductname(req.getProductName());
        bo.setSignmethod(req.getSignMethod());
        bo.setThirdpartyuserid(req.getThirdPartyUserId());
        bo.setThirdpartyusertype(0); // 0-代表JAVA版，null-代表.NET版本
        bo.setUserid(req.getUserId());
        bo.setVersion(req.getVersion());

        return bo;
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.ymatou.payment.domain.pay.service.PayService#getBussinessOrderById(java.lang.String)
     */
    @Override
    public BussinessOrder getBussinessOrderById(String bussinessOrderId) {
        return bussinessOrderRepository.getBussinessOrderById(bussinessOrderId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ymatou.payment.domain.pay.service.PayService#setPaymentOrderPaid(com.ymatou.payment.
     * infrastructure.db.model.PaymentPo, java.lang.String)
     */
    @Override
    public void setPaymentOrderPaid(Payment payment, String traceId) {
        PaymentPo paymentPo = new PaymentPo();
        paymentPo.setPaymentid(payment.getPaymentid());
        paymentPo.setInstitutionpaymentid(payment.getInstitutionpaymentid());
        paymentPo.setPaystatus(payment.getPaystatus());
        paymentPo.setActualpayprice(payment.getActualpayprice());
        paymentPo.setActualpaycurrencytype(payment.getActualpaycurrencytype());
        paymentPo.setBankid(payment.getBankid());
        paymentPo.setCardtype(payment.getCardtype());
        paymentPo.setPaytime(payment.getPaytime());
        paymentPo.setLastupdatedtime(new Date());
        paymentPo.setExchangerate(payment.getExchangerate());

        paymentRepository.setPaymentOrderPaid(paymentPo, traceId);
    }

}
