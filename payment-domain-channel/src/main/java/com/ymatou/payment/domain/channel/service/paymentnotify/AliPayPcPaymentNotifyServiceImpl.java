/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.paymentnotify;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.model.PaymentNotifyMessage;
import com.ymatou.payment.domain.channel.service.PaymentNotifyService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.model.PaymentNotifyRequest;
import com.ymatou.payment.infrastructure.util.HttpUtil;

/**
 * 支付宝 APP 回调报文解析器 (10)
 * 
 * @author wangxudong 2016年5月19日 下午2:16:06
 *
 */
@Component
public class AliPayPcPaymentNotifyServiceImpl implements PaymentNotifyService {

    @Resource
    private SignatureService signatureService;

    @Resource
    private InstitutionConfigManager institutionConfigManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.ymatou.payment.domain.channel.service.PaymentNotifyService#ResloveNotifyMessage(com.
     * ymatou.payment.facade.model.PaymentNotifyRequest)
     */
    @Override
    public PaymentNotifyMessage resloveNotifyMessage(PaymentNotifyRequest notifyRequest) {

        Map<String, String> map = new HashMap<String, String>();
        try {
            map = HttpUtil.parseQueryStringToMap(notifyRequest.getRawString());;
        } catch (Exception e) {
            throw new BizException(ErrorCode.FAIL, "parse query string when receive alipay payment notify.", e);
        }

        InstitutionConfig instConfig = institutionConfigManager.getConfig(notifyRequest.getPayType());
        boolean isSignValidate = signatureService.validateSign(map, instConfig, notifyRequest.getMockHeader());
        if (isSignValidate == false) {
            throw new BizException(ErrorCode.SIGN_NOT_MATCH, "paymentId:" + map.get("out_trade_no"));
        }

        PaymentNotifyMessage paymentNotifyMessage = new PaymentNotifyMessage();
        paymentNotifyMessage.setActualPayCurrency("CNY");
        return paymentNotifyMessage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ymatou.payment.domain.channel.service.PaymentNotifyService#BuildInstNeedResponse(com.
     * ymatou.payment.domain.channel.model.PaymentNotifyMessage)
     */
    @Override
    public String buildInstNeedResponse(PaymentNotifyMessage notifyMessage) {

        return "http://www.baidu.com";
    }

}
