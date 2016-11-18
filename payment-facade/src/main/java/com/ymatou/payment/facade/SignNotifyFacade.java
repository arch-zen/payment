/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade;

import com.ymatou.payment.facade.model.SignNotifyReq;
import com.ymatou.payment.facade.model.SignNotifyResp;

/**
 * 签约通知API
 * 
 * @author wangxudong 2016年11月18日 下午8:11:09
 *
 */
public interface SignNotifyFacade {

    public SignNotifyResp signNotify(SignNotifyReq req);
}
