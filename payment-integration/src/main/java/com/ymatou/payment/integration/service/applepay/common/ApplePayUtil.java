package com.ymatou.payment.integration.service.applepay.common;

import com.ymatou.payment.facade.BizException;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by gejianhua on 2017/4/25.
 */
public class ApplePayUtil {
    /**
     * 过滤请求报文中的空字符串或者空字符串
     *
     * @param contentData
     * @return
     */
    public static Map<String, String> filterBlank(Map<String, String> contentData) {
        if (contentData == null) {
            throw new BizException("filterBlank exception: map is null!");
        }
        Map<String, String> submitFromData = new HashMap<String, String>();
        Set<String> keyset = contentData.keySet();

        for (String key : keyset) {
            String value = contentData.get(key);
            if (StringUtils.isNotBlank(value)) {
                // 对value值进行去除前后空处理
                submitFromData.put(key, value.trim());
            }
        }
        return submitFromData;
    }

    /**
     * 生成signature所需的明文
     * 将Map中的数据转换成key1=value1&key2=value2的形式 不包含签名域signature
     *
     * @param data 待拼接的Map数据
     * @return 拼接好后的字符串
     */
    public static String genPlaintext(Map<String, String> data) {
        TreeMap<String, String> tree = new TreeMap<String, String>();
        Iterator<Map.Entry<String, String>> it = data.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, String> en = it.next();
            if (ApplePayConstants.param_signature.equals(en.getKey().trim())) {
                continue;
            }
            tree.put(en.getKey(), en.getValue());
        }

        it = tree.entrySet().iterator();
        StringBuffer sf = new StringBuffer();

        while (it.hasNext()) {
            Map.Entry<String, String> en = it.next();
            sf.append(en.getKey() + ApplePayConstants.EQUAL + en.getValue()
                    + ApplePayConstants.AMPERSAND);
        }
        return sf.substring(0, sf.length() - 1);
    }

    /**
     * 生成http请求参数报文
     * 将Map存储的对象，转换为key=value&key=value的字符
     *
     * @param requestParam
     * @return
     */
    public static String genRequestParamMessage(Map<String, String> requestParam) {
        if (requestParam == null || requestParam.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> en : requestParam.entrySet()) {
            try {
                if (StringUtils.isBlank(en.getValue())) {
                    continue;
                }
                String value = en.getValue().trim();
                value = URLEncoder.encode(value, ApplePayConstants.encoding);
                sb.append(en.getKey() + ApplePayConstants.EQUAL + value + ApplePayConstants.AMPERSAND);
            } catch (Exception ex) {
                throw new BizException("genRequestParamMessage exception:", ex);
            }
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 将形如key=value&key=value的字符串转换为相应的Map对象
     *
     * @param result
     * @return
     */
    public static Map<String, String> convertResponseStringToMap(String result) {
        Map<String, String> map = null;
        try {
            if (StringUtils.isNotBlank(result)) {
                if (result.startsWith("{") && result.endsWith("}")) {
                    result = result.substring(1, result.length() - 1);
                }
                map = parseQString(result);
            }

        } catch (Exception e) {
            throw new BizException("convertResultStringToMap exception, with result:" + result, e);
        }
        return map;
    }


    /**
     * 解析应答字符串，生成应答要素
     *
     * @param str 需要解析的字符串
     * @return 解析的结果map
     * @throws UnsupportedEncodingException
     */
    private static Map<String, String> parseQString(String str)
            throws UnsupportedEncodingException {

        Map<String, String> map = new HashMap<String, String>();
        int len = str.length();
        StringBuilder temp = new StringBuilder();
        char curChar;
        String key = null;
        boolean isKey = true;
        boolean isOpen = false;//值里有嵌套
        char openName = 0;
        if (len > 0) {
            for (int i = 0; i < len; i++) {// 遍历整个带解析的字符串
                curChar = str.charAt(i);// 取当前字符
                if (isKey) {// 如果当前生成的是key

                    if (curChar == '=') {// 如果读取到=分隔符
                        key = temp.toString();
                        temp.setLength(0);
                        isKey = false;
                    } else {
                        temp.append(curChar);
                    }
                } else {// 如果当前生成的是value
                    if (isOpen) {
                        if (curChar == openName) {
                            isOpen = false;
                        }

                    } else {//如果没开启嵌套
                        if (curChar == '{') {//如果碰到，就开启嵌套
                            isOpen = true;
                            openName = '}';
                        }
                        if (curChar == '[') {
                            isOpen = true;
                            openName = ']';
                        }
                    }
                    if (curChar == '&' && !isOpen) {// 如果读取到&分割符,同时这个分割符不是值域，这时将map里添加
                        putKeyValueToMap(temp, isKey, key, map);
                        temp.setLength(0);
                        isKey = true;
                    } else {
                        temp.append(curChar);
                    }
                }

            }
            putKeyValueToMap(temp, isKey, key, map);
        }
        return map;
    }

    private static void putKeyValueToMap(StringBuilder temp, boolean isKey,
                                         String key, Map<String, String> map)
            throws UnsupportedEncodingException {
        if (isKey) {
            key = temp.toString();
            if (key.length() == 0) {
                throw new BizException("putKeyValueToMap format illegal");
            }
            map.put(key, "");
        } else {
            if (key.length() == 0) {
                throw new BizException("putKeyValueToMap format illegal");
            }
            map.put(key, temp.toString());
        }
    }
}















































