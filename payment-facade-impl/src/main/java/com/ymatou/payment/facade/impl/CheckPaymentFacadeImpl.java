/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.model.PaymentQueryResp;
import com.ymatou.payment.domain.channel.service.PaymentQueryService;
import com.ymatou.payment.domain.channel.service.paymentquery.PaymentQueryServiceFactory;
import com.ymatou.payment.domain.pay.model.ThirdPartyPayment;
import com.ymatou.payment.domain.pay.service.PaymentCheckService;
import com.ymatou.payment.facade.BaseResponse;
import com.ymatou.payment.facade.CheckPaymentFacade;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.model.CheckCmbPaymentReq;
import com.ymatou.payment.facade.model.CheckPaymentRequset;
import com.ymatou.payment.infrastructure.db.mapper.PaymentCheckDetailMapper;
import com.ymatou.payment.infrastructure.db.mapper.PaymentCheckMapper;
import com.ymatou.payment.infrastructure.db.model.PaymentCheckDetailPo;
import com.ymatou.payment.infrastructure.db.model.PaymentCheckExample;
import com.ymatou.payment.infrastructure.db.model.PaymentCheckPo;
import com.ymatou.payment.infrastructure.util.NetUtil;
import com.ymatou.payment.integration.common.CmbSignature;
import com.ymatou.payment.integration.model.CmbQuerySettledOrderRequest;
import com.ymatou.payment.integration.model.CmbQuerySettledOrderResponse;
import com.ymatou.payment.integration.model.CmbSettledOrderInfo;
import com.ymatou.payment.integration.service.cmb.QuerySettledOrderService;

/**
 * 
 * @author qianmin 2016年5月19日 下午3:42:32
 *
 */
@Component
public class CheckPaymentFacadeImpl implements CheckPaymentFacade {

    private static Logger logger = LoggerFactory.getLogger(CheckPaymentFacadeImpl.class);

    @Autowired
    private PaymentQueryServiceFactory paymentQueryServiceFactory;

    @Autowired
    private PaymentCheckService paymentCheckService;

    @Autowired
    private PaymentCheckMapper paymentCheckMapper;

    @Autowired
    private PaymentCheckDetailMapper paymentCheckDetailMapper;

    @Autowired
    private QuerySettledOrderService querySettledOrderService;

    @Autowired
    private InstitutionConfigManager instConfigManager;


    @Override
    public BaseResponse checkPayment(CheckPaymentRequset req) {

        PaymentQueryService paymentQueryService =
                paymentQueryServiceFactory.getInstanceByPayType(req.getPayType());

        PaymentQueryResp paymentQueryResp =
                paymentQueryService.queryPayment(req.getPaymentId(), req.getPayType(), req.getHeader());

        paymentCheckService.doCheck(generateModel(paymentQueryResp), req.getPaymentId(), req.isFinalCheck(),
                req.getHeader());


        BaseResponse resp = new BaseResponse();
        resp.setSuccess(true);

        return resp;
    }


    private ThirdPartyPayment generateModel(PaymentQueryResp paymentQueryResp) {
        ThirdPartyPayment payment = new ThirdPartyPayment();

        payment.setActualPayCurrency(paymentQueryResp.getActualPayCurrency());
        payment.setActualPayPrice(paymentQueryResp.getActualPayPrice());
        payment.setDiscountAmount(paymentQueryResp.getDiscountAmount());
        payment.setBankId(paymentQueryResp.getBankId());
        payment.setCardType(paymentQueryResp.getCardType());
        payment.setInstitutionPaymentId(paymentQueryResp.getInstitutionPaymentId());
        payment.setOriginMessage(paymentQueryResp.getOriginMessage());
        payment.setPayerId(paymentQueryResp.getPayerId());
        payment.setPaymentId(paymentQueryResp.getPaymentId());
        payment.setPayStatus(paymentQueryResp.getPayStatus().getIndex());
        payment.setPayTime(paymentQueryResp.getPayTime());
        payment.setTraceId(paymentQueryResp.getTraceId());

        return payment;
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ymatou.payment.facade.CheckPaymentFacade#checkCmbPayment(com.ymatou.payment.facade.model.
     * CheckCmbPaymentReq)
     */
    @Override
    public BaseResponse checkCmbPayment(CheckCmbPaymentReq req) {
        BaseResponse response = new BaseResponse();
        String checkTitle = String.format("招行一网通自动对账_%s_%s", req.getDate(), req.getNo());
        PaymentCheckPo paymentCheckPo = getPaymentCheckByUploadFile(checkTitle);

        // 如果对账单已经生成，则直接返回成功
        if (paymentCheckPo == null) {
            // 生成对账单
            int checkPaymentId = genPaymentCheck(req, checkTitle);

            // 生成对账单明细数据
            try {
                genPaymentCheckDetail(req, checkPaymentId);
            } catch (Exception e) {
                logger.error(checkTitle + " 对账失败", e);
            }
        }
        response.setSuccess(true);
        return response;
    }

    private PaymentCheckPo getPaymentCheckByUploadFile(String checkTitle) {
        PaymentCheckExample paymentCheckExample = new PaymentCheckExample();
        paymentCheckExample.createCriteria().andUploadFileEqualTo(checkTitle);

        List<PaymentCheckPo> selectByExample = paymentCheckMapper.selectByExample(paymentCheckExample);
        if (selectByExample == null || selectByExample.size() == 0) {
            return null;
        } else {
            return selectByExample.get(0);
        }

    }

    /**
     * 生成对账单
     * 
     * @param req
     * @return
     */
    private int genPaymentCheck(CheckCmbPaymentReq req, String checkTitle) {
        PaymentCheckPo record = new PaymentCheckPo();

        record.setPaymentChannel(Integer.parseInt(PayTypeEnum.CmbApp.getCode()));
        record.setUploadFile(checkTitle);
        record.setOriginalFileName(checkTitle);
        record.setUploadTime(new Date());
        record.setCheckAccount("System");
        record.setServerIp(NetUtil.getHostIp());
        record.setRemark("系统自动对账");

        paymentCheckMapper.insertSelective(record);

        return getPaymentCheckByUploadFile(checkTitle).getPaymentCheckId();
    }

    /**
     * 生成对账明细数据
     * 
     * @param checkPaymentId
     * @throws Exception
     */
    private void genPaymentCheckDetail(CheckCmbPaymentReq req, int checkPaymentId) throws Exception {
        InstitutionConfig config = instConfigManager.getConfig(PayTypeEnum.CmbApp);
        List<CmbSettledOrderInfo> orderList = fetchCmbSettledOrder(req.getDate(), config);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        if (!orderList.isEmpty()) {
            logger.info("fetch cmb settled orders, num:{}", orderList.size());
            int insertNum = 0;
            for (CmbSettledOrderInfo orderInfo : orderList) {
                PaymentCheckDetailPo record = new PaymentCheckDetailPo();
                record.setPaymentCheckId(checkPaymentId);
                record.setInstitutionPaymentId(orderInfo.getBankSerialNo());
                record.setPaymentId(orderInfo.getOrderNo());
                record.setAmount(new BigDecimal(orderInfo.getOrderAmount()));
                record.setMerchantId(config.getMerchantId());
                record.setTrdUserId("");
                record.setPayBank(orderInfo.getCardType()); // 02：一卡通，03：信用卡，07：他行卡
                record.setPayTime(simpleDateFormat.parse(orderInfo.getSettleDate() + orderInfo.getSettleTime()));
                record.setDiscountAmt(new BigDecimal(orderInfo.getDiscountAmount()));

                paymentCheckDetailMapper.insertSelective(record);
                insertNum++;
            }
            logger.info("insert check payment detail num:{}", insertNum);
        } else {
            logger.info("not fetch cmb settled orders");
        }
    }

    /**
     * 查询招行订单数据
     * 
     * @param date
     * @param nextKey
     * @return
     * @throws Exception
     */
    private List<CmbSettledOrderInfo> fetchCmbSettledOrder(String date, InstitutionConfig config) throws Exception {
        CmbQuerySettledOrderRequest request = new CmbQuerySettledOrderRequest();
        request.getReqData().setOperatorNo(config.getOperatorNo());
        request.getReqData().setBranchNo(config.getBranchNo());
        request.getReqData().setMerchantNo(config.getMerchantId());


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date schedulDate = simpleDateFormat.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(schedulDate);
        calendar.add(Calendar.DATE, -1);

        SimpleDateFormat sdfQueryDate = new SimpleDateFormat("yyyyMMdd");
        String queryDate = sdfQueryDate.format(calendar.getTime());
        request.getReqData().setBeginDate(queryDate);
        request.getReqData().setEndDate(queryDate);

        String sign = CmbSignature.shaSign(config.getMd5Key(), request.buildSignString());
        request.setSign(sign);


        CmbQuerySettledOrderResponse fetchCmbSettledOrder = querySettledOrderService.doService(request, null);
        List<CmbSettledOrderInfo> orderList = fetchCmbSettledOrder.parseOrderList();

        if (orderList.isEmpty()) {
            return orderList;
        }

        boolean hasNext = fetchCmbSettledOrder.getRspData().getHasNext().equals("Y");
        while (hasNext) {
            String nextKey = fetchCmbSettledOrder.getRspData().getNextKeyValue();
            request.getReqData().setNextKeyValue(nextKey);

            fetchCmbSettledOrder = querySettledOrderService.doService(request, null);
            orderList.addAll(fetchCmbSettledOrder.parseOrderList());
            hasNext = fetchCmbSettledOrder.getRspData().getHasNext().equals("Y");
        }
        return orderList;
    }

}
