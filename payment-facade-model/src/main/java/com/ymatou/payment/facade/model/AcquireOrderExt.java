/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class AcquireOrderExt {
    /**
     * 扫码方式
     */
    @JsonProperty("SHOWMODE")
    private String showMode;

    /**
     * 支付方式
     * 
     * @see AliPayConsts#PAY_METHOD
     */
    @JsonProperty("PAYMETHOD")
    private String payMethod;

    /**
     * 是否杭报订单
     */
    @JsonProperty("ISHANGZHOU")
    private Integer isHangZhou;

    /**
     * @return the showMode
     */
    public String getShowMode() {
        return showMode;
    }

    /**
     * @param showMode the showMode to set
     */
    public void setShowMode(String showMode) {
        this.showMode = showMode;
    }

    /**
     * @return the payMethod
     */
    public String getPayMethod() {
        return payMethod;
    }

    /**
     * @param payMethod the payMethod to set
     */
    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    /**
     * @return the isHangZhou
     */
    public Integer getIsHangZhou() {
        return isHangZhou;
    }

    /**
     * @param isHangZhou the isHangZhou to set
     */
    public void setIsHangZhou(Integer isHangZhou) {
        this.isHangZhou = isHangZhou;
    }
}
