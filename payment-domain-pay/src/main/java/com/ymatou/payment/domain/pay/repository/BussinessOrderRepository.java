/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.pay.repository;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.infrastructure.db.mapper.BussinessOrderMapper;
import com.ymatou.payment.infrastructure.db.model.BussinessOrderExample;
import com.ymatou.payment.infrastructure.db.model.BussinessOrderPo;

/**
 * 商户订单仓储类
 *
 * @author wangxudong 2016年5月10日 下午5:06:39
 *
 */
@Component
public class BussinessOrderRepository {
    private static final Logger logger = LoggerFactory.getLogger(BussinessOrderRepository.class);

    @Resource
    private BussinessOrderMapper mapper;

    /**
     * 根据商户订单（交易号）获取到商户订单信息
     *
     * @param orderId
     * @return
     */
    public BussinessOrder getByOrderId(String orderId) {
        BussinessOrderExample example = new BussinessOrderExample();
        example.createCriteria().andOrderIdEqualTo(orderId);
        List<BussinessOrderPo> opList = mapper.selectByExample(example);

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
    public int insert(BussinessOrderPo po) {
        return mapper.insertSelective(po);
    }

    /**
     * 更新商户订单状态
     * 
     * @param bussinessorderid
     * @param orderStatus
     */
    public void updateOrderStatus(String bussinessorderid, int orderStatus) {
        BussinessOrderPo bussinessOrderPo = new BussinessOrderPo();
        bussinessOrderPo.setOrderStatus(orderStatus);
        bussinessOrderPo.setBussinessOrderId(bussinessorderid);
        int updated = mapper.updateByPrimaryKeySelective(bussinessOrderPo);
        if (updated <= 0) {
            throw new BizException("Bussinessorder not exist");
        }
    }

    /**
     * 根据Id获取BussinessOrder
     * 
     * @param bussinessorderid
     * @return
     */
    public BussinessOrder getBussinessOrderById(String bussinessorderid) {
        BussinessOrderPo po = mapper.selectByPrimaryKey(bussinessorderid);
        return BussinessOrder.convertFromPo(po);
    }

    /**
     * 根据traceNo找到可以退款的订单
     * 
     * @param traceNo
     * @param orderStatus
     * @param valideDate
     * @return
     */
    public BussinessOrder getBussinessOrderCanRefund(String tradeNo, Integer orderStatus, Date validDate) {
        BussinessOrderExample example = new BussinessOrderExample();
        example.createCriteria().andOrderIdEqualTo(tradeNo)
                .andOrderStatusEqualTo(orderStatus)
                .andCreatedTimeGreaterThanOrEqualTo(validDate);
        List<BussinessOrderPo> pos = mapper.selectByExample(example);
        if (pos == null || pos.size() == 0) {
            return null;
        }
        return BussinessOrder.convertFromPo(pos.get(0));
    }
}
