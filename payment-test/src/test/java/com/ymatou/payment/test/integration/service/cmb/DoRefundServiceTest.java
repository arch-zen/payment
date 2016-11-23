/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.integration.service.cmb;

import static org.junit.Assert.*;

import java.util.HashMap;

import javax.annotation.Resource;

import org.junit.Test;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.integration.common.CmbSignature;
import com.ymatou.payment.integration.model.CmbDoRefundRequest;
import com.ymatou.payment.integration.model.CmbDoRefundResponse;
import com.ymatou.payment.integration.service.cmb.DoRefundService;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 一网通单笔订单查询服务测试用例
 * 
 * @author wangxudong 2016年11月10日 下午3:56:45
 *
 */
public class DoRefundServiceTest extends RestBaseTest {

    @Resource
    private DoRefundService doRefundService;

    @Resource
    private SignatureService singatureService;

    @Resource
    private InstitutionConfigManager instConfigManager;

    @Test
    public void testDoService() throws Exception {
        InstitutionConfig config = instConfigManager.getConfig(PayTypeEnum.CmbApp);
        CmbDoRefundRequest req = new CmbDoRefundRequest();
        req.getReqData().setBranchNo(config.getBranchNo());
        req.getReqData().setMerchantNo(config.getMerchantId());
        req.getReqData().setDate("20160629");
        req.getReqData().setOrderNo("9999000042");
        req.getReqData().setAmount("0.01");
        req.getReqData().setRefundSerialNo("201606250000001");
        req.getReqData().setOperatorNo("9999");
        req.getReqData().setEncrypType("RC4");
        // 密码需要进行RC4签名，9999用户的密码默认是商户号
        req.getReqData().setPwd(CmbSignature.rc4Sign(config.getMd5Key(), config.getMerchantId()));

        String sign = CmbSignature.shaSign(config.getMd5Key(), req.buildSignString());
        req.setSign(sign);

        CmbDoRefundResponse response = doRefundService.doService(req, null);

        assertNotNull(response.getRspData());
        assertEquals("MSS3804", response.getRspData().getRspCode());
    }

    @Test
    public void testDoServiceWhenSignWrong() throws Exception {
        InstitutionConfig config = instConfigManager.getConfig(PayTypeEnum.CmbApp);
        CmbDoRefundRequest req = new CmbDoRefundRequest();
        req.getReqData().setBranchNo(config.getBranchNo());
        req.getReqData().setMerchantNo(config.getMerchantId());
        req.getReqData().setDate("20160629");
        req.getReqData().setOrderNo("9999000042");
        req.getReqData().setAmount("0.01");
        req.getReqData().setRefundSerialNo("201606250000001");
        req.getReqData().setOperatorNo("9999");
        req.getReqData().setEncrypType("RC4");
        // 密码需要进行RC4签名，9999用户的密码默认是商户号
        req.getReqData().setPwd(CmbSignature.rc4Sign(config.getMd5Key(), config.getMerchantId()));

        String sign = CmbSignature.shaSign(config.getMd5Key() + "make sign error", req.buildSignString());
        req.setSign(sign);

        HashMap<String, String> mockHeader = buildMockHeader();
        mockHeader.put("MockResult-Cmb-rspCode", "MSS3411");
        CmbDoRefundResponse response = doRefundService.doService(req, mockHeader);


        assertNotNull(response.getRspData());
        assertEquals("MSS3411", response.getRspData().getRspCode());
    }

    @Test
    public void testDoServiceWhenOperatorPwdWrong() throws Exception {
        InstitutionConfig config = instConfigManager.getConfig(PayTypeEnum.CmbApp);
        CmbDoRefundRequest req = new CmbDoRefundRequest();
        req.getReqData().setBranchNo(config.getBranchNo());
        req.getReqData().setMerchantNo(config.getMerchantId());
        req.getReqData().setDate("20160629");
        req.getReqData().setOrderNo("9999000042");
        req.getReqData().setAmount("0.01");
        req.getReqData().setRefundSerialNo("201606250000001");
        req.getReqData().setOperatorNo("9999");
        req.getReqData().setEncrypType("RC4");
        // 密码需要进行RC4签名，9999用户的密码默认是商户号
        req.getReqData().setPwd(CmbSignature.rc4Sign(config.getMd5Key(), config.getMerchantId() + "make pwd error"));

        String sign = CmbSignature.shaSign(config.getMd5Key(), req.buildSignString());
        req.setSign(sign);

        CmbDoRefundResponse response = doRefundService.doService(req, null);

        assertNotNull(response.getRspData());
        assertEquals("MSS3111", response.getRspData().getRspCode());
    }

}
