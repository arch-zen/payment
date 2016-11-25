/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.acquireorder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.model.AcquireOrderPackageResp;
import com.ymatou.payment.domain.channel.service.AcquireOrderService;
import com.ymatou.payment.domain.channel.util.CmbFormBuilder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.repository.CmbAggrementRepository;
import com.ymatou.payment.facade.constants.AcquireOrderResultTypeEnum;
import com.ymatou.payment.infrastructure.db.model.CmbAggrementPo;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.CmbSignature;
import com.ymatou.payment.integration.model.CmbPayRequest;

/**
 * 招行一网通 收单报文组织（20）
 * 
 * @author wangxudong 2016年11月14日 下午4:47:27
 *
 */
@Component
public class CmbAcquireOrderServiceImpl implements AcquireOrderService {


    @Resource
    private InstitutionConfigManager instConfigManager;

    @Resource
    private IntegrationConfig integrationConfig;

    @Resource
    private CmbAggrementRepository cmbAggrementRepository;

    @Override
    public AcquireOrderPackageResp acquireOrderPackage(Payment payment, HashMap<String, String> mockHeader) {
        // 获取第三方机构配置
        InstitutionConfig instConfig = instConfigManager.getConfig(payment.getPayType());

        // 签约记录
        Integer userId = payment.getBussinessOrder().getUserId();
        CmbAggrementPo aggrement = cmbAggrementRepository.findSignAggrement(userId);
        if (aggrement == null) { // 如果没有签约记录
            aggrement = cmbAggrementRepository.signAggrement(userId);
        }

        // 构建支付请求
        CmbPayRequest payRequest = buildPayReq(payment, instConfig, aggrement);

        // 签名
        String sign = CmbSignature.shaSign(instConfig.getMd5Key(), payRequest.buildSignString());
        payRequest.setSign(sign);

        // 拼装请求报文
        String reqForm = CmbFormBuilder.buildForm(payRequest, integrationConfig.getCmbPayUrl(mockHeader));

        // 返回报文结果
        AcquireOrderPackageResp resp = new AcquireOrderPackageResp();
        resp.setResultType(AcquireOrderResultTypeEnum.Form);
        resp.setResult(reqForm);

        return resp;
    }

    /**
     * 构建支付请求
     * 
     * @param payment
     * @param instConfig
     * @return
     */
    private CmbPayRequest buildPayReq(Payment payment, InstitutionConfig instConfig, CmbAggrementPo aggrement) {
        CmbPayRequest payRequest = new CmbPayRequest();

        payRequest.getReqData()
                .setPayNoticeUrl(String.format("%s/cmbPayNotify", integrationConfig.getYmtCmbPaymentBaseUrl()));
        payRequest.getReqData()
                .setSignNoticeUrl(String.format("%s/cmbSignNotify", integrationConfig.getYmtCmbPaymentBaseUrl()));

        if (StringUtils.isEmpty(payment.getBussinessOrder().getCallbackUrl())) {
            payRequest.getReqData().setReturnUrl("http://YMTCMBPAY");
        } else {
            payRequest.getReqData().setReturnUrl(payment.getBussinessOrder().getCallbackUrl());
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hhmmssSSS");
        payRequest.getReqData().setAgrNo(aggrement.getAggId().toString());
        payRequest.getReqData()
                .setMerchantSerialNo(aggrement.getAggId().toString() + simpleDateFormat.format(new Date())); // 协议号+时间戳
        payRequest.getReqData().setAmount(String.format("%.2f", payment.getPayPrice().getAmount().doubleValue()));
        payRequest.getReqData().setBranchNo(instConfig.getBranchNo());
        payRequest.getReqData().setDate(payment.getBussinessOrder().getOrderTime().substring(0, 8)); // 需要和退款的Date保持一致
        payRequest.getReqData().setMerchantNo(instConfig.getMerchantId());
        payRequest.getReqData().setOrderNo(payment.getPaymentId());
        payRequest.getReqData().setPayNoticePara("Pay");
        payRequest.getReqData().setSignNoticePara("Sign");
        payRequest.getReqData().setUserID(String.valueOf(payment.getBussinessOrder().getUserId()));

        return payRequest;
    }
}
