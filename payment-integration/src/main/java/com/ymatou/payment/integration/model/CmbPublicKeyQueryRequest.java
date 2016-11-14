/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.integration.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 一网通公钥请求报文
 * 
 * @author wangxudong 2016年11月10日 下午3:43:05
 *
 */
public class CmbPublicKeyQueryRequest extends CmbDTO {

    /**
     * 请求数据
     */
    private PublicKeyQueryReqData reqData = new PublicKeyQueryReqData();

    /**
     * @return the reqData
     */
    public PublicKeyQueryReqData getReqData() {
        return reqData;
    }

    /**
     * @param reqData the reqData to set
     */
    public void setReqData(PublicKeyQueryReqData reqData) {
        this.reqData = reqData;
    }



    @Override
    public String buildSignString() {
        return String.format("branchNo=%s&dateTime=%s&merchantNo=%s&txCode=%s", reqData.getBranchNo(),
                reqData.getDateTime(), reqData.getMerchantNo(), reqData.getTxCode());
    }

    public class PublicKeyQueryReqData {

        public PublicKeyQueryReqData() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            this.dateTime = simpleDateFormat.format(new Date());
        }

        /**
         * 请求时间 yyyyMMddHHmmss
         */
        private String dateTime;

        /**
         * 交易码 固定为FBPK
         */
        private String txCode = "FBPK";

        /**
         * 商户分行号
         */
        private String branchNo;

        /**
         * 商户号
         */
        private String merchantNo;

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
         * @return the txCode
         */
        public String getTxCode() {
            return txCode;
        }

        /**
         * @param txCode the txCode to set
         */
        public void setTxCode(String txCode) {
            this.txCode = txCode;
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
    }


}
