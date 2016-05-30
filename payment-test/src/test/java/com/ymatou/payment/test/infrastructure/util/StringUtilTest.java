/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.infrastructure.util;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.ymatou.payment.infrastructure.util.StringUtil;

/**
 * String工具类测试
 * 
 * @author wangxudonng 2016年5月14日 下午11:01:23
 *
 */
public class StringUtilTest {

    @Test
    public void testGetDateFormatString() {
        String format1 = "yyyyMMddHHmmss";
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 0, 1, 1, 1, 1);
        Date date1 = calendar.getTime();
        String date1String = StringUtil.getDateFormatString(format1, date1);

        assertEquals(format1, "20150101010101", date1String);

        calendar.set(2015, 0, 11, 23, 11, 11);
        Date date2 = calendar.getTime();
        String date2String = StringUtil.getDateFormatString(format1, date2);

        assertEquals(format1, "20150111231111", date2String);

        Date dateNow = new Date();
        assertEquals("Year", new Integer(dateNow.getYear() + 1900),
                new Integer(StringUtil.getDateFormatString("yyyy")));

        System.out.println(StringUtil.getDateFormatString("yyyyMMddHHmmss"));
    }
}
