/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.pay.PaymentConfig;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.infrastructure.security.MD5Util;
import com.ymatou.payment.infrastructure.security.RSAUtil;
import com.ymatou.payment.integration.IntegrationConfig;

/**
 * 签名服务实现
 * 
 * @author wangxudong 2016年5月12日 下午7:29:40
 *
 */
@Component
public class SignatureServiceImpl implements SignatureService {

    /**
     * Mock MD5Key
     */
    private final String mockMd5key = "autotestsalt";
    /**
     * Mock 公钥
     */
    private final String mockPublicKey =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDmP+e7IKiIxxGYzYZl5S2qbmCbkgAjeM81mgwp7owBHdncbpcaee2o65zYFI0SXJuzu5rZBe3BPLucuZeg3t7FjNf2C7j8gfDYcviMamOwI7VuWZ+ZEtCHSHkOhUlwcul5xLMXl2nPd1YzL6zMCe2VAK75cHoBKnd+DmEVSOQipQIDAQAB";
    /**
     * Mock 支付宝私钥
     */
    private final String mockYmtPrivateKey =
            "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAOY/57sgqIjHEZjNhmXlLapuYJuSACN4zzWaDCnujAEd2dxulxp57ajrnNgUjRJcm7O7mtkF7cE8u5y5l6De3sWM1/YLuPyB8Nhy+IxqY7AjtW5Zn5kS0IdIeQ6FSXBy6XnEsxeXac93VjMvrMwJ7ZUArvlwegEqd34OYRVI5CKlAgMBAAECgYBApPCKuUCYJkvqesmhEhcgIp09EGC5lNGYWwfPPgpQxfDE0sfZxyHSq1P91sdEwHt2mtV+2QtHlaWW+wR3RhuFEuGM1z8fsvongAk9bNDPvaPz07HF1YwXuviakDYk1bWwqCS+9VFJ82fGae4+ftUQOmJYSH+LV89RRqWdCP5GgQJBAP/nwVbgw/bBR04UDfUK2Bdr+Op+6WFdFoyzK7Kvr5sjO0T5ewswHJ34+B26X50kGqkIU2h2AXh8/AX1ZJUB5vsCQQDmVbglUXIrjLG5zraxstNlDnJvDL3WmYtZJxbKWq9QgSWYzf4iCaAVqsjZHfAHAV2iMGf+x55QGuHk7hZ0SGrfAkEAlVRE0xCX6c8BcANt3Zc1X/2GpDfosgMjHHmVP1Eb1RirBmXasj2iBWD6UEaocsdVs1uDaIqr8wZj/ooi5nzUrwJBAM5R2jETU4FO9aPKVju2Q0UyO67dau7fesLREMkRkhg6lsLZQdqbZJoD8QUKnAaqYoT1dzHw/Q4kBlRaMCLY+2ECQQDVAbyCAAte4LH9EndxkisOZXKMLbjhlmORpyKBUwSwW6Hk4If4hlKTIOUCuwXJzb08BK40AGD+pw6P35e+B3Dh";

    /**
     * @return
     */
    public String ymtPublicKey() {
        return mockPublicKey;
    }

    @Resource
    private IntegrationConfig integrationConfig;

    @Resource
    private PaymentConfig paymentConfig;

    /*
     * (non-Javadoc)
     * 
     * @see com.ymatou.payment.domain.channel.service.SignatureService#signMessage(java.util.Map,
     * com.ymatou.payment.domain.channel.InstitutionConfig, java.lang.Boolean)
     */
    @Override
    public String signMessage(Map<String, String> rawMapData, InstitutionConfig instConfig,
            HashMap<String, String> mockHeader) {
        // 拼装加签报文

        // 13-AliPay-App
        // 收单加签不需要对参数排序
        // 单笔交易查询需要对参数排序
        boolean needSort = !PayTypeEnum.AliPayApp.getCode().equals(instConfig.getPayType())
                || "single_trade_query".equals(rawMapData.get("service"))
                || "refund_fastpay_query".equals(rawMapData.get("service"))
                || "refund_fastpay_by_platform_nopwd".equals(rawMapData.get("service"));
        String rawMessage = mapToString(rawMapData, instConfig, needSort);
        String sign = null;

        if ("MD5".equals(instConfig.getSignType()))
            sign = md5Sign(rawMessage, instConfig, mockHeader);
        else if ("RSA".equals(instConfig.getSignType()))
            sign = rsaSign(rawMessage, instConfig, mockHeader);
        else
            throw new BizException(ErrorCode.INVALID_SIGN_TYPE, instConfig.getSignType());

        return sign;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ymatou.payment.domain.channel.service.SignatureService#validateSign(java.util.Map,
     * com.ymatou.payment.domain.channel.InstitutionConfig, java.lang.Boolean)
     */
    @Override
    public boolean validateSign(Map<String, String> signMapData, InstitutionConfig instConfig,
            HashMap<String, String> mockHeader) {
        // 拼装待验签报文
        boolean needSort = !(PayTypeEnum.AliPayWap.getCode().equals(instConfig.getPayType())
                && !StringUtils.isBlank(signMapData.get("notify_data"))); // AliPay 异步回调不需排序
        String rawMessage = mapToString(signMapData, instConfig, needSort);
        String sign = signMapData.get("sign");

        if ("MD5".equals(instConfig.getSignType())) {
            String md5Sign = md5Sign(rawMessage, instConfig, mockHeader);
            return sign.equalsIgnoreCase(md5Sign);
        }

        if ("RSA".equals(instConfig.getSignType())) {
            return rsaSignValidate(rawMessage, signMapData.get("sign").toString(), instConfig, mockHeader);
        }

        throw new BizException(ErrorCode.INVALID_SIGN_TYPE, instConfig.getSignType());
    }

    /**
     * Map转换成String
     * 
     * @param map
     * @param instConfig
     * @return
     */
    private String mapToString(Map<String, String> map, InstitutionConfig instConfig, boolean needSort) {
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue() != "" && entry.getValue() != null
                    && !entry.getKey().equals("serialVersionUID")
                    && !entry.getKey().equals("sign")
                    && !entry.getKey().equals("sign_type")) {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }

        int size = list.size();
        if (needSort) {
            Collections.sort(list);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(list.get(i));
        }

        return sb.substring(0, sb.length() - 1);
    }

    /**
     * Md5签名
     * 
     * @param rawMessage
     * @param instConfig
     * @param isMock
     * @return
     */
    private String md5Sign(String rawMessage, InstitutionConfig instConfig, HashMap<String, String> mockHeader) {
        try {
            String md5Key = integrationConfig.isMock(mockHeader) ? mockMd5key : instConfig.getMd5Key();
            String md5keyConnector =
                    StringUtils.isBlank(instConfig.getMd5KeyConnector()) ? "" : instConfig.getMd5KeyConnector();
            String targetMessage = rawMessage + md5keyConnector + md5Key;

            // getMd5KeyConnector不为空说明是微信的渠道，需要对签转大写
            if (StringUtils.isBlank(instConfig.getMd5KeyConnector()))
                return MD5Util.encode(targetMessage);
            else
                return MD5Util.encode(targetMessage).toUpperCase();
        } catch (Exception e) {
            throw new BizException(ErrorCode.FAIL, "md5 sign failed with paytype: " + instConfig.getPayType(), e);
        }
    }

    /**
     * RSA签名
     * 
     * @param rawMessage
     * @param instConfig
     * @param isMock
     * @return
     */
    private String rsaSign(String rawMessage, InstitutionConfig instConfig, HashMap<String, String> mockHeader) {
        try {
            String privateKey =
                    integrationConfig.isMock(mockHeader) ? mockYmtPrivateKey : instConfig.getInstYmtPrivateKey();

            return RSAUtil.sign(rawMessage, privateKey);
        } catch (Exception e) {
            throw new BizException(ErrorCode.FAIL, "rsa sign failed with paytype: " + instConfig.getPayType(), e);
        }
    }

    /**
     * RSA验签
     * 
     * @param rawMessage
     * @param sign
     * @param instConfig
     * @param isMock
     * @return
     */
    private boolean rsaSignValidate(String rawMessage, String sign, InstitutionConfig instConfig,
            HashMap<String, String> mockHeader) {
        try {
            String publicKey = integrationConfig.isMock(mockHeader) ? mockPublicKey : instConfig.getInstPublicKey();

            return RSAUtil.doCheck(rawMessage, sign, publicKey);
        } catch (Exception e) {
            throw new BizException(ErrorCode.FAIL, "rsa sign validae failed with paytype: " + instConfig.getPayType(),
                    e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ymatou.payment.domain.channel.service.SignatureService#getMd5MockKey()
     */
    @Override
    public String getMd5MockKey() {
        return mockMd5key;
    }
}
