/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.model;

import com.ymatou.payment.facade.constants.AcquireOrderResultTypeEnum;

/**
 * 收单报文请求
 * 
 * @author wangxudong 2016年5月11日 下午5:03:57
 *
 */
public class AcquireOrderPackageResp {
    /**
     * 返回报文
     */
    private String result;

    /**
     * 返回报文类型
     */
    private AcquireOrderResultTypeEnum resultType;

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

    /**
     * @return the resultType
     */
    public AcquireOrderResultTypeEnum getResultType() {
        return resultType;
    }

    /**
     * @param resultType the resultType to set
     */
    public void setResultType(AcquireOrderResultTypeEnum resultType) {
        this.resultType = resultType;
    }
}
