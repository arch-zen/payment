/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.pay.repository;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.infrastructure.db.mapper.BussinessorderMapper;
import com.ymatou.payment.infrastructure.db.model.BussinessorderExample;
import com.ymatou.payment.infrastructure.db.model.BussinessorderPo;
import com.ymatou.payment.infrastructure.db.model.PaymentPo;

/**
 * 商户订单仓储类
 * 
 * @author wangxudong 2016年5月10日 下午5:06:39
 *
 */
@Component
public class BussinessOrderRepository {
    private static final Logger logger = LoggerFactory.getLogger(PaymentRepository.class);

    @Resource
    private BussinessorderMapper mapper;

    /**
     * 根据商户订单（交易号）获取到商户订单信息
     * 
     * @param orderId
     * @return
     */
    public BussinessOrder getByOrderId(String orderId) {
        BussinessorderExample example = new BussinessorderExample();
        example.createCriteria().andOrderidEqualTo(orderId);
        List<BussinessorderPo> opList = mapper.selectByExample(example);

        if (opList.size() == 0)
            return null;


        return BussinessOrder.convertFromPo(opList.get(0));
    }

    /**
     * 保存商户订单
     * 
     * @param po
     * @return
     */
    public int insert(BussinessorderPo po) {
        return mapper.insert(po);
    }
}
