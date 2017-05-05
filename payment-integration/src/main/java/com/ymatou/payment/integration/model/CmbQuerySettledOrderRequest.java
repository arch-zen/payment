/**
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.integration.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.dubbo.common.utils.StringUtils;

/**
 * 按商户日期查询已结账订单
 * 
 * @see http://58.61.30.110/OpenAPI2/API/IntDeclareAPI8.aspx
 * @author Administrator 2017年1月14日 上午10:25:14
 *
 */
public class CmbQuerySettledOrderRequest extends CmbDTO {

    @Override
    public String buildSignString() {
        return String.format(
                "beginDate=%s&branchNo=%s&dateTime=%s&endDate=%s&merchantNo=%s&nextKeyValue=%s&operatorNo=%s",
                reqData.getBeginDate(), reqData.getBranchNo(), reqData.getDateTime(), reqData.getEndDate(),
                reqData.getMerchantNo(), reqData.getNextKeyValue(), reqData.getOperatorNo());
    }


    public QuerySettledOrderReqData getReqData() {
        return reqData;
    }

    public void setReqData(QuerySettledOrderReqData reqData) {
        this.reqData = reqData;
    }


    /**
     * 请求数据
     */
    private QuerySettledOrderReqData reqData = new QuerySettledOrderReqData();

    public class QuerySettledOrderReqData {

        private String dateTime;

        private String branchNo;

        private String merchantNo;

        private String beginDate;

        private String endDate;

        private String operatorNo;

        private String nextKeyValue;

        public QuerySettledOrderReqData() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            this.dateTime = simpleDateFormat.format(new Date());
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getBranchNo() {
            return branchNo;
        }

        public void setBranchNo(String branchNo) {
            this.branchNo = branchNo;
        }

        public String getMerchantNo() {
            return merchantNo;
        }

        public void setMerchantNo(String merchantNo) {
            this.merchantNo = merchantNo;
        }

        public String getBeginDate() {
            return beginDate;
        }

        public void setBeginDate(String beginDate) {
            this.beginDate = beginDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getOperatorNo() {
            return operatorNo;
        }

        public void setOperatorNo(String operatorNo) {
            this.operatorNo = operatorNo;
        }

        public String getNextKeyValue() {
            if (StringUtils.isEmpty(nextKeyValue)) {
                return "";
            } else {
                return nextKeyValue;
            }
        }

        public void setNextKeyValue(String nextKeyValue) {
            this.nextKeyValue = nextKeyValue;
        }
    }
}
