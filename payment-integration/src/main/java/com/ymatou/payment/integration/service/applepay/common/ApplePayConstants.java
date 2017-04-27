package com.ymatou.payment.integration.service.applepay.common;

/**
 * Created by gejianhua on 2017/4/25.
 * applypay常量定义
 */
public class ApplePayConstants {

    public static final String version = "5.0.0";
    public static final String encoding = "UTF-8";
    public static final String signMethod = "01";
    public static final String currency_code = "156";
    public static final String access_type = "0";
    public static final String channel_type = "08";


    /**
     * 成功
     */
    public static final String response_success_code = "00";
    /**
     * 有缺陷的成功
     */
    public static final String response_success_defect_code = "A6";


    public static final String EQUAL = "=";
    public static final String AMPERSAND = "&";

    public static final String time_format = "YYYYMMddHHmmss";
    public static final String content_type = "application/x-www-form-urlencoded";



    public static final String param_signature = "signature";

}
