package com.ymatou.payment.test.integration.service.alipay;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.payment.integration.model.QueryTimestampResponse;
import com.ymatou.payment.integration.service.alipay.QueryTimestampService;
import com.ymatou.payment.test.RestBaseTest;

public class QueryTimestampServiceTest extends RestBaseTest {

    @Autowired
    private QueryTimestampService queryTimestampService;

    @Test
    public void testDoServiceSuccess() throws Exception {
        // HashMap<String, String> header = new HashMap<>();
        // header.put("Mock", "1");
        // header.put("MockId", "888888");
        QueryTimestampResponse response =
                queryTimestampService.doService("query_timestamp", "2088701734809577", null);
        Assert.assertNotNull(response);
        Assert.assertEquals("T", response.getIsSuccess());
        Assert.assertNotNull(response.getTimestampEncryptKey());
        Assert.assertNull(response.getError());
    }

    @Test
    public void testDoServiceFail() throws Exception {
        // HashMap<String, String> header = new HashMap<>();
        // header.put("Mock", "1");
        // header.put("MockId", "888888");
        QueryTimestampResponse response =
                queryTimestampService.doService("query_timestamp", "20887017348095771", null);
        Assert.assertNotNull(response);
        Assert.assertEquals("F", response.getIsSuccess());
        Assert.assertNull(response.getTimestampEncryptKey());
        Assert.assertNotNull(response.getError());
    }

}
