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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.refund.constants.RefundConstants;
import com.ymatou.payment.facade.constants.AccountOperateTypeEnum;
import com.ymatou.payment.facade.constants.AccountTypeEnum;
import com.ymatou.payment.facade.constants.CurrencyTypeEnum;
import com.ymatou.payment.infrastructure.db.mapper.AccountingLogMapper;
import com.ymatou.payment.infrastructure.db.model.AccountingLogPo;
import com.ymatou.payment.integration.model.AccountingItem;
import com.ymatou.payment.integration.model.AccountingRequest;
import com.ymatou.payment.integration.model.AccountingResponse;
import com.ymatou.payment.integration.service.ymatou.AccountService;

/**
 * 
 * @author qianmin 2016年6月6日 下午7:10:26
 *
 */
@Component
public class AccountOperateServiceImpl implements AccountOperateService {

    private static final Logger logger = LoggerFactory.getLogger(AccountOperateServiceImpl.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountingLogMapper accountingLogMapper;

    @Override
    public boolean deductBalance(Payment payment, BussinessOrder bussinessOrder, HashMap<String, String> header) {
        AccountingRequest request = generateRequest(payment, bussinessOrder);

        try {
            AccountingResponse response = accountService.accounting(request, header);
            if (RefundConstants.ACCOUNTING_SUCCESS.equals(response.getStatusCode())
                    || RefundConstants.ACCOUNTING_IDEMPOTENTE.equals(response.getStatusCode())) {
                logger.info("accouting success. StatusCode:{}", response.getStatusCode());
                saveAccoutingLog(payment, bussinessOrder, response);
                return true;
            } else {
                logger.info("accounting failed. StatusCode:{}, Message:{}", response.getStatusCode(),
                        response.getMessage());
                saveAccoutingLog(payment, bussinessOrder, response);
                return false;
            }
        } catch (IOException e) {
            logger.info("accouting error.", e);
        }
        return false;
    }

    // TODO
    private void saveAccoutingLog(Payment payment, BussinessOrder bussinessOrder, AccountingResponse response) {
        AccountingLogPo log = new AccountingLogPo();
        log.setCreatedTime(new Date());
        log.setAccoutingAmt(new BigDecimal(""));
        log.setAccountingType("");
        log.setStatus(1);
        log.setAccountId(String.valueOf(bussinessOrder.getUserId()));
        log.setBizNo("");
        log.setRespCode(response.getStatusCode());
        log.setRespMsg(response.getMessage());
        log.setMEMO("快速退款");

        accountingLogMapper.insertSelective(log);
    }

    private AccountingRequest generateRequest(Payment payment, BussinessOrder bussinessOrder) {
        List<AccountingItem> itemList = new ArrayList<>();
        AccountingItem item = new AccountingItem();
        item.setUserId(bussinessOrder.getUserId());
        item.setCurrencyType(CurrencyTypeEnum.CNY.code());
        item.setAmount(""); // TODO
        item.setAccountOperateType(AccountOperateTypeEnum.Fundout.code());
        item.setAccountType(AccountTypeEnum.RmbAccount.code());
        item.setAccountingDate(new Date());
        item.setBizCode("300017"); // TODO
        item.setBizNo(payment.getPaymentId()); // TODO
        item.setOriginalNo(payment.getInstitutionPaymentId());
        item.setMemo("快速退款");
        itemList.add(item);

        AccountingRequest request = new AccountingRequest();
        request.setAccountingItems(itemList);
        request.setAppId("payment.ymatou.com");
        return request;
    }

}
