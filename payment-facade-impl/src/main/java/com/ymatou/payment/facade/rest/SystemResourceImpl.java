/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.rest;

import com.ymatou.payment.infrastructure.util.NetUtil;
import com.ymatou.payment.integration.IntegrationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.File;

/**
 * 系统消息实现
 * 
 * @author wangxudong 2016年5月22日 下午10:09:34
 *
 */
@Component("systemResource")
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class SystemResourceImpl implements SystemResource {

    @Autowired
    private IntegrationConfig integrationConfig;

    @GET
    @Path("/version")
    @Override
    public String version() {
        return "{"
                + "\"ip\":\"" + NetUtil.getHostIp() + "\","
                + "\"1.0.0\":\"2016-09-23.1 first add version.\","
                + "\"1.0.1\":\"2016-11-14.2 validate merchant id.\","
                + "\"1.1.0\":\"2016-11-28.2 add cmb pay(50).\","
                + "\"1.1.1\":\"2016-11-28.1 add weixin pc pay(16).\","
                + "\"1.1.2\":\"2016-12-02.1 fix accounting amount when cmb fast refund.\","
                + "\"1.1.3\":\"2016-12-06.1 not set openid when weixin pc pay.\","
                + "\"1.1.4\":\"2016-12-12.2 update payment status and complete refund amount when cmb refund success.\","
                + "\"1.1.5\":\"2017-01-13.1 add refund query by refundno.\","
                + "\"1.1.6\":\"2017-01-14.1 add cmb settled order query api.\","
                + "\"1.1.7\":\"2017-01-17.1 add cmb check order api.\","
                + "\"1.1.8\":\"2017-02-20.1 change user wap openid query api url.\","
                + "\"1.1.9\":\"2017-03-03.1 add refund query by bizno api.\","
                + "\"1.1.10\":\"2017-03-08.6 change pc payment form meta.\","
                + "\"1.1.11\":\"2017-03-22.1 call trading when wx refund return USER_ACCOUNT_ABNORMAL.\""
                + "\"1.1.12\":\"2017-04-25.1 update refund batchno when query return SELLER_BALANCE_NOT_ENOUGH and afterday.\""
                + "\"1.1.13\":\"2017-05-04.1 add applypay and review code.\""
                + "}";
    }


    @Override
    @GET
    @Path("/warmup")
    public String status() {
        File jsapiCert = new File(integrationConfig.getWxJsapiCertPath());
        File appCert = new File(integrationConfig.getWxAppCertPath());
        if (jsapiCert.exists() && appCert.exists()) {
            return "ok";
        } else {
            return "weixin cert not exist.";
        }
    }
}
