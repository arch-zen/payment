/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 * 
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel;

import java.io.File;
import java.io.FileReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.springframework.stereotype.Component;

import com.baidu.disconf.client.DisConf;
import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;

/**
 * 第三方机构配置文件管理器
 * 
 * FIXME, reload参数不要, 实现InitializingBean， reload方法注意线程安全
 * 
 * @author wangxudong
 *
 */
@DisconfUpdateService(confFileKeys = {InstitutionConfigManager.CONFIG_FILE})
@Component
public class InstitutionConfigManager implements IDisconfUpdate{

    /**
     * 第三方机构配置文件
     */
    public static final String CONFIG_FILE = "institutionConfig.xml";

    /**
     * 第三方机构配置
     */
    private InstitutionConfigCollection instConfigCollection;

    /**
     * 重新加载配置文件
     */
    @Override
    public void reload() throws Exception {
        init(true);

    }

    /**
     * 初始化配置文件
     */
    private void init(boolean isReload) {
        if (instConfigCollection != null && isReload == false)
            return;

        try {
            File configFile = DisConf.getLocalConfig(CONFIG_FILE);
            JAXBContext context = JAXBContext.newInstance(InstitutionConfigCollection.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            instConfigCollection = (InstitutionConfigCollection) unmarshaller.unmarshal(new FileReader(configFile));
            
            //FIXME: 需要验证每种PaymentType都有配置?

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 根据PayType获取到第三方机构信息
     * 
     * @param payType
     * @return
     */
    public InstitutionConfig getConfig(String payType) {
        init(false);

        for (InstitutionConfig institutionConfig : instConfigCollection) {
            if (institutionConfig.getPayType().equals(payType))
                return institutionConfig;
        }
        return null;
    }


}
