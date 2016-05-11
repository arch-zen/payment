package com.ymatou.payment.facade.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.service.PayService;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.PaymentFacade;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireOrderResp;

/**
 * 支付接口实现
 * 
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

        BussinessOrder bussinessOrder = payService.GetBussinessOrderByOrderId(req.getOrderId());
        if (bussinessOrder != null)
            throw new BizException(ErrorCode.DB_ERROR, "OrderId已经创建过支付单");

        Payment payment = payService.CreatePayment(req);

        resp.setTraceId(req.getTraceId());

        return resp;
    }

}
