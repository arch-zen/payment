/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.pay.repository;

import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.infrastructure.db.extmapper.CmbPublicKeyExtMapper;
import com.ymatou.payment.infrastructure.db.model.CmbPublicKeyPo;
import com.ymatou.payment.integration.IntegrationConfig;

/**
 * 招行公钥查询
 * 
 * @author wangxudong 2016年11月18日 下午6:53:41
 *
 */
@Component
public class CmbPublicKeyRepository {

    @Resource
    private CmbPublicKeyExtMapper cmbPublicKeyExtMapper;

    @Resource
    private IntegrationConfig integrationConfig;

    /**
     * Mock 公钥
     */
    private final String mockPublicKey =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDmP+e7IKiIxxGYzYZl5S2qbmCbkgAjeM81mgwp7owBHdncbpcaee2o65zYFI0SXJuzu5rZBe3BPLucuZeg3t7FjNf2C7j8gfDYcviMamOwI7VuWZ+ZEtCHSHkOhUlwcul5xLMXl2nPd1YzL6zMCe2VAK75cHoBKnd+DmEVSOQipQIDAQAB";

    /**
     * 获取公钥
     * 
     * @param mockHeader
     * @return
     */
    public String getPublicKey(HashMap<String, String> mockHeader) {
        if (integrationConfig.isMock(mockHeader)) {
            return mockPublicKey;
        } else {
            CmbPublicKeyPo cmbPublicKeyPo = cmbPublicKeyExtMapper.selectLatestOne();
            if (cmbPublicKeyPo == null || StringUtils.isEmpty(cmbPublicKeyPo.getPublicKey())) {
                throw new BizException(ErrorCode.NOT_EXIST_CMB_PUBLIC_KEY, "");
            }

            return cmbPublicKeyPo.getPublicKey();
        }
    }
}
