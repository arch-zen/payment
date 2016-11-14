/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.integration.common;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ymatou.payment.facade.BizException;

/**
 * 招行签名算法
 * 
 * @author wangxudong 2016年11月10日 下午5:40:15
 *
 */
public class CmbSignature {
    private static Logger logger = LoggerFactory.getLogger(Signature.class);

    /**
     * RSA验签
     * 
     * @param strToSign
     * @param strSign
     * @param publicKey
     * @return
     */
    public static boolean isValidSignature(String strToSign, String strSign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.getDecoder().decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");

            signature.initVerify(pubKey);
            signature.update(strToSign.getBytes("UTF-8"));

            boolean bverify = signature.verify(Base64.getDecoder().decode(strSign));
            return bverify;
        } catch (Exception e) {
            logger.error("rsa valid signature failed, with:" + strToSign, e);
            throw new BizException("rsa valid signature failed, with:" + strToSign, e);
        }
    }


    /**
     * SHA-256签名
     * 
     * @param key 商户私钥
     * @param rawString 待签名字符串
     * @return
     */
    public static String shaSign(String key, String rawString) {
        String signString = rawString + "&" + key;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(signString.getBytes("UTF-8"));
            byte byteBuffer[] = messageDigest.digest();

            return Hex.encodeHexStr(byteBuffer, false);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("shaSigin failed,with:" + signString, e);
            throw new BizException("shaSigin failed,with:" + signString, e);
        }
    }

    /**
     * RC4签名
     * 
     * @param key
     * @param rawString
     * @return
     */
    public static String rc4Sign(String key, String rawString) {
        if (rawString == null || key == null) {
            return null;
        }
        return toHexString((asString(encry_RC4_byte(rawString, key))));
    }

    private static byte[] encry_RC4_byte(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        byte b_data[] = data.getBytes();
        return RC4Base(b_data, key);
    }

    private static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch & 0xFF);
            if (s4.length() == 1) {
                s4 = '0' + s4;
            }
            str = str + s4;
        }
        return str;// 0x表示十六进制
    }

    private static String asString(byte[] buf) {
        StringBuffer strbuf = new StringBuffer(buf.length);
        for (int i = 0; i < buf.length; i++) {
            strbuf.append((char) buf[i]);
        }
        return strbuf.toString();
    }

    private static byte[] initKey(String aKey) {
        byte[] b_key = aKey.getBytes();
        byte state[] = new byte[256];

        for (int i = 0; i < 256; i++) {
            state[i] = (byte) i;
        }
        int index1 = 0;
        int index2 = 0;
        if (b_key == null || b_key.length == 0) {
            return null;
        }
        for (int i = 0; i < 256; i++) {
            index2 = ((b_key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
            byte tmp = state[i];
            state[i] = state[index2];
            state[index2] = tmp;
            index1 = (index1 + 1) % b_key.length;
        }
        return state;
    }

    private static byte[] RC4Base(byte[] input, String mKkey) {
        int x = 0;
        int y = 0;
        byte key[] = initKey(mKkey);
        int xorIndex;
        byte[] result = new byte[input.length];

        for (int i = 0; i < input.length; i++) {
            x = (x + 1) & 0xff;
            y = ((key[x] & 0xff) + y) & 0xff;
            byte tmp = key[x];
            key[x] = key[y];
            key[y] = tmp;
            xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
            result[i] = (byte) (input[i] ^ key[xorIndex]);
        }
        return result;
    }
}
