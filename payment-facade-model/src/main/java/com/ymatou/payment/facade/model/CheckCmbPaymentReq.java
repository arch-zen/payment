/**
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.model;

import org.hibernate.validator.constraints.NotEmpty;

import com.ymatou.payment.facade.BaseRequest;

/**
 * 招行反向对账请求
 * 
 * @author wangxudong 2017年1月17日 上午9:42:06
 *
 */
public class CheckCmbPaymentReq extends BaseRequest {

    /**
     * 序列化版本
     */
    private static final long serialVersionUID = -8917122967803664376L;

    /**
     * 对账调度日期
     */
    @NotEmpty(message = "调度日期不能为空")
    private String date;

    /**
     * 对账序号
     */
    @NotEmpty(message = "对账序号不能为空")
    private String no;

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the no
     */
    public String getNo() {
        return no;
    }

    /**
     * @param no the no to set
     */
    public void setNo(String no) {
        this.no = no;
    }
}
