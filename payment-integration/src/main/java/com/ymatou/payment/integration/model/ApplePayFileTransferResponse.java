package com.ymatou.payment.integration.model;

/**
 * Created by zhangxiaoming on 2017/4/24.
 * https://open.unionpay.com/ajweb/product/detail?id=80  [文件传输]-应答报文
 */
public class ApplePayFileTransferResponse extends ApplePayBaseResponse {

    // region 订单信息

    /**
     * 订单发送时间 YYYYMMDDHHmmss
     */
    private String txnTime;

    /**
     * 文件类型
     * 依据实际业务情况定义，取值：00
     */
    private String fileType;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 批量文件内容
     * deflate算法压缩，Base64编码后的文件内容
     * 文件流方式
     */
    private String fileContent;

    /**
     * 商户自定义保留域，交易应答时会原样返回
     */
    private String reqReserved;
    // endregion

    // region 通知信息

    /**
     * 清算日期
     * MMDD
     * 为银联和入网机构间的交易结算日期。一般前一日23点至当天23点为一个清算日。
     * 也就是23点前的交易，当天23点之后开始结算，23点之后的交易，要第二天23点之后才会结算。
     * 测试环境为测试需要，23:30左右日切，所以23:30到23:30为一个清算日，当天23:30之后为下个清算日。
     */
    private String settleDate;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getReqReserved() {
        return reqReserved;
    }

    public void setReqReserved(String reqReserved) {
        this.reqReserved = reqReserved;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
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


    //endregion
}
