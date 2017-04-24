package com.ymatou.payment.integration.service.applepay;

import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.constants.Constants;
import com.ymatou.payment.integration.model.ApplePayRefundRequest;
import com.ymatou.payment.integration.model.ApplePayRefundResponse;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

/**
 * Created by zhangxiaoming on 2017/4/24.
 * 退款申请
 */
public class ApplePayRefundService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ApplePayRefundQueryService.class);

    private CloseableHttpClient httpClient;

    @Autowired
    private IntegrationConfig integrationConfig;

    public ApplePayRefundResponse doService(ApplePayRefundRequest request, HashMap<String, String> header)
            throws Exception {

        return null;
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
