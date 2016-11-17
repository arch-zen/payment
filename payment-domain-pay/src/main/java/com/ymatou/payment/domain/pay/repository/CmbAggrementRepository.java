/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.domain.pay.repository;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.constants.CmbAggrementStatusEnum;
import com.ymatou.payment.infrastructure.db.mapper.CmbAggrementMapper;
import com.ymatou.payment.infrastructure.db.model.CmbAggrementExample;
import com.ymatou.payment.infrastructure.db.model.CmbAggrementPo;
import com.ymatou.payment.integration.model.CmbSignNotifyRequest;
import com.ymatou.payment.integration.model.CmbSignNotifyRequest.SignNoticeData;

/**
 * 招行一网通签约仓储类
 * 
 * @author wangxudong 2016年11月17日 下午2:03:25
 *
 */
@Component
public class CmbAggrementRepository {

    @Resource
    private CmbAggrementMapper cmbAggrementMapper;

    /**
     * 签约
     * 
     * @param newAggrement
     * @return
     */
    public CmbAggrementPo signAggrement(long userId) {
        CmbAggrementPo aggrementPo = findInitAggrement(userId);
        if (aggrementPo == null) {
            CmbAggrementPo newAggrement = new CmbAggrementPo();
            newAggrement.setUserId(userId);
            newAggrement.setAggStatus(CmbAggrementStatusEnum.INIT.code());

            cmbAggrementMapper.insertSelective(newAggrement);
            return findInitAggrement(newAggrement.getUserId());
        } else {
            return aggrementPo;
        }
    }

    /**
     * 获取到用户初始化状态的签约记录
     * 
     * @param userId
     * @return
     */
    public CmbAggrementPo findInitAggrement(long userId) {
        CmbAggrementExample cmbAggrementExample = new CmbAggrementExample();
        cmbAggrementExample.createCriteria().andUserIdEqualTo(userId)
                .andAggStatusEqualTo(CmbAggrementStatusEnum.INIT.code());

        List<CmbAggrementPo> aggList = cmbAggrementMapper.selectByExample(cmbAggrementExample);
        if (aggList == null || aggList.size() == 0) {
            return null;
        } else {
            return aggList.get(0);
        }
    }

    /**
     * 删除用户的签约记录
     * 
     * @param userId
     */
    public void deleteByUserId(long userId) {
        CmbAggrementExample cmbAggrementExample = new CmbAggrementExample();
        cmbAggrementExample.createCriteria().andUserIdEqualTo(userId);

        cmbAggrementMapper.deleteByExample(cmbAggrementExample);
    }

    /**
     * 通知更新签约信息
     * 
     * @param cmbAggrementPo
     */
    public void notifyAggrement(CmbSignNotifyRequest cmbSignNotifyRequest) {
        SignNoticeData noticeData = cmbSignNotifyRequest.getNoticeData();
        CmbAggrementPo aggrementPo = cmbAggrementMapper.selectByPrimaryKey(Long.valueOf(noticeData.getAgrNo()));
        if (aggrementPo == null) {
            throw new BizException("update aggrement not find aggId:" + noticeData.getAgrNo());
        }

        if ("SUC0000".equals(noticeData.getRspCode())) {
            aggrementPo.setAggStatus(CmbAggrementStatusEnum.SIGN.code());
        } else {
            aggrementPo.setAggStatus(CmbAggrementStatusEnum.FAIL.code());
        }

        aggrementPo.setSignDateTime(noticeData.getDateTime());
        aggrementPo.setRespCode(noticeData.getRspCode());
        aggrementPo.setRespMessage(StringUtils.left(noticeData.getRspMsg(), 200));
        aggrementPo.setBankSerialNo(noticeData.getNoticeSerialNo());
        aggrementPo.setUserPidHash(noticeData.getUserPidHash());
        aggrementPo.setUserPidType(noticeData.getUserPidType());
        aggrementPo.setNoPwdPay(noticeData.getNoPwdPay());

        cmbAggrementMapper.updateByPrimaryKeySelective(aggrementPo);
    }
}
