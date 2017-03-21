package com.ymatou.payment.integration.service.ymatou;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.ymatou.payment.integration.EmailConfig;

/**
 * 邮件服务
 * Created by wangxudong on 2017/3/21.
 */
@Component
@DependsOn("disconfMgrBean2")
public class EmailService {
    protected static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    @Resource
    private EmailConfig emailConfig;

    /**
     * 发送Html邮件
     * 
     * @param toAddress
     * @param title
     * @param text
     */
    public void sendHtmlMail(String toAddress, String title, String text) {
        if (StringUtils.isBlank(toAddress) || StringUtils.isBlank(title) || StringUtils.isBlank(text))
            return;
        String[] tos = StringUtils.split(toAddress, ";");
        sendHtmlMail(tos, title, text);
    }

    /**
     * 发送html邮件
     *
     * @param to
     * @param title
     * @param text
     */
    public void sendHtmlMail(String[] to, String title, String text) {
        try {
            logger.info("sendEmail with body:" + text);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            String nick = MimeUtility.encodeText("支付网关");
            InternetAddress[] toArray = new InternetAddress[to.length];
            for (int i = 0; i < to.length; i++) {
                toArray[i] = new InternetAddress(to[i]);
            }

            messageHelper.setFrom(new InternetAddress(String.format("%s<%s>", nick, emailConfig.getFromMailAddress())));
            messageHelper.setTo(toArray);
            messageHelper.setSubject(title);
            messageHelper.setText(text, true);
            mimeMessage = messageHelper.getMimeMessage();
            mailSender.send(mimeMessage);

        } catch (Exception ex) {
            StringBuilder sb = new StringBuilder();
            sb.append("mailto:");
            sb.append(StringUtils.join(to, ";"));
            sb.append(ex.getMessage());
            logger.error(sb.toString(), ex);
        }
    }

    /**
     * 发送微信退款失败邮件
     * 
     * @param orderId
     */
    public void sendWxRefundFailEmail(String orderId) {
        String[] toMailAddress = emailConfig.getToMailAddress().split(";");

        sendHtmlMail(toMailAddress, "退款无法原路退回【微信用户账户异常或已注销】请告知用户",
                "Hi，洋大班： <br> <br>如下订单无法原路退回，原因是：用户账户异常或已注销，请告知用户，谢谢！。<br> <br> <br>订单号：" + orderId);
    }


    /**
     * 设置邮箱host，用户名和密码
     */
    @PostConstruct
    public void init() throws Exception {

        mailSender.setHost(emailConfig.getSmtpHost());
        mailSender.setUsername(emailConfig.getFromMailAddress());
        mailSender.setPassword(emailConfig.getFromMailPassword());
    }
}
