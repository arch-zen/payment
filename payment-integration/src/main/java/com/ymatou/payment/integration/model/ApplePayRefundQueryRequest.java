package com.ymatou.payment.integration.model;

/**
 * Created by zhangxiaoming on 2017/4/28.
 */
public class ApplePayRefundQueryRequest extends ApplePayTradeQueryRequest {
    public ApplePayRefundQueryRequest() {
        this.setTxnType("00"); //交易类型-退货
        this.setTxnSubType("00");
        this.setBizType("000201"); //000201：B2C 网关支付
    }
}
