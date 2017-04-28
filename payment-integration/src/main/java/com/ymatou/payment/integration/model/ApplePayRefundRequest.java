package com.ymatou.payment.integration.model;

/**
 * Created by zhangxiaoming on 2017/4/24.
 * https://open.unionpay.com/ajweb/product/detail?id=80  [退货]-请求报文
 */
public class ApplePayRefundRequest extends ApplePayBaseRequest {

    private String orderId;
    private String txnTime;//订单发送时间 格式： YYYYMMDDHHmmss
    private String txnAmt; //交易金额 单位为分，不能带小数点，样例：1元送100 ,小于等于原交易
    private String backUrl;
    private String origQryId;
    private String reqReserved;//商户自定义保留域，交易应答时会原样返回

    public ApplePayRefundRequest() {
        this.setTxnType("04"); //易类型 04-退货
        this.setTxnSubType("00"); //交易子类型  默认00
        this.setBizType("000201"); //业务类型 B2C 网关支付
    }

    // region get/set


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
    
    public String getTxnAmt() {
        return txnAmt;
    }

    public void setTxnAmt(String txnAmt) {
        this.txnAmt = txnAmt;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public String getOrigQryId() {
        return origQryId;
    }

    public void setOrigQryId(String origQryId) {
        this.origQryId = origQryId;
    }

    // endregion
}
