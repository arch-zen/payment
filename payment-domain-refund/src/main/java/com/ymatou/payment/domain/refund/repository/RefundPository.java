/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.repository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.refund.model.Refund;
import com.ymatou.payment.facade.constants.ApproveStatusEnum;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.infrastructure.db.mapper.PaymentMapper;
import com.ymatou.payment.infrastructure.db.mapper.RefundMiscRequestLogMapper;
import com.ymatou.payment.infrastructure.db.mapper.RefundRequestMapper;
import com.ymatou.payment.infrastructure.db.model.PaymentExample;
import com.ymatou.payment.infrastructure.db.model.PaymentPo;
import com.ymatou.payment.infrastructure.db.model.RefundMiscRequestLogWithBLOBs;
import com.ymatou.payment.infrastructure.db.model.RefundRequestExample;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;

/**
 * 
 * @author qianmin 2016年5月10日 下午4:56:52
 *
 */
@Component
public class RefundPository {

    @Autowired
    private RefundRequestMapper refundRequestMapper;

    @Autowired
    private RefundMiscRequestLogMapper refundMiscRequestLogMapper;

    @Autowired
    private PaymentMapper paymentMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Autowired
    private SqlSession sqlSession;

    /**
     * 获退款批次号
     * 
     * @return
     */
    public String generateRefundBatchNo() {
        HashMap<String, Object> query = new HashMap<>();
        query.put("sequenceType", "refund");
        sqlSession.selectOne("ext-refundrequest.genRefundBatchNoSeq", query);

        return new StringBuilder().append(sdf.format(new Date()))
                .append(StringUtils.leftPad(String.valueOf(query.get("seed")), 10, "0"))
                .toString();
    }

    @Transactional
    public void saveFastRefundrequest(Payment payment, BussinessOrder bussinessorder,
            Refund refundInfo) {
        RefundRequestPo refundrequest = new RefundRequestPo();

        refundrequest.setPaymentId(payment.getPaymentId());
        refundrequest.setInstPaymentId(payment.getInstitutionPaymentId());
        refundrequest.setTradeNo(bussinessorder.getOrderId());

        refundrequest.setOrderId(refundInfo.getOrderIdList().get(0));
        refundrequest.setTraceId(refundInfo.getTraceId());
        refundrequest.setAppId(refundInfo.getAppId());
        refundrequest.setPayType(payment.getPayType().getCode());
        refundrequest.setRefundAmount(refundInfo.getRefundAmt());
        refundrequest.setCurrencyType(payment.getPayCurrencyType());
        refundrequest.setApproveStatus(ApproveStatusEnum.FAST_REFUND.getCode());
        refundrequest.setApprovedTime(new Date());
        refundrequest.setApprovedUser("system");
        refundrequest.setRefundStatus(RefundStatusEnum.INIT.getCode());
        refundrequest.setTradeType(refundInfo.getTradeType());
        // refundrequest.setRefundBatchNo(generateRefundBatchNo()); //需在提交第三方退款时生成
        refundRequestMapper.insertSelective(refundrequest);

        BigDecimal refundAmt = payment.getRefundAmt() == null ? new BigDecimal(0.00) : payment.getRefundAmt();
        PaymentPo paymentPo = new PaymentPo();
        paymentPo.setPaymentId(payment.getPaymentId());
        paymentPo.setRefundAmt(refundAmt.add(refundrequest.getRefundAmount()));
        paymentMapper.updateByPrimaryKeySelective(paymentPo);
    }

    /**
     * 根据refundNo获取Refundrequest
     * 
     * @param paymentId
     * @return
     */
    public RefundRequestPo getRefundRequestByRefundBatchNo(String refundBatchNo) {
        RefundRequestExample example = new RefundRequestExample();
        example.createCriteria().andRefundBatchNoEqualTo(refundBatchNo);
        List<RefundRequestPo> result = refundRequestMapper.selectByExample(example);
        if (result != null && result.size() > 0) {
            return result.get(0); // 有唯一约束
        } else {
            return null;
        }
    }

    public RefundRequestPo getRefundRequestByRefundId(Integer refundId) {
        RefundRequestExample example = new RefundRequestExample();
        example.createCriteria().andRefundIdEqualTo(refundId);
        List<RefundRequestPo> result = refundRequestMapper.selectByExample(example);
        if (result != null && result.size() > 0) {
            return result.get(0); // 自增长
        } else {
            return null;
        }
    }

    /**
     * 保存RefundRequest, 更新退款金额
     * 
     * @param refundrequestWithBLOBs
     */
    @Transactional
    public void batchSaveRefundRequestAndUpdateRefundAmt(List<RefundRequestPo> refundrequestWithBLOBs) {
        for (RefundRequestPo refundrequest : refundrequestWithBLOBs) {
            refundRequestMapper.insertSelective(refundrequest);

            PaymentPo result = paymentMapper.selectByPrimaryKey(refundrequest.getPaymentId());
            BigDecimal refundAmt = result.getRefundAmt() == null ? new BigDecimal(0.00) : result.getRefundAmt();
            PaymentPo paymentPo = new PaymentPo();
            paymentPo.setPaymentId(result.getPaymentId());
            paymentPo.setRefundAmt(refundAmt.add(refundrequest.getRefundAmount()));
            paymentMapper.updateByPrimaryKeySelective(paymentPo);
        }
    }

    /**
     * 保存RefundRequest, 更新退款金额
     * 
     * @param refundrequestWithBLOBs
     */
    @Transactional
    public void saveRefundRequestAndUpdateRefundAmt(RefundRequestPo refundrequest, Payment payment) {
        refundRequestMapper.insertSelective(refundrequest);

        BigDecimal refundAmt = payment.getRefundAmt() == null ? new BigDecimal(0.00) : payment.getRefundAmt();
        PaymentPo paymentPo = new PaymentPo();
        paymentPo.setPaymentId(payment.getPaymentId());
        paymentPo.setRefundAmt(refundAmt.add(refundrequest.getRefundAmount()));
        paymentMapper.updateByPrimaryKeySelective(paymentPo);
    }

    /**
     * 更新RequestRequest
     * 
     * @param refundrequestPos
     * @param compensateprocessinfoWithBLOBs
     */
    @Transactional
    public void approveRefundRequest(List<RefundRequestPo> refundrequestPos) {
        for (RefundRequestPo refundrequestPo : refundrequestPos) {
            RefundRequestExample example = new RefundRequestExample();
            example.createCriteria().andRefundIdEqualTo(refundrequestPo.getRefundId());

            RefundRequestPo record = new RefundRequestPo();
            record.setApprovedUser(refundrequestPo.getApprovedUser());
            record.setApproveStatus(ApproveStatusEnum.APPROVED.getCode());
            record.setApprovedTime(new Date());
            refundRequestMapper.updateByExampleSelective(record, example);
        }
    }


    /**
     * 查询退款单
     * 
     * @param query
     * @return
     */
    public List<RefundRequestPo> queryRefundRequest(HashMap<String, Object> query) {
        return sqlSession.selectList("ext-refundrequest.selectRefundrequestByPage", query);
    }

    /**
     * 保存退款回调日志
     * 
     * @param list
     */
    @Transactional
    public void batchSaveRefundmiscrequestlog(List<RefundMiscRequestLogWithBLOBs> list) {
        for (RefundMiscRequestLogWithBLOBs refundmiscrequestlog : list) {
            refundMiscRequestLogMapper.insertSelective(refundmiscrequestlog);
        }
    }

    @Transactional
    public void updateRefundRequestAccoutingStatus(Integer refundId, int accoutingStatus) {
        RefundRequestExample example = new RefundRequestExample();
        example.createCriteria().andRefundIdEqualTo(refundId);
        RefundRequestPo record = new RefundRequestPo();
        record.setAccoutingStatus(accoutingStatus);
        refundRequestMapper.updateByExampleSelective(record, example);
    }

    /**
     * 更新退款状态，重试次数，退款申请金额， 完成金额
     * 
     * @param refundRequestPo
     * @param paymentPo
     */
    @Transactional
    public void updateRefundRequestAndPayment(RefundRequestPo refundRequestPo, PaymentPo paymentPo) {
        RefundRequestExample example = new RefundRequestExample();
        example.createCriteria().andRefundBatchNoEqualTo(refundRequestPo.getRefundBatchNo());
        refundRequestMapper.updateByExampleSelective(refundRequestPo, example);

        PaymentExample example2 = new PaymentExample();
        example2.createCriteria().andPaymentIdEqualTo(paymentPo.getPaymentId());
        paymentMapper.updateByExampleSelective(paymentPo, example2);
    }

    /**
     * 更新退款状态
     * 
     * @param refundRequestPo
     * @param paymentPo
     */
    @Transactional
    public void updateRefundRequest(RefundRequestPo refundRequestPo) {
        RefundRequestExample example = new RefundRequestExample();
        example.createCriteria().andRefundBatchNoEqualTo(refundRequestPo.getRefundBatchNo());
        refundRequestMapper.updateByExampleSelective(refundRequestPo, example);
    }

    public List<RefundRequestPo> getRefundReqestByTraceId(String traceId) {
        RefundRequestExample example = new RefundRequestExample();
        example.createCriteria().andTraceIdEqualTo(traceId);

        return refundRequestMapper.selectByExample(example);
    }

    @Transactional
    public void updateRetryCount(Integer refundId) {
        RefundRequestPo refundRequestPo = getRefundRequestByRefundId(refundId);
        Integer retryCount = refundRequestPo.getRetryCount() == null ? 1 : refundRequestPo.getRetryCount() + 1;

        RefundRequestPo record = new RefundRequestPo();
        record.setRetryCount(retryCount);
        RefundRequestExample example = new RefundRequestExample();
        example.createCriteria().andRefundIdEqualTo(refundId);
        refundRequestMapper.updateByExampleSelective(record, example);
    }

    @Transactional
    public void updateRefundBatchNoByRefundId(String refundBatchNo, Integer refundId) {
        RefundRequestPo refundRequest = new RefundRequestPo();
        refundRequest.setRefundBatchNo(refundBatchNo);
        RefundRequestExample example = new RefundRequestExample();
        example.createCriteria().andRefundIdEqualTo(refundId);

        refundRequestMapper.updateByExampleSelective(refundRequest, example);
    }

    public List<RefundRequestPo> getRefundRequestByTradeNo(String tradeNo) {
        RefundRequestExample example = new RefundRequestExample();
        example.createCriteria().andTradeNoEqualTo(tradeNo);
        return refundRequestMapper.selectByExample(example);
    }

    @Transactional
    public void softDeleteRefundRequest(Integer refundId) {
        RefundRequestPo record = new RefundRequestPo();
        record.setSoftDeleteFlag(true);
        RefundRequestExample example = new RefundRequestExample();
        example.createCriteria().andRefundIdEqualTo(refundId);
        refundRequestMapper.updateByExampleSelective(record, example);
    }
}
