/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.ymatou.payment.domain.channel.service.AcquireRefundService;
import com.ymatou.payment.domain.channel.service.RefundQueryService;
import com.ymatou.payment.domain.channel.service.acquirerefund.RefundServiceFactory;
import com.ymatou.payment.domain.channel.service.refundquery.RefundQueryServiceFactory;
import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.refund.constants.RefundConstants;
import com.ymatou.payment.domain.refund.repository.RefundPository;
import com.ymatou.payment.facade.constants.AccountOperateTypeEnum;
import com.ymatou.payment.facade.constants.AccountTypeEnum;
import com.ymatou.payment.facade.constants.ApproveStatusEnum;
import com.ymatou.payment.facade.constants.CurrencyTypeEnum;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.infrastructure.db.mapper.AccountingLogMapper;
import com.ymatou.payment.infrastructure.db.model.AccountingLogPo;
import com.ymatou.payment.infrastructure.db.model.PaymentPo;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;
import com.ymatou.payment.integration.model.AccountingItem;
import com.ymatou.payment.integration.model.AccountingRequest;
import com.ymatou.payment.integration.model.AccountingResponse;
import com.ymatou.payment.integration.model.RefundCallbackRequest;
import com.ymatou.payment.integration.service.ymatou.AccountService;
import com.ymatou.payment.integration.service.ymatou.RefundCallbackService;

/**
 * 
 * @author qianmin 2016年6月8日 上午10:48:21
 *
 */
@Component
public class RefundJobServiceImpl implements RefundJobService {

    private static final Logger logger = LoggerFactory.getLogger(RefundJobServiceImpl.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountingLogMapper accountingLogMapper;

    @Autowired
    private RefundPository refundPository;

    @Autowired
    private RefundServiceFactory refundServiceFactory;

    @Autowired
    private RefundQueryServiceFactory refundQueryServiceFactory;

    @Autowired
    private RefundCallbackService refundCallbackService;

    @Override
    public void updateRetryCount(Integer refundId) {
        refundPository.updateRetryCount(refundId);
    }

    @Override
    public RefundRequestPo getRefundRequestByRefundId(Integer refundId) {
        RefundRequestPo refundRequest = refundPository.getRefundRequestByRefundId(refundId);
        logger.info("the refund will be excuting. {}", JSONObject.toJSONString(refundRequest));
        return refundRequest;
    }

    @Override
    public void submitRefund(RefundRequestPo refundRequest, Payment payment, HashMap<String, String> header) {
        String refundBatchNo = StringUtils.isBlank(refundRequest.getRefundBatchNo())
                ? refundPository.generateRefundBatchNo() : refundRequest.getRefundBatchNo();
        refundRequest.setRefundBatchNo(refundBatchNo);
        refundPository.updateRefundBatchNoByRefundId(refundBatchNo, refundRequest.getRefundId()); // 提交第三方退款前，添加RefundBatchNo

        PayTypeEnum payType = payment.getPayType();
        AcquireRefundService refundService = refundServiceFactory.getInstanceByPayType(payType);
        refundService.notifyRefund(refundRequest, payment, header);
    }

    @Override
    public RefundStatusEnum queryRefund(RefundRequestPo refundRequest, Payment payment,
            HashMap<String, String> header) {
        RefundQueryService refundQueryService =
                refundQueryServiceFactory.getInstanceByPayType(payment.getPayType());
        RefundStatusEnum refundStatus = refundQueryService.queryRefund(refundRequest, payment, header);

        return refundStatus;
    }

    @Override
    public boolean dedcutBalance(Payment payment, BussinessOrder bussinessOrder, RefundRequestPo refundRequest,
            HashMap<String, String> header) {
        AccountingRequest request = generateRequest(refundRequest, payment, bussinessOrder);

        try {
            AccountingResponse response = accountService.accounting(request, header);
            if (isAccoutingSuccess(response)) {
                logger.info("accouting success. StatusCode:{}", response.getStatusCode());
                saveAccoutingLog(payment, bussinessOrder, refundRequest, response);
                return true;
            } else {
                logger.info("accounting failed. StatusCode:{}, Message:{}", response.getStatusCode(),
                        response.getMessage());
                saveAccoutingLog(payment, bussinessOrder, refundRequest, response);
                return false;
            }
        } catch (IOException e) {
            saveAccoutingLog(payment, bussinessOrder, refundRequest, e);
            logger.info("accouting error.", e); // 扣款异常只影响是否提交第三方扣款， 不影响返回给交易的应答
        }
        return false;
    }

    private boolean isAccoutingSuccess(AccountingResponse response) {
        return RefundConstants.ACCOUNTING_SUCCESS.equals(response.getStatusCode())
                || RefundConstants.ACCOUNTING_IDEMPOTENTE.equals(response.getStatusCode());
    }

    /*
     * 保存账务操作记录， 更新RefundRequest的AccoutingStatus
     */
    private void saveAccoutingLog(Payment payment, BussinessOrder bussinessOrder, RefundRequestPo refundRequest,
            IOException e) {
        AccountingLogPo log = new AccountingLogPo();
        log.setCreatedTime(new Date());
        log.setAccoutingAmt(refundRequest.getRefundAmount());
        log.setAccountingType("Refund");
        log.setStatus(0); // 成功为1，失败为0
        log.setUserId((long) bussinessOrder.getUserId().intValue());
        log.setBizNo(String.valueOf(refundRequest.getRefundId()));
        log.setRespCode("3"); // 系统异常
        log.setRespMsg(e.getMessage());
        log.setMemo("快速退款");
        accountingLogMapper.insertSelective(log);

        refundPository.updateRefundRequestAccoutingStatus(refundRequest.getRefundId(), log.getStatus());
    }

    /*
     * 保存账务操作记录， 更新RefundRequest的AccoutingStatus
     */
    private void saveAccoutingLog(Payment payment, BussinessOrder bussinessOrder, RefundRequestPo refundRequest,
            AccountingResponse response) {
        AccountingLogPo log = new AccountingLogPo();
        log.setCreatedTime(new Date());
        log.setAccoutingAmt(refundRequest.getRefundAmount());
        log.setAccountingType("Refund");
        log.setStatus(isAccoutingSuccess(response) ? 1 : 0); // 成功为1，失败为0
        log.setUserId((long) bussinessOrder.getUserId().intValue());
        log.setBizNo(String.valueOf(refundRequest.getRefundId()));
        log.setRespCode(response.getStatusCode());
        log.setRespMsg(response.getMessage());
        log.setMemo("快速退款");
        accountingLogMapper.insertSelective(log);

        refundPository.updateRefundRequestAccoutingStatus(refundRequest.getRefundId(), log.getStatus());
    }

    private AccountingRequest generateRequest(RefundRequestPo refundRequest, Payment payment,
            BussinessOrder bussinessOrder) {
        List<AccountingItem> itemList = new ArrayList<>();
        AccountingItem item = new AccountingItem();
        item.setUserId(bussinessOrder.getUserId());
        item.setCurrencyType(CurrencyTypeEnum.CNY.code());
        item.setAmount(refundRequest.getRefundAmount().toString());
        item.setAccountOperateType(AccountOperateTypeEnum.Fundout.code());
        item.setAccountType(AccountTypeEnum.RmbAccount.code());
        item.setAccountingDate(new Date());
        item.setBizCode("300017"); // 快速退款
        item.setBizNo(String.valueOf(refundRequest.getRefundId()));
        item.setOriginalNo(bussinessOrder.getOrderId());
        item.setMemo("快速退款");
        itemList.add(item);

        AccountingRequest request = new AccountingRequest();
        request.setAccountingItems(itemList);
        request.setAppId("payment.ymatou.com");
        return request;
    }

    public void updateRefundRequestAndPayment(RefundRequestPo refundRequest, Payment payment,
            RefundStatusEnum refundStatus) {
        RefundRequestPo refundRequestPo = new RefundRequestPo();
        PaymentPo paymentPo = new PaymentPo();
        paymentPo.setPaymentId(payment.getPaymentId());
        if (RefundStatusEnum.THIRDPART_REFUND_SUCCESS.equals(refundStatus)) {
            refundRequestPo.setRefundBatchNo(refundRequest.getRefundBatchNo());
            refundRequestPo.setRefundStatus(refundStatus.getCode());
            int retryCount = refundRequestPo.getRetryCount() == null ? 1 : refundRequestPo.getRetryCount() + 1;
            refundRequestPo.setRetryCount(retryCount);
            BigDecimal refundAmt = payment.getCompletedRefundAmt() == null ? refundRequest.getRefundAmount()
                    : refundRequest.getRefundAmount().add(payment.getCompletedRefundAmt());

            paymentPo.setPayStatus(PayStatusEnum.Refunded.getIndex());
            paymentPo.setCompletedRefundAmt(refundAmt); // 更新退款完成金额
        } else if (RefundStatusEnum.COMPLETE_FAILED.equals(refundStatus)) {
            refundRequestPo.setRefundBatchNo(refundRequest.getRefundBatchNo());
            refundRequestPo.setRefundStatus(refundStatus.getCode());
            int retryCount = refundRequestPo.getRetryCount() == null ? 1 : refundRequestPo.getRetryCount() + 1;
            refundRequestPo.setRetryCount(retryCount);

            BigDecimal refundAmt = payment.getRefundAmt().subtract(refundRequest.getRefundAmount());
            paymentPo.setRefundAmt(refundAmt); // 更新退款申请金额
        } else {
            refundRequestPo.setRefundBatchNo(refundRequest.getRefundBatchNo());
            refundRequestPo.setRefundStatus(refundStatus.getCode());
            int retryCount = refundRequestPo.getRetryCount() == null ? 1 : refundRequestPo.getRetryCount() + 1;
            refundRequestPo.setRetryCount(retryCount);
        }

        refundPository.updateRefundRequestAndPayment(refundRequestPo, paymentPo);
    }

    @Override
    public boolean callbackTradingSystem(RefundRequestPo refundRequest, Payment payment,
            HashMap<String, String> header) {
        RefundCallbackRequest request = new RefundCallbackRequest();
        request.setActualRefundAmount(refundRequest.getRefundAmount());
        request.setAuditor(refundRequest.getApprovedUser());
        request.setOptType(10);
        request.setOrderID(Long.valueOf(refundRequest.getOrderId()));
        request.setPassAuditTime(refundRequest.getRefundTime());
        request.setRequiredRefundAmount(refundRequest.getRefundAmount());
        request.setThirdPartyName(PayTypeEnum.getThirdPartyName(payment.getPayType()));
        request.setThirdPartyTradingNo(refundRequest.getInstPaymentId());
        request.setTradeNo(refundRequest.getTradeNo());
        request.setIsFastRefund(refundRequest.getApproveStatus().equals(ApproveStatusEnum.FAST_REFUND.getCode()));
        boolean isNewSystem = refundRequest.getTraceId().length() == 24; // Java版交易系统RequestNo长度
        if (isNewSystem) {
            request.setRequestNo(refundRequest.getTraceId());
        }

        boolean isSuccess = false;
        try {
            isSuccess = refundCallbackService.doService(request, isNewSystem, header);
        } catch (IOException e) {
            logger.error("refund notify to trade service failed on {}", refundRequest.getRefundBatchNo());
        }

        return isSuccess;
    }

    @Override
    public void updateRefundRequestToCompletedSuccess(RefundRequestPo refundRequest) {
        RefundRequestPo refundRequestPo = new RefundRequestPo();
        refundRequestPo.setRefundBatchNo(refundRequest.getRefundBatchNo());
        refundRequestPo.setRefundStatus(RefundStatusEnum.COMPLETE_SUCCESS.getCode());

        refundPository.updateRefundRequest(refundRequestPo);
    }
}
