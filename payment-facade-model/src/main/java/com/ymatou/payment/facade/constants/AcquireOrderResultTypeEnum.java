/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.constants;

/**
 * 收单返回类型
 * 
 * @author wangxudong 2016年5月11日 下午5:01:57
 *
 */
public enum AcquireOrderResultTypeEnum {
    /**
     * HTML表单
     */
    Form,

    /**
     * JSON字符串
     */
    JSON,

    /**
     * URL
     */
    Query,

    /**
     * 二位码内容，客户端需要将其转为二维码图片
     */
    QRCode
}
