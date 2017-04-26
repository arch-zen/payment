/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.infrastructure.security;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.integration.common.Hex;
import org.junit.Test;

import com.ymatou.payment.infrastructure.security.RSAUtil;

public class RSAUtilTest {
    /**
     * 公钥
     */
    private static final String PUBLIC_KEY =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDmP+e7IKiIxxGYzYZl5S2qbmCbkgAjeM81mgwp7owBHdncbpcaee2o65zYFI0SXJuzu5rZBe3BPLucuZeg3t7FjNf2C7j8gfDYcviMamOwI7VuWZ+ZEtCHSHkOhUlwcul5xLMXl2nPd1YzL6zMCe2VAK75cHoBKnd+DmEVSOQipQIDAQAB";

    /**
     * 私钥
     */
    private static final String PRIVATE_KEY =
            "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAOY/57sgqIjHEZjNhmXlLapuYJuSACN4zzWaDCnujAEd2dxulxp57ajrnNgUjRJcm7O7mtkF7cE8u5y5l6De3sWM1/YLuPyB8Nhy+IxqY7AjtW5Zn5kS0IdIeQ6FSXBy6XnEsxeXac93VjMvrMwJ7ZUArvlwegEqd34OYRVI5CKlAgMBAAECgYBApPCKuUCYJkvqesmhEhcgIp09EGC5lNGYWwfPPgpQxfDE0sfZxyHSq1P91sdEwHt2mtV+2QtHlaWW+wR3RhuFEuGM1z8fsvongAk9bNDPvaPz07HF1YwXuviakDYk1bWwqCS+9VFJ82fGae4+ftUQOmJYSH+LV89RRqWdCP5GgQJBAP/nwVbgw/bBR04UDfUK2Bdr+Op+6WFdFoyzK7Kvr5sjO0T5ewswHJ34+B26X50kGqkIU2h2AXh8/AX1ZJUB5vsCQQDmVbglUXIrjLG5zraxstNlDnJvDL3WmYtZJxbKWq9QgSWYzf4iCaAVqsjZHfAHAV2iMGf+x55QGuHk7hZ0SGrfAkEAlVRE0xCX6c8BcANt3Zc1X/2GpDfosgMjHHmVP1Eb1RirBmXasj2iBWD6UEaocsdVs1uDaIqr8wZj/ooi5nzUrwJBAM5R2jETU4FO9aPKVju2Q0UyO67dau7fesLREMkRkhg6lsLZQdqbZJoD8QUKnAaqYoT1dzHw/Q4kBlRaMCLY+2ECQQDVAbyCAAte4LH9EndxkisOZXKMLbjhlmORpyKBUwSwW6Hk4If4hlKTIOUCuwXJzb08BK40AGD+pw6P35e+B3Dh";

    /**
     * Apple Pay 商户公钥
     */
    private static final String PUBLIC_KEY_APPLE_YMT =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCQvyFGMGkKxojiX2VUkXvu6mNJk/ayHRHXo7KOxC3oQE+QZPk24MnBT5eqn7pmxxkZ2Ky5NKCN914LEPJ+6+XatDEIxU7ZXDHQuGa6/Q5W35lh9aC+M7ULZ8eWjIKM+zlnS5MrUOML8EpvoETCnC2V3YfgM45tTIXEEo3Dkx5RWwIDAQAB";

    /**
     * Apple Pay 商户私钥
     */
    private static  final String PRIVATE_KEY_APPLE_YMT =
            "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJC/IUYwaQrGiOJfZVSRe+7qY0mT9rIdEdejso7ELehAT5Bk+TbgycFPl6qfumbHGRnYrLk0oI33XgsQ8n7r5dq0MQjFTtlcMdC4Zrr9DlbfmWH1oL4ztQtnx5aMgoz7OWdLkytQ4wvwSm+gRMKcLZXdh+Azjm1MhcQSjcOTHlFbAgMBAAECgYA/0XU7PBbkIFqz7DmCCs7orMDv7OPk7foy6ELOvWI8NzadRFe4wW1S5OPK37fQntHLWoP0+GYdkcYN/P/dw+ZIWDE6YSS9z/E3RMY1835Zl1xz+fXoMq85Rd8RQTWfHSDpQ8/cwjzpNohf5yYcW5Ih9GBwU6PX73GzHwO1iRVVmQJBAMgLDOWrd4PDs37OQEKJ79SG8Y5PGM+lkfKNpHfP0JYERPLPDdYzemAHEVp9hejZp3sdSNibtsIgz1sMHoO5+tcCQQC5PFTtedbySEpWpqzOV6tYT2iGECCfoQxHiLc9ylgUXyt3PgBZB+eB1+ECj1QBFbSCM4x716+cG6ntEs40zHEdAkEAlLlCeW4liNzykhAdTlrm54b8B+VeeOKLw1zzLfBfD2lVKYAutVXIYaRfjntMF3XaQnnfMstx8JocD4DPHvgiOQJAQp246UKlE7L90RXWFcsfmv3L2FLaeK4BbYR0aetoK8NiLVsF4v7duit6B2KmUlGM/jotrqgNxrWIMfNOZ1yS3QJAVK7437NWnPnzYlxE7VUtK5DP1YOWK+2UzwIlbhe2irVx4oMhudgKQZYrJ+WmAjvtzGBHO/2mHS8Tiv0uapTCwQ==";

    /**
     * Apple Pay 银联公钥
     */
    private static  final String PUBLIC_KEY_APPLE_YL =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxQNhgIti+/dz3XXOFEOtJYJ50BgMlB58lSNkojZq8W0Hb9ttKYpB3Q1W+H7Ne16EJgvEo6W07oMNNEfAf3F+FFjcIEyZw693bFBC8wlxX1fCiq0IqqHOC6gPEl9h4a6K3sOm5GwMYjT7IqBzVFjby/VW25cbzjl9M/s9G0mtqt5BiVBeCEyAcUBn+9F2qUulWg2ZrkLnpwmTlt+vaeAOVdtSo9cuLWOJcAVv/Lhvx4YQiXUuYqndo8wM8rt6yFzt8s40SnWhDkl3WcT8ZmP6ULfjlo1aYuyX/T24C+jgu76lCyYbJ8ttQZHnUpuYdqMXgyuvq5KqJTbUK2vW5AaRxwIDAQAB";

    @Test
    public void testSign() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
            SignatureException, UnsupportedEncodingException {
        String target = "123456";
        String rsaSign =
                "Tr0A6L06m93pOh5cEUhYv6jWDtfxirOUQk2oXszKrIv0xwp71GgvL69csT7j2y+x+hnteoCUbHw6lyqP/hcprlEMJ1GTXiEnu2eECYSf5bhbw1xOdF/db7HMM2JxwPhY/lsEoC4d/TmDNzeK5nDT2PZGkcp53nM7hfS+zK2hMf8=";
        String result = RSAUtil.sign(target, PRIVATE_KEY);

        String raw =
                "BM3xEGZ9GKZDPGPNrDAY1xjaJrbL/Av4b2RpWYjZSDYmEs8QB6wPli90pzudoo7BnZM0UPgK2asnzb9Lui41t8XxGeSHw+cmqMDcr+AdQ+Ibi/liPH/eetB4sFeGvJM5xtOYbl225ErOK7RhxXzQ/ZXCP9PUups9jLCoF5zB1CE=";

        Base64.getDecoder().decode(raw);

        assertEquals(rsaSign, result);
    }

    @Test
    public void testSignAndCheck() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
            SignatureException, UnsupportedEncodingException {
        String target = "123456中国";
        String rsaSign = RSAUtil.sign(target, PRIVATE_KEY);
        boolean signCheck = RSAUtil.doCheck(target, rsaSign, PUBLIC_KEY);

        assertEquals(true, signCheck);
    }

    /**
     * 测试验签
     * 
     * @throws UnsupportedEncodingException
     * @throws SignatureException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    @Test
    public void testDoCheck() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
            SignatureException, UnsupportedEncodingException {
        String target = "123456";
        String sign =
                "Tr0A6L06m93pOh5cEUhYv6jWDtfxirOUQk2oXszKrIv0xwp71GgvL69csT7j2y+x+hnteoCUbHw6lyqP/hcprlEMJ1GTXiEnu2eECYSf5bhbw1xOdF/db7HMM2JxwPhY/lsEoC4d/TmDNzeK5nDT2PZGkcp53nM7hfS+zK2hMf8=";

        boolean result = RSAUtil.doCheck(target, sign, PUBLIC_KEY);

        assertEquals(true, result);
    }

    /**
     * 测试验签失败
     * 
     * @throws UnsupportedEncodingException
     * @throws SignatureException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    @Test
    public void testDoCheckFailed() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
            SignatureException, UnsupportedEncodingException {
        String target = "1234561";
        String sign =
                "Tr0A6L06m93pOh5cEUhYv6jWDtfxirOUQk2oXszKrIv0xwp71GgvL69csT7j2y+x+hnteoCUbHw6lyqP/hcprlEMJ1GTXiEnu2eECYSf5bhbw1xOdF/db7HMM2JxwPhY/lsEoC4d/TmDNzeK5nDT2PZGkcp53nM7hfS+zK2hMf8=";

        boolean result = RSAUtil.doCheck(target, sign, PUBLIC_KEY);

        assertEquals(false, result);
    }

    @Test
    public void testAppleKey() throws InvalidKeySpecException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, SignatureException {
        String content = "123456789";
        String sign = RSAUtil.sign(content, PRIVATE_KEY_APPLE_YMT);

        System.out.println("sign:" + sign);

        boolean result = RSAUtil.doCheck(content, sign, PUBLIC_KEY_APPLE_YMT);
        assertEquals(true, result);
    }

    @Test
    public void testAppleNotify() throws InvalidKeySpecException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, SignatureException {
        String content = "accessType=0&bizType=000201&certId=68759585097&encoding=UTF-8&merId=777290058146088&orderId=20170426135825&respCode=00&respMsg=成功[0000000]&signMethod=01&tn=609085064834331077901&txnSubType=01&txnTime=20170426135825&txnType=01&version=5.0.0";
        String sign = "rqisvyIjvi/eTKfcA88Y0oSpvATgbc3xvAaF3Pj7xIAMCBPamuuplg0oxcoppZ4dKpWCYtFL090imKQjdlN5uc03HWoB3uihWCG+odDBcOggDV8OYorfAzRN7Nt223dQnqqb7RuwW0Eqil0DnGQPTPnaTQgvW4Yzu/C7OPLCeBeB0jWdyYojroT27CswPCf2bTHvycbNBg+Eh6BQQUl4Ryz+lP0n8tuP7spSEG+fI6tTFsAhLnJH77hf9VO9OL4seICQzcC4KYmtBpEgAOzD2xzMbHl8u8czH7QzKPujwizUeeEpVdJwo4d0NxRCvgLdbqbtQOaZzVsymWzQAghtAw==";

        boolean result = doCheck(content, sign, PUBLIC_KEY_APPLE_YL);
        assertEquals(true, result);

    }

    public static boolean doCheck(String content, String sign, String publicKey) throws NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedKey = Base64.getDecoder().decode(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

        java.security.Signature signature = java.security.Signature.getInstance("SHA1withRSA");
        signature.initVerify(pubKey);
        signature.update(sha1X16(content));

        sign = sign.replaceAll("\r|\n", "");

        boolean bverify = signature.verify(Base64.getDecoder().decode(sign));
        return bverify;
    }


    public static byte[] sha1X16(String data) throws UnsupportedEncodingException {
        byte[] bytes = sha1(data.getBytes("UTF-8"));
        StringBuilder sha1StrBuff = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
                sha1StrBuff.append("0").append(
                        Integer.toHexString(0xFF & bytes[i]));
            } else {
                sha1StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
            }
        }
        try {
            return sha1StrBuff.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private static byte[] sha1(byte[] data) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(data);
            return md.digest();
        } catch (Exception e) {
            return null;
        }
    }

}
