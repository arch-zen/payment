package com.ymatou.payment.facade.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.model.AcquireOrderPackageResp;
import com.ymatou.payment.domain.channel.service.acquireorder.AcquireOrderPackageFactory;
import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.service.PayService;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.PaymentFacade;
import com.ymatou.payment.facade.constants.BizCodeEnum;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.constants.PaymentNotifyStatusEnum;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireOrderResp;
import com.ymatou.payment.facade.model.ExecutePayNotifyReq;
import com.ymatou.payment.facade.model.ExecutePayNotifyResp;
import com.ymatou.payment.facade.rest.PaymentResourceImpl;

/**
 * 支付接口实现
 * 
 * @author wangxudong
 *
 */
@Component("paymentFacade")
public class PaymentFacadeImpl implements PaymentFacade {

    private static Logger logger = LoggerFactory.getLogger(PaymentFacadeImpl.class);

    @Resource
    private PayService payService;

    @Resource
    private AcquireOrderPackageFactory acquireOrderPackageFactory;

    @Resource
    private InstitutionConfigManager instConfigManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.ymatou.payment.facade.PaymentFacade#acquireOrder(com.ymatou.payment.facade.model.
     * AcquireOrderReq)
     */
    @Override
    public AcquireOrderResp acquireOrder(AcquireOrderReq req) {

        // 校验请求参数
        validateReqParam(req);

        // 创建支付单
        Payment payment = payService.createPayment(req);

        // 拼装支付报文
        AcquireOrderPackageResp packageResp =
                acquireOrderPackageFactory.getInstance(payment.getPayType()).acquireOrderPackage(payment,
                        req.getMockHeader());

        // 返回收单结果
        AcquireOrderResp resp = new AcquireOrderResp();
        resp.setTraceId(req.getTraceId());
        resp.setAppId(req.getAppId());
        resp.setVersion(req.getVersion());
        resp.setResult(packageResp.getResult());
        resp.setResultType(packageResp.getResultType().name());

        if (resp.getErrorCode() == 0)
            resp.setSuccess(true);
        else
            resp.setSuccess(false);

        return resp;
    }

    /**
     * 请求参数校验
     * 
     * @param req
     */
    private void validateReqParam(AcquireOrderReq req) {
        if (req.getVersion() != 1) {
            throw new BizException(ErrorCode.ILLEGAL_ARGUMENT, req.getVersion().toString());
        }

        InstitutionConfig instConfig = instConfigManager.getConfig(PayTypeEnum.parse(req.getPayType()));
        if (instConfig == null)
            throw new BizException(ErrorCode.INVALID_PAY_TYPE, req.getPayType());

        BizCodeEnum bizCodeEnum = BizCodeEnum.parse(req.getBizCode());
        if (bizCodeEnum == null) {
            throw new BizException(ErrorCode.ILLEGAL_ARGUMENT, "无效的BizCode:" + req.getBizCode());
        }

        BussinessOrder bussinessOrder = payService.getBussinessOrderByOrderId(req.getOrderId());
        if (bussinessOrder != null) {
            throw new BizException(ErrorCode.FAIL, "OrderId已经创建过支付单");
        }

        if ((new BigDecimal(req.getPayPrice()).doubleValue() < 0.01)) {
            throw new BizException(ErrorCode.ILLEGAL_ARGUMENT, "无效的支付金额");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ymatou.payment.facade.PaymentFacade#executePayNotify(com.ymatou.payment.facade.model.
     * ExecutePayNotifyReq)
     */
    @Override
    public ExecutePayNotifyResp executePayNotify(ExecutePayNotifyReq req) {
        Payment payment = payService.getPaymentByPaymentId(req.getPaymentId());
        ExecutePayNotifyResp resp = new ExecutePayNotifyResp();

        if (payment == null) {
            logger.error("execute pay notify failed because payment id not exist:{}", req.getPaymentId());
            throw new BizException(ErrorCode.FAIL, "payment id not exist:" + req.getPaymentId());
        }

        if (payment.getPayStatus() != PayStatusEnum.Paied) {
            logger.error("execute pay notify failed because payment id not exist:{}", req.getPaymentId());
            throw new BizException(ErrorCode.FAIL, "payment status not paied:" + req.getPaymentId());
        }

        payService.executePayNotify(payment, req.getMockHeader());


        return resp;
    }
}
