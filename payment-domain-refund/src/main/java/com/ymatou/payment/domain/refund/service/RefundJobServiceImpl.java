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
import com.ymatou.payment.domain.refund.repository.RefundPository;
import com.ymatou.payment.facade.constants.AccountOperateTypeEnum;
import com.ymatou.payment.facade.constants.AccountTypeEnum;
import com.ymatou.payment.facade.constants.AccountingStatusEnum;
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

    private static final int REFUND_SUCCESS_OPT_TYPE = 10;

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
    public boolean isContinueExecute(RefundRequestPo refundRequest) {
        // refundRequest=null;approveStatus=0;refundStatus=4,5,6,-2;softDelete;不再执行退款
        boolean isContinue = false;
        if (refundRequest == null) {
            logger.info("RefundRequest not exist.");
        } else if (refundRequest.getApproveStatus().equals(ApproveStatusEnum.NOT_APPROVED.getCode())
                || refundRequest.getSoftDeleteFlag()) {
            logger.info("RefundRequest can not be excuted. ApproveStatus:{}, SoftDeleteFlag:{}",
                    refundRequest.getApproveStatus(), refundRequest.getSoftDeleteFlag());
        } else if (refundRequest.getRefundStatus().equals(RefundStatusEnum.COMPLETE_FAILED.getCode())
                || refundRequest.getRefundStatus().compareTo((RefundStatusEnum.COMPLETE_SUCCESS.getCode())) >= 0) {
            logger.info("RefundRequest completed. RefundStatus: {}", refundRequest.getRefundStatus());
        } else {
            isContinue = true;
        }

        return isContinue;
    }

    @Override
    public void updateRetryCount(Integer refundId) {
        refundPository.updateRetryCount(refundId);
    }

    @Override
    public RefundRequestPo getRefundRequestByRefundId(Integer refundId) {
        RefundRequestPo refundRequest = refundPository.getRefundRequestByRefundId(refundId);
        logger.info("the refund will be excuted. {}", JSONObject.toJSONString(refundRequest));
        return refundRequest;
    }

    @Override
    public RefundStatusEnum submitRefund(RefundRequestPo refundRequest, Payment payment,
            HashMap<String, String> header) {
        String refundBatchNo = StringUtils.isBlank(refundRequest.getRefundBatchNo())
                ? refundPository.generateRefundBatchNo() : refundRequest.getRefundBatchNo();
        refundRequest.setRefundBatchNo(refundBatchNo);
        refundPository.updateRefundBatchNoByRefundId(refundBatchNo, refundRequest.getRefundId()); // 提交第三方退款前，添加RefundBatchNo

        PayTypeEnum payType = payment.getPayType();
        AcquireRefundService refundService = refundServiceFactory.getInstanceByPayType(payType);
        return refundService.notifyRefund(refundRequest, payment, header);
    }

    @Override
    public RefundStatusEnum queryRefund(RefundRequestPo refundRequest, Payment payment,
            HashMap<String, String> header) {
        RefundQueryService refundQueryService = refundQueryServiceFactory.getInstanceByPayType(payment.getPayType());
        RefundStatusEnum refundStatus = refundQueryService.queryRefund(refundRequest, payment, header);

        return refundStatus;
    }

    @Override
    public AccountingStatusEnum dedcutBalance(Payment payment, BussinessOrder bussinessOrder,
            RefundRequestPo refundRequest, HashMap<String, String> header) {
        AccountingResponse accountingResponse = dedcutBalanceOnce(payment, bussinessOrder, refundRequest, header);
        if (AccountingStatusEnum.UNKNOW.equals(accountingResponse.getAccountingStatus())) { // 发送异常重试
            accountingResponse = dedcutBalanceOnce(payment, bussinessOrder, refundRequest, header);
        }

        if (AccountingResponse.BALANCE_LIMIT.equals(accountingResponse.getStatusCode())) {
            logger.error("dedcut user balance failed, stop refund. RefundId: {}", refundRequest.getRefundId());
            refundPository.softDeleteRefundRequest(refundRequest.getRefundId()); // 逻辑删除refundRequest
        }

        return accountingResponse.getAccountingStatus();
    }

    private AccountingResponse dedcutBalanceOnce(Payment payment, BussinessOrder bussinessOrder,
            RefundRequestPo refundRequest, HashMap<String, String> header) {
        AccountingRequest request = generateRequest(refundRequest, payment, bussinessOrder);
        AccountingResponse response = accountService.accounting(request, header);
        saveAccoutingLog(bussinessOrder, refundRequest, response);
        logger.info("accounting result. StatusCode:{}, Message:{}", response.getStatusCode(),
                response.getMessage());

        return response;
    }

    private void saveAccoutingLog(BussinessOrder bussinessOrder, RefundRequestPo refundRequest,
            AccountingResponse response) {
        AccountingLogPo log = new AccountingLogPo();
        log.setCreatedTime(new Date());
        log.setAccoutingAmt(refundRequest.getRefundAmount());
        log.setAccountingType("Refund");
        log.setUserId((long) bussinessOrder.getUserId().intValue());
        log.setBizNo(String.valueOf(refundRequest.getRefundId()));
        log.setMemo("快速退款");
        log.setRespCode(response.getStatusCode());
        log.setRespMsg(response.getMessage());
        log.setStatus(response.isAccoutingSuccess()); // 成功为1，失败为0
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

        refundRequestPo.setRefundTime(new Date());
        paymentPo.setPaymentId(payment.getPaymentId());
        if (RefundStatusEnum.THIRDPART_REFUND_SUCCESS.equals(refundStatus)) {
            refundRequestPo.setRefundBatchNo(refundRequest.getRefundBatchNo());
            refundRequestPo.setRefundStatus(refundStatus.getCode());
            BigDecimal refundAmt = payment.getCompletedRefundAmt() == null ? refundRequest.getRefundAmount()
                    : refundRequest.getRefundAmount().add(payment.getCompletedRefundAmt());
            paymentPo.setPayStatus(PayStatusEnum.Refunded.getIndex());
            paymentPo.setCompletedRefundAmt(refundAmt); // 更新退款完成金额
        } else if (RefundStatusEnum.COMPLETE_FAILED.equals(refundStatus)) {
            refundRequestPo.setRefundBatchNo(refundRequest.getRefundBatchNo());
            refundRequestPo.setRefundStatus(refundStatus.getCode());
            BigDecimal refundAmt = payment.getRefundAmt().subtract(refundRequest.getRefundAmount());
            paymentPo.setRefundAmt(refundAmt); // 更新退款申请金额
        } else {
            refundRequestPo.setRefundBatchNo(refundRequest.getRefundBatchNo());
            refundRequestPo.setRefundStatus(refundStatus.getCode());
        }

        refundPository.updateRefundRequestAndPayment(refundRequestPo, paymentPo);
    }

    @Override
    public boolean callbackTradingSystem(RefundRequestPo refundRequest, Payment payment,
            HashMap<String, String> header) {
        RefundCallbackRequest request = new RefundCallbackRequest();
        request.setActualRefundAmount(refundRequest.getRefundAmount());
        request.setAuditor(refundRequest.getApprovedUser());
        request.setOptType(REFUND_SUCCESS_OPT_TYPE);
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

        try {
            return refundCallbackService.doService(request, isNewSystem, header);
        } catch (IOException e) {
            logger.error("refund notify to trade service failed on {}", refundRequest.getRefundBatchNo());
            return false;
        }
    }

    @Override
    public void updateRefundRequestToCompletedSuccess(RefundRequestPo refundRequest) {
        RefundRequestPo refundRequestPo = new RefundRequestPo();
        refundRequestPo.setRefundTime(new Date());
        refundRequestPo.setRefundBatchNo(refundRequest.getRefundBatchNo());
        refundRequestPo.setRefundStatus(RefundStatusEnum.COMPLETE_SUCCESS.getCode());

        refundPository.updateRefundRequest(refundRequestPo);
    }
}
