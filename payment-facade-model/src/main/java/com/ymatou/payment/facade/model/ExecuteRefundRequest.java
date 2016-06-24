/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.util.HashMap;

import com.ymatou.payment.facade.BaseRequest;

/**
 * 执行退款Request
 * 
 * @author qianmin 2016年6月24日 下午2:11:19
 *
 */
public class ExecuteRefundRequest extends BaseRequest {

    private static final long serialVersionUID = -3031119076319521163L;

    private String refundNo;

    private HashMap<String, String> header;

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }
}
