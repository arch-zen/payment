/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.refund.constants.RefundConstants;
import com.ymatou.payment.domain.refund.repository.RefundPository;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.model.QueryRefundByBizNoReq;
import com.ymatou.payment.facade.model.QueryRefundByRefundNoReq;
import com.ymatou.payment.facade.model.QueryRefundDetail;
import com.ymatou.payment.facade.model.QueryRefundRequest;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;

/**
 * 
 * @author qianmin 2016年5月13日 下午3:40:40
 * 
 */
@Component
public class QueryRefundServiceImpl implements QueryRefundService {

    private static final Logger logger = LoggerFactory.getLogger(QueryRefundServiceImpl.class);

    @Autowired
    private RefundPository refundPository;

    @Override
    public List<QueryRefundDetail> queryRefundRequest(QueryRefundRequest req) {
        Integer pageIndex = req.getPageIndex() < 1 ? 1 : req.getPageIndex();
        Integer pageSize = req.getPageSize() < 1 ? 20 : req.getPageSize();
        String orderId = StringUtils.isBlank(req.getKey()) ? null : req.getKey();
        String refundStatusQuery = generateRefundStatusQuery(req.getRefundStatus());

        HashMap<String, Object> query = new HashMap<>();
        query.put("pageIndex", pageIndex);
        query.put("pageSize", pageSize);
        query.put("approveStatus", req.getApproveStatus());
        query.put("orderId", orderId);
        query.put("refundStatus", refundStatusQuery);
        logger.info("query refundRequest options: {}", query.toString());

        List<RefundRequestPo> refundrequestPos = refundPository.queryRefundRequest(query);
        logger.info("query refundRequest result count: {}", String.valueOf(refundrequestPos.size()));

        return generateRefundDetail(refundrequestPos);
    }

    private String generateRefundStatusQuery(List<Integer> refundStatusList) {
        if (refundStatusList == null || refundStatusList.size() == 0) {
            refundStatusList = RefundConstants.REFUND_QUERY_DEFULT_STATUS; // 默认条件
        }
        StringBuilder sb = new StringBuilder().append("(");
        for (Integer rs : refundStatusList) {
            sb.append(rs).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");

        return sb.toString();
    }

    /**
     * 根据RefundRequest PO生成应答detail
     * 
     * @param refundrequestPos
     * @return
     */
    private List<QueryRefundDetail> generateRefundDetail(List<RefundRequestPo> refundrequestPos) {
        List<QueryRefundDetail> queryRefundDetails = new ArrayList<>(refundrequestPos.size());

        int index = 0;
        for (RefundRequestPo rq : refundrequestPos) {
            logger.info("RefundRequestPos[{}]: {}", index++, rq);
            QueryRefundDetail detail = new QueryRefundDetail();
            detail.setTradeNo(rq.getTradeNo());
            detail.setOrderId(rq.getOrderId());
            detail.setAppId(rq.getAppId());
            detail.setPayType(rq.getPayType());
            detail.setRefundAmount(rq.getRefundAmount());
            detail.setCurrencyType(rq.getCurrencyType());
            detail.setApproveStatus(rq.getApproveStatus());
            detail.setApprovedTime(rq.getApprovedTime());
            detail.setCreatedTime(rq.getCreatedTime());
            detail.setApprovedUser(rq.getApprovedUser());
            detail.setRefundStatus(rq.getRefundStatus());
            detail.setRefundTime(rq.getRefundTime());
            detail.setRefundBatchNo(rq.getRefundBatchNo());
            detail.setPaymentId(rq.getPaymentId());
            detail.setPayChannel(Integer.valueOf(PayTypeEnum.getChannelType(rq.getPayType()).getCode()));
            detail.setTradeType(rq.getTradeType());
            detail.setIsIntercept(rq.getSoftDeleteFlag());

            queryRefundDetails.add(detail);
        }

        return queryRefundDetails;
    }

    @Override
    public List<QueryRefundDetail> queryRefundByRefundNo(QueryRefundByRefundNoReq req) {
        List<RefundRequestPo> refundrequestPos = refundPository.queryRefundByRefundNo(req.getRefundNoList());
        logger.info("query queryRefundByRefundNo result count: {}", String.valueOf(refundrequestPos.size()));

        return generateRefundDetail(refundrequestPos);
    }

    @Override
    public List<QueryRefundDetail> queryRefundByBizNo(QueryRefundByBizNoReq req) {
        List<RefundRequestPo> refundrequestPos = refundPository.queryRefundByBizNo(req.getBizNoList());
        logger.info("query queryRefundByBizNo result count: {}", String.valueOf(refundrequestPos.size()));

        return generateRefundDetail(refundrequestPos);
    }
}
