/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.acquireorder;

import java.util.HashMap;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esotericsoftware.minlog.Log;
import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.constants.AliPayConsts;
import com.ymatou.payment.domain.channel.model.AcquireOrderPackageResp;
import com.ymatou.payment.domain.channel.model.AcquireOrderResultType;
import com.ymatou.payment.domain.channel.service.AcquireOrderService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.domain.channel.util.AliPayFormBuilder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.model.AcquireOrderExt;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.HttpClientUtil;
import com.ymatou.payment.integration.model.QueryTimestampResponse;
import com.ymatou.payment.integration.service.alipay.QueryTimestampService;

/**
 * AliPay PC 收单报文组织（10）
 * 
 * @author wangxudong 2016年5月11日 下午5:22:24
 *
 */
@Component
public class AliPayPcAcquireOrderServiceImpl implements AcquireOrderService {

    private static Logger logger = LoggerFactory.getLogger(AliPayPcAcquireOrderServiceImpl.class);


    @Resource
    private InstitutionConfigManager instConfigManager;

    @Resource
    private IntegrationConfig integrationConfig;

    @Resource
    private QueryTimestampService queryTimestampService;

    @Resource
    private SignatureService signatureService;

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
        InstitutionConfig instConfig = instConfigManager.getConfig(payment.getPaytype());

        // 拼装请求Map
        HashMap<String, String> reqMap = buildReqMap(payment, instConfig);

        // 签名
        String sign = signatureService.signMessage(reqMap, instConfig, false);
        reqMap.put("sign", sign);

        // 拼装请求报文
        String reqForm = AliPayFormBuilder.buildForm(reqMap, integrationConfig.getAliPayBaseUrl("1"));

        // 返回报文结果
        AcquireOrderPackageResp resp = new AcquireOrderPackageResp();
        resp.setResultType(AcquireOrderResultType.Form);
        resp.setResult(reqForm);

        return resp;
    }

    /**
     * 构造请求字典
     * 
     * @param payment
     * @return
     */
    private HashMap<String, String> buildReqMap(Payment payment, InstitutionConfig instConfig) {
        AcquireOrderExt acquireOrderExt = getExt(payment.getBussinessOrder().getExt());

        HashMap<String, String> reqDict = new HashMap<String, String>();
        reqDict.put("payment_type", AliPayConsts.PAYMENT_TYPE_PURCHARE);
        reqDict.put("partner", instConfig.getMerchantId());
        reqDict.put("seller_email", instConfig.getSellerEmail());
        reqDict.put("return_url",
                String.format("%s/callback/%s", integrationConfig.getYmtPaymentBaseUrl(), payment.getPaytype()));
        reqDict.put("notify_url",
                String.format("%s/notify/%s", integrationConfig.getYmtPaymentBaseUrl(), payment.getPaytype()));
        reqDict.put("_input_charset", AliPayConsts.INPUT_CHARSET);
        reqDict.put("show_url", payment.getBussinessOrder().getProducturl());
        reqDict.put("out_trade_no", payment.getPaymentid());
        reqDict.put("subject", payment.getBussinessOrder().getSubject());
        reqDict.put("body", payment.getBussinessOrder().getProductdesc());
        reqDict.put("total_fee", String.format("%.2f", payment.getPayprice().doubleValue()));
        reqDict.put("paymethod", acquireOrderExt.getPayMethod());
        reqDict.put("anti_phishing_key", getAntiFishingKey(instConfig.getMerchantId()));
        reqDict.put("exter_invoke_ip", payment.getBussinessOrder().getClientip());
        reqDict.put("buyer_email", payment.getBussinessOrder().getThirdpartyuserid());
        reqDict.put("sign_type", instConfig.getSignType());

        if (acquireOrderExt.getIsHangZhou() == 1) {
            reqDict.put("service", "alipay.acquire.page.createandpay");
            reqDict.put("buyer_info", "{\"needBuyerRealnamed\":\"T\"}");
            reqDict.put("product_code", "FAST_INSTANT_TRADE_PAY");

            String showMode = acquireOrderExt.getShowMode();
            if (showMode != null && !showMode.isEmpty()) {
                reqDict.put("extend_parameters", String.format("{\"qrPayMode\":\"%s\"}", showMode));
            }
        } else {
            reqDict.put("service", "create_direct_pay_by_user");
            reqDict.put("defaultbank", payment.getBankid());

            String thirdPartyUserId = payment.getBussinessOrder().getThirdpartyuserid();
            if (thirdPartyUserId != null && !thirdPartyUserId.isEmpty())
                reqDict.put("default_login", "Y");
            else
                reqDict.put("default_login", "N");

            String showMode = acquireOrderExt.getShowMode();
            if (showMode != null && !showMode.isEmpty()) {
                reqDict.put("qr_pay_mode", showMode);
            }
        }

        return reqDict;
    }

    /**
     * 获取到防钓鱼参数
     * 
     * @param merchantId
     * @return
     */
    private String getAntiFishingKey(String merchantId) {
        String antiFishKey = "";
        try {
            QueryTimestampResponse response = queryTimestampService.doService("query_timestamp", merchantId, null);
            if (response != null)
                antiFishKey = response.getTimestampEncryptKey();
        } catch (Exception ex) {
            logger.error("get anti fishing key failed", ex);
        }

        if (antiFishKey.isEmpty())
            throw new BizException(ErrorCode.FAIL_QUERY_ANTI_FISHING_KEY, null);

        return antiFishKey;
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
                ObjectMapper objectMapper = new ObjectMapper();
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
}
