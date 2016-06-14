/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.repository;

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
import com.ymatou.payment.facade.constants.ChannelTypeEnum;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.constants.RefundStatusEnum;
import com.ymatou.payment.infrastructure.db.mapper.CompensateProcessInfoMapper;
import com.ymatou.payment.infrastructure.db.mapper.PaymentMapper;
import com.ymatou.payment.infrastructure.db.mapper.RefundMiscRequestLogMapper;
import com.ymatou.payment.infrastructure.db.mapper.RefundRequestMapper;
import com.ymatou.payment.infrastructure.db.model.CompensateProcessInfoPo;
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
    private CompensateProcessInfoMapper compensateProcessInfoMapper;

    @Autowired
    private RefundRequestMapper refundRequestMapper;

    @Autowired
    private RefundMiscRequestLogMapper refundMiscRequestLogMapper;

    @Autowired
    private PaymentMapper paymentMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Autowired
    private SqlSession sqlSession;

    @Transactional
    public void addFastRefundInfo(Payment payment, BussinessOrder bussinessorder,
            Refund refundInfo) {
        RefundRequestPo refundrequest = new RefundRequestPo();
        refundrequest.setPaymentId(payment.getPaymentId());
        refundrequest.setInstPaymentId(payment.getInstitutionPaymentId());
        refundrequest.setTradeNo(bussinessorder.getOrderId());
        refundrequest.setOrderId(refundInfo.getOrderIdList().get(0));
        refundrequest.setTraceId(refundInfo.getTraceId());
        refundrequest.setAppId(refundInfo.getAppId());
        refundrequest.setPayType(payment.getPayType().getCode());
        refundrequest.setRefundAmount(payment.getPayPrice().getAmount());
        refundrequest.setCurrencyType(payment.getPayCurrencyType());
        refundrequest.setApproveStatus(ApproveStatusEnum.FAST_REFUND.getCode());
        refundrequest.setApprovedTime(new Date());
        refundrequest.setApprovedUser("system");
        refundrequest.setRefundStatus(RefundStatusEnum.INIT.getCode());
        refundrequest.setTradeType(refundInfo.getTradeType());
        refundrequest.setRefundBatchNo(generateRefundBatchNo());
    }

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
    public void addRefundrequestAndCompensateprocessinfo(Payment payment, BussinessOrder bussinessorder,
            Refund refundInfo) {
        RefundRequestPo refundrequest = new RefundRequestPo();

        refundrequest.setPaymentId(payment.getPaymentId());
        refundrequest.setInstPaymentId(payment.getInstitutionPaymentId());
        refundrequest.setTradeNo(bussinessorder.getOrderId());


        refundrequest.setOrderId(refundInfo.getOrderIdList().get(0));
        refundrequest.setTraceId(refundInfo.getTraceId());
        refundrequest.setAppId(refundInfo.getAppId());
        refundrequest.setPayType(payment.getPayType().getCode());
        refundrequest.setRefundAmount(payment.getPayPrice().getAmount());
        refundrequest.setCurrencyType(payment.getPayCurrencyType());
        refundrequest.setApproveStatus(ApproveStatusEnum.FAST_REFUND.getCode());
        refundrequest.setApprovedTime(new Date());
        refundrequest.setApprovedUser("system");
        refundrequest.setRefundStatus(RefundStatusEnum.INIT.getCode());
        refundrequest.setTradeType(refundInfo.getTradeType());

        CompensateProcessInfoPo compensateprocessinfo = new CompensateProcessInfoPo();

        compensateprocessinfo.setCorrelateId(payment.getPaymentId());
        compensateprocessinfo.setAppId("1");
        compensateprocessinfo.setPayType(null);
        compensateprocessinfo.setMethodName("Refund");
        compensateprocessinfo.setRequestUrl("");
        compensateprocessinfo.setRequestData(payment.getPaymentId());
        compensateprocessinfo.setProcessMachineName(null);
        compensateprocessinfo.setLastProcessedTime(null);
        compensateprocessinfo.setCompensateType(1); // 退款

        compensateProcessInfoMapper.insertSelective(compensateprocessinfo);
        refundRequestMapper.insertSelective(refundrequest);
    }

    /**
     * 根据paymentId获取Refundrequest TODO
     * 
     * @param paymentId
     * @return
     */
    public RefundRequestPo getRefundRequestByPaymentId(String paymentId) {
        return refundRequestMapper.selectByPrimaryKey(paymentId);
    }

    /**
     * 根据refundNo获取Refundrequest
     * 
     * @param paymentId
     * @return
     */
    public RefundRequestPo getRefundRequestByRefundNo(String refundNo) {
        RefundRequestExample example = new RefundRequestExample();
        example.createCriteria().andRefundBatchNoEqualTo(refundNo);
        List<RefundRequestPo> result = refundRequestMapper.selectByExample(example);
        if (result != null && result.size() > 0) {
            return result.get(0); // 有唯一约束
        } else {
            return null;
        }
    }

    /**
     * 保存RefundRequest
     * 
     * @param refundrequestWithBLOBs
     */
    @Transactional
    public void batchSaveRefundRequest(List<RefundRequestPo> refundrequestWithBLOBs) {
        for (RefundRequestPo refundrequest : refundrequestWithBLOBs) {
            refundRequestMapper.insertSelective(refundrequest);
        }
    }

    /**
     * 
     * 更新RequestRequest，保存Compensateprocessinfo
     * 
     * @param refundrequestPos
     * @param compensateprocessinfoWithBLOBs
     */
    @Transactional
    public void updateRefundRequestAndSaveCompensateprocessinfo(List<RefundRequestPo> refundrequestPos,
            List<CompensateProcessInfoPo> compensateprocessinfoWithBLOBs) {
        for (RefundRequestPo refundrequestPo : refundrequestPos) {
            RefundRequestExample example = new RefundRequestExample();
            example.createCriteria().andPaymentIdEqualTo(refundrequestPo.getPaymentId());
            refundRequestMapper.updateByExample(refundrequestPo, example);
        }
        for (CompensateProcessInfoPo info : compensateprocessinfoWithBLOBs) {
            compensateProcessInfoMapper.insertSelective(info);
        }
    }

    /**
     * 更新RequestRequest
     * 
     * @param refundrequestPos
     * @param compensateprocessinfoWithBLOBs
     */
    @Transactional
    public void updateRefundRequest(List<RefundRequestPo> refundrequestPos) {
        for (RefundRequestPo refundrequestPo : refundrequestPos) {
            RefundRequestExample example = new RefundRequestExample();
            example.createCriteria().andPaymentIdEqualTo(refundrequestPo.getPaymentId());
            refundRequestMapper.updateByExample(refundrequestPo, example);
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

    public String convertPayTypeToPayChannel(String payType) {

        if (payType.equals(PayTypeEnum.AliPayApp.getCode()) || payType.equals(PayTypeEnum.AliPayPc.getCode())
                || payType.equals(PayTypeEnum.AliPayWap.getCode())) {
            return ChannelTypeEnum.AliPay.getCode();
        } else {
            return ChannelTypeEnum.WeiXinPay.getCode();
        }
    }

    /**
     * 通过RefundBatchNo查询RefundRequest
     * 
     * @param batchNo
     * @return
     */
    public List<RefundRequestPo> queryRefundRequestByRefundBatchNo(String batchNo) {
        RefundRequestExample example = new RefundRequestExample();
        example.createCriteria().andRefundBatchNoEqualTo(batchNo);
        List<RefundRequestPo> pos = refundRequestMapper.selectByExample(example);

        return pos;
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
    public void updateRefundRequestAccoutingStatus(String refundNo, int accoutingStatus) {
        RefundRequestExample example = new RefundRequestExample();
        example.createCriteria().andRefundBatchNoEqualTo(refundNo);
        RefundRequestPo record = new RefundRequestPo();
        record.setRefundBatchNo(refundNo);
        record.setAccoutingStatus(accoutingStatus);
        refundRequestMapper.updateByExample(record, example);
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
        refundRequestMapper.updateByExample(refundRequestPo, example);

        PaymentExample example2 = new PaymentExample();
        example2.createCriteria().andPaymentIdEqualTo(paymentPo.getPaymentId());
        paymentMapper.updateByExample(paymentPo, example2);
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
        refundRequestMapper.updateByExample(refundRequestPo, example);
    }
}
