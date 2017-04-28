package com.ymatou.payment.domain.refund.service.notify;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.service.PayService;
import com.ymatou.payment.domain.refund.model.ApplePayRefundNotifyRequest;
import com.ymatou.payment.domain.refund.repository.RefundPository;
import com.ymatou.payment.domain.refund.service.RefundJobService;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.facade.model.RefundNotifyRequest;
import com.ymatou.payment.infrastructure.Money;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.service.applepay.common.ApplePayConstants;
import com.ymatou.payment.integration.service.applepay.common.ApplePaySignatureUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxiaoming on 2017/4/27.
 */
@Service("applePayRefundNotifyService")
public class ApplePayRefundNotifyServiceImpl implements RefundNotifyService {
    @Resource
    private InstitutionConfigManager instConfigManager;

    @Resource
    private RefundPository refundPository;

    @Resource
    private RefundJobService refundJobService;

    @Resource
    private PayService payService;

    @Resource
    private SignatureService signatureService;

    @Resource
    private IntegrationConfig integrationConfig;

    @Override
    public void processRefundCallback(RefundNotifyRequest request) {
        ApplePayRefundNotifyRequest applePayRefundNotifyRequest = new ApplePayRefundNotifyRequest(request);
        Map<String, String> signMap = applePayRefundNotifyRequest.getRawMap();
        InstitutionConfig config = instConfigManager.getConfig(PayTypeEnum.parse(applePayRefundNotifyRequest.getPayType()));
        HashMap<String, String> header = applePayRefundNotifyRequest.getMockHeader();

        //验签
        String pubkey = config.getInstPublicKey();
        if (integrationConfig.isMock(header)) {
            pubkey = this.signatureService.getMockPublicKey();
        }
        boolean signResult = ApplePaySignatureUtil.validate(signMap, pubkey);
        if (!signResult) {
            throw new BizException("signdata is invalid");
        }

        if (!StringUtils.equals(config.getMerchantId(), applePayRefundNotifyRequest.getMerId())) {
            throw new BizException("商户号不匹配！");
        }

        String refundBatchNo = applePayRefundNotifyRequest.getOrderId();
        RefundRequestPo refundRequest = refundPository.getRefundRequestByRefundBatchNo(refundBatchNo);
        Payment payment = payService.getPaymentByPaymentId(refundRequest.getPaymentId());

        if (!StringUtils.equals(refundRequest.getRefundBatchNo(), applePayRefundNotifyRequest.getOrderId())) {
            throw new BizException("单号不匹配！");
        }

        Money money = new Money(refundRequest.getRefundAmount());
        if (!StringUtils.equals(String.valueOf(money.getCent()), applePayRefundNotifyRequest.getTxnAmt())) {
            throw new BizException("金额不匹配！");
        }

        if (!StringUtils.equals(ApplePayConstants.currency_code, applePayRefundNotifyRequest.getCurrencyCode())) {
            throw new BizException("币种不匹配！");
        }

        if (RefundStatusEnum.COMPLETE_SUCCESS.getCode() != refundRequest.getRefundStatus().intValue()
                && RefundStatusEnum.COMPLETE_FAILED.getCode() != refundRequest.getRefundStatus().intValue()) {

            RefundStatusEnum refundStatus;
            if (StringUtils.equals(applePayRefundNotifyRequest.getRespCode(), ApplePayConstants.response_success_code) ||
                    StringUtils.equals(applePayRefundNotifyRequest.getRespCode(), ApplePayConstants.response_success_defect_code)) {
                refundStatus = RefundStatusEnum.THIRDPART_REFUND_SUCCESS;
            } else {
                refundStatus = RefundStatusEnum.REFUND_FAILED;
            }

            refundJobService.updateRefundRequestAndPayment(refundRequest, payment, refundStatus);// 更新退款状态
            if (RefundStatusEnum.THIRDPART_REFUND_SUCCESS.equals(refundStatus)) {

                if (refundJobService.callbackTradingSystem(refundRequest, payment, true, header)) {// 通知交易
                    refundJobService.updateRefundRequestToCompletedSuccess(refundRequest);
                }
            }
        }
    }
}
