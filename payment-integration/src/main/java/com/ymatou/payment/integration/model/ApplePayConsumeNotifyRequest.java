package com.ymatou.payment.integration.model;

/**
 * Created by gejianhua on 2017/4/25.
 * 消费通知
 */
public class ApplePayConsumeNotifyRequest extends ApplePayBaseResponse {
    /////////////订单信息///////////////////////////////
    /**
     * 商户订单号
     */
    private String orderId;

    /**
     * 交易币种
     */
    private String currencyCode;

    /**
     * 交易金额
     */
    private String txnAmt;

    /**
     * 订单发送时间
     */
    private String txnTime;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 账号
     */
    private String accNo;

    /**
     * 支付卡类型
     */
    private String payCardType;

    /**
     * 请求方自定义域
     */
    private String reqReserved;

    /**
     * 保留域
     */
    private String reserved;


    ///////////////通知信息//////////////////////////////////////////

    /**
     * 查询流水号
     */
    private String queryId;

    /**
     * 系统跟踪号
     */
    private String traceNo;

    /**
     * 交易传输时间
     */
    private String traceTime;

    /**
     * 清算日期
     */
    private String settleDate;

    /**
     * 清算币种
     */
    private String settleCurrencyCode;

    /**
     * 清算金额
     */
    private String settleAmt;

    /**
     * 应答码
     */
    private String respCode;

    /**
     * 应答信息
     */
    private String respMsg;

    /**
     * 支付卡标识
     */
    private String payCardNo;

    /**
     * 支付卡名称
     */
    private String payCardIssueName;



    //////////////机构信息///////////////////////////////////////
    /**
     * 收单机构代码
     */
    private String acqInsCode;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getTxnAmt() {
        return txnAmt;
    }

    public void setTxnAmt(String txnAmt) {
        this.txnAmt = txnAmt;
    }

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getPayCardType() {
        return payCardType;
    }

    public void setPayCardType(String payCardType) {
        this.payCardType = payCardType;
    }

    public String getReqReserved() {
        return reqReserved;
    }

    public void setReqReserved(String reqReserved) {
        this.reqReserved = reqReserved;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getTraceTime() {
        return traceTime;
    }

    public void setTraceTime(String traceTime) {
        this.traceTime = traceTime;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    public String getSettleCurrencyCode() {
        return settleCurrencyCode;
    }

    public void setSettleCurrencyCode(String settleCurrencyCode) {
        this.settleCurrencyCode = settleCurrencyCode;
    }

    public String getSettleAmt() {
        return settleAmt;
    }

    public void setSettleAmt(String settleAmt) {
        this.settleAmt = settleAmt;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getPayCardNo() {
        return payCardNo;
    }

    public void setPayCardNo(String payCardNo) {
        this.payCardNo = payCardNo;
    }

    public String getPayCardIssueName() {
        return payCardIssueName;
    }

    public void setPayCardIssueName(String payCardIssueName) {
        this.payCardIssueName = payCardIssueName;
    }

    public String getAcqInsCode() {
        return acqInsCode;
    }

    public void setAcqInsCode(String acqInsCode) {
        this.acqInsCode = acqInsCode;
    }
}
