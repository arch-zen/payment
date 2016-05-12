package com.ymatou.payment.test.integration.service.alipay;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ymatou.payment.integration.model.QueryTimestampResponse;
import com.ymatou.payment.integration.service.alipay.QueryTimestampService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class QueryTimestampServiceTest {

    @Autowired
    private QueryTimestampService queryTimestampService;

    @Test
    public void testDoServiceSuccess() throws Exception {
        HashMap<String, String> header = new HashMap<>();
        // header.put("Mock", "1");
        // header.put("MockId", "888888");
        QueryTimestampResponse response =
                queryTimestampService.doService("query_timestamp", "2088701734809577", header);
        Assert.assertNotNull(response);
        Assert.assertEquals("T", response.getIsSuccess());
        Assert.assertNotNull(response.getTimestampEncryptKey());
        Assert.assertNull(response.getError());
    }

    @Test
    public void testDoServiceFail() throws Exception {
        HashMap<String, String> header = new HashMap<>();
        // header.put("Mock", "1");
        // header.put("MockId", "888888");
        QueryTimestampResponse response =
                queryTimestampService.doService("query_timestamp", "20887017348095771", header);
        Assert.assertNotNull(response);
        Assert.assertEquals("F", response.getIsSuccess());
        Assert.assertNull(response.getTimestampEncryptKey());
        Assert.assertNotNull(response.getError());
    }

}
