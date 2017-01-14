/**
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.integration.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.common.utils.StringUtils;

/**
 * 按商户日期查询已结账订单
 * 
 * @see http://58.61.30.110/OpenAPI2/API/IntDeclareAPI8.aspx
 * @author Administrator 2017年1月14日 上午10:25:14
 *
 */
public class CmbQuerySettledOrderResponse extends CmbDTO {

    public QuerySettledOrderRspData rspData;

    public QuerySettledOrderRspData getRspData() {
        return rspData;
    }

    public void setRspData(QuerySettledOrderRspData rspData) {
        this.rspData = rspData;
    }


    /**
     * 从报文中解析出订单信息列表
     * 
     * @return
     */
    public List<CmbSettledOrderInfo> parseOrderList() {
        List<CmbSettledOrderInfo> orderInfos = new ArrayList<>();
        if (rspData == null || rspData.dataCount.equals("0") || StringUtils.isEmpty(rspData.getDataList())) {
            return orderInfos;
        }

        String[] dataList = rspData.getDataList().split("\\\r\\\n");
        if (dataList == null || dataList.length < 1) {
            return orderInfos;
        }

        Map<String, Integer> headerMap = getDetailHeaderMap(dataList[0]);
        for (int i = 1; i < dataList.length; i++) {
            CmbSettledOrderInfo orderInfo = genOrderInfo(headerMap, dataList[i]);
            orderInfos.add(orderInfo);
        }

        return orderInfos;
    }

    /**
     * 从第一行中获取到列名对应的序号
     * 
     * @param firstLine
     * @return
     */
    private Map<String, Integer> getDetailHeaderMap(String firstLine) {
        Map<String, Integer> headerMap = new HashMap<>();

        String[] heads = firstLine.split(",`");
        for (int i = 0; i < heads.length; i++) {
            headerMap.put(heads[i], i);
        }

        return headerMap;
    }

    /**
     * 根据列名解析出招行订单信息
     * 
     * @param headerMap
     * @param line
     * @return
     */
    private CmbSettledOrderInfo genOrderInfo(Map<String, Integer> headerMap, String line) {
        String[] lineData = line.split(",`");

        CmbSettledOrderInfo orderInfo = new CmbSettledOrderInfo();
        orderInfo.setAcceptDate(lineData[headerMap.get("acceptDate")]);
        orderInfo.setDate(lineData[headerMap.get("date")]);
        orderInfo.setOrderNo(lineData[headerMap.get("orderNo")]);
        orderInfo.setSettleTime(lineData[headerMap.get("settleTime")]);
        orderInfo.setFee(lineData[headerMap.get("fee")]);
        orderInfo.setSettleAmount(lineData[headerMap.get("settleAmount")]);
        orderInfo.setAcceptTime(lineData[headerMap.get("acceptTime")]);
        orderInfo.setCardType(lineData[headerMap.get("cardType")]);
        orderInfo.setDiscountAmount(lineData[headerMap.get("discountAmount")]);
        orderInfo.setOrderStatus(lineData[headerMap.get("orderStatus")]);
        orderInfo.setSettleDate(lineData[headerMap.get("settleDate")]);
        orderInfo.setOrderRefNo(lineData[headerMap.get("orderRefNo")]);
        orderInfo.setOrderAmount(lineData[headerMap.get("orderAmount")]);
        orderInfo.setBankSerialNo(lineData[headerMap.get("bankSerialNo")]);
        orderInfo.setCurrency(lineData[headerMap.get("currency")]);

        return orderInfo;
    }

    public class QuerySettledOrderRspData {
        /**
         * 处理结果
         */
        private String rspCode;

        /**
         * 响应信息
         */
        private String rspMsg;

        /**
         * 银行返回该数据的时间 格式：yyyyMMddHHmmss
         * 
         */
        private String dateTime;

        /**
         * 续传标志,Y:有续传（还有更多记录）； N：无续传（记录已返回完毕）。
         */
        private String hasNext;

        /**
         * 续传键值,当hasNext=Y时必填
         */
        private String nextKeyValue;

        /**
         * 返回条数,本次查询返回条数。
         */
        private String dataCount;

        public String getRspCode() {
            return rspCode;
        }

        public void setRspCode(String rspCode) {
            this.rspCode = rspCode;
        }

        public String getRspMsg() {
            return rspMsg;
        }

        public void setRspMsg(String rspMsg) {
            this.rspMsg = rspMsg;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getHasNext() {
            return hasNext;
        }

        public void setHasNext(String hasNext) {
            this.hasNext = hasNext;
        }

        public String getNextKeyValue() {
            return nextKeyValue;
        }

        public void setNextKeyValue(String nextKeyValue) {
            this.nextKeyValue = nextKeyValue;
        }

        public String getDataCount() {
            return dataCount;
        }

        public void setDataCount(String dataCount) {
            this.dataCount = dataCount;
        }

        public String getDataList() {
            return dataList;
        }

        public void setDataList(String dataList) {
            this.dataList = dataList;
        }

        /**
         * 记录数据,每笔记录一行，行之间以\r\n分隔。 第一行为表头; 从第二行起为数据记录; 行内的参数以逗号和`符号分隔(`为标准键盘1 左边键的字符)，字段顺序与表头一致。
         * 数据记录定义如下：
         */
        private String dataList;
    }

    @Override
    public String buildSignString() {
        return null;
    }

}
