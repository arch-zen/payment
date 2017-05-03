package com.ymatou.payment.domain.channel.service.paymentnotify;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.model.PaymentNotifyMessage;
import com.ymatou.payment.domain.channel.service.PaymentNotifyService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.constants.PaymentNotifyType;
import com.ymatou.payment.facade.model.PaymentNotifyReq;
import com.ymatou.payment.infrastructure.Money;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.model.ApplePayConsumeNotifyRequest;
import com.ymatou.payment.integration.service.applepay.common.ApplePayConstants;
import com.ymatou.payment.integration.service.applepay.common.ApplePayMessageUtil;
import com.ymatou.payment.integration.service.applepay.common.ApplePaySignatureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gejianhua on 2017/4/27.
 */
@Service
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

        //获取配置
        InstitutionConfig config = this.institutionConfigManager.getConfig(PayTypeEnum.ApplePay);

        //验证执行是否成功，商户号，验签
        this.validateMessage(consumeNotifyRequest, config, notifyRequest.getMockHeader());

        //构建PaymentNotifyMessage
        PaymentNotifyMessage notifyMessage = new PaymentNotifyMessage();
        notifyMessage.setPaymentId(consumeNotifyRequest.getOrderId());
        notifyMessage.setInstitutionPaymentId(consumeNotifyRequest.getQueryId());
        notifyMessage.setActualPayPrice(new Money(consumeNotifyRequest.getSettleAmt()).divide(new BigDecimal("100")));
        notifyMessage.setActualPayCurrency(consumeNotifyRequest.getSettleCurrencyCode());
        notifyMessage.setDiscountAmt(new Money(0));
        notifyMessage.setPayerId("");
        notifyMessage.setPayerEmail("");
        notifyMessage.setBankId("");

        try {
            Integer cardType = Integer.parseInt(consumeNotifyRequest.getPayCardType());
            notifyMessage.setCardType(cardType);
        }
        catch(Exception ex){
            logger.error("paycardtype convert to integer exception, with paycardtype:" + consumeNotifyRequest.getPayCardType());
        }

        notifyMessage.setPayTime(new Date());
        notifyMessage.setPayStatus(PayStatusEnum.Paied);
        notifyMessage.setTraceId(consumeNotifyRequest.getTraceNo());
        return notifyMessage;
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



































































