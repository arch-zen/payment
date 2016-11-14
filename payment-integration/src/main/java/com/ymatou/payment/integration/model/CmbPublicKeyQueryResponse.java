/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.integration.model;

/**
 * 一网通公钥响应报文
 * 
 * @author wangxudong 2016年11月10日 下午3:43:51
 *
 */
public class CmbPublicKeyQueryResponse extends CmbDTO {

    /**
     * 响应时间
     */
    private PublicKeyQueryRspData rspData;

    /**
     * @return the rspData
     */
    public PublicKeyQueryRspData getRspData() {
        return rspData;
    }

    /**
     * @param rspData the rspData to set
     */
    public void setRspData(PublicKeyQueryRspData rspData) {
        this.rspData = rspData;
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.ymatou.payment.integration.model.CmbDTO#getSignString()
     */
    @Override
    public String buildSignString() {
        String sign;
        if ("SUC0000".equals(rspData.getRspCode())) {
            sign = String.format("dateTime=%s&fbPubKey=%s&rspCode=%s&rspMsg=%s", rspData.getDateTime(),
                    rspData.getFbPubKey(), rspData.getRspCode(), rspData.getRspMsg());
        } else {
            sign = String.format("dateTime=%s&rspCode=%s&rspMsg=%s", rspData.getDateTime(),
                    rspData.getRspCode(), rspData.getRspMsg());
        }
        return sign;
    }

    public class PublicKeyQueryRspData {
        /**
         * 处理结果
         */
        private String rspCode;

        /**
         * 详细信息
         */
        private String rspMsg;

        /**
         * 招行公钥
         */
        private String fbPubKey;

        /**
         * 响应时间
         */
        private String dateTime;

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
         * @return the fbPubKey
         */
        public String getFbPubKey() {
            return fbPubKey;
        }

        /**
         * @param fbPubKey the fbPubKey to set
         */
        public void setFbPubKey(String fbPubKey) {
            this.fbPubKey = fbPubKey;
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
    }
}
