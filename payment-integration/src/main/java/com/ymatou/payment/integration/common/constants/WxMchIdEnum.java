/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.common.constants;

/**
 * ymatou在微信支付的商户号
 * 
 * @author qianmin 2016年5月9日 上午10:42:18
 *
 */
public enum WxMchIdEnum {

    APP("1234079001"), JSAPI("1278350701");

    private String mchId;

    private WxMchIdEnum(String mchId) {
        this.mchId = mchId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }
}
