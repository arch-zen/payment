/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.service.alipay;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.HttpClientUtil;
import com.ymatou.payment.integration.common.constants.Constants;
import com.ymatou.payment.integration.model.QueryTimestampResponse;

/**
 * alipay时间戳查询
 * 
 * @author qianmin 2016年5月9日 上午10:42:18
 *
 */
@Component
public class QueryTimestampService implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(QueryTimestampService.class);

    private CloseableHttpClient httpClient;

    @Autowired
    private IntegrationConfig integrationConfig;

    /**
     * 支付宝时间戳服务
     * 
     * @param service
     * @param partner
     * @param header
     * @return
     * @throws Exception
     */
    public QueryTimestampResponse doService(String service, String partner, HashMap<String, String> header)
            throws Exception {
        String url = new StringBuilder(100).append(integrationConfig.getAliPayBaseUrl(header))
                .append("?service=").append(service).append("&partner=").append(partner).toString();

        try {
            String respXmlStr = HttpClientUtil.sendGet(url, header, httpClient);
            QueryTimestampResponse response = generateResponseData(respXmlStr);
            logger.info("queryTimestampResponse: " + JSON.toJSONString(response));

            return response;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

    }

    private QueryTimestampResponse generateResponseData(String respXmlStr) throws DocumentException {
        QueryTimestampResponse response = new QueryTimestampResponse();
        Document document = DocumentHelper.parseText(respXmlStr);
        Element root = document.getRootElement();
        response.setIsSuccess(root.elementText("is_success"));

        if ("F".equals(response.getIsSuccess())) {// 当is_success=F时, 只有error信息
            response.setError(root.elementText("error"));
        } else {// 当is_success=T时
            @SuppressWarnings("unchecked")
            List<Element> requestParam = root.element("request").elements("param");
            response.setService(requestParam.get(0).getData().toString());
            response.setPartner(requestParam.get(1).getData().toString());
            response.setTimestampEncryptKey(root.element("response").element("timestamp").elementText("encrypt_key"));
            response.setSign(root.elementText("sign"));
            response.setSingType(root.elementText("sign_type"));
        }
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
