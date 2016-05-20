package com.ymatou.payment.facade.impl;

import javax.annotation.Resource;

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
                acquireOrderPackageFactory.getInstance(payment.getPaytype()).acquireOrderPackage(payment);

        // 返回收单结果
        AcquireOrderResp resp = new AcquireOrderResp();
        resp.setTraceId(req.getTraceId());
        resp.setAppId(req.getAppId());
        resp.setVersion(req.getVersion());
        resp.setResult(packageResp.getResult());
        resp.setResultType(packageResp.getResultType().name());

        return resp;
    }

    /**
     * 请求参数校验
     * 
     * @param req
     */
    private void validateReqParam(AcquireOrderReq req) {
        if (req.getVersion() != 1)
            throw new BizException(ErrorCode.NOT_SUPPORT_VERSION, req.getVersion().toString());

        InstitutionConfig instConfig = instConfigManager.getConfig(req.getPayType());
        if (instConfig == null)
            throw new BizException(ErrorCode.INVALID_PAYTYPE, req.getPayType());

        BussinessOrder bussinessOrder = payService.getBussinessOrderByOrderId(req.getOrderId());
        if (bussinessOrder != null)
            throw new BizException(ErrorCode.DB_ERROR, "OrderId已经创建过支付单");
    }

}
