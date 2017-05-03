package com.ymatou.payment.domain.channel.service.acquirerefund;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.service.AcquireRefundService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.infrastructure.Money;
import com.ymatou.payment.infrastructure.db.mapper.RefundMiscRequestLogMapper;
import com.ymatou.payment.infrastructure.db.mapper.RefundRequestMapper;
import com.ymatou.payment.infrastructure.db.model.RefundMiscRequestLogWithBLOBs;
import com.ymatou.payment.infrastructure.db.model.RefundRequestExample;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.model.ApplePayRefundRequest;
import com.ymatou.payment.integration.model.ApplePayRefundResponse;
import com.ymatou.payment.integration.service.applepay.ApplePayRefundService;
import com.ymatou.payment.integration.service.applepay.common.ApplePayConstants;
import com.ymatou.payment.integration.service.applepay.common.ApplePayMessageUtil;
import com.ymatou.payment.integration.service.applepay.common.ApplePaySignatureUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by zhangxiaoming on 2017/4/27.
 */
@Service
public class ApplePayRefundServiceImpl implements AcquireRefundService {
    private static final Logger logger = LoggerFactory.getLogger(ApplePayRefundServiceImpl.class);

    @Resource
    private InstitutionConfigManager configManager;

    @Resource
    private RefundRequestMapper refundRequestMapper;

    @Resource
    private RefundMiscRequestLogMapper refundMiscRequestLogMapper;

    @Resource
    private IntegrationConfig integrationConfig;

    @Resource
    private ApplePayRefundService applePayRefundService;


    @Resource
    private SignatureService singatureService;

    @Override
    public RefundStatusEnum notifyRefund(RefundRequestPo refundRequest, Payment payment, HashMap<String, String> header) {
        InstitutionConfig config = configManager.getConfig(PayTypeEnum.parse(refundRequest.getPayType()));
        Date requestTime = new Date();

        ApplePayRefundRequest applePayRefundRequest = generateRequest(refundRequest, payment, config, header);

        try {
            ApplePayRefundResponse response = applePayRefundService.doService(applePayRefundRequest, header);// 提交退款申请
            saveRefundMiscRequestLog(refundRequest, requestTime, applePayRefundRequest, response, null);// 保存退款应答
            RefundStatusEnum refundStatus = generateRefundStatus(response);
            updateRefundRequestStatus(refundRequest, refundStatus);// 更新退款状态

            return refundStatus;
        } catch (Exception e) {
            logger.error("call ApplePay Refund fail. RefundBatchNo: " + refundRequest.getRefundBatchNo(), e);

            saveRefundMiscRequestLog(refundRequest, requestTime, applePayRefundRequest, null, e);
            updateRefundRequestStatus(refundRequest, RefundStatusEnum.REFUND_FAILED);

            return RefundStatusEnum.REFUND_FAILED;
        }
    }

    private ApplePayRefundRequest generateRequest(RefundRequestPo refundRequest, Payment payment,
                                                  InstitutionConfig config, HashMap<String, String> header) {
        String url = new StringBuilder().append(integrationConfig.getYmtPaymentBaseUrl(header))
                .append("/RefundNotify/").append(payment.getPayType()).toString();
        String txnTime = new DateTime(refundRequest.getCreatedTime()).toString(ApplePayConstants.time_format);
        ApplePayRefundRequest applePayRefundRequest = new ApplePayRefundRequest();
        applePayRefundRequest.setMerId(config.getMerchantId());
        applePayRefundRequest.setBackUrl(url);
        applePayRefundRequest.setOrderId(refundRequest.getRefundBatchNo());
        applePayRefundRequest.setOrigQryId(refundRequest.getInstPaymentId());
        applePayRefundRequest.setTxnTime(txnTime);
        Money money = new Money(refundRequest.getRefundAmount());
        applePayRefundRequest.setTxnAmt(String.valueOf(money.getCent()));

        String privateKey = config.getInstYmtPrivateKey();
        if (integrationConfig.isMock(header)) {
            privateKey = singatureService.getMockPrivateKey();
        }

        String sign = ApplePaySignatureUtil.sign(applePayRefundRequest.genMap(), privateKey);
        applePayRefundRequest.setSignature(sign);

        return applePayRefundRequest;
    }

    private RefundStatusEnum generateRefundStatus(ApplePayRefundResponse response) {
        if (ApplePayConstants.response_success_code.equals(response.getRespCode())) {
            return RefundStatusEnum.COMMIT;
        } else {
            return RefundStatusEnum.REFUND_FAILED;
        }
    }

    private void saveRefundMiscRequestLog(RefundRequestPo refundRequest, Date requestTime,
                                          ApplePayRefundRequest applePayRefundRequest, ApplePayRefundResponse response, Exception e) {
        RefundMiscRequestLogWithBLOBs requestLog = new RefundMiscRequestLogWithBLOBs();
        requestLog.setCorrelateId(String.valueOf(refundRequest.getRefundId()));
        requestLog.setMethod("ApplePayRefund");
        String requestMessage = ApplePayMessageUtil.genRequestMessage(applePayRefundRequest.genMap());
        requestLog.setRequestData(requestMessage);
        requestLog.setRequestTime(requestTime);
        requestLog.setResponseTime(new Date());
        if (e != null) {
            requestLog.setIsException(true);
            requestLog.setExceptionDetail(e.toString());
        }
        if (response != null) {
            String responseMessage = ApplePayMessageUtil.genRequestMessage(response.getOriginMap());
            requestLog.setResponseData(responseMessage);
        }
        requestLog.setRefundBatchNo(refundRequest.getRefundBatchNo());
        refundMiscRequestLogMapper.insertSelective(requestLog);
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
