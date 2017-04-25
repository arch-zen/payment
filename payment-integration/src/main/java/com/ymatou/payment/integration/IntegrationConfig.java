/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Disconf配置信息读取
 *
 * @author qianmin 2016年5月9日 上午10:42:18
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
    private String ymtTriggerOrderRefundUrlJava; // 交易系统接收退款回调的url(Java)
    private String cmbPublicKeyQueryUrl; // 一网通公钥查询
    private String cmbQuerySingleOrderUrl; // 一网通单笔订单查询
    private String cmbDoRefundUrl;// 一网通退款
    private String cmbPayUrl; // 一网通支付
    private String cmbQuerySettledOrderUrl; // 一网通按商户日期查询已结账订单
    private String ymtCmbPaymentBaseUrl; // 洋码头一网通通知支付url基路径

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
    private String ymtTriggerOrderRefundUrlJavaMock; // 交易系统接收退款回调的url(Java mock)
    private String cmbPublicKeyQueryUrlMock; // 一网通公钥查询（mock）
    private String cmbQuerySingleOrderUrlMock; // 一网通单笔订单查询（mock）
    private String cmbDoRefundUrlMock;// 一网通退款(mock)
    private String cmbPayUrlMock; // 一网通支付
    private String cmbQuerySettledOrderUrlMock; // 一网通按商户日期查询已结账订单
    private String ymtCmbPaymentBaseUrlMock; // 洋码头一网通通知支付url基路径

    //region applePay
    private String applePayConsumeUrl;//applePay消费接口地址 (支付收单)
    private String applePayConsumeCallbackUrl;///applePay消费通知接口地址(支付回调)
    private String applePayConsumeQueryUrl;//applePay交易状态查询接口(单笔支付查询)
    private String applePayRefundUrl;//applePay退货接口地址(退款申请)
    private String applePayRefundQueryUrl;//applePay交易状态查询接口(退款查询)
    private String applePayRefundCallbackUrl;//applePay退货通知(退款回调)
    private String applePayfileTransferUrl;//applePay对账文件传输接口(正向对账)

    private String applePayConsumeUrlMock;//applePay消费接口地址 (支付收单)
    private String applePayConsumeCallbackUrlMock;///applePay消费通知接口地址(支付回调)
    private String applePayConsumeQueryUrlMock;//applePay交易状态查询接口(单笔支付查询)
    private String applePayRefundUrlMock;//applePay退货接口地址(退款申请)
    private String applePayRefundQueryUrlMock;//applePay交易状态查询接口(退款查询)
    private String applePayRefundCallbackUrlMock;//applePay退货通知(退款回调)
    private String applePayfileTransferUrlMock;//applePay对账文件传输接口(正向对账)


    @DisconfFileItem(name = "applePay.consume.url")
    public String getApplePayConsumeUrl() {
        return applePayConsumeUrl;
    }

    public void setApplePayConsumeUrl(String applePayConsumeUrl) {
        this.applePayConsumeUrl = applePayConsumeUrl;
    }

    @DisconfFileItem(name = "applePay.consume.callback.url")
    public String getApplePayConsumeCallbackUrl() {
        return applePayConsumeCallbackUrl;
    }

    public void setApplePayConsumeCallbackUrl(String applePayConsumeCallbackUrl) {
        this.applePayConsumeCallbackUrl = applePayConsumeCallbackUrl;
    }

    @DisconfFileItem(name = "applePay.consume.query.url")
    public String getApplePayConsumeQueryUrl() {
        return applePayConsumeQueryUrl;
    }

    public void setApplePayConsumeQueryUrl(String applePayConsumeQueryUrl) {
        this.applePayConsumeQueryUrl = applePayConsumeQueryUrl;
    }

    @DisconfFileItem(name = "applePay.refund.url")
    public String getApplePayRefundUrl() {
        return applePayRefundUrl;
    }

    public void setApplePayRefundUrl(String applePayRefundUrl) {
        this.applePayRefundUrl = applePayRefundUrl;
    }

    @DisconfFileItem(name = "applePay.refund.query.url")
    public String getApplePayRefundQueryUrl() {
        return applePayRefundQueryUrl;
    }

    public void setApplePayRefundQueryUrl(String applePayRefundQueryUrl) {
        this.applePayRefundQueryUrl = applePayRefundQueryUrl;
    }

    @DisconfFileItem(name = "applePay.refund.callback.url")
    public String getApplePayRefundCallbackUrl() {
        return applePayRefundCallbackUrl;
    }

    public void setApplePayRefundCallbackUrl(String applePayRefundCallbackUrl) {
        this.applePayRefundCallbackUrl = applePayRefundCallbackUrl;
    }

    @DisconfFileItem(name = "applePay.filetransfer.url")
    public String getApplePayfileTransferUrl() {
        return applePayfileTransferUrl;
    }

    public void setApplePayfileTransferUrl(String applePayfileTransferUrl) {
        this.applePayfileTransferUrl = applePayfileTransferUrl;
    }


    @DisconfFileItem(name = "applePay.consume.url.mock")
    public String getApplePayConsumeUrlMock() {
        return applePayConsumeUrlMock;
    }

    public void setApplePayConsumeUrlMock(String applePayConsumeUrlMock) {
        this.applePayConsumeUrlMock = applePayConsumeUrlMock;
    }

    @DisconfFileItem(name = "applePay.consume.callback.url.mock")
    public String getApplePayConsumeCallbackUrlMock() {
        return applePayConsumeCallbackUrlMock;
    }

    public void setApplePayConsumeCallbackUrlMock(String applePayConsumeCallbackUrlMock) {
        this.applePayConsumeCallbackUrlMock = applePayConsumeCallbackUrlMock;
    }

    @DisconfFileItem(name = "applePay.consume.query.url.mock")
    public String getApplePayConsumeQueryUrlMock() {
        return applePayConsumeQueryUrlMock;
    }

    public void setApplePayConsumeQueryUrlMock(String applePayConsumeQueryUrlMock) {
        this.applePayConsumeQueryUrlMock = applePayConsumeQueryUrlMock;
    }

    @DisconfFileItem(name = "applePay.refund.url.mock")
    public String getApplePayRefundUrlMock() {
        return applePayRefundUrlMock;
    }

    public void setApplePayRefundUrlMock(String applePayRefundUrlMock) {
        this.applePayRefundUrlMock = applePayRefundUrlMock;
    }


    @DisconfFileItem(name = "applePay.refund.query.url.mock")
    public String getApplePayRefundQueryUrlMock() {
        return applePayRefundQueryUrlMock;
    }

    public void setApplePayRefundQueryUrlMock(String applePayRefundQueryUrlMock) {
        this.applePayRefundQueryUrlMock = applePayRefundQueryUrlMock;
    }

    @DisconfFileItem(name = "applePay.refund.callback.url.mock")
    public String getApplePayRefundCallbackUrlMock() {
        return applePayRefundCallbackUrlMock;
    }

    public void setApplePayRefundCallbackUrlMock(String applePayRefundCallbackUrlMock) {
        this.applePayRefundCallbackUrlMock = applePayRefundCallbackUrlMock;
    }

    @DisconfFileItem(name = "applePay.filetransfer.url.mock")
    public String getApplePayfileTransferUrlMock() {
        return applePayfileTransferUrlMock;
    }

    public void setApplePayfileTransferUrlMock(String applePayfileTransferUrlMock) {
        this.applePayfileTransferUrlMock = applePayfileTransferUrlMock;
    }

    public String getApplePayConsumeUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getApplePayConsumeUrl();
        } else {
            return getApplePayConsumeUrlMock();
        }
    }

    public String getApplePayConsumeCallbackUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getApplePayConsumeCallbackUrl();
        } else {
            return getApplePayConsumeCallbackUrlMock();
        }
    }

    public String getApplePayConsumeQueryUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getApplePayConsumeQueryUrl();
        } else {
            return getApplePayConsumeQueryUrlMock();
        }
    }

    public String getApplePayRefundUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getApplePayRefundUrl();
        } else {
            return getApplePayRefundUrlMock();
        }
    }

    public String getApplePayRefundQueryUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getApplePayRefundQueryUrl();
        } else {
            return getApplePayRefundQueryUrlMock();
        }
    }

    public String getApplePayRefundCallbackUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getApplePayRefundCallbackUrl();
        } else {
            return getApplePayRefundCallbackUrlMock();
        }
    }

    public String getApplePayfileTransferUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getApplePayfileTransferUrl();
        } else {
            return getApplePayfileTransferUrlMock();
        }
    }

    //endregion


    @DisconfFileItem(name = "cmb.querysettledorder.url")
    public String getCmbQuerySettledOrderUrl() {
        return cmbQuerySettledOrderUrl;
    }

    public void setCmbQuerySettledOrderUrl(String cmbQuerySettledOrderUrl) {
        this.cmbQuerySettledOrderUrl = cmbQuerySettledOrderUrl;
    }


    @DisconfFileItem(name = "cmb.querysettledorder.url.mock")
    public String getCmbQuerySettledOrderUrlMock() {
        return cmbQuerySettledOrderUrlMock;
    }

    public void setCmbQuerySettledOrderUrlMock(String cmbQuerySettledOrderUrlMock) {
        this.cmbQuerySettledOrderUrlMock = cmbQuerySettledOrderUrlMock;
    }

    public String getCmbQuerySettledOrderUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getCmbQuerySettledOrderUrlMock();
        } else {
            return getCmbQuerySettledOrderUrl();
        }
    }


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

    @DisconfFileItem(name = "ymt.triggerorderrefund.url.java")
    public String getYmtTriggerOrderRefundUrlJava() {
        return ymtTriggerOrderRefundUrlJava;
    }

    public void setYmtTriggerOrderRefundUrlJava(String ymtTriggerOrderRefundUrlJava) {
        this.ymtTriggerOrderRefundUrlJava = ymtTriggerOrderRefundUrlJava;
    }

    @DisconfFileItem(name = "ymt.triggerorderrefund.url.java.mock")
    public String getYmtTriggerOrderRefundUrlJavaMock() {
        return ymtTriggerOrderRefundUrlJavaMock;
    }

    public void setYmtTriggerOrderRefundUrlJavaMock(String ymtTriggerOrderRefundUrlJavaMock) {
        this.ymtTriggerOrderRefundUrlJavaMock = ymtTriggerOrderRefundUrlJavaMock;
    }

    public String getYmtTriggerOrderRefundUrlJava(HashMap<String, String> header) {
        if (isMock(header)) {
            return getYmtTriggerOrderRefundUrlJavaMock();
        } else {
            return getYmtTriggerOrderRefundUrlJava();
        }
    }

    /**
     * @return the cmbPublicKeyQueryUrl
     */
    @DisconfFileItem(name = "cmb.publickeyquery.url")
    public String getCmbPublicKeyQueryUrl() {
        return cmbPublicKeyQueryUrl;
    }

    /**
     * @param cmbPublicKeyQueryUrl the cmbPublicKeyQueryUrl to set
     */
    public void setCmbPublicKeyQueryUrl(String cmbPublicKeyQueryUrl) {
        this.cmbPublicKeyQueryUrl = cmbPublicKeyQueryUrl;
    }


    /**
     * @return the cmbPublicKeyQueryUrlMock
     */
    @DisconfFileItem(name = "cmb.publickeyquery.url.mock")
    public String getCmbPublicKeyQueryUrlMock() {
        return cmbPublicKeyQueryUrlMock;
    }

    /**
     * @param cmbPublicKeyQueryUrlMock the cmbPublicKeyQueryUrlMock to set
     */
    public void setCmbPublicKeyQueryUrlMock(String cmbPublicKeyQueryUrlMock) {
        this.cmbPublicKeyQueryUrlMock = cmbPublicKeyQueryUrlMock;
    }

    public String getCmbPublicKeyQueryUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getCmbPublicKeyQueryUrlMock();
        } else {
            return getCmbPublicKeyQueryUrl();
        }
    }

    /**
     * @return the cmbQuerySingleOrderUrl
     */
    @DisconfFileItem(name = "cmb.querysingleorder.url")
    public String getCmbQuerySingleOrderUrl() {
        return cmbQuerySingleOrderUrl;
    }

    /**
     * @param cmbQuerySingleOrderUrl the cmbQuerySingleOrderUrl to set
     */
    public void setCmbQuerySingleOrderUrl(String cmbQuerySingleOrderUrl) {
        this.cmbQuerySingleOrderUrl = cmbQuerySingleOrderUrl;
    }

    /**
     * @return the cmbQuerySingleOrderUrlMock
     */
    @DisconfFileItem(name = "cmb.querysingleorder.url.mock")
    public String getCmbQuerySingleOrderUrlMock() {
        return cmbQuerySingleOrderUrlMock;
    }

    /**
     * @param cmbQuerySingleOrderUrlMock the cmbQuerySingleOrderUrlMock to set
     */
    public void setCmbQuerySingleOrderUrlMock(String cmbQuerySingleOrderUrlMock) {
        this.cmbQuerySingleOrderUrlMock = cmbQuerySingleOrderUrlMock;
    }

    public String getCmbQuerySingleOrderUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getCmbQuerySingleOrderUrlMock();
        } else {
            return getCmbQuerySingleOrderUrl();
        }
    }

    /**
     * @return the cmbDoRefundUrl
     */
    @DisconfFileItem(name = "cmb.dorefund.url")
    public final String getCmbDoRefundUrl() {
        return cmbDoRefundUrl;
    }

    /**
     * @param cmbDoRefundUrl the cmbDoRefundUrl to set
     */
    public final void setCmbDoRefundUrl(String cmbDoRefundUrl) {
        this.cmbDoRefundUrl = cmbDoRefundUrl;
    }

    /**
     * @return the cmbDoRefundUrlMock
     */
    @DisconfFileItem(name = "cmb.dorefund.url.mock")
    public final String getCmbDoRefundUrlMock() {
        return cmbDoRefundUrlMock;
    }

    /**
     * @param cmbDoRefundUrlMock the cmbDoRefundUrlMock to set
     */
    public final void setCmbDoRefundUrlMock(String cmbDoRefundUrlMock) {
        this.cmbDoRefundUrlMock = cmbDoRefundUrlMock;
    }

    public String getCmbDoRefundUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getCmbDoRefundUrlMock();
        } else {
            return getCmbDoRefundUrl();
        }
    }

    /**
     * @return the cmbPayUrl
     */
    @DisconfFileItem(name = "cmb.pay.url")
    public String getCmbPayUrl() {
        return cmbPayUrl;
    }

    /**
     * @param cmbPayUrl the cmbPayUrl to set
     */
    public void setCmbPayUrl(String cmbPayUrl) {
        this.cmbPayUrl = cmbPayUrl;
    }

    /**
     * @return the cmbPayUrlMock
     */
    @DisconfFileItem(name = "cmb.pay.url.mock")
    public String getCmbPayUrlMock() {
        return cmbPayUrlMock;
    }

    /**
     * @param cmbPayUrlMock the cmbPayUrlMock to set
     */
    public void setCmbPayUrlMock(String cmbPayUrlMock) {
        this.cmbPayUrlMock = cmbPayUrlMock;
    }

    public String getCmbPayUrl(HashMap<String, String> header) {
        if (isMock(header)) {
            return getCmbPayUrlMock();
        } else {
            return getCmbPayUrl();
        }
    }

    /**
     * @return the ymtCmbPaymentBaseUrl
     */
    @DisconfFileItem(name = "ymt.cmbpayment.baseurl")
    public String getYmtCmbPaymentBaseUrl() {
        return ymtCmbPaymentBaseUrl;
    }

    /**
     * @param ymtCmbPaymentBaseUrl the ymtCmbPaymentBaseUrl to set
     */
    public void setYmtCmbPaymentBaseUrl(String ymtCmbPaymentBaseUrl) {
        this.ymtCmbPaymentBaseUrl = ymtCmbPaymentBaseUrl;
    }

    /**
     * @return the ymtCmbPaymentBaseUrlMock
     */
    @DisconfFileItem(name = "ymt.cmbpayment.baseurl.mock")
    public String getYmtCmbPaymentBaseUrlMock() {
        return ymtCmbPaymentBaseUrlMock;
    }

    /**
     * @param ymtCmbPaymentBaseUrlMock the ymtCmbPaymentBaseUrlMock to set
     */
    public void setYmtCmbPaymentBaseUrlMock(String ymtCmbPaymentBaseUrlMock) {
        this.ymtCmbPaymentBaseUrlMock = ymtCmbPaymentBaseUrlMock;
    }
}
