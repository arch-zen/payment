/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.paymentnotify;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
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
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.constants.PaymentNotifyType;
import com.ymatou.payment.facade.model.PaymentNotifyReq;
import com.ymatou.payment.infrastructure.util.HttpUtil;
import com.ymatou.payment.infrastructure.util.StringUtil;

/**
 * 支付宝 APP 回调报文解析器 (10)
 * 
 * @author wangxudong 2016年5月19日 下午2:16:06
 *
 */
@Component
public class AliPayPaymentNotifyServiceImpl implements PaymentNotifyService {

    @Resource
    private SignatureService signatureService;

    @Resource
    private InstitutionConfigManager institutionConfigManager;

    private static final Logger logger = LoggerFactory.getLogger(AliPayPaymentNotifyServiceImpl.class);

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
            map = HttpUtil.parseQueryStringToMap(notifyRequest.getRawString());;
        } catch (Exception e) {
            throw new BizException(ErrorCode.FAIL, "parse query string when receive alipay payment notify.", e);
        }

        // 验签
        InstitutionConfig instConfig =
                institutionConfigManager.getConfig(PayTypeEnum.parse(notifyRequest.getPayType()));
        boolean isSignValidate = signatureService.validateSign(map, instConfig, notifyRequest.getMockHeader());
        if (!isSignValidate) {
            throw new BizException(ErrorCode.SIGN_NOT_MATCH, "paymentId:" + map.get("out_trade_no"));
        }

        // 从报文中获取到有效信息

        PaymentNotifyMessage paymentNotifyMessage = new PaymentNotifyMessage();
        paymentNotifyMessage.setTraceId(UUID.randomUUID().toString());
        paymentNotifyMessage.setPayerId(
                StringUtils.isBlank(map.get("buyer_email")) ? map.get("buyer_id") : map.get("buyer_email"));
        paymentNotifyMessage.setActualPayCurrency(
                StringUtils.isBlank(map.get("currency")) ? "CNY" : map.get("currency"));
        paymentNotifyMessage.setActualPayPrice(new BigDecimal(map.get("total_fee")));
        paymentNotifyMessage.setInstitutionPaymentId(map.get("trade_no"));
        paymentNotifyMessage.setPaymentId(map.get("out_trade_no"));
        paymentNotifyMessage
                .setPayTime(
                        map.get("gmt_payment") != null
                                ? parseDate(map.get("gmt_payment"))
                                : new Date());

        // 根据返回报文判断支付是否成功
        String payStatus = map.get("trade_status");
        if (!StringUtils.isBlank(payStatus)
                && (payStatus.equals("TRADE_SUCCESS") || payStatus.equals("TRADE_FINISHED")))
            paymentNotifyMessage.setPayStatus(PayStatusEnum.Paied);

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
        sbUrl.append(queryStringFormat("PayTime",
                StringUtil.getDateFormatString("yyyyMMddHHmmss", notifyMessage.getPayTime())));
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

    /**
     * 将日期字符串转成日期格式
     * 
     * @param dateString
     * @return
     */
    private Date parseDate(String dateString) {
        try {

            return DateUtils.parseDate(dateString, new String[] {"yyyy-MM-dd HH:mm:ss"});
        } catch (ParseException e) {
            logger.error("pare date when process alipay notify with date string:" + dateString, e);
            return new Date();
        }
    }
}
