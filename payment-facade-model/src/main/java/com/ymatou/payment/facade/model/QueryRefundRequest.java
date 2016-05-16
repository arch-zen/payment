/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.util.List;

import com.ymatou.payment.facade.BaseRequest;

/**
 * 查询退款单Request
 * 
 * @author qianmin 2016年5月13日 下午3:18:24
 * 
 */
public class QueryRefundRequest extends BaseRequest {

    private static final long serialVersionUID = -1288101039491576260L;

    /**
     * 检索关键字
     */
    private String key;
    /**
     * 每页条数
     */
    private int pageSize;
    /**
     * 页码
     */
    private int pageIndex;
    /**
     * 审核状态
     */
    private int approveStatus;
    /**
     * 退款状态
     */
    private List<Integer> refundStatus;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(int approveStatus) {
        this.approveStatus = approveStatus;
    }

    public List<Integer> getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(List<Integer> refundStatus) {
        this.refundStatus = refundStatus;
    }
}
