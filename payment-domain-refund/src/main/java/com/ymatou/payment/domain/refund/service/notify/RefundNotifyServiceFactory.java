package com.ymatou.payment.domain.refund.service.notify;

import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.model.RefundNotifyRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zhangxiaoming on 2017/4/27.
 */
@Service
public class RefundNotifyServiceFactory {

    @Resource(name = "aliPayRefundNotifyService")
    private RefundNotifyService aliPayRefundNotifyService;

    @Resource(name = "applePayRefundNotifyService")
    private RefundNotifyService applePayRefundNotifyService;

    public RefundNotifyService createRefundNotifyService(RefundNotifyRequest request) {
        if (request == null)
            return null;
        if (PayTypeEnum.ApplePay.getCode().equals(request.getPayType())) {
            return applePayRefundNotifyService;
        }

        if (PayTypeEnum.AliPayApp.getCode().equals(request.getPayType())
                || PayTypeEnum.AliPayPc.getCode().equals(request.getPayType())
                || PayTypeEnum.AliPayWap.getCode().equals(request.getPayType())) {
            return aliPayRefundNotifyService;
        }
        return null;
    }
}
