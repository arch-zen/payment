package com.ymatou.payment.integration.service.applepay;

import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.HttpClientUtil;
import com.ymatou.payment.integration.common.constants.Constants;
import com.ymatou.payment.integration.model.ApplePayTradeQueryRequest;
import com.ymatou.payment.integration.model.ApplePayTradeQueryResponse;
import com.ymatou.payment.integration.service.applepay.common.ApplePayConstants;
import com.ymatou.payment.integration.service.applepay.common.ApplePayMessageUtil;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gejianhua on 2017/4/27.
 * applepay交易查询服务
 */
@Service
public class ApplePayTradeQueryService implements InitializingBean {

    private CloseableHttpClient httpClient;

    @Autowired
    private IntegrationConfig integrationConfig;

    public ApplePayTradeQueryResponse doPost(ApplePayTradeQueryRequest request, HashMap<String, String> header) {

        try {
            String body = ApplePayMessageUtil.genRequestMessage(request.genMap());
            String url = this.integrationConfig.getApplePayTradeQueryUrl(header);

            String result = HttpClientUtil.sendPost(url, body, ApplePayConstants.content_type, header, httpClient);

            Map<String, String> resultMap = ApplePayMessageUtil.genResponseMessage(result);
            ApplePayTradeQueryResponse response = ApplePayTradeQueryResponse.loadProperty(resultMap, ApplePayTradeQueryResponse.class);
            response.setOriginalResponse(result);
            return response;

        } catch (Exception ex) {
            throw new BizException("ApplePayTradeQueryService.doPost Exception", ex);
        }
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
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);

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
