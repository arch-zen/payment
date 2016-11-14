/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.common.constants;

/**
 * 
 * @author qianmin 2016年5月23日 下午6:51:06
 *
 */
public class Constants {
    public static final Integer CONN_TIME_OUT = 1000 * 60;
    public static final Integer SOCKET_TIME_OUT = 1000 * 60;
    public static final Integer DEFAULT_MAX_PER_ROUTE = 20;
    public static final Integer MAX_TOTAL = 100;
    public static final Long CONN_MANAGER_TIMEOUT = 500L;

    public static final String MOCK = "1"; // http header (mock=1)
    public static final String WEIXIN_RESPONSE_BODY_START = "<xml>";
    public static final String CONTENT_TYPE_XML = "text/xml";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";


    public static final Integer SUCCESS_CODE = 200;
}
