/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.infrastructure.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 字符串工具类
 * 
 * @author wangxudong 2016年5月10日 下午7:03:59
 *
 */
/**
 * @author Administrator 2016年5月14日 下午10:58:42
 *
 */
public final class StringUtil {
    public static String subString(String oriStr, int beginIndex, int len) {
        int strlen = oriStr.length();
        String str = null;
        beginIndex = beginIndex - 1;
        if (strlen <= beginIndex) {
            System.out.println("out of " + oriStr + "'s length, please recheck!");
        } else if (strlen <= beginIndex + len) {
            str = oriStr.substring(beginIndex);
        } else {
            str = oriStr.substring(beginIndex, beginIndex + len);
        }
        return str;
    }

    /**
     * 右补位，左对齐
     * 
     * @param oriStr 原字符串
     * @param len 目标字符串长度
     * @param alexin 补位字符
     * @return 目标字符串
     */
    public static String padRight(String oriStr, int len, char alexin) {
        int strlen = oriStr.length();
        String str = null;
        if (strlen < len) {
            for (int i = 0; i < len - strlen; i++) {
                str = str + alexin;
            }
        }
        str = oriStr + str;
        return str;
    }

    /**
     * 左补位，右对齐
     * 
     * @param oriStr 原字符串
     * @param len 目标字符串长度
     * @param alexin 补位字符
     * @return 目标字符串
     */
    public static String padLeft(String oriStr, int len, char alexin) {
        int strlen = oriStr.length();
        String str = null;
        if (strlen < len) {
            for (int i = 0; i < len - strlen; i++) {
                str = str + alexin;
            }
        }
        str = str + oriStr;
        return str;
    }

    /**
     * 构造当前日期字符串(yyyyMMddHHmmss)
     * 
     * @return
     */
    public static String getDateFormatString() {
        return getDateFormatString("yyyyMMddHHmmss");
    }

    /**
     * 构造当前日期字符串
     * 
     * @return
     */
    public static String getDateFormatString(String format) {
        return getDateFormatString(format, new Date());
    }

    /**
     * 构造指定日期字符串
     * 
     * @param format
     * @param date
     * @return
     */
    public static String getDateFormatString(String format, Date date) {
        DateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(date);
    }
}
