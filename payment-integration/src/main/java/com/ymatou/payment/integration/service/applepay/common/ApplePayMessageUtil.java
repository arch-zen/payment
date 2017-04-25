package com.ymatou.payment.integration.service.applepay.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by gejianhua on 2017/4/24.
 * applepay报文帮助类
 */
@Component
public class ApplePayMessageUtil {

    private static Logger logger = LoggerFactory.getLogger(ApplePayMessageUtil.class);


    /**
     * 生成请求报文
     * @param reqMessage
     * @return
     */
    public String genRequestMessage(Map<String, String> reqMessage){

        return ApplyPayUtil.genRequestParamMessage(reqMessage);
    }


    /**
     * 生成响应Map
     * @param respMessage
     * @return
     */
    public Map<String, String> genResponseMessage(String respMessage){

        return ApplyPayUtil.convertResponseStringToMap(respMessage);
    }


}















































