package com.ymatou.payment.integration;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import org.springframework.stereotype.Component;

/**
 * Created by wangxudong on 2017/3/21.
 */
@Component
@DisconfFile(fileName = "email.properties")
public class EmailConfig {
    /**
     * 邮件服务器地址
     */
    private String smtpHost;
    /**
     * 发件人邮箱
     */
    private String fromMailAddress;
    /**
     * 发件人邮箱密码
     */
    private String fromMailPassword;
    /**
     * 收件人地址列表
     */
    private String toMailAddress;

    @DisconfFileItem(name = "smtpHost")
    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    @DisconfFileItem(name = "fromMailAddress")
    public String getFromMailAddress() {
        return fromMailAddress;
    }

    public void setFromMailAddress(String fromMailAddress) {
        this.fromMailAddress = fromMailAddress;
    }

    @DisconfFileItem(name = "fromMailPassword")
    public String getFromMailPassword() {
        return fromMailPassword;
    }

    public void setFromMailPassword(String fromMailPassword) {
        this.fromMailPassword = fromMailPassword;
    }

    @DisconfFileItem(name = "toMailAddress")
    public String getToMailAddress() {
        return toMailAddress;
    }

    public void setToMailAddress(String toMailAddress) {
        this.toMailAddress = toMailAddress;
    }
}
