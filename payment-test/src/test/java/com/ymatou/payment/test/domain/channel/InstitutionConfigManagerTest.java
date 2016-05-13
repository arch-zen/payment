package com.ymatou.payment.test.domain.channel;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.test.RestBaseTest;

public class InstitutionConfigManagerTest extends RestBaseTest {

    private InstitutionConfigManager instConfigManager = new InstitutionConfigManager();


    @Test
    public void testGetConfigAliPayPc() {
        String payType = "10";
        InstitutionConfig config = instConfigManager.getConfig(payType);

        assertEquals("验证商户号", "2088701734809577", config.getMerchantId());
        assertEquals("验证支付宝账号", "ap.ymt@ymatou.com", config.getSellerEmail());
        assertEquals("验证签名方式", "MD5", config.getSignType());
        assertEquals("验证机构名称", "AliPay", config.getInstName());
    }

    @Test
    public void testGetConfigAliPayApp() {
        String payType = "13";
        InstitutionConfig config = instConfigManager.getConfig(payType);

        assertEquals("验证商户号", "2088701734809577", config.getMerchantId());
        assertEquals("验证支付宝账号", "ap.ymt@ymatou.com", config.getSellerEmail());
        assertEquals("验证签名方式", "RSA", config.getSignType());
        assertEquals("验证机构名称", "AliPay", config.getInstName());
        assertEquals("验证支付宝公钥",
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB",
                config.getInstPublicKey());
    }

    @Test
    public void testGetConfigWeiXinJSAPI() {
        String payType = "14";
        InstitutionConfig config = instConfigManager.getConfig(payType);

        assertEquals("验证商户号", "1278350701", config.getMerchantId());
        assertEquals("验证签名方式", "MD5", config.getSignType());
        assertEquals("验证机构名称", "Weixin", config.getInstName());
        assertEquals("验证AppId", "wxa06ebe9f39751792", config.getAppId());
        assertEquals("验证Md5Key", "es839gnc8451lp0s943n568xzskjgdbv", config.getMd5Key());
        assertEquals("验证Md5KeyConnector", "&key=", config.getMd5KeyConnector());

    }

    @Test
    public void testGetConfigWeiXinApp() {

        String payType = "15";
        InstitutionConfig config = instConfigManager.getConfig(payType);

        assertEquals("验证商户号", "1234079001", config.getMerchantId());
        assertEquals("验证签名方式", "MD5", config.getSignType());
        assertEquals("验证机构名称", "WeixinApp", config.getInstName());
        assertEquals("验证AppId", "wxf51a439c0416f182", config.getAppId());
        assertEquals("验证Md5Key", "c5781df6b8f149adca6094cdac4ac684", config.getMd5Key());
        assertEquals("验证Md5KeyConnector", "&key=", config.getMd5KeyConnector());

    }

}
