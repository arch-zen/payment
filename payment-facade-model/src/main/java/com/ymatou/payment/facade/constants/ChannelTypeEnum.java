/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.constants;

/**
 * 渠道类型
 *
 * @author qianmin 2016年5月24日 下午4:52:11
 */
public enum ChannelTypeEnum {

    WeiXinPay("2"), AliPay("1"), CmbPay("3"), ApplePay("4");

    private ChannelTypeEnum(String code) {
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
