/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.channel.service;

import java.util.HashMap;

import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;

/**
 * 退款查询
 * 
 * @author qianmin 2016年6月8日 下午3:36:12
 *
 */
public interface RefundQueryService {

    /**
     * 提交第三方退款查询
     * 
     * @param refundRefund
     * @param payment
     * @param header
     * @return
     */
    public RefundStatusEnum queryRefund(RefundRequestPo refundRequest, Payment payment, HashMap<String, String> header);

}
