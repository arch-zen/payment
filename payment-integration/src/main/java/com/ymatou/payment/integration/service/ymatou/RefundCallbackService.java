/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.service.ymatou;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.HttpClientUtil;
import com.ymatou.payment.integration.common.constants.Constants;
import com.ymatou.payment.integration.model.RefundCallbackRequest;

/**
 * 退款发货回调, 通知交易系统
 * 
 * @author qianmin 2016年5月31日 下午4:26:58
 *
 */
@Component
public class RefundCallbackService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(RefundCallbackService.class);

    private CloseableHttpClient httpClient;

    @Autowired
    private IntegrationConfig integrationConfig;

    public boolean doService(RefundCallbackRequest request, boolean isNewSystem, HashMap<String, String> header)
            throws IOException {
        String url = getRequestUrl(isNewSystem, header);
        boolean isSuccess = HttpClientUtil.sendPostToGetStatus(url, JSONObject.toJSONString(request),
                Constants.CONTENT_TYPE_JSON, header, httpClient);
        logger.info("refund callback response isSuccess: {}", isSuccess);

        return isSuccess;
    }

    private String getRequestUrl(boolean isNewSystem, HashMap<String, String> header) {
        if (isNewSystem) {
            return integrationConfig.getYmtTriggerOrderRefundUrlJava(header);
        } else {
            return integrationConfig.getYmtTriggerOrderRefundUrl(header);
        }
    }

    @Override
    public void afterPropertiesSet() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setDefaultMaxPerRoute(Constants.DEFAULT_MAX_PER_ROUTE);
        cm.setMaxTotal(Constants.MAX_TOTAL);

        httpClient = HttpClients.custom().setConnectionManager(cm).build();
    }
}
