package com.ymatou.payment.test.integration.service.ymatou;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.payment.integration.model.UserServiceResponse;
import com.ymatou.payment.integration.service.ymatou.UserService;
import com.ymatou.payment.test.RestBaseTest;

public class UserServiceTest extends RestBaseTest {

    @Autowired
    private UserService userService;

    @Test
    public void testDoServiceSuccess() throws IOException {
        HashMap<String, String> header = new HashMap<>();
        UserServiceResponse response = userService.doService("3790800", "Wap", header);
        Assert.assertNotNull(response);
        Assert.assertEquals("true", response.getSuccess());
        Assert.assertEquals("ok", response.getSuccessMessage());
        Assert.assertNotNull(response.getResult());
    }

    @Test
    public void testDoServiceFail() throws IOException {
        HashMap<String, String> header = new HashMap<>();
        UserServiceResponse response = userService.doService("37908001", "Wap", header);
        Assert.assertNotNull(response);
        Assert.assertEquals("false", response.getSuccess());
        Assert.assertNull(response.getSuccessMessage());
        Assert.assertEquals("", response.getResult());

    }
}
