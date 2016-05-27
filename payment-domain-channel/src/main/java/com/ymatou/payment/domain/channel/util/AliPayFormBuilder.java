/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.util;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.HtmlUtils;

import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;

/**
 * 支付宝报文拼接
 * 
 * @author wangxudong 2016年5月13日 下午2:34:57
 *
 */
/**
 * @author wangxudong 2016年5月13日 下午2:39:45
 *
 */
public final class AliPayFormBuilder {
    private static Logger logger = LoggerFactory.getLogger(AliPayFormBuilder.class);

    /**
     * 表单
     */
    private final static String formFormat =
            "<html>%s<body><form id=\"postform\" method=\"GET\" action=\"%s\">%s</form><script type=\"text/javascript\">document.getElementById(\"postform\").submit();</script></body></html>";

    /**
     * Meta
     */
    private final static String metaFormat = "<meta content=\"charset=utf-8\" />";

    /**
     * Input
     */
    private final static String inputFormat = "<input name=\"%s\" type=\"hidden\" value=\"%s\" />";

    /**
     * 构建AliPay网关表单
     * 
     * @param reqMap
     * @param action
     * @return
     */
    public static String buildForm(Map<String, String> reqMap, String url) {
        try {
            StringBuilder sbInput = new StringBuilder();
            for (Map.Entry<String, String> entry : reqMap.entrySet()) {
                if (!StringUtils.isBlank(entry.getValue())) {
                    sbInput.append(
                            String.format(inputFormat, entry.getKey(), HtmlUtils.htmlEscape(entry.getValue())));
                }
            }
            String form = String.format(formFormat, metaFormat, url, sbInput.toString());

            return form;
        } catch (Exception ex) {
            logger.error("build alipay form failed", ex);
            throw new BizException(ErrorCode.FAIL, "BuildAliPayForm", ex);
        }
    }
}
