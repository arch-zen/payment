/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.test.integration.service.alipay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.integration.model.AliPayRefundRequest;
import com.ymatou.payment.integration.model.AliPayRefundResponse;
import com.ymatou.payment.integration.service.alipay.AliPayRefundService;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 
 * @author qianmin 2016年5月30日 下午5:39:10
 *
 */
public class AliPayRefundServiceTest extends RestBaseTest {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    @Autowired
    private AliPayRefundService refundService;

    @Autowired
    private SignatureService signatureService;

    @Autowired
    private InstitutionConfigManager institutionConfigManager;

    @Test
    public void testDoService() throws Exception {
        AliPayRefundRequest request = new AliPayRefundRequest();
        request.setPartner("2088701734809577");
        request.setNotifyUrl("http://ymatou.com");
        request.setSignType("MD5");
        request.setDbackNotifyUrl("http://ymatou.com");
        request.setBatchNo(sdf.format(new Date()) + "000" + new Random().nextInt(10));
        request.setRefundDate(new Date());
        request.setBatchNum("1");
        request.setDetailData("2016052721001004980280896845^0.01^abc");
        request.setUseFreezeAmount("N");

        InstitutionConfig instConfig = institutionConfigManager.getConfig(PayTypeEnum.AliPayWap);
        String sign = signatureService.signMessage(request.mapForSign(), instConfig, null);

        request.setSign(sign);

        AliPayRefundResponse response = refundService.doService(request, null);
        System.out.println(JSONObject.toJSONString(response));
    }

}
