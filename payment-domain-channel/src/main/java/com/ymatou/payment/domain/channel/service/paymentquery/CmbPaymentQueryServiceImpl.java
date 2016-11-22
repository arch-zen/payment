/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.paymentquery;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.model.PaymentQueryResp;
import com.ymatou.payment.domain.channel.service.PaymentQueryService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.repository.BussinessOrderRepository;
import com.ymatou.payment.domain.pay.repository.PaymentRepository;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.infrastructure.Money;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.CmbSignature;
import com.ymatou.payment.integration.model.CmbQuerySingleOrderRequest;
import com.ymatou.payment.integration.model.CmbQuerySingleOrderResponse;
import com.ymatou.payment.integration.model.CmbQuerySingleOrderResponse.QuerySingleOrderRspData;
import com.ymatou.payment.integration.service.cmb.QuerySingleOrderService;

/**
 * 招行一网通订单查询服务
 * 
 * @author wangxudong 2016年11月22日 下午1:56:59
 *
 */
@Component
public class CmbPaymentQueryServiceImpl implements PaymentQueryService {

    private static final Logger logger = LoggerFactory.getLogger(CmbPaymentQueryServiceImpl.class);

    @Resource
    private QuerySingleOrderService querySingleOrderService;

    @Resource
    private SignatureService signatureService;

    @Resource
    private InstitutionConfigManager institutionConfigManager;

    @Resource
    private IntegrationConfig integrationConfig;

    @Resource
    private PaymentRepository paymentRepository;

    @Resource
    private BussinessOrderRepository bussinessOrderRepository;

    private static final String CMB_PAID = "0";

    @Override
    public PaymentQueryResp queryPayment(String paymentId, String payType, HashMap<String, String> header) {
        Payment payment = paymentRepository.getByPaymentId(paymentId);
        BussinessOrder bussinessOrder =
                bussinessOrderRepository.getBussinessOrderById(payment.getBussinessOrderId());

        CmbQuerySingleOrderRequest orderQueryRequest =
                generateRequest(StringUtils.left(bussinessOrder.getOrderTime(), 8), paymentId, payType, header);
        try {
            // 调用招行一网通支付查询订单接口
            CmbQuerySingleOrderResponse response = querySingleOrderService.doService(orderQueryRequest, header);

            PaymentQueryResp resp = new PaymentQueryResp();
            QuerySingleOrderRspData rspData = response.getRspData();


            resp.setOriginMessage(JSON.toJSONString(response));
            if ("SUC0000".equals(rspData.getRspCode()) && CMB_PAID.equals(rspData.getOrderStatus())) {
                resp = generateResponse(response);
                resp.setPayStatus(PayStatusEnum.Paied);
                return resp;
            } else {
                resp.setPayStatus(PayStatusEnum.Failed);
                return resp;
            }

        } catch (Exception e) {
            logger.error("call cmb order query failed", e);
            throw new BizException(ErrorCode.SERVER_SIDE_ACQUIRE_ORDER_FAILED,
                    "Paymentid: " + paymentId, e);
        }
    }

    private CmbQuerySingleOrderRequest generateRequest(String orderTime, String paymentId, String payType,
            HashMap<String, String> header) {
        // 根据payType获取appId,mchId信息
        InstitutionConfig institutionConfig = institutionConfigManager.getConfig(PayTypeEnum.parse(payType));
        String branchNo = institutionConfig.getBranchNo();
        String mchId = institutionConfig.getMerchantId();
        String md5Key =
                integrationConfig.isMock(header) ? signatureService.getMd5MockKey() : institutionConfig.getMd5Key();

        // 组装request
        CmbQuerySingleOrderRequest orderQueryRequest = new CmbQuerySingleOrderRequest();

        orderQueryRequest.getReqData().setBranchNo(branchNo);
        orderQueryRequest.getReqData().setMerchantNo(mchId);
        orderQueryRequest.getReqData().setOrderNo(paymentId);
        orderQueryRequest.getReqData().setDate(orderTime);

        String sign = CmbSignature.shaSign(md5Key, orderQueryRequest.buildSignString());
        orderQueryRequest.setSign(sign);

        logger.info("cmb order query request: {}", JSON.toJSONString(orderQueryRequest));
        return orderQueryRequest;
    }

    private PaymentQueryResp generateResponse(CmbQuerySingleOrderResponse response) throws ParseException {

        QuerySingleOrderRspData rspData = response.getRspData();

        PaymentQueryResp resp = new PaymentQueryResp();
        resp.setActualPayCurrency("CNY");
        resp.setBankId("");
        resp.setCardType(Integer.valueOf(rspData.getCardType()));
        resp.setInstitutionPaymentId(rspData.getBankSerialNo());
        resp.setPayerId("");
        resp.setPaymentId(rspData.getOrderNo());
        resp.setPayTime(new Date());
        resp.setTraceId(UUID.randomUUID().toString());

        Money orderAmount = new Money(rspData.getOrderAmount());
        Money discontAmount = new Money(rspData.getDiscountAmount());
        resp.setActualPayPrice(orderAmount.subtract(discontAmount).getAmount()); // 实际支付金额=订单金额-优惠金额
        resp.setDiscountAmount(discontAmount);

        return resp;
    }

}
