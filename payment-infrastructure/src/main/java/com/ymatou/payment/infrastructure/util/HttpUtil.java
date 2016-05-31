/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.infrastructure.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Http 工具类
 * 
 * @author wangxudong 2016年5月20日 下午7:35:59
 *
 */
public final class HttpUtil {

    /**
     * 将查询字符串转成Map
     * 
     * @param queryString
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> parseQueryStringToMap(String queryString) throws UnsupportedEncodingException {
        if (StringUtils.isBlank(queryString)) {
            return null;
        }

        Map<String, String> map = new HashMap<String, String>();
        String[] qitemList = queryString.split("&");
        for (String item : qitemList) {
            String[] kvItem = item.split("=");
            map.put(kvItem[0], URLDecoder.decode(kvItem[1], "utf-8"));
        }

        return map;
    }

    /**
     * 将Map转成QueryString
     * 
     * @param map
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String parseMapToQueryString(Map<String, String> map) throws UnsupportedEncodingException {
        if (map == null || map.size() == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> item : map.entrySet()) {
            sb.append(String.format("&%s=%s", item.getKey(), URLEncoder.encode(item.getValue(), "utf-8")));
        }

        return sb.toString().replaceFirst("&", "");
    }
}
