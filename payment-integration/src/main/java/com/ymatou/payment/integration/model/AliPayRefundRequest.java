/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 即时到账批量退款无密接口 同步Request
 * 
 * @author qianmin 2016年5月30日 下午5:22:59
 *
 */
public class AliPayRefundRequest {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    /**
     * 接口名称
     */
    private String service = "refund_fastpay_by_platform_nopwd";
    /**
     * 合作者身份ID
     */
    private String partner;
    /**
     * 参数编码字符集
     */
    private String inputCharset = "UTF-8";
    /**
     * 签名方式
     */
    private String signType;
    /**
     * 签名
     */
    private String sign;
    /**
     * 服务器异步通知页面路径
     */
    private String notifyUrl;
    /**
     * 充退通知地址
     */
    private String dbackNotifyUrl;
    /**
     * 退款批次号
     */
    private String batchNo;
    /**
     * 退款请求时间
     */
    private Date refundDate;
    /**
     * 退款总笔数
     */
    private String batchNum;
    /**
     * 单笔数据集
     */
    private String detailData;
    /**
     * 是否使用冻结金额退款
     */
    private String useFreezeAmount;
    /**
     * 申请结果返回类型
     */
    private String returnType = "xml";

    public String getService() {
        return service;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getInputCharset() {
        return inputCharset;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getDbackNotifyUrl() {
        return dbackNotifyUrl;
    }

    public void setDbackNotifyUrl(String dbackNotifyUrl) {
        this.dbackNotifyUrl = dbackNotifyUrl;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getRefundDate() {
        return sdf.format(refundDate);
    }

    public void setRefundDate(Date refundDate) {
        this.refundDate = refundDate;
    }

    public String getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(String batchNum) {
        this.batchNum = batchNum;
    }

    public String getDetailData() {
        return detailData;
    }

    public void setDetailData(String detailData) {
        this.detailData = detailData;
    }

    public String getUseFreezeAmount() {
        return useFreezeAmount;
    }

    public void setUseFreezeAmount(String useFreezeAmount) {
        this.useFreezeAmount = useFreezeAmount;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public HashMap<String, String> mapForSign() {
        HashMap<String, String> map = new HashMap<>();
        map.put("service", this.getService());
        map.put("partner", this.getPartner());
        map.put("_input_charset", this.getInputCharset());
        // 无SignType
        map.put("notify_url", this.getNotifyUrl());
        map.put("dback_notify_url", this.getDbackNotifyUrl());
        map.put("batch_no", this.getBatchNo());
        map.put("refund_date", this.getRefundDate());
        map.put("batch_num", this.getBatchNum());
        map.put("detail_data", this.getDetailData());
        map.put("use_freeze_amount", this.getUseFreezeAmount());
        map.put("return_type", this.getReturnType());
        return map;
    }

    public String getRequestData() {
        HashMap<String, String> map = mapForSign();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.append("sign").append("=").append(this.getSign()).append("&");
        sb.append("signType").append("=").append(this.getSignType()).append("&");

        return StringUtils.removeEnd(sb.toString(), "&");
    }
}
