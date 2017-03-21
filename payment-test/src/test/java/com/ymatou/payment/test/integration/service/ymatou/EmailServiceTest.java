package com.ymatou.payment.test.integration.service.ymatou;

import javax.annotation.Resource;

import org.junit.Test;

import com.ymatou.payment.integration.EmailConfig;
import com.ymatou.payment.integration.service.ymatou.EmailService;
import com.ymatou.payment.test.RestBaseTest;

/**
 * Created by wangxudong on 2017/3/21.
 */
public class EmailServiceTest extends RestBaseTest {
    @Resource
    private EmailService emailService;

    @Resource
    private EmailConfig emailConfig;

    @Test
    public void testSendHtmlMail() {
        String[] toMailAddress = emailConfig.getToMailAddress().split(";");

        emailService.sendHtmlMail(toMailAddress, "退款无法原路退回【微信用户账户异常或已注销】告知用户",
                "Hi，杨大班： <br> <br>如下订单无法原路退回，原因是：用户账户异常或已注销，请告知用户，谢谢！。<br> <br> <br>订单号：1101111");
    }

    @Test
    public void testSendWxRefundFailEmail() {
        emailService.sendWxRefundFailEmail("9999");
    }

}
