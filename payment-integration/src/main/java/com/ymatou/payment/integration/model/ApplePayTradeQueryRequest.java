package com.ymatou.payment.integration.model;

/**
 * Created by zhangxiaoming on 2017/4/24.
 * applepay交易查询请求信息
 */
public class ApplePayTradeQueryRequest extends  ApplePayBaseRequest{

    ///////////订单信息//////////////////////////////////////
    /**
     * 商户订单号
     */
    private String orderId;

    /**
     * 订单发送时间
     */
    private String txnTime;



    ///////////////机构信息///////////////////////////////////////////

    /**
     * 收单机构代码
     */
    private  String acqInsCode;


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

    public String getAcqInsCode() {
        return acqInsCode;
    }

    public void setAcqInsCode(String acqInsCode) {
        this.acqInsCode = acqInsCode;
    }
}
