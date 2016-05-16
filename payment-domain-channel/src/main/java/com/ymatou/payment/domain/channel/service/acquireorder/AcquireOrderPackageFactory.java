/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.acquireorder;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.service.AcquireOrderService;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;

/**
 * 收单报文工厂
 * 
 * @author wangxudong 2016年5月11日 下午6:10:33
 *
 */
@Component
public class AcquireOrderPackageFactory {

    @Resource
    private AliPayPcAcquireOrderServiceImpl aliPayPcAcquireOrderServiceImpl;

    @Resource
    private AliPayAppAcquireOrderServiceImpl aliPayAppAcquireOrderServiceImpl;

    @Resource
    private WeiXinAppAcquireOrderServiceImpl weiXinAppAcquireOrderServiceImpl;

    @Resource
    private WeiXinJSAPIAcquireOrderServiceImpl weiXinJSAPIAcquireOrderServiceImpl;


    /**
     * 获取到收单
     * 
     * @param payType
     * @return
     */
    public AcquireOrderService getInstance(String payType) {
        if (payType == "10")
            return aliPayPcAcquireOrderServiceImpl;
        else if (payType == "13")
            return aliPayAppAcquireOrderServiceImpl;
        else if (payType == "14")
            return weiXinJSAPIAcquireOrderServiceImpl;
        else if (payType == "15")
            return weiXinAppAcquireOrderServiceImpl;
        else
            throw new BizException(ErrorCode.INVALID_PAYTYPE, payType);
    }
}
