/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 第三方机构常量
 * 
 * @author wangxudong 2016年5月12日 下午4:35:52
 *
 */
public final class AliPayConsts {
    /**
     * 收款类型：商品购买
     * 
     * @see <a href='https://doc.open.alipay.com/doc2/detail?treeId=62&articleId=103748&docType=1'>
     *      支付宝文档 - 商品购买</a>
     */
    public final static String PAYMENT_TYPE_PURCHARE = "1";

    /**
     * 字符编码集
     */
    public final static String INPUT_CHARSET = "utf-8";

    /**
     * 支付方式
     */
    public final static HashMap<String, String> PAY_METHOD_MAP = new HashMap<String, String>() {
        /**
         * 序列化版本
         */
        private static final long serialVersionUID = -5559567189017277137L;

        {
            put(null, "bankPay");
            put("1", "creditCard");
            put("2", "bankPay");
            put("3", "directPay");
        }
    };

    /**
     * 及时到账支付接口
     */
    public final static String PAY_SERVICE_NORMAL = "create_direct_pay_by_user";

    /**
     * 统一下单支付接口（杭报订单专用）
     */
    public final static String PAY_SERVICE_HANBO = "alipay.acquire.page.createandpay";

    /**
     * App订单关闭时间
     */
    public final static String ALI_APP_ORDER_CLOSE_TIME = "10d";

    public final static Set<String> ALI_TRADE_OK_STATUS =
            new HashSet<String>(Arrays.asList(new String[] {"TRADE_SUCCESS", "TRADE_FINISHED"}));

    public final static String TRADE_REFUND_SUCCESS = "REFUND_SUCCESS";

    public final static String WAP_ACQUIRE_ORDER_SERVICE = "alipay.wap.auth.authAndExecute";
}
