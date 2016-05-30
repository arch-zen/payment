/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.model;

import com.ymatou.payment.facade.BaseResponse;

public class PaymentNotifyResp extends BaseResponse {

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = 2810460658379056916L;

    /**
     * 回调处理结果
     */
    private String result;

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(String result) {
        this.result = result;
    }

}
