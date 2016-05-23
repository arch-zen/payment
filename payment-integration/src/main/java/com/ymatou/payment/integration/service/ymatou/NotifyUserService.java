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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.HttpClientUtil;
import com.ymatou.payment.integration.common.constants.Constants;
import com.ymatou.payment.integration.model.NotifyUserRequest;

/**
 * 通知用户服务
 * 
 * @author qianmin 2016年5月11日 下午12:03:52
 * 
 */
@Component
public class NotifyUserService implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(NotifyUserService.class);

    private CloseableHttpAsyncClient httpClient;

    @Autowired
    private IntegrationConfig integrationConfig;

    public void sendTradingMessage(NotifyUserRequest request, HashMap<String, String> header) throws Exception {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("BusinessType", request.getBusinessType()));
            params.add(new BasicNameValuePair("BuyerId", request.getBuyerId()));
            params.add(new BasicNameValuePair("IsShangouOrder", String.valueOf(request.isIsShangouOrder())));
            params.add(new BasicNameValuePair("OrderId", request.getOrderId()));
            HttpClientUtil.sendPost(integrationConfig.getYmtNotifytradingeventUrl(header),
                    params, header, httpClient);
        } catch (Exception e) {
            logger.error("async send trading message", e);
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
