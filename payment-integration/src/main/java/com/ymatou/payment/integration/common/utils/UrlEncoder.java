package com.ymatou.payment.integration.common.utils;

import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;

/**
 * Created by zhangxiaoming on 2017/4/25.
 */
public class UrlEncoder {
    private static Logger logger = LoggerFactory.getLogger(UrlEncoder.class);

    /**
     * URL 编码
     *
     * @param value
     * @return
     */
    public static String encode(String value, String encoding) {
        try {
            return URLEncoder.encode(value, encoding);
        } catch (Exception e) {
            logger.error("url encode failed", e);
            throw new BizException(ErrorCode.FAIL, "url encode failed");
        }
    }

    /**
     * URL 编码
     *
     * @param value
     * @return
     */
    public static String encode(String value) {
        return encode(value, "utf-8");
    }
}
