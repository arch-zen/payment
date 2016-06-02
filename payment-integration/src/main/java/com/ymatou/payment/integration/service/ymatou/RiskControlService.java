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
import com.ymatou.payment.integration.model.RiskControlRequest;

/**
 * 风控服务调用
 * 
 * @author qianmin 2016年5月26日 下午5:15:05
 *
 */
@Component
public class RiskControlService implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(RiskControlService.class);

    private CloseableHttpClient httpClient;

    @Autowired
    private IntegrationConfig integrationConfig;

    public boolean doService(RiskControlRequest request, HashMap<String, String> header) throws IOException {
        String url = integrationConfig.getYmtRiskControlUrl(header);

        try {
            String result = HttpClientUtil.sendPost(url, JSONObject.toJSONString(request), Constants.CONTENT_TTPE_JSON,
                    header, httpClient);
            return JSONObject.parseObject(result).getInteger("Code") == 200;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw e;
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
