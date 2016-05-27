/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.paymentnotify;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jboss.resteasy.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.model.PaymentNotifyMessage;
import com.ymatou.payment.domain.channel.service.PaymentNotifyService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.facade.constants.PaymentNotifyType;
import com.ymatou.payment.facade.model.PaymentNotifyReq;
import com.ymatou.payment.infrastructure.util.HttpUtil;

/**
 * 支付宝Wap支付回调处理
 * 
 * @author qianmin 2016年5月27日 下午4:06:10
 *
 */
@Component
public class AliPayWapPaymentNotifyServiceImpl implements PaymentNotifyService {

    private static String[] sdf = new String[] {"yyyy-MM-dd HH:mm:ss"};

    private static Logger logger = LoggerFactory.getLogger(AliPayPaymentNotifyServiceImpl.class);

    @Resource
    private SignatureService signatureService;

    @Resource
    private InstitutionConfigManager institutionConfigManager;

    @Override
    public PaymentNotifyMessage resloveNotifyMessage(PaymentNotifyReq notifyRequest) {
        // 解析报文
        Map<String, String> map = new HashMap<String, String>();
        try {
            map = HttpUtil.parseQueryStringToMap(notifyRequest.getRawString());;
        } catch (Exception e) {
            throw new BizException(ErrorCode.FAIL, "parse query string when receive alipay payment notify.", e);
        }

        if (PaymentNotifyType.Server.equals(notifyRequest.getNotifyType())) {
            map = getOrderedMapForSign(map); // 重新按规定顺序排序
        }

        // 验签
        InstitutionConfig instConfig = institutionConfigManager.getConfig(notifyRequest.getPayType());
        boolean isSignValidate = signatureService.validateSign(map, instConfig, notifyRequest.getMockHeader());
        if (!isSignValidate) {
            throw new BizException(ErrorCode.SIGN_NOT_MATCH, "paymentId:" + map.get("out_trade_no"));
        }

        // 从报文中获取到有效信息
        PaymentNotifyMessage paymentNotifyMessage = new PaymentNotifyMessage();
        if (PaymentNotifyType.Client.equals(notifyRequest.getNotifyType())) { // 同步回调
            paymentNotifyMessage.setTraceId(UUID.randomUUID().toString());
            paymentNotifyMessage.setPayerId("");
            paymentNotifyMessage.setActualPayCurrency("CNY");
            paymentNotifyMessage.setActualPayPrice(new BigDecimal("0.00"));
            paymentNotifyMessage.setInstitutionPaymentId(map.get("trade_no"));
            paymentNotifyMessage.setPaymentId(map.get("out_trade_no"));
            paymentNotifyMessage.setPayTime(new Date());
            if ("success".equals(map.get("result"))) {
                paymentNotifyMessage.setPayStatus(PayStatusEnum.Paied);
            }
        } else { // 异步回调
            String notifyData = map.get("notify_data");

            try {
                Document document = DocumentHelper.parseText(notifyData);
                Element root = document.getRootElement();
                String buyerEmail = root.elementText("buyer_email");
                String currency = root.elementText("currency");
                String gmtPayment = root.elementText("gmt_payment");

                paymentNotifyMessage.setTraceId(UUID.randomUUID().toString());
                paymentNotifyMessage
                        .setPayerId(StringUtils.isBlank(buyerEmail) ? root.elementText("buyer_id") : buyerEmail);
                paymentNotifyMessage.setActualPayCurrency(StringUtils.isBlank(currency) ? "CNY" : currency);
                paymentNotifyMessage.setActualPayPrice(new BigDecimal(root.elementText("total_fee")));
                paymentNotifyMessage.setInstitutionPaymentId(root.elementText("trade_no"));
                paymentNotifyMessage.setPaymentId(root.elementText("out_trade_no"));
                paymentNotifyMessage
                        .setPayTime(
                                StringUtils.isBlank(gmtPayment) ? new Date() : DateUtils.parseDate(gmtPayment, sdf));
                // 根据返回报文判断支付是否成功
                String payStatus = root.elementText("trade_status");
                if (!StringUtils.isBlank(payStatus)
                        && (payStatus.equals("TRADE_SUCCESS") || payStatus.equals("TRADE_FINISHED")))
                    paymentNotifyMessage.setPayStatus(PayStatusEnum.Paied);

            } catch (DocumentException e) {
                logger.error("parse notify_data error. {}", notifyData);
                throw new BizException(ErrorCode.UNKNOWN, "paymentId:" + map.get("out_trade_no"), e);
            }


        }

        return paymentNotifyMessage;
    }

    /**
     * AliPay Wap异步回调延签顺序固定，非按字母排序
     * 
     * @param respMap
     * @return
     */
    private Map<String, String> getOrderedMapForSign(Map<String, String> respMap) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("service", respMap.get("service"));
        map.put("v", respMap.get("v"));
        map.put("sec_id", respMap.get("sec_id"));
        map.put("notify_data", respMap.get("notify_data"));
        map.put("sign", respMap.get("sign"));
        return map;
    }

    @Override
    public String buildResponse(PaymentNotifyMessage notifyMessage, Payment payment, PaymentNotifyType notifyType) {
        if (notifyType == PaymentNotifyType.Client)
            return buildClientResponse(notifyMessage, payment);
        else
            return "success";

    }

    /**
     * 构建客户端回调Url
     * 
     * @param notifyMessage
     * @param payment
     * @return
     */
    private String buildClientResponse(PaymentNotifyMessage notifyMessage, Payment payment) {
        BussinessOrder bussinessOrder = payment.getBussinessOrder();

        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(queryStringFormat("AppId", bussinessOrder.getAppId()));
        sbUrl.append(queryStringFormat("Currency", notifyMessage.getActualPayCurrency()));
        sbUrl.append(queryStringFormat("InstPaymentId", notifyMessage.getInstitutionPaymentId()));
        sbUrl.append(queryStringFormat("Memo", bussinessOrder.getMemo()));
        sbUrl.append(queryStringFormat("TradingId", bussinessOrder.getOrderId()));
        sbUrl.append(queryStringFormat("PaymentId", notifyMessage.getPaymentId()));
        sbUrl.append(queryStringFormat("PayPrice", notifyMessage.getActualPayPrice().setScale(2).toString()));
        sbUrl.append(queryStringFormat("PayTime", DateUtil.formatDate(notifyMessage.getPayTime(), "yyyyMMddHHmmss")));
        sbUrl.append(queryStringFormat("TraceId", notifyMessage.getTraceId()));
        sbUrl.append(queryStringFormat("Version", bussinessOrder.getVersion().toString()));
        sbUrl.append(queryStringFormat("PayType", bussinessOrder.getPayType()));

        return bussinessOrder.getCallbackUrl() + "?" + sbUrl.toString().substring(1);
    }

    /**
     * 构建QueryString字符串
     * 
     * @param paramKey
     * @param paramVal
     * @return
     */
    private String queryStringFormat(String paramKey, String paramVal) {
        try {
            String paramEncodeVal = "";
            if (!StringUtils.isBlank(paramVal)) {
                paramEncodeVal = URLEncoder.encode(paramVal, "utf-8");
            }
            return String.format("&%s=%s", paramKey, paramEncodeVal);

        } catch (UnsupportedEncodingException e) {
            throw new BizException(ErrorCode.UNKNOWN,
                    "url encode failed when process alipay callback with key: " + paramKey, e);
        }
    }
}
