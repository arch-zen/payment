/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.integration.model.CmbPayRequest;

/**
 * 一网通支付表单构建器
 * 
 * @author wangxudong 2016年11月14日 下午5:29:44
 *
 */
public class CmbFormBuilder {
    private static Logger logger = LoggerFactory.getLogger(CmbFormBuilder.class);

    /**
     * 表单
     */
    private final static String formFormat =
            "<html>%s<body><form id=\"postform\" method=\"POST\" action=\"%s\">%s</form><script type=\"text/javascript\">document.getElementById(\"postform\").submit();</script></body></html>";

    /**
     * Meta
     */
    private final static String metaFormat = "<meta content=\"charset=utf-8\" />";

    /**
     * Input
     */
    private final static String inputFormat = "<input name=\"jsonRequestData\" type=\"hidden\" value=\'%s\' />";

    public static String buildForm(CmbPayRequest req, String url) {
        try {
            String inputs = String.format(inputFormat, JSONObject.toJSONString(req));

            String form = String.format(formFormat, metaFormat, url, inputs);

            return form;
        } catch (Exception ex) {
            logger.error("build cmb form failed", ex);
            throw new BizException(ErrorCode.FAIL, "BuildCmbPayForm", ex);
        }
    }
}
