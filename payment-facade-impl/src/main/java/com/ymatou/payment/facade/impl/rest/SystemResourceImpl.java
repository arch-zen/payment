/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.impl.rest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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

    private static final Logger logger = LoggerFactory.getLogger(SystemResourceImpl.class);

    @Override
    @GET
    @Path("/warmup")
    public String status() {

        return "ok";
    }

    @Override
    @GET
    @Path("/appid")
    public String appid() {
        try {
            String appid = getProperty("app.name");
            return appid;
        } catch (Exception e) {
            logger.error("failed when query appid", e);
            return "unkonw";
        }
    }

    /**
     * 获取到properties文件属性
     * 
     * @param name
     * @return
     * @throws IOException
     */
    private String getProperty(String name) throws IOException {
        // String file = "/META-INF/app.properties";
        // Properties props = new Properties();
        // props.load(this.getClass().getResourceAsStream(file));
        //
        // return props.getProperty(name);


        String path = System.getProperty("user.dir");// 这个即可获取当前项目所在磁盘路径，那么程序里面就可以根据这个path拼接了

        Properties props = new Properties();
        // 这里可以利用上面path找到jar包同目录的properties文件，如果properties文件在jar里面，那么是无法修改的，因为IO流写不到jar包里面去
        FileInputStream fis = new FileInputStream(path + "/META-INF/app.properties");
        props.load(fis);
        fis.close();

        return props.getProperty(name);
    }

}
