/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.constants;

/**
 * 账户类型
 * 
 * @author qianmin 2016年6月6日 下午7:47:41
 *
 */
public enum AccountTypeEnum {
    Undefined(0),
    // 人民币账户（默认账户）
    RmbAccount(1),
    // 美元账户
    UsdAccount(4),
    // 注册保证金人民币账户
    RegisteredDepositRmbAccount(11),
    // 快周转保证金人民币账户
    FastTurnaroundDepositRmbAccount(12),
    // 系统内部人民币账户
    SystemRmbAccount(21);

    private AccountTypeEnum(int code) {
        this.code = code;
    }

    private int code;

    public int code() {
        return this.code;
    }
}
