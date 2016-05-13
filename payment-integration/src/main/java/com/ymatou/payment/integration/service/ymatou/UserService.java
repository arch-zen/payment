/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.service.ymatou;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.HttpClientUtil;
import com.ymatou.payment.integration.model.UserServiceResponse;

/**
 * ymatou用户服务
 * 
 * @author qianmin 2016年5月9日 上午10:42:18
 *
 */
@Component
public class UserService implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    private CloseableHttpClient httpClient;

    @Autowired
    private IntegrationConfig integrationConfig;

    /**
     * 用户服务
     * 
     * @param userId
     * @param reqSource
     * @param header 请求header
     * @return UserServiceResponse
     * @throws IOException
     */
    public UserServiceResponse doService(String userId, String reqSource, HashMap<String, String> header)
            throws IOException {
        String url = new StringBuilder(128).append(integrationConfig.getYmtUserServiceUrl(header))
                .append("?UserId=").append(userId).append("&ReqSource=").append(reqSource).toString();

        try {
            String result = HttpClientUtil.sendGet(url, header, httpClient);
            UserServiceResponse userServiceResponse = new Gson().fromJson(result, UserServiceResponse.class);
            return userServiceResponse;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.httpClient = HttpClientBuilder.create().build();
    }
}
