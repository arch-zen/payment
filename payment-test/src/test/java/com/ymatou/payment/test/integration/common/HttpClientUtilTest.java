/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.integration.common;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.junit.Test;

import com.ymatou.payment.integration.common.HttpClientUtil;
import com.ymatou.payment.integration.common.constants.Constants;

public class HttpClientUtilTest {

    private CloseableHttpAsyncClient httpClient;

    @Test
    public void testSendPost() throws IOException, InterruptedException, ExecutionException {
        ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
        PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
        cm.setDefaultMaxPerRoute(Constants.DEFAULT_MAX_PER_ROUTE);
        cm.setMaxTotal(Constants.MAX_TOTAL);

        httpClient = HttpAsyncClients.custom().setConnectionManager(cm).build();
        httpClient.start();

        String url = "http://deliveryservice.ymatou.com:12346/api/Delivery";
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("PaymentId", "15151254300985970"));
        params.add(new BasicNameValuePair("TraceId", "xxxx-xxxx-111"));

        HttpClientUtil.sendPost(url, params, null, httpClient);

        assertEquals("null==null", true, null == null);
    }
}
