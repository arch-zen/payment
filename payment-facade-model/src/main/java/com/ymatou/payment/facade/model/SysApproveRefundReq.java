/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.model;

import com.ymatou.payment.facade.BaseRequest;

/**
 * 系统自动审核退款请求
 * 
 * @author wangxudong 2016年6月13日 下午12:11:08
 *
 */
public class SysApproveRefundReq extends BaseRequest {

    /**
     * 序列化版本
     */
    private static final long serialVersionUID = 4875984275998773811L;

    /**
     * 业务主键：yyyy-MM-dd HH:00:00
     * 
     * 审批业务主键代表的前一个小时的退款申请
     */
    private String bizId;

    /**
     * @return the bizId
     */
    public String getBizId() {
        return bizId;
    }

    /**
     * @param bizId the bizId to set
     */
    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

}
