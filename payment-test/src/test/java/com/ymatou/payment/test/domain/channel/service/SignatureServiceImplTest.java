/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.domain.channel.service;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 签名服务测试
 * 
 * @author wangxudong 2016年5月12日 下午8:16:46
 *
 */
public class SignatureServiceImplTest extends RestBaseTest {

    @Resource
    private SignatureService signatureService;

    @Resource
    private InstitutionConfigManager instConfigManager;

    @Resource
    private IntegrationConfig config;

    /**
     * AliPay App 验签
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testAliPayAppNotify() throws UnsupportedEncodingException {
        System.out.println(config.getAliPayBaseUrl());
        String payType = "13";
        String notifyMessage =
                "discount=0.00&payment_type=1&subject=3252820.weixin%E7%9A%84%E8%AE%A2%E5%8D%95&trade_no=2016051321001004220248687425&buyer_email=ki.doshi%40hotmail.com&gmt_create=2016-05-13+11%3A33%3A21&notify_type=trade_status_sync&quantity=1&out_trade_no=21897556000062808&seller_id=2088701734809577&notify_time=2016-05-13+11%3A33%3A23&body=3252820.weixin%E7%9A%84%E8%AE%A2%E5%8D%95&trade_status=TRADE_SUCCESS&is_total_fee_adjust=N&total_fee=33.00&gmt_payment=2016-05-13+11%3A33%3A23&seller_email=ap.ymt%40ymatou.com&price=33.00&buyer_id=2088002653527220&notify_id=fb493a38a9b5e24bc53ba1d8ce158c7hp6&use_coupon=N&sign_type=RSA&sign=itrjn%2FrbQFi4oC1RQoaXxjtvA2AvNk2H%2Bc7Kou%2FC%2BNZql04G7wI2bdY5QSyPVu1xZ28A%2FAIW1qkcUQltDDY%2BmR9G%2BbWTePHxI5C0Zjfsur%2FKyMikG21NvwjmsViKp2irVtUtnDPoWr8BwRt7rbD74cXsaVfHbKPj%2B0GjJCRB1xk%3D";
        Map<String, String> signMap = parseQueryStringToMap(notifyMessage);

        boolean signResult = signatureService.validateSign(signMap, instConfigManager.getConfig(payType), false);

        assertEquals("验证RSA签名", true, signResult);
    }


    /**
     * AliPay Pc 验签
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testAliPayPCNotify() throws UnsupportedEncodingException {
        String payType = "10";
        String notifyMessage =
                "discount=0.00&payment_type=1&subject=dearoou%E7%9A%84%E8%AE%A2%E5%8D%95&trade_no=2016051321001004790242297728&buyer_email=benbenyu1989111%40163.com&gmt_create=2016-05-13+11%3A58%3A46&notify_type=trade_status_sync&quantity=1&out_trade_no=21897626500822310&seller_id=2088701734809577&notify_time=2016-05-13+11%3A59%3A05&trade_status=TRADE_SUCCESS&is_total_fee_adjust=N&total_fee=396.00&gmt_payment=2016-05-13+11%3A59%3A05&seller_email=ap.ymt%40ymatou.com&price=396.00&buyer_id=2088302134796793&notify_id=c0fe678eb06fdcb795e8f465a21d70bm3i&use_coupon=N&sign_type=MD5&sign=240dd0275cc965c9ef2a3fc9e1121d9e";
        Map<String, String> signMap = parseQueryStringToMap(notifyMessage);

        boolean signResult = signatureService.validateSign(signMap, instConfigManager.getConfig(payType), false);

        assertEquals("验证MD5签名", true, signResult);
    }

    /**
     * Weixin App 验签
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testWeixinAppNotify() throws UnsupportedEncodingException {
        String payType = "15";
        Map<String, String> notifyMap = new HashMap<String, String>();
        notifyMap.put("appid", "wxf51a439c0416f182");
        notifyMap.put("bank_type", "CFT");
        notifyMap.put("cash_fee", "21000");
        notifyMap.put("fee_type", "CNY");
        notifyMap.put("is_subscribe", "N");
        notifyMap.put("mch_id", "1234079001");
        notifyMap.put("nonce_str", "9f93b638ad5a4a1588acf99d0d3922b7");
        notifyMap.put("openid", "oASzYjlvO_pZ00egqodr98xG1T5g");
        notifyMap.put("out_trade_no", "20160513131450803000000000018176");
        notifyMap.put("result_code", "SUCCESS");
        notifyMap.put("return_code", "SUCCESS");
        notifyMap.put("sign", "83B96CAA5F7ACFAA2A9E1F05BDFC5230");
        notifyMap.put("time_end", "20160513131458");
        notifyMap.put("total_fee", "21000");
        notifyMap.put("trade_type", "APP");
        notifyMap.put("transaction_id", "4010032001201605135802475848");

        boolean signResult = signatureService.validateSign(notifyMap, instConfigManager.getConfig(payType), false);

        assertEquals("验证MD5签名", true, signResult);
    }

    /**
     * Weixin JSAPI 验签
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testWeixinJSAPINotify() throws UnsupportedEncodingException {
        String payType = "14";
        Map<String, String> notifyMap = new HashMap<String, String>();
        notifyMap.put("appid", "wxa06ebe9f39751792");
        notifyMap.put("bank_type", "CFT");
        notifyMap.put("cash_fee", "8500");
        notifyMap.put("fee_type", "CNY");
        notifyMap.put("is_subscribe", "N");
        notifyMap.put("mch_id", "1278350701");
        notifyMap.put("nonce_str", "bec6026461e34c2a87c7fa4eae6cc06e");
        notifyMap.put("openid", "oR5W7jjZfohXk0uTWHjC4RvDK8Ms");
        notifyMap.put("out_trade_no", "21886574600419213");
        notifyMap.put("result_code", "SUCCESS");
        notifyMap.put("return_code", "SUCCESS");
        notifyMap.put("sign", "1A75C7A02219924DFDAA92D9494D42E0");
        notifyMap.put("time_end", "20160509175301");
        notifyMap.put("total_fee", "8500");
        notifyMap.put("trade_type", "JSAPI");
        notifyMap.put("transaction_id", "4008442001201605095674716576");

        boolean signResult = signatureService.validateSign(notifyMap, instConfigManager.getConfig(payType), false);

        assertEquals("验证MD5签名", true, signResult);
    }

    @Test
    public void testAliPayAppSign() {
        String payType = "13";
        Map<String, String> notifyMap = new LinkedHashMap<String, String>();
        notifyMap.put("partner", "\"2088701734809577\"");
        notifyMap.put("seller_id", "\"ap.ymt@ymatou.com\"");
        notifyMap.put("out_trade_no", "\"2016051317583592700920947\"");
        notifyMap.put("subject", "\"你好\"");
        notifyMap.put("body", "\"你好\"");
        notifyMap.put("rn_check", "\"T\"");
        notifyMap.put("total_fee", "\"10.00\"");
        notifyMap.put("notify_url", "\"http://localhost:12345/notify/13\"");
        notifyMap.put("service", "\"mobile.securitypay.pay\"");
        notifyMap.put("payment_type", "\"1\"");
        notifyMap.put("_input_charset", "\"utf-8\"");
        notifyMap.put("it_b_pay", "\"10d\"");
        notifyMap.put("show_url", "\"m.alipay.com\"");

        String assertSign =
                "DtbWbaOFCZO4k5TBOHTtNQpuz8Sejpq/xnYa2eHlOJaQm1Cr90/zNGFBL0SBb80uQ2ilQ+1myFbTG9e6EmIo3B/2a3LBGFMEbVWJn5lb2ibRB7+u55yQWekaGcfGIg8z97PbIZBhD+bQxLYpOWoWg8hh1VFQDDaYeVW5o9tk3xc=";
        String sign = signatureService.signMessage(notifyMap, instConfigManager.getConfig(payType), false);

        assertEquals("验证RSA签名", assertSign, sign);
    }


    /**
     * 将url字符串转成Map
     * 
     * @param queryString
     * @return
     * @throws UnsupportedEncodingException
     */
    private Map<String, String> parseQueryStringToMap(String queryString) throws UnsupportedEncodingException {
        Map<String, String> signMap = new HashMap<String, String>();
        String[] queryList = queryString.split("&");
        for (int i = 0; i < queryList.length; i++) {
            String[] kvItem = queryList[i].split("=");
            signMap.put(kvItem[0], java.net.URLDecoder.decode(kvItem[1], "utf-8"));
        }

        return signMap;
    }
}
