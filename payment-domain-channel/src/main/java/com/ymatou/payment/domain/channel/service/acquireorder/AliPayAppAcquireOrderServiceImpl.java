/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.acquireorder;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.esotericsoftware.minlog.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.constants.AliPayConsts;
import com.ymatou.payment.domain.channel.model.AcquireOrderPackageResp;
import com.ymatou.payment.domain.channel.service.AcquireOrderService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.domain.channel.util.UrlEncoder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.constants.AcquireOrderResultTypeEnum;
import com.ymatou.payment.facade.model.AcquireOrderExt;
import com.ymatou.payment.facade.model.QueryEncryptResult;
import com.ymatou.payment.integration.IntegrationConfig;

/**
 * 支付宝App收单报文组织
 * 
 * @author wangxudong 2016年5月11日 下午6:27:55
 *
 */
@Component
public class AliPayAppAcquireOrderServiceImpl implements AcquireOrderService {

    private static Logger logger = LoggerFactory.getLogger(AliPayAppAcquireOrderServiceImpl.class);

    @Resource
    private InstitutionConfigManager instConfigManager;

    @Resource
    private IntegrationConfig integrationConfig;

    @Resource
    private SignatureService signatureService;

    // JSON 序列化工具
    ObjectMapper objectMapper = new ObjectMapper();

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ymatou.payment.domain.channel.service.AcquireOrderService#AcquireOrder(com.ymatou.payment
     * .domain.pay.model.Payment)
     */
    @Override
    public AcquireOrderPackageResp acquireOrderPackage(Payment payment) {
        // 获取第三方机构配置
        InstitutionConfig instConfig = instConfigManager.getConfig(payment.getPayType());

        // 拼装请求Map
        Map<String, String> reqMap = buildReqMap(payment, instConfig);

        // 签名
        String sign = signatureService.signMessage(reqMap, instConfig, payment.getAcquireOrderReq().getMockHeader());
        reqMap.put("sign_type", "\"RSA\"");
        reqMap.put("sign", String.format("\"%s\"", UrlEncoder.encode(sign)));

        // 拼装请求报文
        String reqForm = buildForm(payment, instConfig, reqMap, sign);

        // 返回报文结果
        AcquireOrderPackageResp resp = new AcquireOrderPackageResp();
        resp.setResultType(AcquireOrderResultTypeEnum.JSON);
        resp.setResult(reqForm);

        return resp;
    }

    /**
     * 构造请求字典
     * 
     * @param payment
     * @return
     */
    private Map<String, String> buildReqMap(Payment payment, InstitutionConfig instConfig) {
        AcquireOrderExt acquireOrderExt = getExt(payment.getBussinessOrder().getExt());

        // 此处必须用LinkedHashMap，保证添加的顺序
        Map<String, String> reqDict = new LinkedHashMap<String, String>();
        reqDict.put("service", "mobile.securitypay.pay");
        reqDict.put("partner", instConfig.getMerchantId());
        reqDict.put("seller_id", instConfig.getSellerEmail());
        reqDict.put("out_trade_no", payment.getPaymentId());
        reqDict.put("subject", payment.getBussinessOrder().getSubject());
        reqDict.put("body", payment.getBussinessOrder().getProductDesc());
        reqDict.put("rn_check", acquireOrderExt.getIsHangZhou() == 1 ? "T" : null);
        reqDict.put("total_fee", String.format("%.2f", payment.getPayPrice().doubleValue()));
        reqDict.put("notify_url",
                String.format("%s/notify/%s", integrationConfig.getYmtPaymentBaseUrl(), payment.getPayType()));
        reqDict.put("payment_type", AliPayConsts.PAYMENT_TYPE_PURCHARE);
        reqDict.put("_input_charset", AliPayConsts.INPUT_CHARSET);
        reqDict.put("it_b_pay", AliPayConsts.ALI_APP_ORDER_CLOSE_TIME);
        reqDict.put("show_url", "m.alipay.com");

        // 格式化成AliPay需要的格式
        for (Map.Entry<String, String> entry : reqDict.entrySet()) {
            String itemValue = entry.getValue();
            if (StringUtils.isBlank(itemValue))
                reqDict.put(entry.getKey(), null);
            else
                reqDict.put(entry.getKey(), String.format("\"%s\"", itemValue));
        }

        return reqDict;
    }

    /**
     * 获取到扩展参数
     * 
     * @param ext
     * @return
     */
    private AcquireOrderExt getExt(String extJson) {
        AcquireOrderExt acquireOrderExt = new AcquireOrderExt();
        if (extJson == null || extJson.isEmpty()) {
            acquireOrderExt.setShowMode("2");
            acquireOrderExt.setPayMethod("bankPay");
            acquireOrderExt.setIsHangZhou(0);
        } else {
            try {
                acquireOrderExt = objectMapper.readValue(extJson.toUpperCase(), AcquireOrderExt.class);

                String payMethod = acquireOrderExt.getPayMethod();
                if (payMethod == null)
                    payMethod = "2";

                payMethod = AliPayConsts.PAY_METHOD_MAP.getOrDefault(payMethod, "bankPay");
                acquireOrderExt.setPayMethod(payMethod);

            } catch (Exception ex) {
                Log.error("unrecognize ext param", ex);
                throw new BizException(ErrorCode.EXT_MESSAGE_NOT_RECOGNIZE, extJson);
            }
        }

        return acquireOrderExt;
    }

    /**
     * 构建返回APP的JSON报文
     * 
     * @param payment
     * @param instConfig
     * @param reqMap
     * @return
     */
    private String buildForm(Payment payment, InstitutionConfig instConfig, Map<String, String> reqMap,
            String originSign) {

        try {
            QueryEncryptResult result = new QueryEncryptResult();
            result.Partner = instConfig.getMerchantId();
            result.SellerId = instConfig.getSellerEmail();
            result.OutTradeNo = payment.getPaymentId();
            result.Subject = payment.getBussinessOrder().getProductName();
            result.Body = payment.getBussinessOrder().getProductDesc();
            result.TotalFee = String.format("%.2f", payment.getPayPrice().doubleValue());
            result.NotifyUrl =
                    String.format("%s/notify/%s", integrationConfig.getYmtPaymentBaseUrl(), payment.getPayType());
            result.Service = "mobile.securitypay.pay";
            result.PaymentType = AliPayConsts.PAYMENT_TYPE_PURCHARE;
            result.InputCharset = "utf-8";
            result.ItBPay = AliPayConsts.ALI_APP_ORDER_CLOSE_TIME;
            result.ShowUrl = payment.getBussinessOrder().getProductUrl();
            result.Sign = originSign;
            result.SignType = "RSA";
            result.EncryptStr = mapToString(reqMap);
            result.QuerySuccess = true;
            result.Message = "成功";

            return objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            logger.error("build alipay app form failed.", e);
            throw new BizException(ErrorCode.FAIL, "build alipay app form failed.");
        }
    }

    /**
     * Map转成String
     * 
     * @param reqMap
     * @return
     */
    private String mapToString(Map<String, String> reqMap) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : reqMap.entrySet()) {
            sb.append(String.format("%s=%s&", entry.getKey(), entry.getValue()));
        }

        String s = sb.toString();
        return s.substring(0, s.length() - 1);
    }
}
