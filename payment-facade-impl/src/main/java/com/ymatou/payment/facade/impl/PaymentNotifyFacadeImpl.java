/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.impl;

import java.sql.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.model.PaymentNotifyMessage;
import com.ymatou.payment.domain.channel.service.PaymentNotifyService;
import com.ymatou.payment.domain.channel.service.paymentnotify.PaymentNotifyMessageResolverFactory;
import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.PayStatus;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.service.PayService;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.PaymentNotifyFacade;
import com.ymatou.payment.facade.model.PaymentNotifyRequest;
import com.ymatou.payment.facade.model.PaymentNotifyType;
import com.ymatou.payment.infrastructure.db.mapper.AlipaynotifylogMapper;
import com.ymatou.payment.infrastructure.db.model.AlipaynotifylogPo;

/**
 * 支付回调接口
 * 
 * @author wangxudong 2016年5月17日 下午8:19:16
 *
 */
@Component("paymentNotifyFacade")
public class PaymentNotifyFacadeImpl implements PaymentNotifyFacade {

    @Resource
    PaymentNotifyMessageResolverFactory notityMessageResolverFactory;

    @Resource
    InstitutionConfigManager institutionConfigManager;

    @Resource
    AlipaynotifylogMapper alipaynotifylogMapper;

    @Resource
    PayService payService;

    /*
     * (non-Javadoc)
     * 
     * @see com.ymatou.payment.facade.PaymentNotifyFacade#notify(com.ymatou.payment.facade.model.
     * PaymentNotifyRequest)
     */
    @Override
    public String notify(PaymentNotifyRequest req) {
        // STEP.1 获取到第三方机构配置
        InstitutionConfig instConfig = institutionConfigManager.getConfig(req.getPayType());

        // STEP.2 解析并验证报文
        PaymentNotifyService notifyService = notityMessageResolverFactory.getInstance(req.getPayType());
        PaymentNotifyMessage notifyMessage = notifyService.resloveNotifyMessage(req);

        // STEP.3 如果支付成功
        if (notifyMessage.getPayStatus() == PayStatus.Paied) {
            // 添加日志
            AlipaynotifylogPo notifyLog = new AlipaynotifylogPo();
            notifyLog.setBizno(notifyMessage.getPaymentId());
            notifyLog.setSparameters(req.getRawString());
            alipaynotifylogMapper.insertSelective(notifyLog);

            // 校验支付单Id
            Payment payment = payService.getPaymentByPaymentId(notifyMessage.getPaymentId());
            if (payment == null)
                throw new BizException(ErrorCode.DATA_NOT_FOUND,
                        String.format("can not find paymentid %s", notifyMessage.getPayerId()));

            // 校验商户订单
            BussinessOrder bussinessOrder = payService.getBussinessOrderById(payment.getBussinessorderid());
            if (bussinessOrder == null)
                throw new BizException(ErrorCode.DATA_NOT_FOUND,
                        String.format("can not find order %s", payment.getBussinessorderid()));

            // 更改订单状态
            if (req.getNotifyType() == PaymentNotifyType.Server) {
                setPaymentOrderPaid(payment, notifyMessage);
            }
        }

        return notifyService.buildInstNeedResponse(notifyMessage);
    }

    /**
     * 设置支付单已经支付
     * 
     * @param payment
     * @param notifyMessage
     */
    private void setPaymentOrderPaid(Payment payment, PaymentNotifyMessage notifyMessage) {
        payment.setInstitutionpaymentid(notifyMessage.getInstitutionPaymentId());
        payment.setPaystatus(PayStatus.Paied.getIndex());
        payment.setActualpayprice(notifyMessage.getActualPayPrice());
        payment.setActualpaycurrencytype(notifyMessage.getActualPayCurrency());
        payment.setBankid(notifyMessage.getBankId());
        payment.setCardtype(notifyMessage.getCardType());
        payment.setPaytime(notifyMessage.getPayTime());
        payment.setPayerid(notifyMessage.getPayerId());
        payService.setPaymentOrderPaid(payment, notifyMessage.getTraceId());
    }
}
