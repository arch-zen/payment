/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.channel.service;

import java.util.HashMap;

import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;

/**
 * 提交第三方退款
 * 
 * @author qianmin 2016年6月6日 上午11:35:50
 *
 */
public interface AcquireRefundService {

    public void notifyRefund(RefundRequestPo refundRequest, Payment payment, HashMap<String, String> header);
}
