/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.constants;

/**
 * 招行一网通签约状态
 * 
 * @author qianmin 2016年6月8日 下午3:11:36
 *
 */
public enum CmbAggrementStatusEnum {

    INIT(0), SIGN(1), UNSIGN(2), FAIL(3);

    private Integer code;

    private CmbAggrementStatusEnum(Integer code) {
        this.code = code;
    }

    public Integer code() {
        return this.code;
    }
}
