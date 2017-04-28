package com.ymatou.payment.domain.channel.service.refundquery;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.service.RefundQueryService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.infrastructure.Money;
import com.ymatou.payment.infrastructure.db.mapper.RefundMiscRequestLogMapper;
import com.ymatou.payment.infrastructure.db.model.RefundMiscRequestLogWithBLOBs;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.model.ApplePayRefundQueryRequest;
import com.ymatou.payment.integration.model.ApplePayTradeQueryResponse;
import com.ymatou.payment.integration.service.applepay.ApplePayTradeQueryService;
import com.ymatou.payment.integration.service.applepay.common.ApplePayConstants;
import com.ymatou.payment.integration.service.applepay.common.ApplePayMessageUtil;
import com.ymatou.payment.integration.service.applepay.common.ApplePaySignatureUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by zhangxiaoming on 2017/4/28.
 */
public class ApplePayRefundQueryServiceImpl implements RefundQueryService {
    private static final Logger logger = LoggerFactory.getLogger(ApplePayRefundQueryServiceImpl.class);
    @Resource
    private InstitutionConfigManager configManager;

    @Resource
    private ApplePayTradeQueryService applePayTradeQueryService;

    @Resource
    private IntegrationConfig integrationConfig;

    @Resource
    private SignatureService singatureService;

    @Resource
    private RefundMiscRequestLogMapper refundMiscRequestLogMapper;

    @Override
    public RefundStatusEnum queryRefund(RefundRequestPo refundRequest, Payment payment, HashMap<String, String> header) {
        logger.info("ApplePay refund query begin. PaymentId:{}, RefundNo:{}", refundRequest.getPaymentId(),
                refundRequest.getRefundBatchNo());
        InstitutionConfig config = configManager.getConfig(payment.getPayType());
        Date requestTime = new Date();
        RefundStatusEnum refundStatus;
        ApplePayRefundQueryRequest request = generateRequest(refundRequest, payment, config, header); // 组装退款查询申请
        try {
            ApplePayTradeQueryResponse response = applePayTradeQueryService.doPost(request, header);// 提交支付宝退款查询
            saveRefundMiscRequestLog(refundRequest, requestTime, request, response, null);// 保存退款查询结果
            refundStatus = calcRefundStatus(refundRequest, payment, response, config);// 计算退款状态
        } catch (Exception e) {
            logger.warn("ApplePay refund query error.RefundNo:{}, ex:{}", refundRequest.getRefundBatchNo(), e);

            saveRefundMiscRequestLog(refundRequest, requestTime, request, null, e);
            refundStatus = RefundStatusEnum.REFUND_FAILED;
        }

        logger.info("ApplePay refund query end. RefundStatus:{}", refundStatus);
        return refundStatus;
    }

    private ApplePayRefundQueryRequest generateRequest(RefundRequestPo refundRequest, Payment payment,
                                                       InstitutionConfig config, HashMap<String, String> header) {
        String txnTime = new DateTime().toString(ApplePayConstants.time_format);
        ApplePayRefundQueryRequest request = new ApplePayRefundQueryRequest();
        request.setOrderId(refundRequest.getRefundBatchNo());
        request.setTxnTime(txnTime);
        request.setMerId(config.getMerchantId());
        request.setMerId(config.getCertId());

        String privateKey = config.getInstYmtPrivateKey();
        if (integrationConfig.isMock(header)) {
            privateKey = singatureService.getMockPrivateKey();
        }

        String sign = ApplePaySignatureUtil.sign(request.genMap(), privateKey);
        request.setSignature(sign);
        return request;
    }

    private RefundStatusEnum calcRefundStatus(RefundRequestPo refundRequest, Payment payment,
                                              ApplePayTradeQueryResponse response, InstitutionConfig config) {
        RefundStatusEnum refundStatus = RefundStatusEnum.withCode(refundRequest.getRefundStatus().intValue());

        if (!StringUtils.equals(ApplePayConstants.response_success_code, response.getRespCode())) {
            return refundStatus; //查询失败返回原来的状态
        }
        boolean codeFlag = StringUtils.equals(ApplePayConstants.response_success_code, response.getOrigRespCode());
        boolean orderIdFlag = StringUtils.equals(refundRequest.getRefundBatchNo(), response.getOrderId());
        Money money = new Money(refundRequest.getRefundAmount());
        boolean refundAmountFlag = StringUtils.equals(String.valueOf(money.getCent()), response.getTxnAmt());
        boolean currencyCodeFlag = StringUtils.equals(ApplePayConstants.currency_code, response.getCurrencyCode());
        boolean merchantIdFlag = StringUtils.equals(config.getMerchantId(), response.getMerId());
        if (codeFlag && orderIdFlag && refundAmountFlag && currencyCodeFlag && merchantIdFlag) {
            refundStatus = RefundStatusEnum.THIRDPART_REFUND_SUCCESS;
        } else {
            logger.info("ApplePayRefundQueryServiceImpl,RefundBatchNo:{},codeFlag={},orderIdFlag={},refundAmountFlag={},currencyCodeFlag={},merchantIdFlag={}",
                    refundRequest.getRefundBatchNo(), codeFlag, orderIdFlag, refundAmountFlag, currencyCodeFlag, merchantIdFlag);
        }
        return refundStatus;
    }

    private void saveRefundMiscRequestLog(RefundRequestPo refundRequest, Date requestTime,
                                          ApplePayRefundQueryRequest queryRefundRequest, ApplePayTradeQueryResponse response, Exception e) {

        RefundMiscRequestLogWithBLOBs requestLog = new RefundMiscRequestLogWithBLOBs();
        requestLog.setCorrelateId(String.valueOf(refundRequest.getRefundId()));
        requestLog.setMethod("ApplePayRefundQuery");
        String requestMessage = ApplePayMessageUtil.genRequestMessage(queryRefundRequest.genMap());
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
}
