/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.integration.model;

/**
 * 招行签约成功通知报文
 * 
 * @author wangxudong 2016年11月15日 下午4:46:47
 *
 */
public class CmbSignNotifyRequest extends CmbDTO {

    @Override
    public String buildSignString() {
        return String.format(
                "agrNo=%s&branchNo=%s&dateTime=%s&httpMethod=%s&merchantNo=%s&noPwdPay=%s&noticePara=%s&noticeSerialNo=%s&noticeType=%s&noticeUrl=%s&rspCode=%s&rspMsg=%s&userID=%s&userPidHash=%s&userPidType=%s",
                noticeData.getAgrNo(), noticeData.getBranchNo(), noticeData.getDateTime(), noticeData.getHttpMethod(),
                noticeData.getMerchantNo(), noticeData.getNoPwdPay(), noticeData.getNoticePara(),
                noticeData.getNoticeSerialNo(),
                noticeData.getNoticeType(), noticeData.getNoticeUrl(), noticeData.getRspCode(), noticeData.getRspMsg(),
                noticeData.getUserID(), noticeData.getUserPidHash(), noticeData.getUserPidType());
    }

    /**
     * @return the noticeData
     */
    public SignNoticeData getNoticeData() {
        return noticeData;
    }


    /**
     * @param noticeData the noticeData to set
     */
    public void setNoticeData(SignNoticeData noticeData) {
        this.noticeData = noticeData;
    }

    /**
     * 签约通知
     */
    private SignNoticeData noticeData = new SignNoticeData();


    /**
     * 签约通知报文
     *
     */
    public class SignNoticeData {
        /**
         * 请求时间
         * 银行返回该数据的时间格式：yyyyMMddHHmmss
         */
        private String dateTime;

        /**
         * 处理结果,SUC0000：签约成功
         * 其他：签约失败
         * 注意：通常只有签约成功才会发送签约结果通知，但极少数异常情况下可能会发送签约失败的结果通知。因此，商户收到通知报文后，应先判断rspCode是否为SUC0000,
         * 以确认签约是否成功。
         */
        private String rspCode;

        /**
         * 详细信息,失败时返回的失败原因
         */
        private String rspMsg;

        /**
         * 通知类型,本接口，固定为BKQY
         */
        private String noticeType;

        /**
         * 银行通知序号
         */
        private String noticeSerialNo;

        /**
         * 商户分行号，4位数字
         */
        private String branchNo;

        /**
         * 商户号，6位数字
         */
        private String merchantNo;

        /**
         * 签约请求时填写的签约结果通知地址
         */
        private String noticeUrl;

        /**
         * 回调HTTP地址,固定为“POST”
         */
        private String httpMethod;

        /**
         * 签约请求时填写的客户协议号
         */
        private String agrNo;

        /**
         * 原样返回商户在签约或一网通支付请求报文中传送的成功签约通知附加参数
         */
        private String noticePara;

        /**
         * 签约请求时填写的用于标识商户用户的唯一ID
         */
        private String userID;

        /**
         * 用户证件类型，1：身份证
         */
        private String userPidType;

        /**
         * 证件号映射的30位hash值，成功交易返回
         */
        private String userPidHash;

        /**
         * 免密标识，固定为“N”，表示不开通免密。
         */
        private String noPwdPay;

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
         * @return the rspCode
         */
        public String getRspCode() {
            return rspCode;
        }

        /**
         * @param rspCode the rspCode to set
         */
        public void setRspCode(String rspCode) {
            this.rspCode = rspCode;
        }

        /**
         * @return the rspMsg
         */
        public String getRspMsg() {
            return rspMsg;
        }

        /**
         * @param rspMsg the rspMsg to set
         */
        public void setRspMsg(String rspMsg) {
            this.rspMsg = rspMsg;
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
         * @return the noticePara
         */
        public String getNoticePara() {
            return noticePara;
        }

        /**
         * @param noticePara the noticePara to set
         */
        public void setNoticePara(String noticePara) {
            this.noticePara = noticePara;
        }

        /**
         * @return the userID
         */
        public String getUserID() {
            return userID;
        }

        /**
         * @param userID the userID to set
         */
        public void setUserID(String userID) {
            this.userID = userID;
        }

        /**
         * @return the userPidType
         */
        public String getUserPidType() {
            return userPidType;
        }

        /**
         * @param userPidType the userPidType to set
         */
        public void setUserPidType(String userPidType) {
            this.userPidType = userPidType;
        }

        /**
         * @return the userPidHash
         */
        public String getUserPidHash() {
            return userPidHash;
        }

        /**
         * @param userPidHash the userPidHash to set
         */
        public void setUserPidHash(String userPidHash) {
            this.userPidHash = userPidHash;
        }

        /**
         * @return the noPwdPay
         */
        public String getNoPwdPay() {
            return noPwdPay;
        }

        /**
         * @param noPwdPay the noPwdPay to set
         */
        public void setNoPwdPay(String noPwdPay) {
            this.noPwdPay = noPwdPay;
        }

    }
}
