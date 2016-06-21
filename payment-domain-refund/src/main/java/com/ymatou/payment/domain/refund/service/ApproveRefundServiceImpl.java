/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.refund.repository.RefundPository;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.constants.ApproveStatusEnum;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;

/**
 * 
 * @author qianmin 2016年5月13日 下午12:00:33
 * 
 */
@Component
public class ApproveRefundServiceImpl implements ApproveRefundService {

    private static final Logger logger = LoggerFactory.getLogger(ApproveRefundServiceImpl.class);

    @Autowired
    private RefundPository refundPository;

    @Override
    public List<RefundRequestPo> approveRefund(List<String> refundNos, String approveUser) {
        List<RefundRequestPo> refundrequestPos = new ArrayList<>();
        for (String refundNo : refundNos) {
            RefundRequestPo refundrequestPo = refundPository.getRefundRequestByRefundNo(refundNo);
            if (refundrequestPo == null) {
                throw new BizException(ErrorCode.NOT_EXIST_REFUNDNO, "refund request not exist.");
            }
            if (refundrequestPo.getApproveStatus() != ApproveStatusEnum.NOT_APPROVED.getCode()) {
                logger.info("RefundRequest ApproveStaus expected 0, but {}. RefundNo: {}",
                        refundrequestPo.getApproveStatus(), refundNo);
                continue;
            }

            // 组装更新RefundRequest
            refundrequestPo.setApproveStatus(ApproveStatusEnum.APPROVED.getCode());
            refundrequestPo.setApprovedTime(new Date());
            refundrequestPo.setApprovedUser(approveUser);
            refundrequestPos.add(refundrequestPo);
        }

        logger.info("update refundRequest status.");
        refundPository.updateRefundRequest(refundrequestPos);

        return refundrequestPos;
    }
}
