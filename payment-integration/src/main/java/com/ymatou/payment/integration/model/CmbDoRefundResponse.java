/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.integration.model;

/**
 * 退款响应报文
 * 
 * @author wangxudong 2016年11月11日 下午5:09:28
 *
 */
public class CmbDoRefundResponse extends CmbDTO {

    @Override
    public String buildSignString() {
        return null;
    }

    public CmbDoRefundRspData rspData;

    public class CmbDoRefundRspData {
        /**
         * 处理结果
         */
        private String rspCode;

        /**
         * 响应信息
         */
        private String rspMesg;

        /**
         * 银行返回该数据的时间
         * 格式：yyyyMMddHHmmss
         * 
         */
        private String dateTime;

        /**
         * 银行的订单流水号
         */
        private String bankSerialNo;

        /**
         * 交易币种 固定为：“10”
         */
        private String currency;

        /**
         * 订单金额
         */
        private String amount;

        /**
         * 费用金额
         */
        private String refundRefNo;

        /**
         * 银行受理日期
         */
        private String bankDate;

        /**
         * 银行受理时间
         */
        private String bankTime;


        /**
         * 商户自定义参数
         * 商户在支付接口中传送的payNoticePara参数，超过100字节自动截断
         */
        private String merchantPara;

        /**
         * @return the rspCode
         */
        public final String getRspCode() {
            return rspCode;
        }

        /**
         * @param rspCode the rspCode to set
         */
        public final void setRspCode(String rspCode) {
            this.rspCode = rspCode;
        }

        /**
         * @return the rspMesg
         */
        public final String getRspMesg() {
            return rspMesg;
        }

        /**
         * @param rspMesg the rspMesg to set
         */
        public final void setRspMesg(String rspMesg) {
            this.rspMesg = rspMesg;
        }

        /**
         * @return the dateTime
         */
        public final String getDateTime() {
            return dateTime;
        }

        /**
         * @param dateTime the dateTime to set
         */
        public final void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        /**
         * @return the bankSerialNo
         */
        public final String getBankSerialNo() {
            return bankSerialNo;
        }

        /**
         * @param bankSerialNo the bankSerialNo to set
         */
        public final void setBankSerialNo(String bankSerialNo) {
            this.bankSerialNo = bankSerialNo;
        }

        /**
         * @return the currency
         */
        public final String getCurrency() {
            return currency;
        }

        /**
         * @param currency the currency to set
         */
        public final void setCurrency(String currency) {
            this.currency = currency;
        }

        /**
         * @return the bankDate
         */
        public final String getBankDate() {
            return bankDate;
        }

        /**
         * @param bankDate the bankDate to set
         */
        public final void setBankDate(String bankDate) {
            this.bankDate = bankDate;
        }

        /**
         * @return the bankTime
         */
        public final String getBankTime() {
            return bankTime;
        }

        /**
         * @param bankTime the bankTime to set
         */
        public final void setBankTime(String bankTime) {
            this.bankTime = bankTime;
        }


        /**
         * @return the merchantPara
         */
        public final String getMerchantPara() {
            return merchantPara;
        }

        /**
         * @param merchantPara the merchantPara to set
         */
        public final void setMerchantPara(String merchantPara) {
            this.merchantPara = merchantPara;
        }

        /**
         * @return the amount
         */
        protected final String getAmount() {
            return amount;
        }

        /**
         * @param amount the amount to set
         */
        protected final void setAmount(String amount) {
            this.amount = amount;
        }

        /**
         * @return the refundRefNo
         */
        protected final String getRefundRefNo() {
            return refundRefNo;
        }

        /**
         * @param refundRefNo the refundRefNo to set
         */
        protected final void setRefundRefNo(String refundRefNo) {
            this.refundRefNo = refundRefNo;
        }
    }

    /**
     * @return the rspData
     */
    public final CmbDoRefundRspData getRspData() {
        return rspData;
    }

    /**
     * @param rspData the rspData to set
     */
    public final void setRspData(CmbDoRefundRspData rspData) {
        this.rspData = rspData;
    }
}
