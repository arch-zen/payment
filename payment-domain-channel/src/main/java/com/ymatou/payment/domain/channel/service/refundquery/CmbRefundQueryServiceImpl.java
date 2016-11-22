/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.refundquery;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.service.RefundQueryService;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;

/**
 * 招行一网通退款查询服务
 * 
 * 退款API是实时接口，并且目前没有单笔退款的查询接口，所以此服务实现直接返回初始化状态
 * 
 * @author wangxudong 2016年11月22日 上午11:24:40
 *
 */
@Component
public class CmbRefundQueryServiceImpl implements RefundQueryService {

    @Override
    public RefundStatusEnum queryRefund(RefundRequestPo refundRequest, Payment payment,
            HashMap<String, String> header) {
        return RefundStatusEnum.INIT;
    }

}
