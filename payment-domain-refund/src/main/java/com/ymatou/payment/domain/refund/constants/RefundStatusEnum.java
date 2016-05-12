/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.constants;

/**
 * 
 * @author qianmin 2016年5月10日 下午7:17:14
 * 
 */
public enum RefundStatusEnum {

    COMPLETE_FAILED(-2), REFUND_FAILED(-1), INIT(0), COMMIT(1), WAIT_THIRDPART_REFUND(2), THIRDPART_REFUND_SUCCESS(
            3), COMPLETE_SUCCESS(4), RETURN_TRANSACTION(5), RETURN_BALANCE(6);
    private int code;

    private RefundStatusEnum(int code) {
        this.code = code;
    }



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
