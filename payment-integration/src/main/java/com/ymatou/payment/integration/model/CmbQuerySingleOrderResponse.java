/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.integration.model;

/**
 * 单笔订单查询响应报文
 * 
 * @author wangxudong 2016年11月11日 下午5:09:28
 *
 */
public class CmbQuerySingleOrderResponse extends CmbDTO {

    @Override
    public String buildSignString() {
        return null;
    }

    public QuerySingleOrderRspData rspData;

    public class QuerySingleOrderRspData {
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
         * 商户分行号，4位数字
         */
        private String branchNo;

        /**
         * 商户号，6位数字
         */
        private String merchantNo;

        /**
         * 商户订单日期，格式：yyyyMMdd
         */
        private String date;

        /**
         * 商户订单号
         */
        private String orderNo;

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
        private String orderAmount;

        /**
         * 费用金额
         */
        private String fee;

        /**
         * 银行受理日期
         */
        private String bankDate;

        /**
         * 银行受理时间
         */
        private String bankTime;

        /**
         * 结算金额 格式：xxxx.xx
         */
        private String settleAmount;

        /**
         * 优惠金额
         */
        private String discountAmount;

        /**
         * 订单状态
         * 0:已结帐
         * 1:已撤销
         * 2:部分结帐
         * 4:未结帐
         * 7:冻结交易-冻结金额已经全部结账
         * 8:冻结交易，冻结金额只结帐了一部分
         */
        private String orderStatus;

        /**
         * 银行处理日期 yyyyMMdd
         */
        private String settleDate;

        /**
         * 银行处理时间 HHmmss
         */
        private String settleTime;

        /**
         * 卡类型
         */
        private String cardType;

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
         * @return the branchNo
         */
        public final String getBranchNo() {
            return branchNo;
        }

        /**
         * @param branchNo the branchNo to set
         */
        public final void setBranchNo(String branchNo) {
            this.branchNo = branchNo;
        }

        /**
         * @return the merchantNo
         */
        public final String getMerchantNo() {
            return merchantNo;
        }

        /**
         * @param merchantNo the merchantNo to set
         */
        public final void setMerchantNo(String merchantNo) {
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
         * @return the orderAmount
         */
        public final String getOrderAmount() {
            return orderAmount;
        }

        /**
         * @param orderAmount the orderAmount to set
         */
        public final void setOrderAmount(String orderAmount) {
            this.orderAmount = orderAmount;
        }

        /**
         * @return the fee
         */
        public final String getFee() {
            return fee;
        }

        /**
         * @param fee the fee to set
         */
        public final void setFee(String fee) {
            this.fee = fee;
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
         * @return the settleAmount
         */
        public final String getSettleAmount() {
            return settleAmount;
        }

        /**
         * @param settleAmount the settleAmount to set
         */
        public final void setSettleAmount(String settleAmount) {
            this.settleAmount = settleAmount;
        }

        /**
         * @return the discountAmount
         */
        public final String getDiscountAmount() {
            return discountAmount;
        }

        /**
         * @param discountAmount the discountAmount to set
         */
        public final void setDiscountAmount(String discountAmount) {
            this.discountAmount = discountAmount;
        }

        /**
         * @return the orderStatus
         */
        public final String getOrderStatus() {
            return orderStatus;
        }

        /**
         * @param orderStatus the orderStatus to set
         */
        public final void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        /**
         * @return the settleDate
         */
        public final String getSettleDate() {
            return settleDate;
        }

        /**
         * @param settleDate the settleDate to set
         */
        public final void setSettleDate(String settleDate) {
            this.settleDate = settleDate;
        }

        /**
         * @return the settleTime
         */
        public final String getSettleTime() {
            return settleTime;
        }

        /**
         * @param settleTime the settleTime to set
         */
        public final void setSettleTime(String settleTime) {
            this.settleTime = settleTime;
        }

        /**
         * @return the cardType
         */
        public final String getCardType() {
            return cardType;
        }

        /**
         * @param cardType the cardType to set
         */
        public final void setCardType(String cardType) {
            this.cardType = cardType;
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
    }

    /**
     * @return the rspData
     */
    public final QuerySingleOrderRspData getRspData() {
        return rspData;
    }

    /**
     * @param rspData the rspData to set
     */
    public final void setRspData(QuerySingleOrderRspData rspData) {
        this.rspData = rspData;
    }
}
