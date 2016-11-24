/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.integration.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 取消查询请求报文
 * 
 * @author wangxudong 2016年11月24日 下午7:56:12
 *
 */
public class CmbAggrementQueryRequest extends CmbDTO {

    @Override
    public String buildSignString() {
        return String.format("agrNo=%s&branchNo=%s&dateTime=%s&merchantNo=%s&merchantSerialNo=%s&txCode=%s",
                reqData.getAgrNo(), reqData.getBranchNo(), reqData.getDateTime(), reqData.getMerchantNo(),
                reqData.getMerchantSerialNo(), reqData.getTxCode()).replace("null", "");
    }

    /**
     * @return the reqData
     */
    public AggrementQueryReqData getReqData() {
        return reqData;
    }

    /**
     * @param reqData the reqData to set
     */
    public void setReqData(AggrementQueryReqData reqData) {
        this.reqData = reqData;
    }

    private AggrementQueryReqData reqData = new AggrementQueryReqData();

    public class AggrementQueryReqData {
        public AggrementQueryReqData() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            this.dateTime = simpleDateFormat.format(new Date());
        }

        /**
         * 请求时间 yyyyMMddHHmmss
         */
        private String dateTime;

        /**
         * 交易码,本接口固定为CMCX
         */
        private String txCode = "CMCX";

        /**
         * 商户分行号，4位数字
         */
        private String branchNo;

        /**
         * 商户号，6位数字
         */
        private String merchantNo;

        /**
         * 商户流水号,商户做此查询请求的流水号
         */
        private String merchantSerialNo;

        /**
         * 客户签约的协议号
         */
        private String agrNo;

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
    }

}
