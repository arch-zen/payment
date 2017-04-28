package com.ymatou.payment.integration.model;

/**
 * Created by zhangxiaoming on 2017/4/24.
 * https://open.unionpay.com/ajweb/product/detail?id=80  [文件传输]-请求报文
 */
public class ApplePayFileTransferRequest extends ApplePayBaseRequest {

    // region 订单信息

    /**
     * 订单发送时间
     * YYYYMMDDHHmmss
     */
    private String txnTime;

    /**
     * 文件类型
     */
    private String fileType;

    // endregion

    // region 通知信息

    /**
     * 清算日期
     * MMDD
     * <p>
     * 为银联和入网机构间的交易结算日期。一般前一日23点至当天23点为一个清算日。
     * 也就是23点前的交易，当天23点之后开始结算，23点之后的交易，要第二天23点之后才会结算。
     * 测试环境为测试需要，23:30左右日切，所以23:30到23:30为一个清算日，当天23:30之后为下个清算日。
     */
    private String settleDate;
    // endregion

    public ApplePayFileTransferRequest() {
        this.setTxnType("76");//交易类型 76-对账文件下载
        this.setTxnSubType("01");//交易子类型 01-对账文件下载
        this.setBizType("000000");//业务类型 固定填写000000
        this.setFileType("00"); //固定填写00
    }

    // region get/set

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    // endregion
}
