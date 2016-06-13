/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.constants;

/**
 * 账务操作状态
 * 
 * @author qianmin 2016年6月8日 下午3:11:36
 *
 */
public enum AccountingStatusEnum {

    SUCCESS(1), FAIL(0);

    private Integer code;

    private AccountingStatusEnum(Integer code) {
        this.code = code;
    }

    public Integer code() {
        return this.code;
    }
}
