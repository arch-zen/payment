/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 * 
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 第三方机构配置文件监听
 * 
 * @author wangxudong
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "institutionConfigCollection")
public class InstitutionConfigCollection extends ArrayList<InstitutionConfig> {

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = -8335215458091158756L;

    /**
     * 第三方机构配置列表
     */
    @XmlElement(name = "config")
    public List<InstitutionConfig> getConfigs() {
        return this;
    }
}
