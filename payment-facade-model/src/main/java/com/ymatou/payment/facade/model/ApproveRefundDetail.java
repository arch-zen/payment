/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import com.ymatou.payment.facade.PrintFriendliness;

/**
 * 退款审核结果
 * 
 * @author qianmin 2016年5月13日 上午11:19:10
 * 
 */
public class ApproveRefundDetail extends PrintFriendliness {

    private static final long serialVersionUID = -6152679398050627204L;

    /**
     * 是否成功
     */
    private boolean okFlag;

    public boolean isOkFlag() {
        return okFlag;
    }

    public void setOkFlag(boolean okFlag) {
        this.okFlag = okFlag;
    }

    public ApproveRefundDetail(boolean okFlag) {
        this.okFlag = okFlag;
    }

    public ApproveRefundDetail() {}
}
