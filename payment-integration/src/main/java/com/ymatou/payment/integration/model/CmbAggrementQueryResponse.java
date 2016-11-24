/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.integration.model;

/**
 * 取消协议响应报文
 * 
 * @author wangxudong 2016年11月24日 下午7:56:12
 *
 */
public class CmbAggrementQueryResponse extends CmbDTO {

    @Override
    public String buildSignString() {
        return String.format(
                "agrNo=%s&dateTime=%s&merchantSerialNo=%s&noPwdPay=%s&rspCode=%s&rspMsg=%s&userPidHash=%s&userPidType=%s&userSignDateTime=%s",
                rspData.getAgrNo(), rspData.getDateTime(), rspData.getMerchantSerialNo(), rspData.getNoPwdPay(),
                rspData.getRspCode(), rspData.getRspMsg(), rspData.getUserPidHash(), rspData.getUserPidType(),
                rspData.getUserSignDateTime());
    }


    /**
     * @return the rspData
     */
    public AggrementQueryRspData getRspData() {
        return rspData;
    }

    /**
     * @param rspData the rspData to set
     */
    public void setRspData(AggrementQueryRspData rspData) {
        this.rspData = rspData;
    }


    private AggrementQueryRspData rspData = new AggrementQueryRspData();

    public class AggrementQueryRspData {


        /**
         * 处理结果,SUC0000：请求处理成功
         * 其他：请求处理失败
         */
        private String rspCode;

        /**
         * 详细信息,失败时返回具体失败原因
         */
        private String rspMsg;

        /**
         * 响应时间,银行返回该数据的时间格式：yyyyMMddHHmmss
         */
        private String dateTime;

        /**
         * 商户流水号,原样返回商户做请求时的流水号
         */
        private String merchantSerialNo;

        /**
         * 客户协议号，成功交易返回
         */
        private String agrNo;

        /**
         * 客户签订协议的时间
         * yyyyMMddHHmmss，成功交易返回
         */
        private String userSignDateTime;

        /**
         * 证件类型 目前只有”1”，表示身份证
         */
        private String userPidType;

        /**
         * 证件号映射的30位hash值，成功交易返回
         */
        private String userPidHash;

        /**
         * 免密标识，固定为”N”，表示不开通免密。
         */
        private String noPwdPay;

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
         * @return the userSignDateTime
         */
        public String getUserSignDateTime() {
            return userSignDateTime;
        }

        /**
         * @param userSignDateTime the userSignDateTime to set
         */
        public void setUserSignDateTime(String userSignDateTime) {
            this.userSignDateTime = userSignDateTime;
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
