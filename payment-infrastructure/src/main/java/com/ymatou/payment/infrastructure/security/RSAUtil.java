/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.infrastructure.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA帮助类
 * 
 * @author wangxudong 2016年5月12日 下午7:55:26
 *
 */
public class RSAUtil {
    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    /**
     * RSA签名
     * 
     * @param content 待签名数据
     * @param privateKey 商户私钥
     * @param encode 字符集编码
     * @return 签名值
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws SignatureException
     */
    public static String sign(String content, String privateKey, String encode) throws NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));

        KeyFactory keyf = KeyFactory.getInstance("RSA");
        PrivateKey priKey = keyf.generatePrivate(priPKCS8);

        java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

        signature.initSign(priKey);
        signature.update(content.getBytes(encode));

        byte[] signed = signature.sign();

        return Base64.getEncoder().encodeToString(signed);
    }

    /**
     * RSA签名(UTF8)
     * 
     * @param content
     * @param privateKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws SignatureException
     */
    public static String sign(String content, String privateKey) throws NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        KeyFactory keyf = KeyFactory.getInstance("RSA");
        PrivateKey priKey = keyf.generatePrivate(priPKCS8);
        java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
        signature.initSign(priKey);
        signature.update(content.getBytes("utf-8"));
        byte[] signed = signature.sign();
        return Base64.getEncoder().encodeToString(signed);
    }

    /**
     * RSA验签名检查
     * 
     * @param content 待签名数据
     * @param sign 签名值
     * @param publicKey 分配给开发商公钥
     * @param encode 字符集编码
     * @return 布尔值
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws SignatureException
     */
    public static boolean doCheck(String content, String sign, String publicKey, String encode)
            throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException,
            UnsupportedEncodingException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedKey = Base64.getDecoder().decode(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));


        java.security.Signature signature = java.security.Signature
                .getInstance(SIGN_ALGORITHMS);

        signature.initVerify(pubKey);
        signature.update(content.getBytes(encode));

        boolean bverify = signature.verify(Base64.getDecoder().decode(sign));
        return bverify;
    }

    /**
     * RSA验签
     * 
     * @param content
     * @param sign
     * @param publicKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws SignatureException
     */
    public static boolean doCheck(String content, String sign, String publicKey) throws NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedKey = Base64.getDecoder().decode(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

        java.security.Signature signature = java.security.Signature
                .getInstance(SIGN_ALGORITHMS);

        signature.initVerify(pubKey);
        signature.update(content.getBytes("utf-8"));

        sign = sign.replaceAll("\r|\n", "");

        boolean bverify = signature.verify(Base64.getDecoder().decode(sign));
        return bverify;
    }
}
