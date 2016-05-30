/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.service.wxpay;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.HttpClientUtil;
import com.ymatou.payment.integration.common.XmlParser;
import com.ymatou.payment.integration.common.constants.Constants;
import com.ymatou.payment.integration.model.UnifiedOrderRequest;
import com.ymatou.payment.integration.model.UnifiedOrderResponse;

/**
 * 微信支付统一下单
 * 
 * @author qianmin 2016年5月9日 上午10:42:18
 *
 */
@Component
public class UnifiedOrderService implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(UnifiedOrderService.class);

    private CloseableHttpClient httpClient;

    @Autowired
    private IntegrationConfig integrationConfig;

    /**
     * 微信支付统一下单服务
     * FIXME: 明确throw Exception，为什么还要try/catch ??
     * @param request
     * @param header
     * @return
     * @throws Exception
     */
    public UnifiedOrderResponse doService(UnifiedOrderRequest request, HashMap<String, String> header)
            throws Exception {
        try {
            String respXmlStr = HttpClientUtil.sendPost(integrationConfig.getWxUnifiedOrderUrl(header),
                    getPostDataXml(request), Constants.CONTENT_TTPE_XML, header, httpClient);
            if (!StringUtils.isEmpty(respXmlStr) && respXmlStr.startsWith(Constants.WEIXIN_RESPONSE_BODY_START)) {
                Map<String, Object> respMap = XmlParser.getMapFromXML(respXmlStr);
                UnifiedOrderResponse response = generateResponseData(respMap);
                return response;
            }
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    private String getPostDataXml(UnifiedOrderRequest request) {
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8",
                new XmlFriendlyNameCoder("-_", "_"))); // 解决XStream对出现双下划线的bug
        String postDataXML = xStreamForRequestPostData.toXML(request);
        postDataXML = postDataXML.replace("com.ymatou.payment.integration.model.UnifiedOrderRequest", "xml");
        return postDataXML;
    }

    private UnifiedOrderResponse generateResponseData(Map<String, Object> responseMap) {
        UnifiedOrderResponse response = new UnifiedOrderResponse();
        response.setReturn_code((String) responseMap.get("return_code"));
        response.setReturn_msg((String) responseMap.get("return_msg"));
        response.setAppid((String) responseMap.get("appid"));
        response.setMch_id((String) responseMap.get("mch_id"));
        response.setDevice_info((String) responseMap.get("device_info"));
        response.setNonce_str((String) responseMap.get("nonce_str"));
        response.setSign((String) responseMap.get("sign"));
        response.setResult_code((String) responseMap.get("result_code"));
        response.setErr_code((String) responseMap.get("err_code"));
        response.setErr_code_des((String) responseMap.get("err_code_des"));
        response.setTrade_type((String) responseMap.get("trade_type"));
        response.setPrepay_id((String) responseMap.get("prepay_id"));
        response.setCode_url((String) responseMap.get("code_url"));
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
