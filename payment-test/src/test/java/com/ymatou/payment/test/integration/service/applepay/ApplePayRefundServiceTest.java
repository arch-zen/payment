package com.ymatou.payment.test.integration.service.applepay;

import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.service.applepay.ApplePayRefundService;
import com.ymatou.payment.test.RestBaseTest;

import javax.annotation.Resource;

/**
 * Created by zhangxiaoming on 2017/4/28.
 */
public class ApplePayRefundServiceTest extends RestBaseTest {
    @Resource
    private InstitutionConfigManager instConfigManager;

    @Resource
    private IntegrationConfig integrationConfig;

    @Resource
    private ApplePayRefundService applePayRefundService;


}
