/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.pay.integration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.constants.AccountOperateTypeEnum;
import com.ymatou.payment.facade.constants.AccountTypeEnum;
import com.ymatou.payment.facade.constants.AccountingStatusEnum;
import com.ymatou.payment.facade.constants.CurrencyTypeEnum;
import com.ymatou.payment.infrastructure.db.mapper.AccountingLogMapper;
import com.ymatou.payment.infrastructure.db.model.AccountingLogPo;
import com.ymatou.payment.integration.model.AccountingItem;
import com.ymatou.payment.integration.model.AccountingRequest;
import com.ymatou.payment.integration.model.AccountingResponse;
import com.ymatou.payment.integration.service.ymatou.AccountService;

@Component
public class AccountingService {

    private static Logger logger = LoggerFactory.getLogger(AccountingService.class);

    @Autowired
    private AccountingLogMapper accountingLogMapper;

    @Autowired
    private AccountService accountService;

    /**
     * 资金账户充值
     * 
     * @param payment
     * @param bussinessOrder
     * @return
     */
    public boolean recharge(Payment payment, BussinessOrder bussinessOrder) {
        AccountingStatusEnum result = rechargeInternal(payment, bussinessOrder);

        // 如果调用资金服务异常，进行一次重试
        if (result == AccountingStatusEnum.UNKNOW) {
            result = rechargeInternal(payment, bussinessOrder);
        }

        return result == AccountingStatusEnum.SUCCESS;

    }

    /**
     * 资金账户充值
     * 
     * @param payment
     * @param bussinessOrder
     * @return
     */
    public AccountingStatusEnum rechargeInternal(Payment payment, BussinessOrder bussinessOrder) {
        AccountingRequest request = generateRequest(payment, bussinessOrder);
        AccountingResponse response = accountService.accounting(request, null);
        saveAccoutingLog(payment, bussinessOrder, response);
        return response.getAccountingStatus();
    }

    /**
     * 生成账户操作指令
     * 
     * @param payment
     * @param bussinessOrder
     * @return
     */
    private AccountingRequest generateRequest(Payment payment, BussinessOrder bussinessOrder) {
        List<AccountingItem> itemList = new ArrayList<>();
        AccountingItem item = new AccountingItem();
        item.setUserId(bussinessOrder.getUserId());
        item.setCurrencyType(CurrencyTypeEnum.CNY.code());
        item.setAmount(payment.getPayPrice().getAmount().toString());
        item.setAccountOperateType(AccountOperateTypeEnum.Fundin.code());
        item.setAccountType(AccountTypeEnum.RmbAccount.code());
        item.setAccountingDate(new Date());
        item.setBizCode(bizcode(bussinessOrder));
        item.setBizNo(payment.getPaymentId());
        item.setOriginalNo(bussinessOrder.getOrderId());
        item.setMemo(memo(bussinessOrder));
        itemList.add(item);

        AccountingRequest request = new AccountingRequest();
        request.setAccountingItems(itemList);
        request.setAppId("payment.iapi.ymatou.com");
        return request;
    }


    /*
     * 保存账务操作记录
     */
    private void saveAccoutingLog(Payment payment, BussinessOrder bussinessOrder, AccountingResponse response) {
        AccountingLogPo log = new AccountingLogPo();
        log.setCreatedTime(new Date());
        log.setAccoutingAmt(payment.getPayPrice().getAmount());
        log.setAccountingType("Payment");
        log.setStatus(response.isAccoutingSuccess()); // 成功为1，失败为0
        log.setUserId((long) bussinessOrder.getUserId().intValue());
        log.setBizNo(payment.getPaymentId());
        log.setRespCode(response.getStatusCode());
        log.setRespMsg(response.getMessage());
        log.setMemo(memo(bussinessOrder));
        accountingLogMapper.insertSelective(log);

    }

    /**
     * 根据BizCode填写Memo
     * 
     * @param bussinessOrder
     * @return
     */
    private String memo(BussinessOrder bussinessOrder) {
        switch (bussinessOrder.getBizCode()) {
            case POST_RECHARGE:
                return "补款充值";
            case PAY_RECHARGE:
                return "定金充值";
            case SELLER_MARGIN_RECHARGE:
                return "卖家保证金充值";
            case SELLER_RECHARGE:
                return "卖家余额充值";
            default:
                throw new BizException(ErrorCode.FAIL, "invalid bizcode:" + bussinessOrder.getBizCode());
        }
    }

    /**
     * 获取到资金账户操作的BizCode
     * 
     * @param bussinessOrder
     * @return
     */
    private String bizcode(BussinessOrder bussinessOrder) {
        switch (bussinessOrder.getBizCode()) {
            case POST_RECHARGE:
                return "100002";
            case PAY_RECHARGE:
                return "100001";
            case SELLER_MARGIN_RECHARGE:
                return "100003";
            case SELLER_RECHARGE:
                return "100003";
            default:
                throw new BizException(ErrorCode.FAIL, "invalid bizcode:" + bussinessOrder.getBizCode());
        }
    }

}
