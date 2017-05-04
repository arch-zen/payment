package com.ymatou.payment.test.integration.service.ymatou;

import com.ymatou.payment.integration.common.constants.BusinessTypeEnum;
import com.ymatou.payment.integration.model.NotifyUserRequest;
import com.ymatou.payment.integration.service.ymatou.NotifyUserService;
import com.ymatou.payment.test.RestBaseTest;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;

/**
 * Created by wangxudong on 2017/4/24.
 */
public class NotifyUserServiceTest extends RestBaseTest {

    @Resource
    private NotifyUserService notifyUserService;

    @Test
    public void testSendTradingMessage() throws Exception {
        NotifyUserRequest request = new NotifyUserRequest();
        request.setBusinessType(BusinessTypeEnum.FAST_REFUND.getCode());
        request.setBuyerId("100");
        request.setIsShangouOrder(true);
        request.setOrderId("1000022765");
        String result = notifyUserService.sendTradingMessage(request, null);

        assertEquals("ok", result);
    }
}
