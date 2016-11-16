package com.ymatou.payment.facade;

import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireOrderResp;
import com.ymatou.payment.facade.model.ExecutePayNotifyReq;
import com.ymatou.payment.facade.model.ExecutePayNotifyResp;
import com.ymatou.payment.facade.model.SyncCmbPublicKeyReq;
import com.ymatou.payment.facade.model.SyncCmbPublicKeyResp;

/**
 * 支付接口
 * 
 * @author wangxudong
 *
 */
public interface PaymentFacade {

    /**
     * 支付收单
     * 
     * @param req
     * @return
     */
    AcquireOrderResp acquireOrder(AcquireOrderReq req);

    /**
     * 执行支付通知
     * 将已经支付的订单执行充值和通知调用系统
     * 
     * @param req
     * @return
     */
    ExecutePayNotifyResp executePayNotify(ExecutePayNotifyReq req);

    /**
     * 同步招行一网通公钥
     * 
     * @param req
     * @return
     */
    SyncCmbPublicKeyResp syncCmbPublicKey(SyncCmbPublicKeyReq req);
}
