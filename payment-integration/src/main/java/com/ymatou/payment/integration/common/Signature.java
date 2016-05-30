/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.common;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * 加签验签工具类
 * 
 * @author qianmin 2016年5月9日 上午10:42:18
 *
 */
public class Signature {

    private static Logger logger = LoggerFactory.getLogger(Signature.class);

    /**
     * 签名算法
     * 
     * @param o 要参与签名的数据对象
     * @param key 盐值
     * @return 签名
     * @throws NoSuchAlgorithmException
     * @throws IllegalAccessException
     */
    public static String getSign(Object o, String key) throws NoSuchAlgorithmException {
        ArrayList<String> list = new ArrayList<String>();
        Class<?> cls = o.getClass();
        Field[] fields = cls.getDeclaredFields();
        try {
            for (Field f : fields) {
                f.setAccessible(true); // 设置成员变量可访问
                if (f.get(o) != null && f.get(o) != "") {
                    list.add(f.getName() + "=" + f.get(o) + "&");
                }
            }
        } catch (IllegalAccessException e) {
            // 不会出现非法访问异常
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + key;
        logger.info("Sign Before MD5:" + result);
        result = MD5.MD5Encode(result).toUpperCase();
        logger.info("Sign Result:" + result);
        return result;
    }

    /**
     * 签名算法
     * 
     * @param map 要参与签名的map
     * @param key 盐值
     * @return 签名
     * @throws NoSuchAlgorithmException
     */
    public static String getSign(Map<String, Object> map, String key) throws NoSuchAlgorithmException {
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != "") {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + key;
        logger.info("Sign Before MD5:" + result);
        result = MD5.MD5Encode(result).toUpperCase();
        logger.info("Sign Result:" + result);
        return result;
    }

    /**
     * 从API返回的XML数据里面重新计算一次签名
     * 
     * @param responseString API返回的XML数据
     * @param key 盐值
     * @return 签名
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws NoSuchAlgorithmException
     */
    public static String getSignFromResponseString(String responseString, String key)
            throws IOException, SAXException, ParserConfigurationException, NoSuchAlgorithmException {
        Map<String, Object> map = XmlParser.getMapFromXML(responseString);
        // 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
        map.put("sign", "");
        // 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
        return Signature.getSign(map, key);
    }

    /**
     * 检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改
     * 
     * @param responseString API返回的XML数据字符串
     * @param key 盐值
     * @return API签名是否合法
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws NoSuchAlgorithmException
     */
    public static boolean checkIsSignValidFromResponseString(String responseString, String key)
            throws ParserConfigurationException, IOException, SAXException, NoSuchAlgorithmException {

        Map<String, Object> map = XmlParser.getMapFromXML(responseString);

        String signFromAPIResponse = (String) map.get("sign");
        if (signFromAPIResponse == "" || signFromAPIResponse == null) {
            logger.info("sign not exist!");
            return false;
        }
        logger.info("sign in response:" + signFromAPIResponse);

        map.put("sign", "");
        String signForAPIResponse = Signature.getSign(map, key); // 计算签名
        if (!signForAPIResponse.equals(signFromAPIResponse)) { // 与返回签名进行比较
            logger.info("Signature verification does not pass");
            return false;
        }
        return true;
    }

}
