/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test;

import java.util.HashMap;
import java.util.UUID;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.StringKeySerializer;

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
}
