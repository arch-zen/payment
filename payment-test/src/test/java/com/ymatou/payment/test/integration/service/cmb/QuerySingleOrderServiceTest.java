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
import com.ymatou.payment.integration.model.CmbQuerySingleOrderRequest;
import com.ymatou.payment.integration.model.CmbQuerySingleOrderResponse;
import com.ymatou.payment.integration.service.cmb.QuerySingleOrderService;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 一网通单笔订单查询服务测试用例
 * 
 * @author wangxudong 2016年11月10日 下午3:56:45
 *
 */
public class QuerySingleOrderServiceTest extends RestBaseTest {

    @Resource
    private QuerySingleOrderService querySingleOrderService;

    @Resource
    private SignatureService singatureService;

    @Resource
    private InstitutionConfigManager instConfigManager;

    @Test
    public void testDoService() throws Exception {
        InstitutionConfig config = instConfigManager.getConfig(PayTypeEnum.CmbApp);
        CmbQuerySingleOrderRequest req = new CmbQuerySingleOrderRequest();
        req.getReqData().setType("A");
        req.getReqData().setBranchNo(config.getBranchNo());
        req.getReqData().setMerchantNo(config.getMerchantId());
        req.getReqData().setBankSerialNo("16250327200000000020");

        String sign = CmbSignature.shaSign(config.getMd5Key(), req.buildSignString());
        req.setSign(sign);

        CmbQuerySingleOrderResponse response = querySingleOrderService.doService(req, null);

        assertNotNull(response.getRspData());
        assertEquals("MSS3803", response.getRspData().getRspCode());
    }

    @Test
    public void testDoServiceWhenSignWrong() throws Exception {
        InstitutionConfig config = instConfigManager.getConfig(PayTypeEnum.CmbApp);
        CmbQuerySingleOrderRequest req = new CmbQuerySingleOrderRequest();
        req.getReqData().setBranchNo(config.getBranchNo());
        req.getReqData().setMerchantNo(config.getMerchantId());
        req.getReqData().setBankSerialNo("16250327200000000020");

        String sign = CmbSignature.shaSign(config.getMd5Key() + "make sign error", req.buildSignString());
        req.setSign(sign);

        CmbQuerySingleOrderResponse response = querySingleOrderService.doService(req, null);

        assertNotNull(response.getRspData());
        assertEquals("MSS3411", response.getRspData().getRspCode());
    }

}
