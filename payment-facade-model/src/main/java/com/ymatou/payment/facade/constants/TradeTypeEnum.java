/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.constants;

/**
 * 
 * @author qianmin 2016年5月10日 下午7:26:04
 * 
 */
public enum TradeTypeEnum {
    EARNEST(1), // 定金
    REPLENISHMENT(2); // 补款

    private int code;

    private TradeTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
