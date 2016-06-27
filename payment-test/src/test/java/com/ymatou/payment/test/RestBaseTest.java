/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test;

import java.util.HashMap;
import java.util.UUID;

import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContextRestTest.xml"})
public class RestBaseTest extends BaseTest {

    /**
     * 构建MockHeader
     * 
     * @return
     */
    protected HashMap<String, String> buildMockHeader() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("mock", "1");
        header.put("mockId", UUID.randomUUID().toString());

        return header;
    }

    /**
     * 设置MockHeader
     * 
     * @param servletRequest
     */
    protected void setMockHeader(MockHttpServletRequest servletRequest) {
        servletRequest.addHeader("mock", 1);
        servletRequest.addHeader("mockId", UUID.randomUUID().toString());
    }
}
