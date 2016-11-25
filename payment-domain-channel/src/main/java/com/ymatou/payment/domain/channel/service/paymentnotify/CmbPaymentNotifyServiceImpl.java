/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.paymentnotify;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.model.PaymentNotifyMessage;
import com.ymatou.payment.domain.channel.service.PaymentNotifyService;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.repository.CmbPublicKeyRepository;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.constants.PaymentNotifyType;
import com.ymatou.payment.facade.model.PaymentNotifyReq;
import com.ymatou.payment.infrastructure.Money;
import com.ymatou.payment.infrastructure.util.HttpUtil;
import com.ymatou.payment.integration.common.CmbSignature;
import com.ymatou.payment.integration.model.CmbPayNotifyRequest;
import com.ymatou.payment.integration.model.CmbPayNotifyRequest.PayNoticeData;

@Component
public class CmbPaymentNotifyServiceImpl implements PaymentNotifyService {

    private static Logger logger = LoggerFactory.getLogger(CmbPaymentNotifyServiceImpl.class);

    @Resource
    private InstitutionConfigManager institutionConfigManager;

    @Resource
    private CmbPublicKeyRepository cmbPublicKeyRepository;

    @Override
    public PaymentNotifyMessage resloveNotifyMessage(PaymentNotifyReq notifyRequest) {
        // 解析报文
        Map<String, String> map = new HashMap<String, String>();
        try {
            map = HttpUtil.parseQueryStringToMap(notifyRequest.getRawString());;
        } catch (Exception e) {
            throw new BizException(ErrorCode.FAIL, "parse form data when receive cmb payment notify.", e);
        }

        String reqData = map.get("jsonRequestData");
        CmbPayNotifyRequest cmbPayNotifyRequest = JSONObject.parseObject(reqData, CmbPayNotifyRequest.class);

        // 获取公钥 & 签名字符串 & 签名
        String pubKey = cmbPublicKeyRepository.getPublicKey(notifyRequest.getMockHeader());
        String signString = CmbSignature.buildSignString(reqData, "noticeData");
        String sign = cmbPayNotifyRequest.getSign();
        boolean isSignValidate = CmbSignature.isValidSignature(signString, sign, pubKey);
        if (!isSignValidate) {
            throw new BizException(ErrorCode.SIGN_NOT_MATCH,
                    "paymentId:" + cmbPayNotifyRequest.getNoticeData().getOrderNo());
        }

        // 验证商户号
        // 防止黑客利用其它商户号的数据伪造支付成功报文
        PayNoticeData payNoticeData = cmbPayNotifyRequest.getNoticeData();
        InstitutionConfig instConfig =
                institutionConfigManager.getConfig(PayTypeEnum.parse(notifyRequest.getPayType()));
        if (!instConfig.getMerchantId().equals(payNoticeData.getMerchantNo())) {
            throw new BizException(ErrorCode.INVALID_MERCHANT_ID,
                    "paymentId:" + payNoticeData.getOrderNo() + ",merchantNo:" + payNoticeData.getMerchantNo());
        }

        // 从报文中获取有效信息
        PayNoticeData noticeData = cmbPayNotifyRequest.getNoticeData();
        PaymentNotifyMessage paymentNotifyMessage = new PaymentNotifyMessage();
        paymentNotifyMessage.setTraceId(UUID.randomUUID().toString());
        paymentNotifyMessage.setPayerId("");
        paymentNotifyMessage.setPayerEmail("");
        paymentNotifyMessage.setActualPayCurrency("CNY");
        paymentNotifyMessage.setInstitutionPaymentId(noticeData.getBankSerialNo());
        paymentNotifyMessage.setPaymentId(noticeData.getOrderNo());

        // 获取到银行返回的支付时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            paymentNotifyMessage.setPayTime(simpleDateFormat.parse(noticeData.getDateTime()));
        } catch (Exception e) {
            logger.error(String.format("parse PayTime String faild, paymentid:{}, paytime:{}", noticeData.getOrderNo(),
                    noticeData.getDateTime()), e);
            paymentNotifyMessage.setPayTime(new Date());
        }

        // 如果有优惠金额则记录下来
        if ("Y".equals(noticeData.getDiscountFlag())) {
            paymentNotifyMessage.setDiscountAmt(new Money(noticeData.getDiscountAmount()));
        } else {
            paymentNotifyMessage.setDiscountAmt(new Money(0));
        }

        Money actualPayPrice = new Money(noticeData.getAmount()).subtract(paymentNotifyMessage.getDiscountAmt());
        paymentNotifyMessage.setActualPayPrice(actualPayPrice);
        paymentNotifyMessage.setPayStatus(PayStatusEnum.Paied);

        return paymentNotifyMessage;
    }

    @Override
    public String buildResponse(PaymentNotifyMessage notifyMessage, Payment payment, PaymentNotifyType notifyType) {
        return "success";
    }

}
