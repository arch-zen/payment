/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.pay;

import org.springframework.stereotype.Component;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

@Component
@DisconfFile(fileName = "domain-pay.properties")
public class PaymentConfig {
    /**
     * 支付宝内部用户Id(匿名支付时支付宝回调会传递这个号)
     */
    private String aliInternalBuyerId;

    /**
     * 支付回调通知MD5Key
     */
    private String notifySignSalt;

    /**
     * @return the aliInternalBuyerId
     */
    @DisconfFileItem(name = "ali_internal_buyer_id")
    public String getAliInternalBuyerId() {
        return aliInternalBuyerId;
    }

    /**
     * @param aliInternalBuyerId the aliInternalBuyerId to set
     */
    public void setAliInternalBuyerId(String aliInternalBuyerId) {
        this.aliInternalBuyerId = aliInternalBuyerId;
    }

    /**
     * @return the notifySignSalt
     */
    @DisconfFileItem(name = "notify_sign_salt")
    public String getNotifySignSalt() {
        return notifySignSalt;
    }

    /**
     * @param notifySignSalt the notifySignSalt to set
     */
    public void setNotifySignSalt(String notifySignSalt) {
        this.notifySignSalt = notifySignSalt;
    }


}
