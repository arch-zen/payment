/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.constants;

/**
 * 币种枚举
 * 
 * @author qianmin 2016年6月6日 下午7:36:41
 *
 */
public enum CurrencyTypeEnum {
    // 未定义
    Undefined(0),

    // 人民币
    CNY(1),

    // 英镑
    GBP(2),

    // 港币
    HKD(3),

    // 美元
    USD(4),
    //

    // 瑞士法郎
    CHF(5),

    // 新加坡元
    SGD(6),

    // 瑞典克朗
    SEK(7),

    // 丹麦克朗
    DKK(8),

    // 挪威克朗
    NOK(9),

    // 日元
    JPY(10),

    // 加拿大元
    CAD(11),

    // 澳大利亚元
    AUD(12),

    // 欧元
    EUR(13),

    // 新西兰元
    NZD(14),

    // 俄罗斯卢布
    RUB(15),

    // 澳门元
    MOP(16),

    // 注册保证金专用人民币
    CNYForRegister(1001);

    private CurrencyTypeEnum(int code) {
        this.code = code;
    }

    private int code;

    public int code() {
        return this.code;
    }
}
