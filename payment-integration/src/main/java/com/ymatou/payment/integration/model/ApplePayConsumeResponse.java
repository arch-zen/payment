package com.ymatou.payment.integration.model;

/**
 * Created by gejianhua on 2017/4/25.
 * applypay消费回应信息
 */
public class ApplePayConsumeResponse extends ApplePayBaseResponse {

    /////订单信息//////////////////////
    /**
     * 商户订单号
     */
    private String orderId;

    /**
     * 订单发送时间
     */
    private String txnTime;

    /**
     * 请求方自定义域
     */
    private String reqReserved;

    /**
     * 保留域
     */
    private String reserved;

    //////////通知信息///////////////////////////////

    /**
     * 应答码
     */
    private String respCode;

    /**
     * 应答信息
     */
    private String respMsg;

    /**
     * 银联受理订单号
     */
    private String tn;

    //////////////机构信息///////////////////////////////

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

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
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

    public String getTn() {
        return tn;
    }

    public void setTn(String tn) {
        this.tn = tn;
    }

    public String getAcqInsCode() {
        return acqInsCode;
    }

    public void setAcqInsCode(String acqInsCode) {
        this.acqInsCode = acqInsCode;
    }






}













































