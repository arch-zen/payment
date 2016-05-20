/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ymatou.payment.facade.PrintFriendliness;

/**
 * Weixin App 收单请求
 * 
 * @author wangxudong 2016年5月15日 下午5:02:30
 *
 */
public class WeixinAppOrderRequest extends PrintFriendliness {

    /**
     * 序列化版本
     */
    @JsonIgnore
    private static final long serialVersionUID = 3534095711312771602L;

    /**
     * AppId
     */
    public String AppID;

    /**
     * 商户号
     */
    public String MerchantId;

    /**
     * PrePayId
     */
    public String PayToken;

    /**
     * 时间戳
     */
    public String TimeStamp;

    /**
     * 随机数
     */
    public String NonceStr;

    /**
     * 签名
     */
    public String Sign;

    /**
     * Package
     */
    public String Package;

}
