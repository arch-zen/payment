/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.infrastructure.security;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

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
}
