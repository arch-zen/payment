/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.constants;

/**
 * 支付通知状态枚举
 * 
 * @author wangxudong 2016年6月22日 下午6:52:23
 *
 */
public enum PaymentNotifyStatusEnum {

    /**
     * 已经调用资金服务完成（充值操作）
     */
    ACCOUNTED(1),


    /**
     * 已经成功通知调用系统（交易服务、商家后台）
     */
    NOTIFIED(2);

    private Integer code;

    private PaymentNotifyStatusEnum(Integer code) {
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
    public static PaymentNotifyStatusEnum parse(int index) {
        switch (index) {
            case 1:
                return ACCOUNTED;
            case 2:
                return NOTIFIED;
            default:
                return null;
        }
    }
}
