/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.util;

import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;

/**
 * Url编码
 * 
 * @author wangxudong 2016年5月13日 下午7:28:57
 *
 */
public final class UrlEncoder {
    private static Logger logger = LoggerFactory.getLogger(UrlEncoder.class);

    /**
     * URL 编码
     * 
     * @param s
     * @return
     */
    public static String encode(String s) {
        try {
            return URLEncoder.encode(s, "utf-8");

        } catch (Exception e) {
            logger.error("url encode failed", e);
            throw new BizException(ErrorCode.FAIL, "url encode failed");
        }
    }
}
