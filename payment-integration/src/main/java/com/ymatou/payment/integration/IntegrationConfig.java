/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

/**
 * Disconf配置信息读取
 * 
 * @author qianmin 2016年5月9日 上午10:42:18
 *
 */
@Component
@DisconfFile(fileName = "integration.properties")
public class IntegrationConfig {

    private String wxUnifiedOrderUrl; // 微信统一下单url
    private String wxRefundQueryUrl; // 微信退款查询url
    private String wxOrderQueryUrl; // 微信查询订单url
    private String wxRefundUrl; // 微信退款申请url
    private String wxJsapiCertPath; // 微信Jsapi(1278350701)对应的证书路径
    private String wxJsapiCertPass; // 微信Jsapi(1278350701)对应的证书密码
    private String wxJsapiMchId; // 微信Jsapi的商户号(1234079001)
    private String wxAppCertPath; // 微信APP(1234079001)对应的证书路径
    private String wxAppCertPass; // 微信APP(1234079001)对应的证书密码
    private String wxAppMchId; // 微信App的商户号(1234079001)
    private String aliPayBaseUrl; // 支付宝防钓鱼时间戳url
    private String aliPayWapUrl; // 支付宝wap支付url
    private String ymtUserServiceUrl; // 用户服务url
    private String ymtNotifyRefundUrl; // 通知退款url
    private String ymtNotifyPaymentUrl; // 通知支付url
    private String ymtNotifytradingeventUrl; // 通知用户交易信息url
    private String ymtPaymentBaseUrl; // ymt.payment.baseurl
    private String ymtAccountingUrl; // 资金账户出入账服务url
    private String ymtRiskControlUrl; // 风控urk
    private String ymtTriggerOrderRefundUrl; // 交易系统接收退款回调的url

    private String openMock;
    private String aliPayBaseUrlMock; // 支付宝网关url(mock)
    private String aliPayWapUrlMock; // 支付宝wap支付url(mock)
    private String wxUnifiedOrderUrlMock; // 微信统一下单url(mock)
    private String wxRefundQueryUrlMock; // 微信退款查询url(mock)
    private String wxRefundUrlMock; // 微信退款申请url(mock)
    private String wxOrderQueryUrlMock; // 微信查询订单url(mock)
    private String ymtPaymentBaseUrlMock; // ymt.payment.baseurl.mock
    private String ymtUserServiceUrlMock; // 用户服务url(mock)
    private String ymtNotifyRefundUrlMock; // 通知退款url(mock)
    private String ymtNotifyPaymentUrlMock; // 通知支付url(mock)
    private String ymtNotifytradingeventUrlMock; // 通知用户交易信息url(mock)
    private String ymtAccountingUrlMock; // 资金账户出入账服务url(mock)
    private String ymtRiskControlUrlMock; // 风控urk(mock)
    private String ymtTriggerOrderRefundUrlMock; // 交易系统接收退款回调的url(mock)


    public String getYmtTriggerOrderRefundUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getYmtTriggerOrderRefundUrlMock();
        } else {
            return getYmtTriggerOrderRefundUrl();
        }
    }

    @DisconfFileItem(name = "ymt.triggerorderrefund.url")
    public String getYmtTriggerOrderRefundUrl() {
        return ymtTriggerOrderRefundUrl;
    }

    public void setYmtTriggerOrderRefundUrl(String ymtTriggerOrderRefundUrl) {
        this.ymtTriggerOrderRefundUrl = ymtTriggerOrderRefundUrl;
    }

    @DisconfFileItem(name = "ymt.triggerorderrefund.url.mock")
    public String getYmtTriggerOrderRefundUrlMock() {
        return ymtTriggerOrderRefundUrlMock;
    }

    public void setYmtTriggerOrderRefundUrlMock(String ymtTriggerOrderRefundUrlMock) {
        this.ymtTriggerOrderRefundUrlMock = ymtTriggerOrderRefundUrlMock;
    }

    public String getWxRefundUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getWxRefundUrlMock();
        } else {
            return getWxRefundUrl();
        }
    }

    @DisconfFileItem(name = "wx.reund.url")
    public String getWxRefundUrl() {
        return wxRefundUrl;
    }

    public void setWxRefundUrl(String wxRefundUrl) {
        this.wxRefundUrl = wxRefundUrl;
    }

    @DisconfFileItem(name = "wx.reund.url.mock")
    public String getWxRefundUrlMock() {
        return wxRefundUrlMock;
    }

    public void setWxRefundUrlMock(String wxRefundUrlMock) {
        this.wxRefundUrlMock = wxRefundUrlMock;
    }

    public String getYmtRiskControlUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getYmtRiskControlUrlMock();
        } else {
            return getYmtRiskControlUrl();
        }
    }

    @DisconfFileItem(name = "ymt.riskcontrol.url")
    public String getYmtRiskControlUrl() {
        return ymtRiskControlUrl;
    }

    public void setYmtRiskControlUrl(String ymtRiskControlUrl) {
        this.ymtRiskControlUrl = ymtRiskControlUrl;
    }

    @DisconfFileItem(name = "ymt.riskcontrol.url.mock")
    public String getYmtRiskControlUrlMock() {
        return ymtRiskControlUrlMock;
    }

    public void setYmtRiskControlUrlMock(String ymtRiskControlUrlMock) {
        this.ymtRiskControlUrlMock = ymtRiskControlUrlMock;
    }

    public String getYmtAccountingUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getYmtAccountingUrlMock();
        } else {
            return getYmtAccountingUrl();
        }
    }

    @DisconfFileItem(name = "ymt.accounting.url")
    public String getYmtAccountingUrl() {
        return ymtAccountingUrl;
    }


    public void setYmtAccountingUrl(String ymtAccountingUrl) {
        this.ymtAccountingUrl = ymtAccountingUrl;
    }

    @DisconfFileItem(name = "ymt.accounting.url.mock")
    public String getYmtAccountingUrlMock() {
        return ymtAccountingUrlMock;
    }


    public void setYmtAccountingUrlMock(String ymtAccountingUrlMock) {
        this.ymtAccountingUrlMock = ymtAccountingUrlMock;
    }

    public String getAliPayWapUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getAliPayWapUrlMock();
        } else {
            return getAliPayWapUrl();
        }
    }

    @DisconfFileItem(name = "ali.wap.url")
    public String getAliPayWapUrl() {
        return aliPayWapUrl;
    }

    public void setAliPayWapUrl(String aliPayWapUrl) {
        this.aliPayWapUrl = aliPayWapUrl;
    }

    @DisconfFileItem(name = "ali.wap.url.mock")
    public String getAliPayWapUrlMock() {
        return aliPayWapUrlMock;
    }

    public void setAliPayWapUrlMock(String aliPayWapUrlMock) {
        this.aliPayWapUrlMock = aliPayWapUrlMock;
    }

    public String getWxOrderQueryUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getWxOrderQueryUrlMock();
        } else {
            return getWxOrderQueryUrl();
        }
    }

    @DisconfFileItem(name = "wx.orderquery.url")
    public String getWxOrderQueryUrl() {
        return wxOrderQueryUrl;
    }

    public void setWxOrderQueryUrl(String wxOrderQueryUrl) {
        this.wxOrderQueryUrl = wxOrderQueryUrl;
    }

    @DisconfFileItem(name = "wx.orderquery.url.mock")
    public String getWxOrderQueryUrlMock() {
        return wxOrderQueryUrlMock;
    }

    public void setWxOrderQueryUrlMock(String wxOrderQueryUrlMock) {
        this.wxOrderQueryUrlMock = wxOrderQueryUrlMock;
    }

    public String getYmtNotifyRefundUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getYmtNotifyRefundUrlMock();
        } else {
            return getYmtNotifyRefundUrl();
        }
    }

    public String getYmtNotifyPaymentUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getYmtNotifyPaymentUrlMock();
        } else {
            return getYmtNotifyPaymentUrl();
        }
    }

    public String getYmtNotifytradingeventUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getYmtNotifytradingeventUrlMock();
        } else {
            return getYmtNotifytradingeventUrl();
        }
    }

    @DisconfFileItem(name = "ymt.notifyrefund.url")
    public String getYmtNotifyRefundUrl() {
        return ymtNotifyRefundUrl;
    }

    public void setYmtNotifyRefundUrl(String ymtNotifyRefundUrl) {
        this.ymtNotifyRefundUrl = ymtNotifyRefundUrl;
    }

    @DisconfFileItem(name = "ymt.notifypayment.url")
    public String getYmtNotifyPaymentUrl() {
        return ymtNotifyPaymentUrl;
    }

    public void setYmtNotifyPaymentUrl(String ymtNotifyPaymentUrl) {
        this.ymtNotifyPaymentUrl = ymtNotifyPaymentUrl;
    }

    @DisconfFileItem(name = "ymt.notifytradingevent.url")
    public String getYmtNotifytradingeventUrl() {
        return ymtNotifytradingeventUrl;
    }

    public void setYmtNotifytradingeventUrl(String ymtNotifytradingeventUrl) {
        this.ymtNotifytradingeventUrl = ymtNotifytradingeventUrl;
    }

    @DisconfFileItem(name = "ymt.notifyrefund.url.mock")
    public String getYmtNotifyRefundUrlMock() {
        return ymtNotifyRefundUrlMock;
    }

    public void setYmtNotifyRefundUrlMock(String ymtNotifyRefundUrlMock) {
        this.ymtNotifyRefundUrlMock = ymtNotifyRefundUrlMock;
    }

    @DisconfFileItem(name = "ymt.notifypayment.url.mock")
    public String getYmtNotifyPaymentUrlMock() {
        return ymtNotifyPaymentUrlMock;
    }

    public void setYmtNotifyPaymentUrlMock(String ymtNotifyPaymentUrlMock) {
        this.ymtNotifyPaymentUrlMock = ymtNotifyPaymentUrlMock;
    }

    @DisconfFileItem(name = "ymt.notifytradingevent.url.mock")
    public String getYmtNotifytradingeventUrlMock() {
        return ymtNotifytradingeventUrlMock;
    }

    public void setYmtNotifytradingeventUrlMock(String ymtNotifytradingeventUrlMock) {
        this.ymtNotifytradingeventUrlMock = ymtNotifytradingeventUrlMock;
    }

    @DisconfFileItem(name = "wx.unifiedorder.url")
    public String getWxUnifiedOrderUrl() {
        return wxUnifiedOrderUrl;
    }

    public void setWxUnifiedOrderUrl(String wxUnifiedOrderUrl) {
        this.wxUnifiedOrderUrl = wxUnifiedOrderUrl;
    }

    @DisconfFileItem(name = "wx.refundquery.url")
    public String getWxRefundQueryUrl() {
        return wxRefundQueryUrl;
    }

    public void setWxRefundQueryUrl(String wxRefundQueryUrl) {
        this.wxRefundQueryUrl = wxRefundQueryUrl;
    }

    @DisconfFileItem(name = "wx.jsapi.certpath")
    public String getWxJsapiCertPath() {
        return wxJsapiCertPath;
    }

    public void setWxJsapiCertPath(String wxJsapiCertPath) {
        this.wxJsapiCertPath = wxJsapiCertPath;
    }

    @DisconfFileItem(name = "wx.app.certpath")
    public String getWxAppCertPath() {
        return wxAppCertPath;
    }

    public void setWxAppCertPath(String wxAppCertPath) {
        this.wxAppCertPath = wxAppCertPath;
    }

    @DisconfFileItem(name = "ali.base.url")
    public String getAliPayBaseUrl() {
        return aliPayBaseUrl;
    }

    public void setAliPayBaseUrl(String aliPayBaseUrl) {
        this.aliPayBaseUrl = aliPayBaseUrl;
    }

    @DisconfFileItem(name = "ymt.userservice.url")
    public String getYmtUserServiceUrl() {
        return ymtUserServiceUrl;
    }

    public void setYmtUserServiceUrl(String ymtUserServiceUrl) {
        this.ymtUserServiceUrl = ymtUserServiceUrl;
    }

    @DisconfFileItem(name = "wx.jsapi.certpass")
    public String getWxJsapiCertPass() {
        return wxJsapiCertPass;
    }

    public void setWxJsapiCertPass(String wxJsapiCertPass) {
        this.wxJsapiCertPass = wxJsapiCertPass;
    }

    @DisconfFileItem(name = "wx.app.certpass")
    public String getWxAppCertPass() {
        return wxAppCertPass;
    }

    public void setWxAppCertPass(String wxAppCertPass) {
        this.wxAppCertPass = wxAppCertPass;
    }

    @DisconfFileItem(name = "wx.jsapi.mchid")
    public String getWxJsapiMchId() {
        return wxJsapiMchId;
    }

    public void setWxJsapiMchId(String wxJsapiMchId) {
        this.wxJsapiMchId = wxJsapiMchId;
    }

    @DisconfFileItem(name = "wx.app.mchid")
    public String getWxAppMchId() {
        return wxAppMchId;
    }

    public void setWxAppMchId(String wxAppMchId) {
        this.wxAppMchId = wxAppMchId;
    }

    @DisconfFileItem(name = "ali.base.url.mock")
    public String getAliPayBaseUrlMock() {
        return aliPayBaseUrlMock;
    }

    public void setAliPayBaseUrlMock(String aliPayBaseUrlMock) {
        this.aliPayBaseUrlMock = aliPayBaseUrlMock;
    }

    @DisconfFileItem(name = "wx.unifiedorder.url.mock")
    public String getWxUnifiedOrderUrlMock() {
        return wxUnifiedOrderUrlMock;
    }

    public void setWxUnifiedOrderUrlMock(String wxUnifiedOrderUrlMock) {
        this.wxUnifiedOrderUrlMock = wxUnifiedOrderUrlMock;
    }

    @DisconfFileItem(name = "wx.refundquery.url.mock")
    public String getWxRefundQueryUrlMock() {
        return wxRefundQueryUrlMock;
    }

    public void setWxRefundQueryUrlMock(String wxRefundQueryUrlMock) {
        this.wxRefundQueryUrlMock = wxRefundQueryUrlMock;
    }

    @DisconfFileItem(name = "ymt.userservice.url.mock")
    public String getYmtUserServiceUrlMock() {
        return ymtUserServiceUrlMock;
    }

    public void setYmtUserServiceUrlMock(String ymtUserServiceUrlMock) {
        this.ymtUserServiceUrlMock = ymtUserServiceUrlMock;
    }

    public String getWxRefundQueryUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getWxRefundQueryUrlMock();
        } else {
            return getWxRefundQueryUrl();
        }
    }

    public String getWxUnifiedOrderUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getWxUnifiedOrderUrlMock();
        } else {
            return getWxUnifiedOrderUrl();
        }
    }

    public String getYmtUserServiceUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getYmtUserServiceUrlMock();
        } else {
            return getYmtUserServiceUrl();
        }
    }

    public String getAliPayBaseUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getAliPayBaseUrlMock();
        } else {
            return getAliPayBaseUrl();
        }
    }

    @DisconfFileItem(name = "open.mock")
    public String getOpenMock() {
        return openMock;
    }

    public void setOpenMock(String openMock) {
        this.openMock = openMock;
    }

    public boolean isMock(HashMap<String, String> header) {
        return header != null
                && "1".equals(header.get("mock"))
                && "true".equals(getOpenMock());
    }

    @DisconfFileItem(name = "ymt.payment.baseurl")
    public String getYmtPaymentBaseUrl() {
        return ymtPaymentBaseUrl;
    }

    public void setYmtPaymentBaseUrl(String ymtPaymentBaseUrl) {
        this.ymtPaymentBaseUrl = ymtPaymentBaseUrl;
    }

    @DisconfFileItem(name = "ymt.payment.baseurl.mock")
    public String getYmtPaymentBaseUrlMock() {
        return ymtPaymentBaseUrlMock;
    }

    public void setYmtPaymentBaseUrlMock(String ymtPaymentBaseUrlMock) {
        this.ymtPaymentBaseUrlMock = ymtPaymentBaseUrlMock;
    }

    public String getYmtPaymentBaseUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getYmtPaymentBaseUrlMock();
        } else {
            return getYmtPaymentBaseUrl();
        }
    }
}
