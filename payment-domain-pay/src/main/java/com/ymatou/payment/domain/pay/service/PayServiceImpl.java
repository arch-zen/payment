package com.ymatou.payment.domain.pay.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.pay.integration.AccountingService;
import com.ymatou.payment.domain.pay.integration.PaymentNotifyService;
import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.repository.BussinessOrderRepository;
import com.ymatou.payment.domain.pay.repository.PaymentRepository;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.constants.OrderStatusEnum;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.facade.constants.PaymentNotifyStatusEnum;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.infrastructure.db.mapper.CmbPublicKeyMapper;
import com.ymatou.payment.infrastructure.db.model.BussinessOrderPo;
import com.ymatou.payment.infrastructure.db.model.CmbPublicKeyPo;
import com.ymatou.payment.infrastructure.db.model.PaymentPo;
import com.ymatou.payment.infrastructure.util.StringUtil;

@Component
public class PayServiceImpl implements PayService {

    private static Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

    @Resource
    private PaymentRepository paymentRepository;

    @Resource
    private BussinessOrderRepository bussinessOrderRepository;

    @Resource
    private AccountingService accountingService;

    @Resource
    private PaymentNotifyService paymentNotifyService;

    @Resource
    private CmbPublicKeyMapper cmbPublicKeyMapper;

    /*
     * (non-Javadoc)
     * 
     * @see com.payment.domain.pay.service.PayService#GetByPaymentId(java.lang.String)
     */
    @Override
    public Payment getPaymentByPaymentId(String paymentId) {
        Payment payment = paymentRepository.getByPaymentId(paymentId);
        payment.setBussinessOrder(getBussinessOrderById(payment.getBussinessOrderId()));

        return payment;
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
        BussinessOrderPo bussinessOrderPo = buildBussinessOrder(req);
        PaymentPo paymentPo = buildPayment(req, bussinessOrderPo);

        paymentRepository.acquireOrder(paymentPo, bussinessOrderPo);

        Payment payment = Payment.convertFromPo(paymentPo);
        payment.setBussinessOrder(BussinessOrder.convertFromPo(bussinessOrderPo));

        return payment;
    }

    /**
     * 构建支付单
     * 
     * @param req
     * @param bussinessOrder
     * @return
     */
    private PaymentPo buildPayment(AcquireOrderReq req, BussinessOrderPo bussinessOrder) {
        PaymentPo payment = new PaymentPo();
        payment.setBankId(req.getBankId());
        payment.setBussinessOrderId(bussinessOrder.getBussinessOrderId());
        payment.setPayStatus(PayStatusEnum.Init.getIndex());
        payment.setPayCurrencyType(req.getCurrency());
        payment.setPayPrice(bussinessOrder.getOrderPrice());
        payment.setPayType(req.getPayType());

        return payment;
    }


    /**
     * 构建商户订单信息
     * 
     * @param req
     * @return
     */
    private BussinessOrderPo buildBussinessOrder(AcquireOrderReq req) {
        BussinessOrderPo bo = new BussinessOrderPo();
        bo.setBussinessOrderId(UUID.randomUUID().toString());
        bo.setAppId(req.getAppId());
        bo.setTraceId(req.getTraceId());
        bo.setBizCode(req.getBizCode());
        bo.setCallbackUrl(req.getCallbackUrl());
        bo.setClientIp(req.getUserIp());
        bo.setCodePage(req.getEncoding());
        bo.setCurrencyType(req.getCurrency());
        bo.setExt(req.getExt());
        bo.setMemo(StringUtil.TrimMax(req.getMemo(), 512));
        bo.setNotifyUrl(req.getNotifyUrl());
        bo.setOrderId(req.getOrderId());
        bo.setOrderPrice(new BigDecimal(req.getPayPrice()));
        bo.setOrderStatus(OrderStatusEnum.Init.getIndex());
        bo.setOrderTime(req.getOrderTime());
        bo.setOriginAppId(req.getOriginAppId());
        bo.setPayType(req.getPayType());
        bo.setProductDesc(req.getProductDesc());
        bo.setProductUrl(req.getProductUrl());
        bo.setProductName(req.getProductName());
        bo.setSignMethod(req.getSignMethod());
        bo.setThirdPartyUserId(req.getThirdPartyUserId());
        bo.setThirdPartyUserType(0); // 0-代表JAVA版，null-代表.NET版本
        bo.setUserId(req.getUserId().intValue());
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
        paymentPo.setBussinessOrderId(payment.getBussinessOrderId());
        paymentPo.setPaymentId(payment.getPaymentId());
        paymentPo.setInstitutionPaymentId(payment.getInstitutionPaymentId());
        paymentPo.setPayStatus(payment.getPayStatus().getIndex());
        paymentPo.setActualPayPrice(payment.getActualPayPrice().getAmount());
        paymentPo.setActualPayCurrencyType(payment.getActualPayCurrencyType());
        paymentPo.setBankId(payment.getBankId());
        paymentPo.setCardType(payment.getCardType());
        paymentPo.setPayTime(payment.getPayTime());
        paymentPo.setLastUpdatedTime(new Date());
        paymentPo.setExchangeRate(payment.getExchangeRate());
        paymentPo.setCheckStatus(payment.getCheckStatus());
        paymentPo.setPayerId(payment.getPayerId());
        paymentPo.setPayerEmail(payment.getPayerEmail());
        paymentPo.setDiscountAmt(payment.getDiscountAmt().getAmount());

        paymentRepository.setPaymentOrderPaid(paymentPo, traceId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ymatou.payment.domain.pay.service.PayService#executePayNotify(com.ymatou.payment.domain.
     * pay.model.Payment)
     */
    @Override
    public void executePayNotify(Payment payment, HashMap<String, String> mockHeader) {
        if (payment.getNotifyStatus() == PaymentNotifyStatusEnum.NOTIFIED) {
            return;
        }

        BussinessOrder bussinessOrder = payment.getBussinessOrder();
        if (bussinessOrder == null) {
            logger.error("execute pay notify found payment[{}] has not bussiness order", payment.getPaymentId());
            throw new BizException(ErrorCode.NOT_EXIST_BUSINESS_ORDER_ID, "paymentId:" + payment.getPaymentId());
        }

        paymentRepository.increaseRetryCount(payment);

        // 执行充值操作
        if (payment.getNotifyStatus() == null) {
            logger.info("step.1 pay notify execute accounting");

            if (!accountingService.recharge(payment, bussinessOrder)) {
                logger.error("pay notify execute accounting failed:" + payment.getPaymentId());
                throw new BizException(ErrorCode.PAYMENT_NOTIFY_ACCOUNTING_FAILED,
                        "pay notify execute accounting failed:" + payment.getPaymentId());
            }
            payment.setNotifyStatus(PaymentNotifyStatusEnum.ACCOUNTED);
            paymentRepository.updatePaymentNotifyStatus(payment);
        }

        // 执行通知业务系统的操作
        if (payment.getNotifyStatus() == PaymentNotifyStatusEnum.ACCOUNTED) {
            logger.info("step.2 pay notify call inner system");


            // 如果NotifyUrl为空表示业务系统不需要回调，此时可以直接把状态改成NOTIFIED
            if (StringUtils.isEmpty(bussinessOrder.getNotifyUrl())) {
                logger.info("execute pay notity succssess when notify url is empty with paymentid:{}",
                        payment.getPaymentId());
            } else {
                if (!paymentNotifyService.notifyBizSystem(payment, bussinessOrder, mockHeader)) {
                    logger.error("pay notify call inner system failed:" + payment.getPaymentId());
                    throw new BizException(ErrorCode.PAYMENT_NOTIFY_INNER_SYSTEM_FAILED,
                            "pay notify call inner system failed:" + payment.getPaymentId());
                }
            }
            payment.setNotifyStatus(PaymentNotifyStatusEnum.NOTIFIED);
            paymentRepository.updatePaymentNotifyStatus(payment);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ymatou.payment.domain.pay.service.PayService#syncCmbPublicKey()
     */
    @Override
    public void saveCmbPublicKey(String publicKey) {
        CmbPublicKeyPo cmbPublicKeyPo = new CmbPublicKeyPo();
        cmbPublicKeyPo.setPublicKey(publicKey);
        cmbPublicKeyMapper.insertSelective(cmbPublicKeyPo);
    }

}
