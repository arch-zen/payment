/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.constants;


/**
 * 支付类型枚举
 * 
 * @author wangxudong 2016年6月23日 下午6:36:04
 *
 */
public enum BizCodeEnum {
    /**
     * 补款充值
     */
    POST_RECHARGE(1),

    /**
     * 定金充值
     */
    PAY_RECHARGE(3),

    /**
     * 卖家注册保证金充值
     */
    SELLER_MARGIN_RECHARGE(4),

    /**
     * 卖家余额充值
     */
    SELLER_RECHARGE(5);

    private Integer code;

    private BizCodeEnum(Integer code) {
        this.code = code;
    }

    public Integer code() {
        return this.code;
    }

    /**
     * 枚举转换
     * 
     * @param index
     * @return
     */
    public static BizCodeEnum parse(int index) {
        switch (index) {
            case 1:
                return POST_RECHARGE;
            case 3:
                return PAY_RECHARGE;
            case 4:
                return SELLER_MARGIN_RECHARGE;
            case 5:
                return SELLER_RECHARGE;
            default:
                return null;
        }
    }
}
