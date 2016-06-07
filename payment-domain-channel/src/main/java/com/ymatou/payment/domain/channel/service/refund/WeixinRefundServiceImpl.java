/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.refund;

import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.constants.WeixinPayConstants;
import com.ymatou.payment.domain.channel.service.RefundService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.infrastructure.db.mapper.PaymentMapper;
import com.ymatou.payment.infrastructure.db.mapper.RefundMiscRequestLogMapper;
import com.ymatou.payment.infrastructure.db.mapper.RefundRequestMapper;
import com.ymatou.payment.infrastructure.db.model.PaymentPo;
import com.ymatou.payment.infrastructure.db.model.RefundMiscRequestLogWithBLOBs;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;
import com.ymatou.payment.integration.model.WxRefundRequest;
import com.ymatou.payment.integration.model.WxRefundResponse;
import com.ymatou.payment.integration.service.wxpay.WxRefundService;

/**
 * 通知微信支付退款
 * 
 * @author qianmin 2016年6月6日 上午11:40:36
 *
 */
@Component
public class WeixinRefundServiceImpl implements RefundService {

    public static final Logger logger = LoggerFactory.getLogger(WeixinRefundServiceImpl.class);

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private InstitutionConfigManager configManager;

    @Autowired
    private RefundRequestMapper refundRequestMapper;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private SignatureService signatureService;

    @Autowired
    private WxRefundService wxRefundService;

    @Autowired
    private RefundMiscRequestLogMapper refundMiscRequestLogMapper;

    @Override
    public void notifyRefund(String refundBatchNo, HashMap<String, String> header) {
        taskExecutor.execute(new Runnable() {

            @Override
            public void run() {

                RefundRequestPo refundRequest = refundRequestMapper.selectByPrimaryKey(refundBatchNo); // TODO
                PaymentPo payment = paymentMapper.selectByPrimaryKey(refundRequest.getPaymentId());
                InstitutionConfig config = configManager.getConfig(PayTypeEnum.parse(refundRequest.getPayType()));
                Date requestTime = new Date();

                WxRefundRequest wxRefundRequest = generateRequest(refundRequest, payment, config, null);
                try {
                    WxRefundResponse response = wxRefundService.doService(wxRefundRequest, header);

                    // save RefundMiscRequestLog
                    RefundMiscRequestLogWithBLOBs requestLog = new RefundMiscRequestLogWithBLOBs();
                    requestLog.setCorrelateId(refundRequest.getPaymentId());
                    requestLog.setMethod("AliRefund");
                    requestLog.setRequestData(wxRefundRequest.getRequestData());
                    requestLog.setResponseData(response.getOriginalResponse());
                    requestLog.setRefundBatchNo(refundBatchNo);
                    requestLog.setRequestTime(requestTime); // TODO
                    requestLog.setResponseTime(new Date()); // TODO
                    refundMiscRequestLogMapper.insertSelective(requestLog);

                    // update RefundRequest
                    RefundStatusEnum refundStatus =
                            isSuccess(response, config) ? RefundStatusEnum.COMMIT : RefundStatusEnum.REFUND_FAILED;
                    refundRequest.setRefundStatus(refundStatus.getCode());
                    refundRequestMapper.updateByPrimaryKeySelective(refundRequest);

                } catch (Exception e) {
                    logger.error("call WeiXin Refund fail", e);

                    RefundMiscRequestLogWithBLOBs requestLog = new RefundMiscRequestLogWithBLOBs();
                    requestLog.setCorrelateId(refundRequest.getPaymentId());
                    requestLog.setMethod("WeiXinRefund");
                    requestLog.setRequestData(wxRefundRequest.getRequestData());
                    requestLog.setExceptionDetail(e.toString());
                    requestLog.setRequestTime(requestTime); // TODO
                    requestLog.setResponseTime(new Date()); // TODO
                    refundMiscRequestLogMapper.insertSelective(requestLog);
                    // update refundRequest status? //TODO
                }
            }
        });
    }

    private boolean isSuccess(WxRefundResponse response, InstitutionConfig config) {
        if (!WeixinPayConstants.SUCCESS_FLAG.equals(response.getReturn_code())
                || !WeixinPayConstants.SUCCESS_FLAG.equals(response.getResult_code())
                || !config.getMerchantId().equalsIgnoreCase(response.getMch_id())) {
            return false;
        } else {
            return true;
        }
    }

    private WxRefundRequest generateRequest(RefundRequestPo refundRequest, PaymentPo payment,
            InstitutionConfig config, HashMap<String, String> header) {

        WxRefundRequest request = new WxRefundRequest();
        request.setAppid(config.getAppId());
        request.setMch_id(config.getMerchantId());
        request.setDevice_info("");
        request.setNonce_str("");
        request.setOut_trade_no(refundRequest.getPaymentId());
        // request.setTransaction_id("");
        request.setOut_refund_no(refundRequest.getRefundBatchNo());
        request.setTotal_fee((int) (payment.getActualPayPrice().setScale(2).doubleValue() * 100)); // TODO
        request.setRefund_fee((int) (refundRequest.getRefundAmount().setScale(2).doubleValue() * 100));
        request.setRefund_fee_type("CNY");
        request.setOp_user_id(config.getMerchantId());

        String sign = signatureService.signMessage(request.mapForSign(), config, header);
        request.setSign(sign);

        return request;
    }

}
