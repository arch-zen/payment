/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.constants;

/**
 * 商户订单状态
 * 
 * @author wangxudong 2016年5月11日 上午10:58:36
 *
 */
public enum OrderStatusEnum {
    Init(0, "初始化"),

    Paied(1, "已支付"),

    Failed(-1, "支付失败");

    private int index;
    private String name;

    private OrderStatusEnum(int index, String name) {
        this.index = index;
        this.name = name;
    }

    /**
     * 返回枚举索引值
     * 
     * @return
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * 返回枚举名称
     * 
     * @return
     */
    public String getName() {
        return this.name;
    }
}
