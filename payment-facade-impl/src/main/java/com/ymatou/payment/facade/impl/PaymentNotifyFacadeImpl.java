/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.model.PaymentNotifyMessage;
import com.ymatou.payment.domain.channel.service.PaymentNotifyService;
import com.ymatou.payment.domain.channel.service.paymentnotify.PaymentNotifyMessageResolverFactory;
import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.service.PayService;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.PaymentNotifyFacade;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.constants.PaymentNotifyType;
import com.ymatou.payment.facade.model.PaymentNotifyReq;
import com.ymatou.payment.facade.model.PaymentNotifyResp;
import com.ymatou.payment.infrastructure.Money;
import com.ymatou.payment.infrastructure.db.mapper.AlipayNotifyLogMapper;
import com.ymatou.payment.infrastructure.db.model.AlipayNotifyLogPo;
import com.ymatou.payment.integration.service.ymatou.NotifyPaymentService;

/**
 * 支付回调接口
 * 
 * @author wangxudong 2016年5月17日 下午8:19:16
 *
 */
@Component("paymentNotifyFacade")
public class PaymentNotifyFacadeImpl implements PaymentNotifyFacade {

    private static Logger logger = LoggerFactory.getLogger(PaymentNotifyFacadeImpl.class);

    @Resource
    PaymentNotifyMessageResolverFactory notityMessageResolverFactory;

    @Resource
    InstitutionConfigManager institutionConfigManager;

    @Resource
    AlipayNotifyLogMapper alipayNotifyLogMapper;

    @Resource
    PayService payService;

    @Resource
    NotifyPaymentService notifyPaymentService;

    /*
     * (non-Javadoc)
     * 
     * @see com.ymatou.payment.facade.PaymentNotifyFacade#notify(com.ymatou.payment.facade.model.
     * PaymentNotifyRequest)
     */
    @Override
    public PaymentNotifyResp notify(PaymentNotifyReq req) {
        // STEP.1 获取到第三方机构配置
        InstitutionConfig instConfig = institutionConfigManager.getConfig(PayTypeEnum.parse(req.getPayType()));

        // STEP.2 解析并验证报文
        PaymentNotifyService notifyService = notityMessageResolverFactory.getInstance(req.getPayType());
        PaymentNotifyMessage notifyMessage = notifyService.resloveNotifyMessage(req);

        // STEP.3 如果支付成功
        Payment payment = null;
        PaymentNotifyResp response = new PaymentNotifyResp();
        if (notifyMessage.getPayStatus() == PayStatusEnum.Paied) {
            // 添加日志
            AlipayNotifyLogPo notifyLog = new AlipayNotifyLogPo();
            notifyLog.setBizNo(notifyMessage.getPaymentId());
            notifyLog.setsParameters(req.getRawString());
            alipayNotifyLogMapper.insertSelective(notifyLog);

            // 校验支付单Id
            payment = payService.getPaymentByPaymentId(notifyMessage.getPaymentId());
            if (payment == null)
                throw new BizException(ErrorCode.NOT_EXIST_PAYMENT_ID,
                        String.format("can not find paymentid %s", notifyMessage.getPaymentId()));

            // 校验商户订单
            BussinessOrder bussinessOrder = payService.getBussinessOrderById(payment.getBussinessOrderId());
            if (bussinessOrder == null)
                throw new BizException(ErrorCode.NOT_EXIST_BUSINESS_ORDER_ID,
                        String.format("can not find order %s", payment.getBussinessOrderId()));
            payment.setBussinessOrder(bussinessOrder);


            // 如果支付单的状态 已经成功则直接返回成功
            if (payment.getPayStatus() == PayStatusEnum.Paied) {
                response.setResult(notifyService.buildResponse(notifyMessage, payment, req.getNotifyType()));
                response.setSuccess(true);
                return response;
            }

            // 如果是服务端回调
            if (req.getNotifyType() == PaymentNotifyType.Server) {

                // 验证实际支付金额和支付金额是否一致
                if (!payment.getPayPrice().equals(new Money(notifyMessage.getActualPayPrice()))) {
                    logger.error("inconsistent payPrice and actualPayPrice. PaymentId: {}", payment.getPaymentId());
                    throw new BizException(ErrorCode.INCONSISTENT_PAYPRICE_AND_ACTUALPAYPRICE,
                            "paymentid: " + payment.getPaymentId());
                }

                // 更改订单状态
                setPaymentOrderPaid(payment, notifyMessage);

                // 通知发货服务
                try {

                    payService.executePayNotify(payment, req.getMockHeader());

                    // notifyPaymentService.doService(payment.getPaymentId(),
                    // notifyMessage.getTraceId(),
                    // req.getMockHeader());
                } catch (Exception e) {
                    logger.error("executePayNotify service failed with paymentid :" + payment.getPaymentId(), e);
                }
            }
        }

        // 构造返回报文
        response.setResult(notifyService.buildResponse(notifyMessage, payment, req.getNotifyType()));
        if (response.getErrorCode() == 0) {
            response.setSuccess(true);
        } else {
            response.setSuccess(false);
        }
        return response;
    }

    /**
     * 设置支付单已经支付
     * 
     * @param payment
     * @param notifyMessage
     */
    private void setPaymentOrderPaid(Payment payment, PaymentNotifyMessage notifyMessage) {
        payment.setInstitutionPaymentId(notifyMessage.getInstitutionPaymentId());
        payment.setPayStatus(PayStatusEnum.Paied);
        payment.setActualPayPrice(new Money(notifyMessage.getActualPayPrice()));
        payment.setActualPayCurrencyType(notifyMessage.getActualPayCurrency());
        payment.setBankId(notifyMessage.getBankId());
        payment.setCardType(notifyMessage.getCardType());
        payment.setPayTime(notifyMessage.getPayTime());
        payment.setPayerId(notifyMessage.getPayerId());
        payment.setExchangeRate(1.0); // 没有接支付宝国际，默认汇率为1
        payService.setPaymentOrderPaid(payment, notifyMessage.getTraceId());
    }
}
