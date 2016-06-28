/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 退款发货回调Request
 * 
 * @author qianmin 2016年5月31日 下午4:27:28
 *
 */
public class RefundCallbackRequest {

    private final static DecimalFormat df = new DecimalFormat("0.00");

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * 是否快速退款
     */
    private boolean isFastRefund;
    /**
     * 实际退款金额
     */
    private BigDecimal actualRefundAmount;
    /**
     * 退款审核人
     */
    private String auditor;
    /**
     * 扩展类型(10-正常; 20-无法原路退回)
     */
    private Integer optType;
    /**
     * 订单号
     */
    private Long orderID;
    /**
     * 退款时间
     */
    private Date passAuditTime;
    /**
     * 要求退款金额
     */
    private BigDecimal requiredRefundAmount;
    /**
     * 第三方支付名称(Weixin; Alipay)
     */
    private String thirdPartyName;
    /**
     * 第三方支付订单号
     */
    private String thirdPartyTradingNo;
    /**
     * 交易号
     */
    private String tradeNo;
    /**
     * 退款申请号
     */
    private String requestNo;

    public boolean getIsFastRefund() {
        return isFastRefund;
    }

    public void setIsFastRefund(boolean isFastRefund) {
        this.isFastRefund = isFastRefund;
    }

    public String getActualRefundAmount() {
        return df.format(actualRefundAmount);
    }

    public void setActualRefundAmount(BigDecimal actualRefundAmount) {
        this.actualRefundAmount = actualRefundAmount;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public int getOptType() {
        return optType;
    }

    public void setOptType(int optType) {
        this.optType = optType;
    }

    public long getOrderID() {
        return orderID;
    }

    public void setOrderID(long orderID) {
        this.orderID = orderID;
    }

    public String getPassAuditTime() {
        return sdf.format(passAuditTime);
    }

    public void setPassAuditTime(Date passAuditTime) {
        this.passAuditTime = passAuditTime;
    }

    public String getRequiredRefundAmount() {
        return df.format(requiredRefundAmount);
    }

    public void setRequiredRefundAmount(BigDecimal requiredRefundAmount) {
        this.requiredRefundAmount = requiredRefundAmount;
    }

    public String getThirdPartyName() {
        return thirdPartyName;
    }

    public void setThirdPartyName(String thirdPartyName) {
        this.thirdPartyName = thirdPartyName;
    }

    public String getThirdPartyTradingNo() {
        return thirdPartyTradingNo;
    }

    public void setThirdPartyTradingNo(String thirdPartyTradingNo) {
        this.thirdPartyTradingNo = thirdPartyTradingNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public void setFastRefund(boolean isFastRefund) {
        this.isFastRefund = isFastRefund;
    }
}
