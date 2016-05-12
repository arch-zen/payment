package com.ymatou.payment.test.integration.service.ymatou;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ymatou.payment.integration.model.UserServiceResponse;
import com.ymatou.payment.integration.service.ymatou.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class UserServiceTest {

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
