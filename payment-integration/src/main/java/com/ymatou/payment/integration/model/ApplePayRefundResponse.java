package com.ymatou.payment.integration.model;

/**
 * Created by zhangxiaoming on 2017/4/24.
 * https://open.unionpay.com/ajweb/product/detail?id=80  [退货]-应答报文
 */
public class ApplePayRefundResponse extends ApplePayBaseResponse {

    // region 订单信息
    /**
     * 商户订单号 商户订单号，不应含“-”或“_”
     */
    private String orderId;

    /**
     * 交易金额  单位为分，不能带小数点，样例：1元送100
     */
    private String txnAmt;

    /**
     * 订单发送时间  YYYYMMDDHHmmss
     */
    private String txnTime;

    /**
     * 商户自定义保留域，交易应答时会原样返回
     */
    private String reqReserved;

    /**
     * 保留域包含多个子域，所有子域需用“{}”包含，子域间以“&”符号链接。
     */
    private String reserved;
    // endregion

    // region 通知信息

    /**
     * 查询流水号
     * 由银联返回，用于在后续类交易中唯一标识一笔交易
     * 退货交易的交易流水号 供查询用
     */
    private String queryId;

    /**
     * 原交易查询流水号
     * 后续类交易（如退货、消费撤销等）送原交易的queryId
     * 原始消费交易的queryId
     */
    private String origQryId;

    /**
     * 应答码
     * 具体参见应答码定义章节
     */
    private String respCode;

    /**
     * 应答信息
     */
    private String respMsg;
    // endregion


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getOrigQryId() {
        return origQryId;
    }

    public void setOrigQryId(String origQryId) {
        this.origQryId = origQryId;
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
}
