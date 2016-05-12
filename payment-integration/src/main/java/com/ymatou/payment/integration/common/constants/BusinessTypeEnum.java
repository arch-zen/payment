/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.common.constants;

/**
 * 消息业务类型
 * 
 * @author qianmin 2016年5月11日 下午2:09:42
 * 
 */
public enum BusinessTypeEnum {
    FAST_REFUND("20"), REFUND_SUCCESS("25");

    private String code;

    private BusinessTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
