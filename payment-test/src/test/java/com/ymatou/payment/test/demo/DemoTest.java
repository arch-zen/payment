/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.demo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DemoTest {

    @Test
    public void testNull() {

        Integer integer = 1;
        assertEquals("Null operator", true, null != integer);

        String haha = null;

        System.out.println(haha + "tony");
    }
}
