/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ymatou.payment.domain.channel.constants.ChannelTypeEnum;
import com.ymatou.payment.domain.channel.model.enums.PayTypeEnum;
import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.refund.constants.ApproveStatusEnum;
import com.ymatou.payment.domain.refund.constants.RefundStatusEnum;
import com.ymatou.payment.domain.refund.model.Refund;
import com.ymatou.payment.infrastructure.db.mapper.CompensateProcessInfoMapper;
import com.ymatou.payment.infrastructure.db.mapper.RefundMiscRequestLogMapper;
import com.ymatou.payment.infrastructure.db.mapper.RefundRequestMapper;
import com.ymatou.payment.infrastructure.db.model.CompensateProcessInfoPo;
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
    private SqlSession sqlSession;

    @Transactional
    public void addRefundrequestAndCompensateprocessinfo(Payment payment, BussinessOrder bussinessorder,
            Refund refundInfo) {
        RefundRequestPo refundrequest = new RefundRequestPo();
        refundrequest.setPaymentId(payment.getPaymentid());
        refundrequest.setTradeNo(bussinessorder.getOrderid());
        refundrequest.setOrderId(refundInfo.getOrderIdList().get(0));
        refundrequest.setTraceId(refundInfo.getTraceId());
        refundrequest.setAppId(refundInfo.getAppId());
        refundrequest.setPayType(payment.getPaytype());
        refundrequest.setRefundAmount(payment.getPayprice());
        refundrequest.setCurrencyType(payment.getPaycurrencytype());
        refundrequest.setApproveStatus(ApproveStatusEnum.FAST_REFUND.getCode());
        refundrequest.setApprovedTime(new Date());
        refundrequest.setApprovedUser("system");
        refundrequest.setRefundStatus(RefundStatusEnum.INIT.getCode());
        refundrequest.setTradeType(refundInfo.getTradeType());

        CompensateProcessInfoPo compensateprocessinfo = new CompensateProcessInfoPo();
        compensateprocessinfo.setCorrelateId(payment.getPaymentid());
        compensateprocessinfo.setAppId(refundInfo.getAppId());
        compensateprocessinfo.setPayType(null);
        compensateprocessinfo.setMethodName("Refund");
        compensateprocessinfo.setRequestUrl("");
        compensateprocessinfo.setRequestData(payment.getPaymentid());
        compensateprocessinfo.setProcessMachineName(null);
        compensateprocessinfo.setLastProcessedTime(null);
        compensateprocessinfo.setCompensateType(1); // 退款

        compensateProcessInfoMapper.insertSelective(compensateprocessinfo);
        refundRequestMapper.insertSelective(refundrequest);
    }

    /**
     * 根据paymentId获取Refundrequest
     * 
     * @param paymentId
     * @return
     */
    public RefundRequestPo getRefundRequestByPaymentId(String paymentId) {
        return refundRequestMapper.selectByPrimaryKey(paymentId);
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
     * 查询退款单
     * 
     * @param query
     * @return
     */
    public List<RefundRequestPo> queryRefundRequest(HashMap<String, Object> query) {
        return sqlSession.selectList("ext-refundrequest.selectRefundrequestByPage", query);
    }

    public String convertPayTypeToPayChannel(String payType) {

        if (payType.equals(PayTypeEnum.AliPayApp.getCode()) || payType.equals(PayTypeEnum.AliPayPc.getCode())) {
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
}
