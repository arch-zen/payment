package com.ymatou.payment.facade.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
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
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireOrderResp;
import com.ymatou.payment.facade.model.ExecutePayNotifyReq;
import com.ymatou.payment.facade.model.ExecutePayNotifyResp;
import com.ymatou.payment.facade.model.SyncCmbPublicKeyReq;
import com.ymatou.payment.facade.model.SyncCmbPublicKeyResp;
import com.ymatou.payment.integration.common.CmbSignature;
import com.ymatou.payment.integration.model.CmbPublicKeyQueryRequest;
import com.ymatou.payment.integration.model.CmbPublicKeyQueryResponse;
import com.ymatou.payment.integration.service.cmb.PublicKeyQueryService;

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

    @Resource
    private PublicKeyQueryService publicKeyQueryService;

    /*
     * (non-Javadoc)
     * 
     * @see com.ymatou.payment.facade.PaymentFacade#acquireOrder(com.ymatou.payment.facade.model.
     * AcquireOrderReq)
     */
    @Override
    public AcquireOrderResp acquireOrder(AcquireOrderReq req) {
        String rawString = JSON.toJSONString(req);
        logger.info("receive acquire order request: {}", rawString);

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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ymatou.payment.facade.PaymentFacade#syncCmbPublicKey(com.ymatou.payment.facade.model.
     * SyncCmbPublicKeyReq)
     */
    @Override
    public SyncCmbPublicKeyResp syncCmbPublicKey(SyncCmbPublicKeyReq req) {
        InstitutionConfig config = instConfigManager.getConfig(PayTypeEnum.CmbApp);

        CmbPublicKeyQueryRequest reqPubKey = new CmbPublicKeyQueryRequest();
        reqPubKey.getReqData().setBranchNo(config.getBranchNo());
        reqPubKey.getReqData().setMerchantNo(config.getMerchantId());

        String sign = CmbSignature.shaSign(config.getMd5Key(), reqPubKey.buildSignString());
        reqPubKey.setSign(sign);

        CmbPublicKeyQueryResponse response = publicKeyQueryService.doService(reqPubKey, req.getMockHeader());
        if (response.getRspData().getRspCode().equals("SUC0000")) {
            payService.saveCmbPublicKey(response.getRspData().getFbPubKey());
        } else {
            throw new BizException(ErrorCode.QUERY_CMB_PUBLIC_KEY_FAILED, response.buildErrorMessage());
        }
        SyncCmbPublicKeyResp resp = new SyncCmbPublicKeyResp();
        resp.setSuccess(true);

        return resp;
    }
}
