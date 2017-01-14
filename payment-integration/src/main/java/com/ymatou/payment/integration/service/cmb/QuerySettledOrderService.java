/**
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.integration.service.cmb;

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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.HttpClientUtil;
import com.ymatou.payment.integration.common.constants.Constants;
import com.ymatou.payment.integration.model.CmbQuerySettledOrderRequest;
import com.ymatou.payment.integration.model.CmbQuerySettledOrderResponse;

/**
 * 按商户日期查询已结账订单
 * 
 * @see http://58.61.30.110/OpenAPI2/API/IntDeclareAPI8.aspx
 * @author Administrator 2017年1月14日 上午10:46:47
 *
 */
@Component
public class QuerySettledOrderService implements InitializingBean {
    private CloseableHttpClient httpClient;

    @Autowired
    private IntegrationConfig integrationConfig;

    public CmbQuerySettledOrderResponse doService(CmbQuerySettledOrderRequest request, HashMap<String, String> header)
            throws Exception {
        String url = integrationConfig.getCmbQuerySettledOrderUrl(header);
        List<NameValuePair> body = getRequestBody(request);
        String resp = HttpClientUtil.sendPost(url, body, header, httpClient);

        CmbQuerySettledOrderResponse response = JSON.parseObject(resp, CmbQuerySettledOrderResponse.class);

        return response;
    }

    private List<NameValuePair> getRequestBody(CmbQuerySettledOrderRequest req) {
        List<NameValuePair> nvp = new ArrayList<>();
        nvp.add(new BasicNameValuePair("jsonRequestData", JSONObject.toJSONString(req)));
        return nvp;
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

        SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(ctx,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setDefaultMaxPerRoute(Constants.DEFAULT_MAX_PER_ROUTE);
        cm.setMaxTotal(Constants.MAX_TOTAL);

        this.httpClient = HttpClientBuilder.create()
                .setSSLSocketFactory(ssf)
                .setConnectionManager(cm)
                .build();
    }
}
