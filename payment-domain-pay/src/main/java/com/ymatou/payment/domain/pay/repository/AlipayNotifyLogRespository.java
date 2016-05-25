/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.pay.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ymatou.payment.infrastructure.db.mapper.AlipayNotifyLogMapper;
import com.ymatou.payment.infrastructure.db.model.AlipayNotifyLogPo;

/**
 * 第三方报文
 * 
 * @author qianmin 2016年5月20日 下午2:56:50
 *
 */
@Component
public class AlipayNotifyLogRespository {

    private static final Logger logger = LoggerFactory.getLogger(AlipayNotifyLogRespository.class);

    @Autowired
    private AlipayNotifyLogMapper alipayNotifyLogMapper;

    @Transactional
    public void saveAlipaynoitfylog(String paymentId, String respMsg) {
        AlipayNotifyLogPo alipaynotifylogPo = new AlipayNotifyLogPo();
        alipaynotifylogPo.setBizNo(paymentId);
        alipaynotifylogPo.setsParameters(respMsg);

        alipayNotifyLogMapper.insertSelective(alipaynotifylogPo);
    }
}
