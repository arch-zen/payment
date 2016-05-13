/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.repository;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.refund.constants.ApproveStatusEnum;
import com.ymatou.payment.domain.refund.constants.RefundStatusEnum;
import com.ymatou.payment.domain.refund.model.Refund;
import com.ymatou.payment.infrastructure.db.mapper.CompensateprocessinfoMapper;
import com.ymatou.payment.infrastructure.db.mapper.RefundrequestMapper;
import com.ymatou.payment.infrastructure.db.model.PpCompensateprocessinfoWithBLOBs;
import com.ymatou.payment.infrastructure.db.model.RefundrequestExample;
import com.ymatou.payment.infrastructure.db.model.RefundrequestPo;
import com.ymatou.payment.infrastructure.db.model.RefundrequestWithBLOBs;

/**
 * 
 * @author qianmin 2016年5月10日 下午4:56:52
 *
 */
@Component
public class RefundPository {

    @Autowired
    private CompensateprocessinfoMapper compensateprocessinfoMapper;

    @Autowired
    private RefundrequestMapper refundrequestMapper;

    @Transactional
    public void addRefundrequestAndCompensateprocessinfo(Payment payment, BussinessOrder bussinessorder,
            Refund refundInfo) {
        RefundrequestWithBLOBs refundrequest = new RefundrequestWithBLOBs();
        refundrequest.setPaymentid(payment.getPaymentid());
        refundrequest.setTradeno(bussinessorder.getOrderid());
        refundrequest.setOrderid(refundInfo.getOrderIdList().get(0));
        refundrequest.setTraceid(refundInfo.getTraceId());
        refundrequest.setAppid(refundInfo.getAppId());
        refundrequest.setPaytype(payment.getPaytype());
        refundrequest.setRefundamount(payment.getPayprice());
        refundrequest.setCurrencytype(payment.getPaycurrencytype());
        refundrequest.setApprovestatus(ApproveStatusEnum.FAST_REFUND.getCode());
        refundrequest.setApprovedtime(new Date());
        refundrequest.setApproveduser("system");
        refundrequest.setRefundstatus(RefundStatusEnum.INIT.getCode());
        refundrequest.setTradetype(refundInfo.getTradeType());

        PpCompensateprocessinfoWithBLOBs compensateprocessinfo = new PpCompensateprocessinfoWithBLOBs();
        compensateprocessinfo.setCorrelateid(payment.getPaymentid());
        compensateprocessinfo.setAppid(refundInfo.getAppId());
        compensateprocessinfo.setPaytype(null);
        compensateprocessinfo.setMethodname("Refund");
        compensateprocessinfo.setRequesturl("");
        compensateprocessinfo.setRequestdata(payment.getPaymentid());
        compensateprocessinfo.setProcessmachinename(null);
        compensateprocessinfo.setLastprocessedtime(null);
        compensateprocessinfo.setCompensatetype(1); // 退款

        compensateprocessinfoMapper.insertSelective(compensateprocessinfo);
        refundrequestMapper.insertSelective(refundrequest);
    }

    /**
     * 根据paymentId获取Refundrequest
     * 
     * @param paymentId
     * @return
     */
    public RefundrequestPo getRefundRequestByPaymentId(String paymentId) {
        RefundrequestExample example = new RefundrequestExample();
        example.createCriteria().andPaymentidEqualTo(paymentId);
        List<RefundrequestPo> pos = refundrequestMapper.selectByExample(example);
        if (pos == null || pos.size() == 0) {
            return null;
        }
        return pos.get(0);
    }

    @Transactional
    public void batchSaveRefundRequest(List<RefundrequestWithBLOBs> refundrequestWithBLOBs) {
        for (RefundrequestWithBLOBs refundrequest : refundrequestWithBLOBs) {
            refundrequestMapper.insertSelective(refundrequest);
        }
    }
}
