/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.acquirerefund;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.constants.AliPayConsts;
import com.ymatou.payment.domain.channel.service.AcquireRefundService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.infrastructure.db.mapper.RefundMiscRequestLogMapper;
import com.ymatou.payment.infrastructure.db.mapper.RefundRequestMapper;
import com.ymatou.payment.infrastructure.db.model.RefundMiscRequestLogWithBLOBs;
import com.ymatou.payment.infrastructure.db.model.RefundRequestExample;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.model.AliPayRefundRequest;
import com.ymatou.payment.integration.model.AliPayRefundResponse;
import com.ymatou.payment.integration.service.alipay.AliPayRefundService;

/**
 * 通知支付宝退款
 * 
 * @author qianmin 2016年6月6日 上午11:40:16
 *
 */
@Component
public class AliPayRefundServiceImpl implements AcquireRefundService {

    private static final Logger logger = LoggerFactory.getLogger(AliPayRefundServiceImpl.class);

    @Autowired
    private InstitutionConfigManager configManager;

    @Autowired
    private RefundRequestMapper refundRequestMapper;

    @Autowired
    private SignatureService signatureService;

    @Autowired
    private AliPayRefundService aliPayRefundService;

    @Autowired
    private RefundMiscRequestLogMapper refundMiscRequestLogMapper;

    @Autowired
    private IntegrationConfig integrationConfig;

    @Override
    public void notifyRefund(RefundRequestPo refundRequest, Payment payment, HashMap<String, String> header) {

        InstitutionConfig config = configManager.getConfig(PayTypeEnum.parse(refundRequest.getPayType()));
        Date requestTime = new Date();

        AliPayRefundRequest aliPayRefundRequest = generateRequest(refundRequest, payment, config, null);
        try {
            // submit third party refund
            AliPayRefundResponse response = aliPayRefundService.doService(aliPayRefundRequest, header);

            // save RefundMiscRequestLog
            RefundMiscRequestLogWithBLOBs requestLog = new RefundMiscRequestLogWithBLOBs();
            requestLog.setCorrelateId(refundRequest.getRefundBatchNo());
            requestLog.setMethod("AliRefund");
            requestLog.setRequestData(aliPayRefundRequest.getRequestData());
            requestLog.setResponseData(response.getOriginalResponse());
            requestLog.setRefundBatchNo(refundRequest.getRefundBatchNo());
            requestLog.setRequestTime(requestTime);
            requestLog.setResponseTime(new Date());
            refundMiscRequestLogMapper.insertSelective(requestLog);

            // update RefundRequest
            RefundStatusEnum refundStatus =
                    isSuccess(response) ? RefundStatusEnum.COMMIT : RefundStatusEnum.REFUND_FAILED;
            updateRefundRequestStatus(refundRequest, refundStatus);

        } catch (Exception e) {
            logger.error("call AliPay Refund fail. RefundBatchNo: " + refundRequest.getRefundBatchNo(), e);

            RefundMiscRequestLogWithBLOBs requestLog = new RefundMiscRequestLogWithBLOBs();
            requestLog.setCorrelateId(refundRequest.getPaymentId());
            requestLog.setMethod("AliRefund");
            requestLog.setRequestData(aliPayRefundRequest.getRequestData());
            requestLog.setExceptionDetail(e.toString());
            requestLog.setRequestTime(requestTime);
            requestLog.setResponseTime(new Date());
            refundMiscRequestLogMapper.insertSelective(requestLog);

            updateRefundRequestStatus(refundRequest, RefundStatusEnum.REFUND_FAILED);
        }
    }

    private boolean isSuccess(AliPayRefundResponse response) {
        return AliPayConsts.REFUND_SYNC_STATU_P.equals(response.getIsSuccess())
                || AliPayConsts.REFUND_SYNC_STATU_T.equals(response.getIsSuccess());
    }

    private AliPayRefundRequest generateRequest(RefundRequestPo refundRequest, Payment payment,
            InstitutionConfig config, HashMap<String, String> header) {
        String url = new StringBuilder().append(integrationConfig.getYmtPaymentBaseUrl(header))
                .append("/RefundNotify/").append(payment.getPayType()).toString();
        AliPayRefundRequest request = new AliPayRefundRequest();
        request.setPartner(config.getMerchantId());
        request.setNotifyUrl(url);
        request.setSignType(config.getSignType());
        request.setDbackNotifyUrl(url);
        request.setBatchNo(refundRequest.getRefundBatchNo());
        request.setRefundDate(new Date());
        request.setBatchNum("1");
        String detailData = new StringBuilder().append(refundRequest.getInstPaymentId()).append("^")
                .append(refundRequest.getRefundAmount().setScale(2, BigDecimal.ROUND_HALF_UP)).append("^") // TODO
                .append(AliPayConsts.REFUND_REASON)
                .toString();
        request.setDetailData(detailData);
        request.setUseFreezeAmount("N");

        String sign = signatureService.signMessage(request.mapForSign(), config, header);
        request.setSign(sign);

        return request;
    }

    private void updateRefundRequestStatus(RefundRequestPo refundRequest, RefundStatusEnum refundStatus) {
        RefundRequestPo record = new RefundRequestPo();
        record.setRefundTime(new Date());
        record.setRefundStatus(refundStatus.getCode());
        RefundRequestExample example = new RefundRequestExample();
        example.createCriteria().andRefundBatchNoEqualTo(refundRequest.getRefundBatchNo());

        refundRequestMapper.updateByExampleSelective(record, example);
    }

}
