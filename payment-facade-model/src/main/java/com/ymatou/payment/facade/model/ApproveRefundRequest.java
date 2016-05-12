/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.util.HashMap;
import java.util.List;

import com.ymatou.payment.facade.BaseRequest;

/**
 * 退款审核通过request
 * 
 * @author qianmin 2016年5月11日 下午3:28:03
 * 
 */
public class ApproveRefundRequest extends BaseRequest {

    private static final long serialVersionUID = 704177923514574213L;

    private List<String> paymentIds;
    private String approveUser;
    private HashMap<String, String> header;

    public List<String> getPaymentIds() {
        return paymentIds;
    }

    public void setPaymentIds(List<String> paymentIds) {
        this.paymentIds = paymentIds;
    }

    public String getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }

}
