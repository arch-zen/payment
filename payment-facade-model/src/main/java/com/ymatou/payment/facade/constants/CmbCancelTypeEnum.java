/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.constants;

/**
 * 招行一网通解约类型
 * 
 * @author qianmin 2016年6月8日 下午3:11:36
 *
 */
public enum CmbCancelTypeEnum {

    NOCANCEL(0), CUST(1), BANK(2);

    private Integer code;

    private CmbCancelTypeEnum(Integer code) {
        this.code = code;
    }

    public Integer code() {
        return this.code;
    }
}
