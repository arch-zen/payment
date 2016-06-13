/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.service.alipay;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.NameValuePair;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.HttpClientUtil;
import com.ymatou.payment.integration.common.constants.Constants;
import com.ymatou.payment.integration.model.AliPayRefundRequest;
import com.ymatou.payment.integration.model.AliPayRefundResponse;

/**
 * 即时到账批量退款无密接口
 * 
 * @author qianmin 2016年5月30日 下午5:33:07
 *
 */
@Component
public class AliPayRefundService implements InitializingBean {

    private CloseableHttpClient httpClient;

    @Autowired
    private IntegrationConfig integrationConfig;

    public AliPayRefundResponse doService(AliPayRefundRequest request, HashMap<String, String> header)
            throws Exception {
        String url = integrationConfig.getAliPayBaseUrl(header);
        String result = HttpClientUtil.sendPost(url, generateRequest(request), header, httpClient);

        return generateResponse(result);
    }

    private List<NameValuePair> generateRequest(AliPayRefundRequest request) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("service", request.getService()));
        nvps.add(new BasicNameValuePair("partner", request.getPartner()));
        nvps.add(new BasicNameValuePair("_input_charset", request.getInputCharset()));
        nvps.add(new BasicNameValuePair("sign_type", request.getSignType()));
        nvps.add(new BasicNameValuePair("notify_url", request.getNotifyUrl()));
        nvps.add(new BasicNameValuePair("dback_notify_url", request.getDbackNotifyUrl()));
        nvps.add(new BasicNameValuePair("batch_no", request.getBatchNo()));
        nvps.add(new BasicNameValuePair("refund_date", request.getRefundDate()));
        nvps.add(new BasicNameValuePair("batch_num", request.getBatchNum()));
        nvps.add(new BasicNameValuePair("detail_data", request.getDetailData()));
        nvps.add(new BasicNameValuePair("use_freeze_amount", request.getUseFreezeAmount()));
        nvps.add(new BasicNameValuePair("return_type", request.getReturnType()));
        nvps.add(new BasicNameValuePair("sign", request.getSign()));

        return nvps;
    }

    public AliPayRefundResponse generateResponse(String result) throws DocumentException {
        Document document = DocumentHelper.parseText(result);
        Element root = document.getRootElement();
        AliPayRefundResponse response = new AliPayRefundResponse();
        response.setOriginalResponse(result);
        response.setError(root.elementText("error"));
        response.setIsSuccess(root.elementText("is_success"));
        return response;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SSLContext ctx = SSLContext.getInstance("SSL");
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
        };
        ctx.init(null, new TrustManager[] {tm}, null);

        SSLConnectionSocketFactory ssf =
                new SSLConnectionSocketFactory(ctx, SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setDefaultMaxPerRoute(Constants.DEFAULT_MAX_PER_ROUTE);
        cm.setMaxTotal(Constants.MAX_TOTAL);

        this.httpClient = HttpClientBuilder.create()
                .setSSLSocketFactory(ssf)
                .setConnectionManager(cm)
                .build();
    }
}
