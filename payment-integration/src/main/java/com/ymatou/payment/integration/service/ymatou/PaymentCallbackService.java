/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.service.ymatou;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.ymatou.payment.integration.common.HttpClientUtil;
import com.ymatou.payment.integration.common.constants.Constants;
import com.ymatou.payment.integration.model.PaymentCallbackRequest;

/**
 * 支付发货回调, 通知交易系统
 * http://api.trading.operate.ymatou.com/api/Trading/TradingCompletedNotify
 * 
 * @author qianmin 2016年5月31日 下午4:26:39
 *
 */
@Component
public class PaymentCallbackService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(PaymentCallbackService.class);

    private CloseableHttpClient httpClient;

    public boolean doService(String url, PaymentCallbackRequest request, HashMap<String, String> header)
            throws IOException {
        String result = HttpClientUtil.sendPost(url, JSONObject.toJSONString(request), Constants.CONTENT_TYPE_JSON,
                header, httpClient);
        if (StringUtils.isEmpty(result)) {
            return false;
        }

        Integer code = (Integer) JSONObject.parseObject(result).get("Code");
        if (code == null) {
            code = (Integer) JSONObject.parseObject(result).get("code");
        }

        logger.info("payment callback response code: {}", code);

        return Constants.SUCCESS_CODE.equals(code); // 200成功，其他失败
    }

    @Override
    public void afterPropertiesSet() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setDefaultMaxPerRoute(Constants.DEFAULT_MAX_PER_ROUTE);
        cm.setMaxTotal(Constants.MAX_TOTAL);

        httpClient = HttpClients.custom().setConnectionManager(cm).build();
    }
}
