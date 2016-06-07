/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.constants;

/**
 * 资金操作类型
 * 
 * @author qianmin 2016年6月6日 下午7:42:30
 *
 */
public enum AccountOperateTypeEnum {
    // 可用资金入账
    Fundin(1),
    // 可用资金出账
    Fundout(2),
    // 冻结资金入账
    Freezein(3),
    // 冻结资金出账
    Freezeout(4);

    private AccountOperateTypeEnum(int code) {
        this.code = code;
    }

    private int code;

    public int code() {
        return this.code;
    }
}
