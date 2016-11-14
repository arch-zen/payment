/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.integration.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 一网通查询单笔订单
 * 
 * @author wangxudong 2016年11月11日 下午3:57:47
 *
 */
public class CmbQuerySingleOrderRequest extends CmbDTO {

    @Override
    public String buildSignString() {
        if ("A".equals(reqData.getType())) {
            return String.format("bankSerialNo=%s&branchNo=%s&dateTime=%s&merchantNo=%s&type=%s",
                    reqData.getBankSerialNo(),
                    reqData.getBranchNo(),
                    reqData.getDateTime(), reqData.getMerchantNo(), reqData.getType());
        } else {
            return String.format(
                    "bankSerialNo=%s&branchNo=%s&date=%s&dateTime=%s&merchantNo=%s&operatorNo=%s&orderNo=%s&type=%s",
                    reqData.getBankSerialNo(),
                    reqData.getBranchNo(), reqData.getDate(),
                    reqData.getDateTime(), reqData.getMerchantNo(), reqData.getOperatorNo(), reqData.getOrderNo(),
                    reqData.getType());
        }
    }

    /**
     * @return the reqData
     */
    public QuerySingleOrderReqData getReqData() {
        return reqData;
    }

    /**
     * @param reqData the reqData to set
     */
    public void setReqData(QuerySingleOrderReqData reqData) {
        this.reqData = reqData;
    }

    /**
     * 请求数据
     */
    private QuerySingleOrderReqData reqData = new QuerySingleOrderReqData();

    public class QuerySingleOrderReqData {

        public QuerySingleOrderReqData() {
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
         * 查询类型
         * A：按银行订单流水号查询
         * B：按商户订单日期和订单号查询；
         */
        private String type = "A";

        /**
         * 银行订单流水号
         * type=A时必填
         */
        private String bankSerialNo;

        /**
         * 订单日期
         * type=B时必填
         * 商户订单日期 格式：yyyyMMdd
         */
        private String date;

        /**
         * 商户订单号
         * type=B时必填
         */
        private String orderNo;

        /**
         * 操作员号
         */
        private String operatorNo;

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
         * @return the type
         */
        public final String getType() {
            return type;
        }

        /**
         * @param type the type to set
         */
        public final void setType(String type) {
            this.type = type;
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
         * @return the operatorNo
         */
        public final String getOperatorNo() {
            return operatorNo;
        }

        /**
         * @param operatorNo the operatorNo to set
         */
        public final void setOperatorNo(String operatorNo) {
            this.operatorNo = operatorNo;
        }
    }

}
