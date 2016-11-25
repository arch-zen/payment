/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.integration.model;

import com.alibaba.fastjson.JSONObject;
import com.ymatou.payment.integration.common.CmbSignature;

/**
 * 招行支付成功通知报文
 * 
 * @author wangxudong 2016年11月15日 下午4:46:47
 *
 */
public class CmbPayNotifyRequest extends CmbDTO {

    @Override
    public String buildSignString() {
        return CmbSignature.buildSignString(JSONObject.toJSONString(this), "noticeData");

        // return String.format(
        // "amount=%s&bankDate=%s&bankSerialNo=%s&branchNo=%s&dateTime=%s&discountAmount=%s&discountFlag=%s&httpMethod=%s&merchantNo=%s&merchantPara=%s&noticeSerialNo=%s&noticeType=%s&noticeUrl=%s&orderNo=%s",
        // noticeData.getAmount(), noticeData.getBankDate(), noticeData.getBankSerialNo(),
        // noticeData.getBranchNo(),
        // noticeData.getDateTime(), noticeData.getDiscountAmount(), noticeData.getDiscountFlag(),
        // noticeData.getHttpMethod(),
        // noticeData.getMerchantNo(), noticeData.getMerchantPara(), noticeData.getNoticeSerialNo(),
        // noticeData.getNoticeType(),
        // noticeData.getNoticeUrl(), noticeData.getOrderNo()).replace("null", "");
    }

    /**
     * @return the noticeData
     */
    public PayNoticeData getNoticeData() {
        return noticeData;
    }


    /**
     * @param noticeData the noticeData to set
     */
    public void setNoticeData(PayNoticeData noticeData) {
        this.noticeData = noticeData;
    }

    private PayNoticeData noticeData = new PayNoticeData();


    public class PayNoticeData {
        /**
         * 请求时间，银行返回该数据的时间，精确到秒
         * 格式：yyyyMMddHHmmss
         */
        private String dateTime;

        /**
         * 回调HTTP地址，支付请求时填写的支付结果通知地址
         */
        private String noticeUrl;

        /**
         * 回调HTTP方法，固定为“POST“
         */
        private String httpMethod;

        /**
         * 商户分行号，4位数字
         */
        private String branchNo;

        /**
         * 商户号，6位数字
         */
        private String merchantNo;

        /**
         * 通知类型，本接口固定为：“BKPAYRTN”
         */
        private String noticeType;

        /**
         * 银行通知序号
         */
        private String noticeSerialNo;

        /**
         * 商户订单日期格式：yyyyMMdd
         */
        private String date;

        /**
         * 商户订单号
         */
        private String orderNo;

        /**
         * 支付金额，格式：XXXX.XX
         */
        private String amount;

        /**
         * 银行受理日期
         */
        private String bankDate;

        /**
         * 银行订单流水号
         */
        private String bankSerialNo;

        /**
         * 优惠标志，Y:有优惠 N：无优惠
         */
        private String discountFlag;

        /**
         * 优惠金额，单位为元，精确到小数点后两位。格式为：xxxx.xx元
         */
        private String discountAmount;

        /**
         * 商户附加参数，原样返回商户在一网通支付请求报文中传送的成功支付结果通知附加参数
         */
        private String merchantPara;

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
         * @return the noticeUrl
         */
        public String getNoticeUrl() {
            return noticeUrl;
        }

        /**
         * @param noticeUrl the noticeUrl to set
         */
        public void setNoticeUrl(String noticeUrl) {
            this.noticeUrl = noticeUrl;
        }

        /**
         * @return the httpMethod
         */
        public String getHttpMethod() {
            return httpMethod;
        }

        /**
         * @param httpMethod the httpMethod to set
         */
        public void setHttpMethod(String httpMethod) {
            this.httpMethod = httpMethod;
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
         * @return the noticeType
         */
        public String getNoticeType() {
            return noticeType;
        }

        /**
         * @param noticeType the noticeType to set
         */
        public void setNoticeType(String noticeType) {
            this.noticeType = noticeType;
        }

        /**
         * @return the noticeSerialNo
         */
        public String getNoticeSerialNo() {
            return noticeSerialNo;
        }

        /**
         * @param noticeSerialNo the noticeSerialNo to set
         */
        public void setNoticeSerialNo(String noticeSerialNo) {
            this.noticeSerialNo = noticeSerialNo;
        }

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
         * @return the orderNo
         */
        public String getOrderNo() {
            return orderNo;
        }

        /**
         * @param orderNo the orderNo to set
         */
        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        /**
         * @return the amount
         */
        public String getAmount() {
            return amount;
        }

        /**
         * @param amount the amount to set
         */
        public void setAmount(String amount) {
            this.amount = amount;
        }

        /**
         * @return the bankDate
         */
        public String getBankDate() {
            return bankDate;
        }

        /**
         * @param bankDate the bankDate to set
         */
        public void setBankDate(String bankDate) {
            this.bankDate = bankDate;
        }

        /**
         * @return the bankSerialNo
         */
        public String getBankSerialNo() {
            return bankSerialNo;
        }

        /**
         * @param bankSerialNo the bankSerialNo to set
         */
        public void setBankSerialNo(String bankSerialNo) {
            this.bankSerialNo = bankSerialNo;
        }

        /**
         * @return the discountFlag
         */
        public String getDiscountFlag() {
            return discountFlag;
        }

        /**
         * @param discountFlag the discountFlag to set
         */
        public void setDiscountFlag(String discountFlag) {
            this.discountFlag = discountFlag;
        }

        /**
         * @return the discountAmount
         */
        public String getDiscountAmount() {
            return discountAmount;
        }

        /**
         * @param discountAmount the discountAmount to set
         */
        public void setDiscountAmount(String discountAmount) {
            this.discountAmount = discountAmount;
        }

        /**
         * @return the merchantPara
         */
        public String getMerchantPara() {
            return merchantPara;
        }

        /**
         * @param merchantPara the merchantPara to set
         */
        public void setMerchantPara(String merchantPara) {
            this.merchantPara = merchantPara;
        }

    }
}
