/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.paymentnotify;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.model.PaymentNotifyMessage;
import com.ymatou.payment.domain.channel.service.PaymentNotifyService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.domain.pay.model.PayStatus;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.model.PaymentNotifyReq;
import com.ymatou.payment.facade.model.PaymentNotifyType;
import com.ymatou.payment.infrastructure.util.MapUtil;

/**
 * 微信 APP 回调报文解析器 (13)
 * 
 * @author wangxudong 2016年5月19日 下午2:16:06
 *
 */
@Component
public class WeiXinPaymentNotifyServiceImpl implements PaymentNotifyService {

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
    public PaymentNotifyMessage resloveNotifyMessage(PaymentNotifyReq notifyRequest) {

        // 解析报文
        Map<String, String> map = new HashMap<String, String>();
        try {
            map = MapUtil.parseFromXml(notifyRequest.getRawString());;
        } catch (Exception e) {
            throw new BizException(ErrorCode.FAIL, "parse query string when receive weixin payment notify.", e);
        }

        // 验签
        InstitutionConfig instConfig = institutionConfigManager.getConfig(notifyRequest.getPayType());
        boolean isSignValidate = signatureService.validateSign(map, instConfig, notifyRequest.getMockHeader());
        if (isSignValidate == false) {
            throw new BizException(ErrorCode.SIGN_NOT_MATCH, "paymentId:" + map.get("out_trade_no"));
        }

        // 根据返回报文判断支付是否成功
        String resultCode = map.get("result_code");
        String returnCode = map.get("return_code");
        String transactionId = map.get("transaction_id");
        String outTradeNo = map.get("out_trade_no");
        String mchId = map.get("mch_id");
        BigDecimal totalFee = new BigDecimal(map.get("total_fee")).divide(new BigDecimal(100));

        if (!resultCode.equals("SUCCESS") || !returnCode.equals("SUCCESS") || StringUtils.isBlank(transactionId)
                || StringUtils.isBlank(outTradeNo) || !mchId.equals(instConfig.getMerchantId())
                || totalFee.doubleValue() < 0.00001) {
            throw new BizException(ErrorCode.NOTIFY_VERIFY_FAILED, "weixin notify with paymentid: " + outTradeNo);
        }


        // 从报文中获取到有效信息
        PaymentNotifyMessage paymentNotifyMessage = new PaymentNotifyMessage();
        paymentNotifyMessage.setTraceId(UUID.randomUUID().toString());
        paymentNotifyMessage.setPayerId(map.get("openid"));
        paymentNotifyMessage.setActualPayCurrency(map.get("fee_type"));
        paymentNotifyMessage.setActualPayPrice(totalFee);
        paymentNotifyMessage.setInstitutionPaymentId(map.get("transaction_id"));
        paymentNotifyMessage.setPaymentId(map.get("out_trade_no"));
        paymentNotifyMessage
                .setPayTime(DateUtils.parseDate(map.get("time_end"), new String[] {"yyyyMMddHHmmss"}));
        paymentNotifyMessage.setPayStatus(PayStatus.Paied);


        return paymentNotifyMessage;
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.ymatou.payment.domain.channel.service.PaymentNotifyService#buildResponse(com.ymatou.
     * payment.domain.channel.model.PaymentNotifyMessage,
     * com.ymatou.payment.domain.pay.model.Payment,
     * com.ymatou.payment.facade.model.PaymentNotifyType)
     */
    @Override
    public String buildResponse(PaymentNotifyMessage notifyMessage, Payment payment, PaymentNotifyType notifyType) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        sb.append("<return_code><![CDATA[SUCCESS]]></return_code>");
        sb.append("<return_msg><![CDATA[OK]]></return_msg>");
        sb.append("</xml>");

        return sb.toString();
    }

}
