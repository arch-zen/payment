/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.constants;

/**
 * 对账状态
 * 
 * @author qianmin 2016年5月20日 下午6:52:09
 *
 */
public enum CheckStatusEnum {
    SUCCESS(1), REPAIR_SUCCESS(2), THIRD_PART_NOT_PAID(-20), AMOUNT_NOT_MATCH(-15), FINAL_CHECK_FAIL_INDEX(
            -10), INIT_CHECK_FAIL_INDEX(-1);
    // -1到-10表示对账失败的次数
    private int code;

    private CheckStatusEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
