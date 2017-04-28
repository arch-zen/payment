package com.ymatou.payment.integration.service.applepay.common;

import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.integration.common.Hex;
import com.ymatou.payment.integration.common.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;

/**
 * Created by gejianhua on 2017/4/24.
 * applepay签名帮助类
 */
public class ApplePaySignatureUtil {
    private static Logger logger = LoggerFactory.getLogger(ApplePaySignatureUtil.class);


    /**
     * 签名
     *
     * @param reqMessage
     * @return
     */
    public static String sign(Map<String, String> reqMessage, String priKey) {
        //排除空项
        reqMessage = ApplePayUtil.filterBlank(reqMessage);
        //按key的名称顺序，并且得到明文
        String plaintext = ApplePayUtil.genPlaintext(reqMessage);
        logger.info("待签名报文串：{}", plaintext);

        //签名摘要：sha1并且转换为16进制
        byte[] signDigest = SecurityUtil.sha1(plaintext, ApplePayConstants.encoding);
        signDigest = Hex.toHexBytes(signDigest, ApplePayConstants.encoding);

        //验名
        PrivateKey privateKey = SecurityUtil.genPrivateKeyWithRsa(priKey);
        byte[] signData = SecurityUtil.signWithSha1Rsa(privateKey, signDigest);

        signData = Base64.getEncoder().encode(signData);
        return new String(signData);
    }

    /**
     * 验签
     *
     * @param respMessage 必须是返回字符串直接生成的Map
     * @return
     */
    public static boolean validate(Map<String, String> respMessage, String pubKey) {
        //获取签名原文
        String signOrigin = respMessage.get(ApplePayConstants.param_signature);
        byte[] signOriginBytes = null;
        try {
            signOriginBytes = Base64.getDecoder().decode(signOrigin.getBytes(ApplePayConstants.encoding));
        } catch (Exception ex) {
            throw new BizException("validate exception, with signOrigin:" + signOrigin, ex);
        }

        //生成明文
        String plaintext = ApplePayUtil.genPlaintext(respMessage);
        logger.info("待验签报文串：{}", plaintext);

        //生成签名摘要
        byte[] signDigest = SecurityUtil.sha1(plaintext, ApplePayConstants.encoding);
        signDigest = Hex.toHexBytes(signDigest, ApplePayConstants.encoding);

        PublicKey publicKey = SecurityUtil.genPublicKeyWithRsa(pubKey);

        return SecurityUtil.validateSignWithSha1Rsa(publicKey, signOriginBytes, signDigest);
    }
}
















































