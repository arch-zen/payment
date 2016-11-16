/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.infrastructure.db.extmapper;

import com.ymatou.payment.infrastructure.db.model.CmbPublicKeyPo;

/**
 * 招行公钥查询Mapper
 * 
 * @author wangxudong 2016年11月16日 下午7:48:47
 *
 */
public interface CmbPublicKeyExtMapper {

    /**
     * 获取最新的招行公钥
     * 
     * @return
     */
    public CmbPublicKeyPo selectLatestOne();
}
