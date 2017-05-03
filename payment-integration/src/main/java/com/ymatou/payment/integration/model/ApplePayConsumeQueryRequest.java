package com.ymatou.payment.integration.model;

/**
 * Created by gejianhua on 2017/4/28.
 * applepay消费查询请求信息
 */
public class ApplePayConsumeQueryRequest extends ApplePayTradeQueryRequest {

    public ApplePayConsumeQueryRequest(){
        this.setTxnType("00");
        this.setTxnSubType("00");
        this.setBizType("000201");
    }
}
