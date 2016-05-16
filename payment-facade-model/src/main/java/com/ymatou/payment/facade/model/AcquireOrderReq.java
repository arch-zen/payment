/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 * 
 * All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.util.HashMap;
import java.util.Map;

import com.ymatou.payment.facade.BaseRequest;


/**
 * 收单请求报文
 * 
 * @author wangxudong 2016年5月10日 上午11:55:01
 *
 */
public class AcquireOrderReq extends BaseRequest {

    /**
     * 请求版本号
     */
    private static final long serialVersionUID = -3983050225255199358L;

    /**
     * 接口版本
     */
    private Integer version;

    /**
     * MockHeader
     */
    private HashMap<String, String> mockHeader;

    /**
     * 调用方AppId
     */
    private String appId;

    /**
     * 使用平台，100 + userplatform(由交易提供)
     */
    private String originAppId;

    /**
     * 跟踪Id
     */
    private String traceId;

    /**
     * 签名方式
     */
    private String signMethod;

    /**
     * 编码方式
     */
    private Integer encoding;

    /**
     * 支付金额
     */
    private String payPrice;

    /**
     * 人民币：CNY
     */
    public String currency;

    /**
     * 调用方的业务ID，需要保证唯一性
     */
    public String orderId;

    /**
     * DateTime.Now.ToString("yyyyMMddHHmmss")
     */
    public String orderTime;

    /**
     * 支付渠道：10:支付宝PC. 11.支付宝WAP 13.支付宝APP 14.微信 JSAP 15.微信APP 20.Paypal
     */
    private String payType;

    /**
     * 有银行代码则传，没有置为空
     */
    private String bankId;

    /**
     * 洋码头余额账户所对应的USERID
     */
    private Integer userId;

    /**
     * 第三方用户标识
     */
    private String thirdPartyUserId;

    /// <summary>
    /// 暂时不需要
    /// </summary>
    private Integer thirdPartyUserIdType;

    /// <summary>
    /// IP地址
    /// </summary>
    private String userIp;

    /// <summary>
    /// 服务器端通知地址：用于服务器端回调，调用服务可处理业务
    /// </summary>
    private String notifyUrl;

    /// <summary>
    /// 调用方客户端Redict地址：用于Redict回调用方页面
    /// </summary>
    private String callbackUrl;

    /// <summary>
    /// 商品名字：如果没有产品名字，则添加调用方描述
    /// </summary>
    private String productName;

    /// <summary>
    /// 商品描述
    /// </summary>
    private String productDesc;

    /// <summary>
    /// 商品图片
    /// </summary>
    private String productUrl;

    /// <summary>
    /// 附加参数，仅对PC支付宝支付有效。如果需要自定义，包含三个字段的JsonString
    /// 1.SHOWMODE:扫码方式
    /// 2.PAYMETHOD：支付方式(1为信用卡，2为银行支付，3为直接支付)
    /// 3.ISHANGZHOU：是否保税区订单支付
    /// 样例：{"SHOWMODE":"2","PAYMETHOD":"2", "IsHangZhou":0}
    /// </summary>
    private String ext;

    /// <summary>
    /// 备注，可为空
    /// </summary>
    private String memo;

    /// <summary>
    /// 1为定金或者支付。2为充值 3为补款 4为卖家保证金充值 5为卖家余额充值
    /// </summary>
    private Integer bizCode;

    /**
     * @return the appId
     */
    public String getAppId() {
        return appId;
    }

    /**
     * @param appId the appId to set
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * @return the originAppId
     */
    public String getOriginAppId() {
        return originAppId;
    }

    /**
     * @param originAppId the originAppId to set
     */
    public void setOriginAppId(String originAppId) {
        this.originAppId = originAppId;
    }

    /**
     * @return the traceId
     */
    public String getTraceId() {
        return traceId;
    }

    /**
     * @param traceId the traceId to set
     */
    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    /**
     * @return the payPrice
     */
    public String getPayPrice() {
        return payPrice;
    }

    /**
     * @param payPrice the payPrice to set
     */
    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    /**
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return the orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the orderTime
     */
    public String getOrderTime() {
        return orderTime;
    }

    /**
     * @param orderTime the orderTime to set
     */
    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    /**
     * @return the payType
     */
    public String getPayType() {
        return payType;
    }

    /**
     * @param payType the payType to set
     */
    public void setPayType(String payType) {
        this.payType = payType;
    }

    /**
     * @return the bankId
     */
    public String getBankId() {
        return bankId;
    }

    /**
     * @param bankId the bankId to set
     */
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return the thirdPartyUserId
     */
    public String getThirdPartyUserId() {
        return thirdPartyUserId;
    }

    /**
     * @param thirdPartyUserId the thirdPartyUserId to set
     */
    public void setThirdPartyUserId(String thirdPartyUserId) {
        this.thirdPartyUserId = thirdPartyUserId;
    }

    /**
     * @return the thirdPartyUserIdType
     */
    public Integer getThirdPartyUserIdType() {
        return thirdPartyUserIdType;
    }

    /**
     * @param thirdPartyUserIdType the thirdPartyUserIdType to set
     */
    public void setThirdPartyUserIdType(Integer thirdPartyUserIdType) {
        this.thirdPartyUserIdType = thirdPartyUserIdType;
    }

    /**
     * @return the userIp
     */
    public String getUserIp() {
        return userIp;
    }

    /**
     * @param userIp the userIp to set
     */
    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    /**
     * @return the notifyUrl
     */
    public String getNotifyUrl() {
        return notifyUrl;
    }

    /**
     * @param notifyUrl the notifyUrl to set
     */
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    /**
     * @return the callbackUrl
     */
    public String getCallbackUrl() {
        return callbackUrl;
    }

    /**
     * @param callbackUrl the callbackUrl to set
     */
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the productDesc
     */
    public String getProductDesc() {
        return productDesc;
    }

    /**
     * @param productDesc the productDesc to set
     */
    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    /**
     * @return the productUrl
     */
    public String getProductUrl() {
        return productUrl;
    }

    /**
     * @param productUrl the productUrl to set
     */
    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    /**
     * @return the ext
     */
    public String getExt() {
        return ext;
    }

    /**
     * @param ext the ext to set
     */
    public void setExt(String ext) {
        this.ext = ext;
    }

    /**
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @param memo the memo to set
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * @return the bizCode
     */
    public Integer getBizCode() {
        return bizCode;
    }

    /**
     * @param bizCode the bizCode to set
     */
    public void setBizCode(Integer bizCode) {
        this.bizCode = bizCode;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * @return the encoding
     */
    public Integer getEncoding() {
        return encoding;
    }

    /**
     * @param encoding the encoding to set
     */
    public void setEncoding(Integer encoding) {
        this.encoding = encoding;
    }

    /**
     * @return the signMethod
     */
    public String getSignMethod() {
        return signMethod;
    }

    /**
     * @param signMethod the signMethod to set
     */
    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * @return the mockHeader
     */
    public HashMap<String, String> getMockHeader() {
        return mockHeader;
    }

    /**
     * @param mockHeader the mockHeader to set
     */
    public void setMockHeader(HashMap<String, String> mockHeader) {
        this.mockHeader = mockHeader;
    }



}
