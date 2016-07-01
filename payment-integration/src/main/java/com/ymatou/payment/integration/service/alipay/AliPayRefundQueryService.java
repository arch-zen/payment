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
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.HttpClientUtil;
import com.ymatou.payment.integration.common.constants.Constants;
import com.ymatou.payment.integration.model.AliPayRefundQueryRequest;
import com.ymatou.payment.integration.model.AliPayRefundQueryResponse;

/**
 * 支付宝退款查询
 * 
 * @author qianmin 2016年5月31日 下午2:30:05
 *
 */
@Component
public class AliPayRefundQueryService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(AliPayRefundQueryService.class);

    private CloseableHttpClient httpClient;

    @Autowired
    private IntegrationConfig integrationConfig;

    public AliPayRefundQueryResponse doService(AliPayRefundQueryRequest request, HashMap<String, String> header)
            throws Exception {
        String url = integrationConfig.getAliPayBaseUrl(header);

        String result = HttpClientUtil.sendPost(url, generateRequest(request), header, httpClient);

        AliPayRefundQueryResponse response = generateResponse(result);
        response.setOriginalResponse(result);
        logger.info("refund query response: {}", JSONObject.toJSONString(response));
        return response;
    }

    private List<NameValuePair> generateRequest(AliPayRefundQueryRequest request) {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("service", request.getService()));
        nvps.add(new BasicNameValuePair("partner", request.getPartner()));
        nvps.add(new BasicNameValuePair("_input_charset", request.getInputCharset()));
        nvps.add(new BasicNameValuePair("sign_type", request.getSignType()));
        nvps.add(new BasicNameValuePair("batch_no", request.getBatchNo()));
        nvps.add(new BasicNameValuePair("trade_no", request.getTradeNo()));
        nvps.add(new BasicNameValuePair("sign", request.getSign()));

        return nvps;
    }

    public AliPayRefundQueryResponse generateResponse(String result) throws DocumentException {
        AliPayRefundQueryResponse response = new AliPayRefundQueryResponse();
        String[] respArr = result.split("&");
        HashMap<String, String> map = new HashMap<>();
        for (String str : respArr) {
            String[] item = str.split("=");
            map.put(item[0], item[1]);
        }
        response.setIsSuccess(map.get("is_success"));
        response.setErrorCode(map.get("error_code"));
        response.setResultDetails(map.get("result_details"));
        response.setUnfreezedDetails(map.get("unfreezed_details"));
        response.setSign(map.get("sign"));
        response.setSignType(map.get("sign_type"));

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
