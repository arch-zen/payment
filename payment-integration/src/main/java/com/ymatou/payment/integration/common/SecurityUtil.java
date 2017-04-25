package com.ymatou.payment.integration.common;

import com.ymatou.payment.facade.BizException;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Created by gejianhua on 2017/4/25.
 */
public class SecurityUtil {

    /**
     * 对称加密算法：RSA
     */
    private static final String RSA = "RSA";
    /**
     * 算法常量： SHA1
     */
    private static final String SHA1 = "SHA-1";

    /**
     * 算法常量：SHA1withRSA
     */
    private static final String SHA1RSA = "SHA1withRSA";

    /**
     * 根据字符串生成私钥(RSA)
     *
     * @param priKey
     * @return
     */
    public static PrivateKey genPrivateKeyWithRsa(String priKey) {
        try {
            byte[] base64Bytes = Base64.getDecoder().decode(priKey);

            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(base64Bytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA, "BC");

            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            return privateKey;
        } catch (Exception ex) {
            throw new BizException("genPrivateKeyWithRsa exception, with priKey:" + priKey, ex);
        }
    }

    /**
     * 根据字符串生成公钥
     *
     * @param pubKey
     * @return
     */
    public static PublicKey genPublicKeyWithRsa(String pubKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA, "BC");
            byte[] encodedKey = Base64.getDecoder().decode(pubKey);
            return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

        } catch (Exception ex) {
            throw new BizException("genPublicKeyWithRsa exception, with pubKey:" + pubKey, ex);
        }
    }

    /**
     * rsa签名
     *
     * @param privateKey
     * @param data
     * @return
     */
    public static byte[] signWithRsa(PrivateKey privateKey, byte[] data) {
        try {
            java.security.Signature st = java.security.Signature.getInstance(SHA1RSA, "BC");
            st.initSign(privateKey);
            st.update(data);
            return st.sign();
        } catch (Exception ex) {
            throw new BizException("signWithRsa exception", ex);
        }
    }

    /**
     * 验签
     * @param publicKey
     * @param signData
     * @param digestData
     * @return
     */
    public static boolean validateSignWithRsa(PublicKey publicKey, byte[] signData, byte[] digestData) {
        try {
            java.security.Signature st = java.security.Signature.getInstance(SHA1RSA, "BC");
            st.initVerify(publicKey);
            st.update(digestData);
            return st.verify(signData);
        } catch (Exception ex) {
            throw new BizException("validateSignWithRsa exception", ex);
        }
    }

    /**
     * sha1
     *
     * @param data
     * @param encoding
     * @return
     */
    public static byte[] sha1(String data, String encoding) {
        try {
            MessageDigest md = MessageDigest.getInstance(SHA1);
            md.reset();
            md.update(data.getBytes(encoding));
            return md.digest();
        } catch (Exception ex) {
            throw new BizException("sha1 exception, with data:" + data + ",encoding:" + encoding, ex);
        }
    }


}
















































