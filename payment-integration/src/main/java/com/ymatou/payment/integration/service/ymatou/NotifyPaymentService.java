/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.service.ymatou;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.HttpClientUtil;
import com.ymatou.payment.integration.common.constants.Constants;

/**
 * 支付通知发货服务
 * 
 * @author wangxudong 2016年5月10日 下午4:13:29
 *
 */
@Component
public class NotifyPaymentService implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(NotifyPaymentService.class);

    private CloseableHttpAsyncClient httpClient;

    @Autowired
    private IntegrationConfig integrationConfig;

    /**
     * 支付成功通知
     * 
     * @param paymentId
     * @param traceId
     * @param header
     * @return
     * @throws IOException
     */
    public void doService(String paymentId, String traceId, HashMap<String, String> header) throws Exception {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("PaymentId", paymentId));
            params.add(new BasicNameValuePair("TraceId", traceId));
            String result = HttpClientUtil.sendPost(integrationConfig.getYmtNotifyPaymentUrl(header),
                    params, header, httpClient);
            if (!"OK".equalsIgnoreCase(result)) {
                logger.info("refund compensate call failed on {},{}", paymentId, result);
            }
        } catch (Exception e) {
            logger.error("refund compensate call failed on {}", paymentId, e);
            throw e;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
        PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
        cm.setDefaultMaxPerRoute(Constants.DEFAULT_MAX_PER_ROUTE);
        cm.setMaxTotal(Constants.MAX_TOTAL);

        httpClient = HttpAsyncClients.custom().setConnectionManager(cm).build();
        httpClient.start();
    }
}
