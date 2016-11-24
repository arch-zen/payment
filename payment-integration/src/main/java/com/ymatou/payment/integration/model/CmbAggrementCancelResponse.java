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
public class CmbAggrementCancelResponse extends CmbDTO {

    @Override
    public String buildSignString() {
        return String.format("bankSerialNo=%s&dateTime=%s&merchantSerialNo=%s&rspCode=%s&rspMsg=%s",
                rspData.getBankSerialNo(), rspData.getDateTime(), rspData.getMerchantSerialNo(), rspData.getRspCode(),
                rspData.getRspMsg());
    }


    /**
     * @return the rspData
     */
    public AggrementCancelRspData getRspData() {
        return rspData;
    }

    /**
     * @param rspData the rspData to set
     */
    public void setRspData(AggrementCancelRspData rspData) {
        this.rspData = rspData;
    }


    private AggrementCancelRspData rspData = new AggrementCancelRspData();

    public class AggrementCancelRspData {


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
         * 银行流水号,银行处理本请求的流水号
         */
        private String bankSerialNo;

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


    }

}
