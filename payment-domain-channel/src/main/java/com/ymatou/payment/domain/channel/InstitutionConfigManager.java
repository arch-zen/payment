/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 * 
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel;

import java.io.File;
import java.io.FileReader;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.baidu.disconf.client.DisConf;
import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import com.ymatou.payment.domain.pay.repository.BussinessOrderRepository;
import com.ymatou.payment.facade.constants.PayTypeEnum;

/**
 * 第三方机构配置文件管理器
 * 
 * @author wangxudong
 *
 */
@DisconfUpdateService(confFileKeys = {InstitutionConfigManager.CONFIG_FILE})
@Component
public class InstitutionConfigManager implements IDisconfUpdate, InitializingBean {

    /**
     * 第三方机构配置文件
     */
    public static final String CONFIG_FILE = "institutionConfig.xml";

    /**
     * 第三方机构配置
     */
    private static InstitutionConfigCollection instConfigCollection;

    /**
     * 读写锁
     */
    private ReadWriteLock myLock = new ReentrantReadWriteLock();;

    private static final Logger logger = LoggerFactory.getLogger(BussinessOrderRepository.class);

    /**
     * 重新加载配置文件
     */
    @Override
    public void reload() throws Exception {
        myLock.writeLock().lock();
        try {
            init();
        } catch (Exception e) {
            logger.error("reload institutionConfig.xml file failed.", e);
        } finally {
            myLock.writeLock().unlock();
        }
    }

    /**
     * 初始化配置文件
     * 
     * @throws Exception
     */
    private void init() throws Exception {
        File configFile = DisConf.getLocalConfig(CONFIG_FILE);
        JAXBContext context = JAXBContext.newInstance(InstitutionConfigCollection.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        instConfigCollection = (InstitutionConfigCollection) unmarshaller.unmarshal(new FileReader(configFile));

        //FIXME: {}
        //FIXME: 需要验证没种PaymentType都有config吗
        if (instConfigCollection == null || instConfigCollection.size() == 0)
            throw new Exception("无效的配置文件：institutionConfig.xml");
    }

    /**
     * 根据PayType获取到第三方机构信息
     * 
     * @param payType
     * @return
     */
    public InstitutionConfig getConfig(PayTypeEnum payType) {
        myLock.readLock().lock();

        InstitutionConfig instConfig = null;
        for (InstitutionConfig institutionConfig : instConfigCollection) {
            if (institutionConfig.getPayType().equals(payType.getCode())) {
                instConfig = institutionConfig;
                break;
            }
        }

        myLock.readLock().unlock();
        return instConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
