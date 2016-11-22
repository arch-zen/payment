/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.service;

import java.util.HashMap;
import java.util.Map;

import com.ymatou.payment.domain.channel.InstitutionConfig;

/**
 * 签名服务
 * 
 * @author wangxudong 2016年5月12日 下午7:00:30
 *
 */
public interface SignatureService {

    /**
     * 消息签名
     * 
     * @param rawMapData
     * @param instConfig
     * @param isMock
     * @return 返回签名
     */
    String signMessage(Map<String, String> rawMapData, InstitutionConfig instConfig,
            HashMap<String, String> mockHeader);

    /**
     * 验证签名
     * 
     * @param signMapData 待验证Map中key=sign代表签名字段
     * @param instConfig
     * @param isMock
     * @return
     */
    boolean validateSign(Map<String, String> signMapData, InstitutionConfig instConfig,
            HashMap<String, String> mockHeader);

    /**
     * 获取到Mock的Md5Key
     * 
     * @return
     */
    String getMd5MockKey();
}
