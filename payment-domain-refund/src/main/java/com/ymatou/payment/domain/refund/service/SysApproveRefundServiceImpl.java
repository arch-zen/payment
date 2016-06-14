/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.refund.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.constants.ApproveStatusEnum;
import com.ymatou.payment.infrastructure.db.mapper.PaymentParamMapper;
import com.ymatou.payment.infrastructure.db.mapper.RefundRequestMapper;
import com.ymatou.payment.infrastructure.db.model.PaymentParamExample;
import com.ymatou.payment.infrastructure.db.model.PaymentParamPo;
import com.ymatou.payment.infrastructure.db.model.RefundRequestExample;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;

/**
 * 系统自动审核退款服务
 * 
 * @author wangxudong 2016年6月13日 下午2:02:38
 *
 */
@Component
public class SysApproveRefundServiceImpl implements SysApproveRefundService {

    private static Logger logger = LoggerFactory.getLogger(SysApproveRefundServiceImpl.class);

    /**
     * 默认的退款自动审核阀值
     */
    private static final int DEFUAT_REFUND_MAX_NUM = 200;

    /**
     * 系统参数
     */
    @Resource
    private PaymentParamMapper paymentParamMapper;

    /**
     * 退款请求
     */
    @Resource
    private RefundRequestMapper refundRequestMapper;

    @Override
    public int batchApproveRefundReq(Date scheduleTime) {
        Calendar approveTime = Calendar.getInstance();
        approveTime.setTime(scheduleTime);
        approveTime.add(Calendar.HOUR, -1);

        logger.info("system batch approve refund req with schedule time: {}, approve time: {}", scheduleTime,
                approveTime.getTime());

        int refundNum = getRefundNum(approveTime.getTime(), scheduleTime);
        if (refundNum == 0) {
            return 0;
        }

        int refundMaxNum = getRefundMaxNum(approveTime.get(Calendar.HOUR_OF_DAY));
        if (refundNum > refundMaxNum) {
            String errMsg = String.format("refund num(%d) large then max num(%d) when system batch approve", refundNum,
                    refundMaxNum);
            logger.error(errMsg);
            throw new BizException(errMsg);
        }

        int rowsUpdate = updateRefundApproved(approveTime.getTime(), scheduleTime);
        return rowsUpdate;

    }

    /**
     * 获取到退款自动审批阀值
     * 
     * @return
     */
    private int getRefundMaxNum(int hour) {
        PaymentParamExample paymentParamExample = new PaymentParamExample();
        paymentParamExample.createCriteria()
                .andParamCatEqualTo("RefundApproveRisk").andParamKeyEqualTo(String.valueOf(hour));

        List<PaymentParamPo> lstParam = paymentParamMapper.selectByExample(paymentParamExample);
        if (lstParam.size() == 0) {
            return DEFUAT_REFUND_MAX_NUM;
        } else {
            return lstParam.get(0).getParamIntValue().intValue();
        }
    }

    /**
     * 获取到退款的数量
     * 
     * @param begin
     * @param end
     * @return
     */
    private int getRefundNum(Date begin, Date end) {
        RefundRequestExample refundRequestExample = new RefundRequestExample();
        refundRequestExample.createCriteria()
                .andApproveStatusEqualTo(ApproveStatusEnum.NOT_APPROVED.getCode())
                .andCreatedTimeBetween(begin, end)
                .andSoftDeleteFlagEqualTo(false);

        return refundRequestMapper.countByExample(refundRequestExample);
    }

    /**
     * 批量更新退款申请为已经审核
     * 
     * @param begin
     * @param end
     * @return
     */
    private int updateRefundApproved(Date begin, Date end) {
        RefundRequestExample refundRequestExample = new RefundRequestExample();
        refundRequestExample.createCriteria()
                .andApproveStatusEqualTo(ApproveStatusEnum.NOT_APPROVED.getCode())
                .andCreatedTimeBetween(begin, end)
                .andSoftDeleteFlagEqualTo(false);

        RefundRequestPo refundRequestPo = new RefundRequestPo();
        refundRequestPo.setApprovedTime(Calendar.getInstance().getTime());
        refundRequestPo.setApproveStatus(ApproveStatusEnum.APPROVED.getCode());
        refundRequestPo.setApprovedUser("system");

        return refundRequestMapper.updateByExampleSelective(refundRequestPo, refundRequestExample);
    }
}
