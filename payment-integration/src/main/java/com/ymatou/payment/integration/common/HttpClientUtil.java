/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.common;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于提交get,post请求
 * 
 * @author qianmin 2016年5月9日 上午10:42:18
 *
 */
public class HttpClientUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * 
     * @param url 请求路径
     * @param header 请求Header
     * @param httpClient 执行请求的HttpClient
     * @return 请求应答
     * @throws ParseException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String sendGet(String url, HashMap<String, String> header, HttpClient httpClient) throws IOException {
        String result = null;

        HttpGet httpGet = new HttpGet(url);
        if (header != null && "1".equals(header.get("mock"))) { // mock
            for (Entry<String, String> entry : header.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue()); // add request header
            }
        }
        logger.info("executing request" + httpGet.getRequestLine());
        logger.info("request header: " + Arrays.toString(httpGet.getAllHeaders()));

        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            logger.info("response message:" + result);
        } finally {
            httpGet.abort();
        }

        return result;
    }

    /**
     * 
     * @param url 请求路径
     * @param body 请求body
     * @param header 请求header
     * @param httpClient 执行请求的HttpClient
     * @return 请求应答
     * @throws ParseException
     * @throws IOException
     */
    public static String sendPost(String url, String body, HashMap<String, String> header, HttpClient httpClient)
            throws IOException {
        String result = null;

        HttpPost httpPost = new HttpPost(url);
        StringEntity postEntity = new StringEntity(body, "UTF-8");
        httpPost.setEntity(postEntity); // set request body
        if (header != null && "1".equals(header.get("mock"))) {
            for (Entry<String, String> entry : header.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue()); // add request header
            }
        }
        logger.info("executing request" + httpPost.getRequestLine());
        logger.info("request header: " + Arrays.toString(httpPost.getAllHeaders()));
        logger.info("request body: " + body);

        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            logger.info("response message:" + result);
        } finally {
            httpPost.abort();
        }

        return result;
    }

    /**
     * 
     * @param url 请求路径
     * @param body 请求body
     * @param header 请求header
     * @param httpClient 执行请求的HttpClient
     * @return 请求应答
     * @throws ParseException
     * @throws IOException
     */
    public static String sendPost(String url, List<NameValuePair> body, HashMap<String, String> header,
            HttpClient httpClient)
                    throws IOException {
        String result = null;

        HttpPost httpPost = new HttpPost(url);
        UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(body, "UTF-8");
        httpPost.setEntity(postEntity); // set request body
        if (header != null && "1".equals(header.get("mock"))) {
            for (Entry<String, String> entry : header.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue()); // add request header
            }
        }
        logger.info("executing request" + httpPost.getRequestLine());
        logger.info("request header: " + Arrays.toString(httpPost.getAllHeaders()));
        logger.info("request body: " + body);

        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            logger.info("response message:" + result);
        } finally {
            httpPost.abort();
        }

        return result;
    }


    /**
     * 
     * @param url 请求路径
     * @param body 请求body
     * @param header 请求header
     * @param httpClient 执行请求的HttpClient
     * @return 请求应答
     * @throws ParseException
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static String sendPost(String url, List<NameValuePair> body, HashMap<String, String> header,
            CloseableHttpAsyncClient httpClient)
                    throws IOException, InterruptedException, ExecutionException {
        String result = null;

        HttpPost httpPost = new HttpPost(url);
        UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(body, "UTF-8");
        httpPost.setEntity(postEntity); // set request body
        if (header != null && "1".equals(header.get("mock"))) {
            for (Entry<String, String> entry : header.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue()); // add request header
            }
        }
        logger.info("executing request" + httpPost.getRequestLine());
        logger.info("request header: " + Arrays.toString(httpPost.getAllHeaders()));
        logger.info("request body: " + body);

        try {
            Future<HttpResponse> future = httpClient.execute(httpPost, null);
            HttpEntity entity = future.get().getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            logger.info("response message:" + result);
        } finally {
            httpPost.abort();
        }

        return result;
    }
}
