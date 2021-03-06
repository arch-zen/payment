/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.service.ymatou;

import java.util.HashMap;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.HttpClientUtil;
import com.ymatou.payment.integration.common.constants.Constants;
import com.ymatou.payment.integration.model.AccountingRequest;
import com.ymatou.payment.integration.model.AccountingResponse;

/**
 * 账务服务
 * 
 * @author qianmin 2016年5月26日 下午3:31:22
 *
 */
@Component
public class AccountService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private CloseableHttpClient httpClient;

    @Autowired
    private IntegrationConfig integrationConfig;

    public AccountingResponse accounting(AccountingRequest request, HashMap<String, String> header) {
        try {
            String url = integrationConfig.getYmtAccountingUrl(header);
            String result = HttpClientUtil.sendPost(url, JSONObject.toJSONString(request), Constants.CONTENT_TYPE_JSON,
                    header, httpClient);
            AccountingResponse response = JSON.parseObject(result, AccountingResponse.class);

            return response;
        } catch (Exception e) {
            AccountingResponse response = new AccountingResponse();
            response.setStatusCode(AccountingResponse.AccountingCode_SYSTEMERROR);
            response.setMessage(e.getMessage());
            logger.error("accouting error. BizNo:" + request.getAccountingItems().get(0).getBizNo(), e);

            return response;
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
