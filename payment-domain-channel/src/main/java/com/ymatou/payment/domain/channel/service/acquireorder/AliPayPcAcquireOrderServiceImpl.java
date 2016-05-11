/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.acquireorder;

import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.model.AcquireOrderPackageResp;
import com.ymatou.payment.domain.channel.model.AcquireOrderResultType;
import com.ymatou.payment.domain.channel.service.AcquireOrderService;
import com.ymatou.payment.domain.pay.model.Payment;

/**
 * AliPay PC 收单报文组织（10）
 * 
 * @author wangxudong 2016年5月11日 下午5:22:24
 *
 */
@Component
public class AliPayPcAcquireOrderServiceImpl implements AcquireOrderService {


    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ymatou.payment.domain.channel.service.AcquireOrderService#AcquireOrder(com.ymatou.payment
     * .domain.pay.model.Payment)
     */
    @Override
    public AcquireOrderPackageResp acquireOrderPackage(Payment payment) {
        AcquireOrderPackageResp resp = new AcquireOrderPackageResp();
        resp.setResultType(AcquireOrderResultType.Form);

        return resp;
    }
}
