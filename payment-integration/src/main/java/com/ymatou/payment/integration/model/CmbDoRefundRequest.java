/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.integration.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 一网通退款请求报文
 * 
 * @author wangxudong 2016年11月10日 下午3:43:05
 *
 */
public class CmbDoRefundRequest extends CmbDTO {

    /**
     * 请求数据
     */
    private DoRefundReqData reqData = new DoRefundReqData();

    /**
     * @return the reqData
     */
    public DoRefundReqData getReqData() {
        return reqData;
    }

    /**
     * @param reqData the reqData to set
     */
    public void setReqData(DoRefundReqData reqData) {
        this.reqData = reqData;
    }



    @Override
    public String buildSignString() {
        return String.format(
                "amount=%s&branchNo=%s&date=%s&dateTime=%s&encrypType=%s&merchantNo=%s&operatorNo=%s&orderNo=%s&pwd=%s&refundSerialNo=%s",
                reqData.getAmount(), reqData.getBranchNo(), reqData.getDate(),
                reqData.getDateTime(), reqData.getEncrypType(), reqData.getMerchantNo(), reqData.getOperatorNo(),
                reqData.getOrderNo(), reqData.getPwd(), reqData.refundSerialNo);
    }

    public class DoRefundReqData {

        public DoRefundReqData() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            this.dateTime = simpleDateFormat.format(new Date());
        }

        /**
         * 请求时间 yyyyMMddHHmmss
         */
        private String dateTime;

        /**
         * 商户分行号
         */
        private String branchNo;

        /**
         * 商户号
         */
        private String merchantNo;

        /**
         * 订单日期
         */
        private String date;

        /**
         * 订单号
         */
        private String orderNo;

        /**
         * 退款流水号
         */
        private String refundSerialNo;

        /**
         * 退款金额
         */
        private String amount;

        /**
         * 退款描述
         */
        private String desc;

        /**
         * 操作员号
         */
        private String operatorNo = "9999";

        /**
         * 操作员密码加密算法
         */
        private String encrypType;

        /**
         * 操作员密码
         */
        private String pwd;

        /**
         * @return the dateTime
         */
        public String getDateTime() {
            return dateTime;
        }

        /**
         * @param dateTime the dateTime to set
         */
        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }


        /**
         * @return the branchNo
         */
        public String getBranchNo() {
            return branchNo;
        }

        /**
         * @param branchNo the branchNo to set
         */
        public void setBranchNo(String branchNo) {
            this.branchNo = branchNo;
        }

        /**
         * @return the merchantNo
         */
        public String getMerchantNo() {
            return merchantNo;
        }

        /**
         * @param merchantNo the merchantNo to set
         */
        public void setMerchantNo(String merchantNo) {
            this.merchantNo = merchantNo;
        }

        /**
         * @return the date
         */
        public final String getDate() {
            return date;
        }

        /**
         * @param date the date to set
         */
        public final void setDate(String date) {
            this.date = date;
        }

        /**
         * @return the orderNo
         */
        public final String getOrderNo() {
            return orderNo;
        }

        /**
         * @param orderNo the orderNo to set
         */
        public final void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        /**
         * @return the refundSerialNo
         */
        public final String getRefundSerialNo() {
            return refundSerialNo;
        }

        /**
         * @param refundSerialNo the refundSerialNo to set
         */
        public final void setRefundSerialNo(String refundSerialNo) {
            this.refundSerialNo = refundSerialNo;
        }

        /**
         * @return the amount
         */
        public final String getAmount() {
            return amount;
        }

        /**
         * @param amount the amount to set
         */
        public final void setAmount(String amount) {
            this.amount = amount;
        }

        /**
         * @return the desc
         */
        public final String getDesc() {
            return desc;
        }

        /**
         * @param desc the desc to set
         */
        public final void setDesc(String desc) {
            this.desc = desc;
        }

        /**
         * @return the operatorNo
         */
        public final String getOperatorNo() {
            return operatorNo;
        }

        /**
         * @param operatorNo the operatorNo to set
         */
        public final void setOperatorNo(String operatorNo) {
            this.operatorNo = operatorNo;
        }

        /**
         * @return the encrypType
         */
        public final String getEncrypType() {
            return encrypType;
        }

        /**
         * @param encrypType the encrypType to set
         */
        public final void setEncrypType(String encrypType) {
            this.encrypType = encrypType;
        }

        /**
         * @return the pwd
         */
        public final String getPwd() {
            return pwd;
        }

        /**
         * @param pwd the pwd to set
         */
        public final void setPwd(String pwd) {
            this.pwd = pwd;
        }
    }


}
