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

    /**
     * 退款单号
     */
    private List<String> refundNos;
    /**
     * 审核人
     */
    private String approveUser;
    /**
     * http请求header(可不填)
     */
    private HashMap<String, String> header;

    public List<String> getRefundNos() {
        return refundNos;
    }

    public void setRefundNos(List<String> refundNos) {
        this.refundNos = refundNos;
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
