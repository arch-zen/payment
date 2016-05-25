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
import com.ymatou.payment.facade.constants.PayTypeEnum;

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
     * 获取到收单解析器
     * 
     * @param payType
     * @return
     */
    public AcquireOrderService getInstance(String payType) {
        switch (PayTypeEnum.parse(payType)) {
            case AliPayPc:
                return aliPayPcAcquireOrderServiceImpl;
            case AliPayApp:
                return aliPayAppAcquireOrderServiceImpl;
            case WeiXinJSAPI:
                return weiXinJSAPIAcquireOrderServiceImpl;
            case WeiXinApp:
                return weiXinAppAcquireOrderServiceImpl;
            default:
                throw new BizException(ErrorCode.INVALID_PAYTYPE, payType);

        }
    }
}
