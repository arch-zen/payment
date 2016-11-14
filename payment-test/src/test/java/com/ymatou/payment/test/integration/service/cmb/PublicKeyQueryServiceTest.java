/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.integration.service.cmb;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Test;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.integration.common.CmbSignature;
import com.ymatou.payment.integration.model.CmbPublicKeyQueryRequest;
import com.ymatou.payment.integration.model.CmbPublicKeyQueryResponse;
import com.ymatou.payment.integration.service.cmb.PublicKeyQueryService;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 招行公钥查询测试用例
 * 
 * @author wangxudong 2016年11月10日 下午3:56:45
 *
 */
public class PublicKeyQueryServiceTest extends RestBaseTest {

    @Resource
    private PublicKeyQueryService publicKeyQueryService;

    @Resource
    private SignatureService singatureService;

    @Resource
    private InstitutionConfigManager instConfigManager;

    @Test
    public void testDoService() throws Exception {
        InstitutionConfig config = instConfigManager.getConfig(PayTypeEnum.CmbApp);
        CmbPublicKeyQueryRequest req = new CmbPublicKeyQueryRequest();
        req.getReqData().setBranchNo(config.getBranchNo());
        req.getReqData().setMerchantNo(config.getMerchantId());

        String sign = CmbSignature.shaSign(config.getMd5Key(), req.buildSignString());
        req.setSign(sign);

        CmbPublicKeyQueryResponse response = publicKeyQueryService.doService(req, buildMockHeader());

        assertNotNull(response.getRspData());
        assertEquals("SUC0000", response.getRspData().getRspCode());

        String retSign = CmbSignature.shaSign(config.getMd5Key(), response.buildSignString());
        assertEquals(retSign, response.getSign());
    }

    @Test
    public void testDoServiceWhenSignWrong() throws Exception {
        InstitutionConfig config = instConfigManager.getConfig(PayTypeEnum.CmbApp);
        CmbPublicKeyQueryRequest req = new CmbPublicKeyQueryRequest();
        req.getReqData().setBranchNo(config.getBranchNo());
        req.getReqData().setMerchantNo(config.getMerchantId());

        String sign = CmbSignature.shaSign(config.getMd5Key() + "error", req.buildSignString());
        req.setSign(sign);

        CmbPublicKeyQueryResponse response = publicKeyQueryService.doService(req, buildMockHeader());

        assertNotNull(response.getRspData());
        assertEquals("DCB0008", response.getRspData().getRspCode());

        String retSign = CmbSignature.shaSign(config.getMd5Key(), response.buildSignString());
        assertEquals(retSign, response.getSign());
    }

}
