package com.ymatou.payment.test.integration.service.applepay.common;

import com.ymatou.payment.integration.model.ApplePayConsumeResponse;
import com.ymatou.payment.integration.service.applepay.common.ApplePayMessageUtil;
import com.ymatou.payment.test.RestBaseTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by gejianhua on 2017/4/27.
 */
public class ApplePayMessageUtilTest extends RestBaseTest{

    @Test
    public void testBuildMapEmpty(){
        String str = "";
        Map<String, String> map = ApplePayMessageUtil.genResponseMessage(str);
        Assert.assertEquals(null, map);

        ApplePayConsumeResponse response = ApplePayConsumeResponse.loadProperty(map, ApplePayConsumeResponse.class);
        Assert.assertNotNull( response);

        ApplePayConsumeResponse response1 = new ApplePayConsumeResponse();
        response1.loadProperty(map);
    }

    @Test
    public void testBuildMapErrorFormat(){
        String str = "adfafads";
        Map<String, String> map = ApplePayMessageUtil.genResponseMessage(str);
        Assert.assertNotNull(map);

    }
}
