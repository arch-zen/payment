package com.ymatou.payment.domain.channel.service.paymentnotify;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.model.PaymentNotifyMessage;
import com.ymatou.payment.domain.channel.service.PaymentNotifyService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.constants.PaymentNotifyType;
import com.ymatou.payment.facade.model.PaymentNotifyReq;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.model.ApplePayConsumeNotifyRequest;
import com.ymatou.payment.integration.service.applepay.common.ApplePayConstants;
import com.ymatou.payment.integration.service.applepay.common.ApplePayMessageUtil;
import com.ymatou.payment.integration.service.applepay.common.ApplePaySignatureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gejianhua on 2017/4/27.
 */
public class ApplePayPaymentNotifyServiceImpl implements PaymentNotifyService {


    private static Logger logger = LoggerFactory.getLogger(CmbPaymentNotifyServiceImpl.class);

    @Resource
    private InstitutionConfigManager institutionConfigManager;

    @Resource
    private IntegrationConfig integrationConfig;

    @Resource
    private SignatureService signatureService;

    @Override
    public PaymentNotifyMessage resloveNotifyMessage(PaymentNotifyReq notifyRequest) {
        //result转换为response
        Map<String, String> map = ApplePayMessageUtil.genResponseMessage(notifyRequest.getRawString());
        ApplePayConsumeNotifyRequest consumeNotifyRequest = new ApplePayConsumeNotifyRequest();
        consumeNotifyRequest.loadProperty(map);

        //验证报文，商户号，验签








        return null;
    }

    @Override
    public String buildResponse(PaymentNotifyMessage notifyMessage, Payment payment, PaymentNotifyType notifyType) {
        return "ok";
    }

    /**
     * 验证报文
     * @param request
     * @param config
     * @param mockHeader
     */
    private void validateMessage(ApplePayConsumeNotifyRequest request, InstitutionConfig config, HashMap<String, String> mockHeader){
        //验证respcode
        if (ApplePayConstants.response_success_code.equals(request.getRespCode()) == false
                && ApplePayConstants.response_success_defect_code.equals(request.getRespCode()) == false) {
            throw new BizException(String.format("applepay paynotify fail:paymentId:%s, code:%s, msg:%s",
                    request.getOrderId(),
                    request.getRespCode(),
                    request.getRespMsg()));
        }
        //验商户号
        if(config.getMerchantId().equals(request.getMerId()) == false){
            throw new BizException(String.format("applepay paynotify merchantId error:paymentId:%s, code:%s, msg:%s, merchantId:%s",
                    request.getOrderId(),
                    request.getRespCode(),
                    request.getRespMsg(),
                    request.getMerId()));
        }
        //验签
        String pubkey = config.getInstPublicKey();
        if (this.integrationConfig.isMock(mockHeader)) {
            pubkey = this.signatureService.getMockPublicKey();
        }
        boolean flag = ApplePaySignatureUtil.validate(request.getOriginMap(), pubkey);
        if(flag == false){
            throw new BizException(String.format("applepay paynotify validate sign fail:paymentId:%s, code:%s, msg:%s",
                    request.getOrderId(),
                    request.getRespCode(),
                    request.getRespMsg()));
        }

    }
}



































































