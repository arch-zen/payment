/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.integration.service.cmb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.junit.Test;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.integration.common.CmbSignature;
import com.ymatou.payment.integration.model.CmbAggrementCancelRequest;
import com.ymatou.payment.integration.model.CmbAggrementCancelResponse;
import com.ymatou.payment.integration.service.cmb.AggrementCancelService;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 协议取消服务测试
 * 
 * @author wangxudong 2016年11月10日 下午3:56:45
 *
 */
public class AggrementCancelServiceTest extends RestBaseTest {

    @Resource
    private AggrementCancelService aggrementCancelService;

    @Resource
    private SignatureService singatureService;

    @Resource
    private InstitutionConfigManager instConfigManager;

    @Test
    public void testDoService() throws Exception {
        InstitutionConfig config = instConfigManager.getConfig(PayTypeEnum.CmbApp);
        CmbAggrementCancelRequest req = new CmbAggrementCancelRequest();
        req.getReqData().setBranchNo(config.getBranchNo());
        req.getReqData().setMerchantNo(config.getMerchantId());
        req.getReqData().setAgrNo("1234567");
        req.getReqData().setMerchantSerialNo(String.valueOf(System.currentTimeMillis()));

        String sign = CmbSignature.shaSign(config.getMd5Key(), req.buildSignString());
        req.setSign(sign);

        CmbAggrementCancelResponse response = aggrementCancelService.doService(req, null);

        assertNotNull(response.getRspData());
        assertEquals("SUC0000", response.getRspData().getRspCode());
    }

    @Test
    public void testDoServiceWhenSignWrong() throws Exception {
        InstitutionConfig config = instConfigManager.getConfig(PayTypeEnum.CmbApp);
        CmbAggrementCancelRequest req = new CmbAggrementCancelRequest();
        req.getReqData().setBranchNo(config.getBranchNo());
        req.getReqData().setMerchantNo(config.getMerchantId());
        req.getReqData().setAgrNo("1234567");
        req.getReqData().setMerchantSerialNo(String.valueOf(System.currentTimeMillis()));

        String sign = CmbSignature.shaSign(config.getMd5Key() + "make sign error", req.buildSignString());
        req.setSign(sign);

        CmbAggrementCancelResponse response = aggrementCancelService.doService(req, null);

        assertNotNull(response.getRspData());
        assertEquals("DCB0008", response.getRspData().getRspCode());
    }

}
