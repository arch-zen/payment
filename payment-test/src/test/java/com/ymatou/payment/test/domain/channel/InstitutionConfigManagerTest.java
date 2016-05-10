package com.ymatou.payment.test.domain.channel;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContextDisconfTest.xml"})
public class InstitutionConfigManagerTest{

	private InstitutionConfigManager instConfigManager = new InstitutionConfigManager();

	@Test
	public void testGetConfigAliPayPc() {
		String payType = "10";
		InstitutionConfig config = instConfigManager.getConfig(payType);

		assertEquals("验证商户号", "2088701734809577", config.getMerchantId());
		assertEquals("验证签名方式", "MD5", config.getSignType());
		assertEquals("验证机构名称", "AliPay", config.getInstName());
	}

	@Test
	public void testGetConfigAliPayApp() {
		String payType = "13";
		InstitutionConfig config = instConfigManager.getConfig(payType);

		assertEquals("验证商户号", "2088701734809577", config.getMerchantId());
		assertEquals("验证签名方式", "RSA", config.getSignType());
		assertEquals("验证机构名称", "AliPay", config.getInstName());
		assertEquals("验证支付宝公钥", "instPublicKey", config.getInstPublicKey());
		assertEquals("验证洋码头私钥", "instYmtPrivateKey", config.getInstYmtPrivateKey());
	}
	
	@Test
	public void testGetConfigWeiXinJSAPI(){
		String payType = "14";
		InstitutionConfig config = instConfigManager.getConfig(payType);

		assertEquals("验证商户号", "1278350701", config.getMerchantId());
		assertEquals("验证签名方式", "MD5", config.getSignType());
		assertEquals("验证机构名称", "Weixin", config.getInstName());
		assertEquals("验证AppId", "wxa06ebe9f39751792", config.getAppId());
		assertEquals("验证Md5Key", "es839gnc8451lp0s943n568xzskjgdbv", config.getMd5Key());
		
	}
	
	@Test
	public void testGetConfigWeiXinApp(){
		String payType = "15";
		InstitutionConfig config = instConfigManager.getConfig(payType);

		assertEquals("验证商户号", "1234079001", config.getMerchantId());
		assertEquals("验证签名方式", "MD5", config.getSignType());
		assertEquals("验证机构名称", "WeixinApp", config.getInstName());
		assertEquals("验证AppId", "wxf51a439c0416f182", config.getAppId());
		assertEquals("验证Md5Key", "c5781df6b8f149adca6094cdac4ac684", config.getMd5Key());
		
	}

}
