/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.service.PayService;
import com.ymatou.payment.domain.refund.repository.RefundPository;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.facade.model.AliPayRefundNotifyRequest;
import com.ymatou.payment.infrastructure.db.model.RefundMiscRequestLogWithBLOBs;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;

/**
 * 
 * @author qianmin 2016年5月17日 下午2:55:40
 * 
 */
@Component
public class RefundNotifyServiceImpl implements RefundNotifyService {

    private static final Logger logger = LoggerFactory.getLogger(RefundNotifyServiceImpl.class);

    @Autowired
    private SignatureService signatureService;

    @Autowired
    private InstitutionConfigManager instConfigManager;

    @Autowired
    private RefundPository refundPository;

    @Autowired
    private RefundJobService refundJobService;

    @Autowired
    private PayService payService;

    @Override
    public void processRefundCallback(AliPayRefundNotifyRequest req) {
        // 验签
        Map<String, String> signMap = getRequestMap(req);
        boolean signResult =
                signatureService.validateSign(signMap, instConfigManager.getConfig(PayTypeEnum.parse(req.getPayType())),
                        req.getMockHeader());
        if (!signResult) {
            throw new BizException("signdata is invalid");
        }

        // 解析退款回调的应答
        List<RefundNotifyDetail> details = generateRefundNotifyDetail(req.getResultDetails());

        // 生成退款回调日志
        List<RefundMiscRequestLogWithBLOBs> list = generateRefundmiscrequestlog(req, details, signMap);

        if (list.size() > 0) {
            logger.info("save RefundMiscRequestLog begin.");
            refundPository.batchSaveRefundmiscrequestlog(list); // 保存退款回调日志

            for (RefundMiscRequestLogWithBLOBs rmrl : list) {
                // 重新查询退款结果
                String refundBatchNo = rmrl.getRefundBatchNo();
                RefundRequestPo refundRequest = refundJobService.getRefundRequestById(refundBatchNo);
                Payment payment = payService.getPaymentByPaymentId(refundRequest.getPaymentId());

                RefundStatusEnum refundStatus =
                        refundJobService.queryRefund(refundRequest, payment, req.getMockHeader());
                refundJobService.updateRefundRequestAndPayment(refundRequest, payment, refundStatus);
                if (RefundStatusEnum.THIRDPART_REFUND_SUCCESS.equals(refundStatus)) {
                    boolean isSuccess =
                            refundJobService.callbackTradingSystem(refundRequest, payment, req.getMockHeader());
                    if (isSuccess) {
                        refundJobService.updateRefundRequestToCompletedSuccess(refundRequest);
                    }
                }
            }

        }
    }

    private List<RefundMiscRequestLogWithBLOBs> generateRefundmiscrequestlog(AliPayRefundNotifyRequest req,
            List<RefundNotifyDetail> details, Map<String, String> signMap) {
        List<RefundMiscRequestLogWithBLOBs> refundmiscrequestlogWithBLOBs = new ArrayList<>();

        // 根据refundBatchNo获取RefundRequest
        RefundRequestPo refundrequest = refundPository.getRefundRequestByRefundNo(req.getBatchNo());

        if (refundrequest.getRefundStatus() == RefundStatusEnum.COMPLETE_SUCCESS.getCode()
                || refundrequest.getRefundStatus() == RefundStatusEnum.RETURN_TRANSACTION.getCode()) { // 已退款成功的，忽略
            logger.info("The refundRequest is success. RefundBatchNo[{}]", refundrequest.getRefundBatchNo());
            return refundmiscrequestlogWithBLOBs;
        }

        for (RefundNotifyDetail detail : details) {
            // 第三方退款成功的， 并在RefundRequest找到记录的
            if (detail.getInstitutionPaymentId().equals(refundrequest.getInstPaymentId()) && detail.isSuccess()) {

                logger.info("The refundRequest is thirdPart success. RefundBatchNo[{}]",
                        refundrequest.getRefundBatchNo());
                logger.info("generate RefundMiscRequestLog begin.");
                RefundMiscRequestLogWithBLOBs rmrl = new RefundMiscRequestLogWithBLOBs();
                rmrl.setRefundBatchNo(req.getBatchNo());
                rmrl.setCorrelateId(refundrequest.getPaymentId());
                rmrl.setIsException(false);
                rmrl.setExceptionDetail("");
                rmrl.setLogId(UUID.randomUUID().toString());
                rmrl.setMethod("RefundNotify");
                rmrl.setRequestData("");
                rmrl.setResponseData(JSON.toJSONString(signMap)); // TODO
                rmrl.setRequestTime(new Date());

                if (StringUtils.isBlank(req.getNotifyTime()))
                    rmrl.setResponseTime(new Date());
                else
                    rmrl.setResponseTime(parseDate(req.getNotifyTime()));


                refundmiscrequestlogWithBLOBs.add(rmrl); // 记录退款回调日志
            }
        }

        return refundmiscrequestlogWithBLOBs;
    }

    /**
     * 将日期字符串转成日期格式
     * 
     * @param dateString
     * @return
     */
    private Date parseDate(String dateString) {
        try {

            return DateUtils.parseDate(dateString, new String[] {"yyyy-MM-dd HH:mm:ss"});
        } catch (ParseException e) {
            logger.error("pare date when process alipay notify with date string:" + dateString, e);
            return new Date();
        }
    }


    private HashMap<String, String> getRequestMap(AliPayRefundNotifyRequest req) {
        HashMap<String, String> map = new HashMap<>();
        map.put("notify_time", req.getNotifyTime());
        map.put("notify_type", req.getNotifyType());
        map.put("notify_id", req.getNotifyId());
        map.put("sign_type", req.getSignType());
        map.put("sign", req.getSign());
        map.put("batch_no", req.getBatchNo());
        map.put("success_num", req.getSuccessNum());
        map.put("result_details", req.getResultDetails());
        return map;
    }

    private List<RefundNotifyDetail> generateRefundNotifyDetail(String details) {
        logger.info("refund notify details: {}", details);

        List<RefundNotifyDetail> detailList = new ArrayList<>();
        String[] tempDatas = details.split("\\|"); // 多条退款记录以"|"分割
        if (tempDatas == null || tempDatas.length == 0) {
            throw new BizException("refund data detail is invalid.");
        }

        for (String temp : tempDatas) {

            String[] refundData = temp.split("$"); // 退款信息$退费信息
            if (refundData.length == 0) {
                throw new BizException("refund data detail is invalid.");
            }

            String[] resultDetail = refundData[0].split("\\^"); // 分割退款信息
            logger.info("refund notify detail: {}", Arrays.toString(resultDetail));
            RefundNotifyDetail refundNotifyDetail = new RefundNotifyDetail();
            refundNotifyDetail.setInstitutionPaymentId(resultDetail[0]); // 交易号
            refundNotifyDetail.setRefundAmount(resultDetail[1]); // 退款金额
            refundNotifyDetail.setRefundResult(resultDetail[2]); // 退款结果
            refundNotifyDetail.setSuccess("SUCCESS".equalsIgnoreCase(resultDetail[2]));

            detailList.add(refundNotifyDetail);
        }

        return detailList;
    }

    class RefundNotifyDetail {
        private String institutionPaymentId;
        private String refundAmount;
        private String refundResult;
        private boolean isSuccess;

        public String getInstitutionPaymentId() {
            return institutionPaymentId;
        }

        public void setInstitutionPaymentId(String institutionPaymentId) {
            this.institutionPaymentId = institutionPaymentId;
        }

        public String getRefundAmount() {
            return refundAmount;
        }

        public void setRefundAmount(String refundAmount) {
            this.refundAmount = refundAmount;
        }

        public String getRefundResult() {
            return refundResult;
        }

        public void setRefundResult(String refundResult) {
            this.refundResult = refundResult;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean isSuccess) {
            this.isSuccess = isSuccess;
        }
    }
}
