/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund;

import org.springframework.stereotype.Component;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

/**
 * 
 * @author qianmin 2016年5月12日 下午2:10:46
 * 
 */
@Component
@DisconfFile(fileName = "domain-refund.properties")
public class DomainConfig {

    private String refundSupportDays;
    private String notSupportRefundPayType;

    @DisconfFileItem(name = "refundsupportdays")
    public String getRefundSupportDays() {
        return refundSupportDays;
    }

    public void setRefundSupportDays(String refundSupportDays) {
        this.refundSupportDays = refundSupportDays;
    }

    @DisconfFileItem(name = "not.support.refund.paytype")
    public String getNotSupportRefundPayType() {
        return notSupportRefundPayType;
    }

    public void setNotSupportRefundPayType(String notSupportRefundPayType) {
        this.notSupportRefundPayType = notSupportRefundPayType;
    }
}
