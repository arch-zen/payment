/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.service;

import java.util.HashMap;

import com.ymatou.payment.domain.channel.model.AcquireOrderPackageResp;
import com.ymatou.payment.domain.pay.model.Payment;

public interface AcquireOrderService {
    /**
     * 收单报文组织
     * 
     * @param payment
     * @return
     */
    AcquireOrderPackageResp acquireOrderPackage(Payment payment, HashMap<String, String> mockHeader);
}
