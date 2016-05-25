/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ymatou.payment.facade.BaseRequest;

/**
 * 支付宝异步退款通知request
 * 
 * @author qianmin 2016年5月16日 下午5:26:07
 * 
 */
public class AliPayRefundNotifyRequest extends BaseRequest {

    private static final long serialVersionUID = 6547133957669831614L;

    @JsonProperty("notify_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotNull
    private Date notify_time;

    @JsonProperty("notify_type")
    @NotEmpty
    private String notify_type;

    @JsonProperty("notify_id")
    @NotEmpty
    private String notify_id;

    @JsonProperty("sign_type")
    @NotEmpty
    private String sign_type;

    @JsonProperty("sign")
    @NotEmpty
    private String sign;

    @JsonProperty("batch_no")
    @NotEmpty
    private String batch_no;

    @JsonProperty("success_num")
    @NotEmpty
    private String success_num;

    @JsonProperty("result_details")
    @NotEmpty
    private String result_details;

    private String payType;

    public Date getNotify_time() {
        return notify_time;
    }

    public String getNotify_type() {
        return notify_type;
    }

    public String getNotify_id() {
        return notify_id;
    }

    public String getSign_type() {
        return sign_type;
    }

    public String getSign() {
        return sign;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public String getSuccess_num() {
        return success_num;
    }

    public String getResult_details() {
        return result_details;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
