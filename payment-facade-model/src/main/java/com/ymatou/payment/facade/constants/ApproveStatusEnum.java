/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.constants;

/**
 * 审核状态
 * 
 * @author qianmin 2016年5月10日 下午7:13:20
 * 
 */
public enum ApproveStatusEnum {
    FAST_REFUND(9), NOT_APPROVED(0), APPROVED(1);

    private int code;

    private ApproveStatusEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
