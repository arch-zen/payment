/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.refund.DomainConfig;
import com.ymatou.payment.facade.model.TradeRefundDetail;

/**
 * 
 * @author qianmin 2016年5月13日 下午2:59:40
 * 
 */
@Component
public class CheckRefundableServiceImpl implements CheckRefundableService {

    private static final Logger logger = LoggerFactory.getLogger(CheckRefundableServiceImpl.class);

    @Autowired
    private SubmitRefundService submitRefundService;

    @Autowired
    private DomainConfig domainConfig;

    @Override
    public List<TradeRefundDetail> generateRefundableTrades(List<String> tradeNos) {
        String[] notSupportRefundPayTypes = domainConfig.getNotSupportRefundPayType().split(","); // 不支持退款的
        List<TradeRefundDetail> refundableTrades = new ArrayList<>();

        logger.info("to be checked trade size {}", tradeNos.size());
        List<TradeRefundDetail> tradeRefundDetails = submitRefundService.generateTradeRefundDetailList(tradeNos);
        for (TradeRefundDetail tradeRefundDetail : tradeRefundDetails) { // 筛选出可退款的
            if (tradeRefundDetail.isRefundable()
                    && !ArrayUtils.contains(notSupportRefundPayTypes, tradeRefundDetail.getPayType())) {
                refundableTrades.add(tradeRefundDetail);
            }
        }
        logger.info("refundable trade size: {}", refundableTrades.size());

        return refundableTrades;
    }
}
