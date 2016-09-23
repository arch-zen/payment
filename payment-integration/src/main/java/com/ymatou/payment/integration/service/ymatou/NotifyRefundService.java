/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.service.ymatou;

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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.HttpClientUtil;
import com.ymatou.payment.integration.common.constants.Constants;

/**
 * 通知退款服务
 * 
 * @author qianmin 2016年5月10日 下午4:13:29
 *
 */
@Component
public class NotifyRefundService implements InitializingBean {


    private CloseableHttpAsyncClient httpClient;

    @Autowired
    private IntegrationConfig integrationConfig;

    /**
     * 通知退款
     * 
     * @param paymentId
     * @param traceId
     * @param header
     * @return
     * @throws Exception
     */
    public void doService(String paymentId, String traceId, HashMap<String, String> header) throws Exception {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("PaymentId", paymentId));
        params.add(new BasicNameValuePair("TraceId", traceId));

        HttpClientUtil.sendPost(integrationConfig.getYmtNotifyRefundUrl(header),
                params, header, httpClient);
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
