/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.integration.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 一网通支付请求报文
 * 
 * @author wangxudong 2016年11月14日 下午4:54:45
 *
 */
public class CmbPayRequest extends CmbDTO {

    @Override
    public String buildSignString() {
        return String.format(
                "agrNo=%s&amount=%s&branchNo=%s&date=%s&dateTime=%s&expireTimeSpan=%s&merchantNo=%s&merchantSerialNo=%s&orderNo=%s&payNoticePara=%s&payNoticeUrl=%s&returnUrl=%s&signNoticePara=%s&signNoticeUrl=%s",
                reqData.getAgrNo(), reqData.getAmount(), reqData.getBranchNo(), reqData.getDate(),
                reqData.getDateTime(),
                reqData.getExpireTimeSpan(), reqData.getMerchantNo(), reqData.getMerchantSerialNo(),
                reqData.getOrderNo(), reqData.getPayNoticePara(), reqData.getPayNoticeUrl(), reqData.getReturnUrl(),
                reqData.getSignNoticePara(), reqData.getSignNoticeUrl());
    }

    private PayReqData reqData = new PayReqData();

    /**
     * @return the reqData
     */
    public PayReqData getReqData() {
        return reqData;
    }

    /**
     * @param reqData the reqData to set
     */
    public void setReqData(PayReqData reqData) {
        this.reqData = reqData;
    }

    public class PayReqData {

        public PayReqData() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            this.dateTime = simpleDateFormat.format(new Date());
        }


        /**
         * 请求时间
         */
        private String dateTime;

        /**
         * 分行号
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
         * 10位数字，由商户生成，一天内不能重复。
         * 订单日期+订单号唯一定位一笔订单
         * 
         */
        private String orderNo;

        /**
         * 金额:格式：xxxx.xx
         * 固定两位小数，最大11位整数
         */
        private String amount;

        /**
         * 过期时间跨度
         * 必须为大于零的整数，单位为分钟
         */
        private String expireTimeSpan = "120";

        /**
         * 成功支付结果通知地址
         */
        private String payNoticeUrl;

        /**
         * 成功支付结果通知附加参数
         */
        private String payNoticePara;

        /**
         * 成功页返回商户地址
         * 默认值：http://CMBNPRM，采用默认值的需要商户app拦截该请求，自行决定跳转交互
         */
        private String returnUrl;

        /**
         * 客户协议号，商户生成，必须为纯数字串。
         * 首次签约，填写新增的协议号，用于协议开通；
         * 已签约，填写客户协议号。
         * 
         */
        private String agrNo;

        /**
         * 协议开通请求流水号，开通协议时必填。
         */
        private String merchantSerialNo;

        /**
         * 成功签约结果通知
         */
        private String signNoticeUrl;

        /**
         * 成功签约结果通知附加参数
         */
        private String signNoticePara;

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
         * @return the expireTimeSpan
         */
        public String getExpireTimeSpan() {
            return expireTimeSpan;
        }

        /**
         * @param expireTimeSpan the expireTimeSpan to set
         */
        public void setExpireTimeSpan(String expireTimeSpan) {
            this.expireTimeSpan = expireTimeSpan;
        }

        /**
         * @return the payNoticeUrl
         */
        public String getPayNoticeUrl() {
            return payNoticeUrl;
        }

        /**
         * @param payNoticeUrl the payNoticeUrl to set
         */
        public void setPayNoticeUrl(String payNoticeUrl) {
            this.payNoticeUrl = payNoticeUrl;
        }

        /**
         * @return the payNoticePara
         */
        public String getPayNoticePara() {
            return payNoticePara;
        }

        /**
         * @param payNoticePara the payNoticePara to set
         */
        public void setPayNoticePara(String payNoticePara) {
            this.payNoticePara = payNoticePara;
        }

        /**
         * @return the returnUrl
         */
        public String getReturnUrl() {
            return returnUrl;
        }

        /**
         * @param returnUrl the returnUrl to set
         */
        public void setReturnUrl(String returnUrl) {
            this.returnUrl = returnUrl;
        }

        /**
         * @return the agrNo
         */
        public String getAgrNo() {
            return agrNo;
        }

        /**
         * @param agrNo the agrNo to set
         */
        public void setAgrNo(String agrNo) {
            this.agrNo = agrNo;
        }

        /**
         * @return the merchantSerialNo
         */
        public String getMerchantSerialNo() {
            return merchantSerialNo;
        }

        /**
         * @param merchantSerialNo the merchantSerialNo to set
         */
        public void setMerchantSerialNo(String merchantSerialNo) {
            this.merchantSerialNo = merchantSerialNo;
        }

        /**
         * @return the signNoticeUrl
         */
        public String getSignNoticeUrl() {
            return signNoticeUrl;
        }

        /**
         * @param signNoticeUrl the signNoticeUrl to set
         */
        public void setSignNoticeUrl(String signNoticeUrl) {
            this.signNoticeUrl = signNoticeUrl;
        }

        /**
         * @return the signNoticePara
         */
        public String getSignNoticePara() {
            return signNoticePara;
        }

        /**
         * @param signNoticePara the signNoticePara to set
         */
        public void setSignNoticePara(String signNoticePara) {
            this.signNoticePara = signNoticePara;
        }


    }
}
