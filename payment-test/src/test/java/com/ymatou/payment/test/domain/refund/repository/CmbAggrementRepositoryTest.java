/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.domain.refund.repository;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;

import com.ymatou.payment.domain.pay.repository.CmbAggrementRepository;
import com.ymatou.payment.facade.constants.CmbAggrementStatusEnum;
import com.ymatou.payment.facade.constants.CmbCancelTypeEnum;
import com.ymatou.payment.infrastructure.db.mapper.CmbAggrementMapper;
import com.ymatou.payment.infrastructure.db.model.CmbAggrementPo;
import com.ymatou.payment.integration.model.CmbSignNotifyRequest;
import com.ymatou.payment.integration.model.CmbSignNotifyRequest.SignNoticeData;
import com.ymatou.payment.test.RestBaseTest;

public class CmbAggrementRepositoryTest extends RestBaseTest {

    @Resource
    private CmbAggrementRepository cmbAggrementRepository;

    @Resource
    private CmbAggrementMapper cmbAggrementMapper;

    @Test
    public void testSignAggrement() {
        long userId = 20;

        // 删除用户的签约记录
        cmbAggrementRepository.deleteByUserId(userId);

        // 确认记录已经删除
        CmbAggrementPo findInitAggrement = cmbAggrementRepository.findInitAggrement(userId);
        assertNull(findInitAggrement);

        // 构造签约记录
        CmbAggrementPo newAggrement = cmbAggrementRepository.signAggrement(userId);
        assertNotNull(newAggrement);
        assertEquals(CmbAggrementStatusEnum.INIT.code(), newAggrement.getAggStatus());
        assertEquals(CmbCancelTypeEnum.NOCANCEL.code(), newAggrement.getCancelType());
        assertEquals(userId, newAggrement.getUserId().longValue());
        assertEquals(true, newAggrement.getAggId() > 0);

        // 再次签约验证签约号相同
        CmbAggrementPo newAggrement2 = cmbAggrementRepository.signAggrement(userId);
        assertNotNull(newAggrement2);
        assertEquals(newAggrement.getAggId(), newAggrement2.getAggId());
    }

    @Test
    public void testNotifyAggrement() {
        long userId = 20;

        // 删除用户的签约记录
        cmbAggrementRepository.deleteByUserId(userId);

        // 确认记录已经删除
        CmbAggrementPo findInitAggrement = cmbAggrementRepository.findInitAggrement(userId);
        assertNull(findInitAggrement);

        // 构造签约记录
        CmbAggrementPo newAggrement = cmbAggrementRepository.signAggrement(userId);
        assertNotNull(newAggrement);
        assertEquals(CmbAggrementStatusEnum.INIT.code(), newAggrement.getAggStatus());
        assertEquals(CmbCancelTypeEnum.NOCANCEL.code(), newAggrement.getCancelType());
        assertEquals(userId, newAggrement.getUserId().longValue());
        assertEquals(true, newAggrement.getAggId() > 0);

        // 构造签约通知
        CmbSignNotifyRequest cmbSignNotifyRequest = new CmbSignNotifyRequest();
        cmbSignNotifyRequest.getNoticeData().setAgrNo(newAggrement.getAggId().toString());
        cmbSignNotifyRequest.getNoticeData().setDateTime("20121117");
        cmbSignNotifyRequest.getNoticeData().setNoticeSerialNo(UUID.randomUUID().toString());
        cmbSignNotifyRequest.getNoticeData().setNoPwdPay("N");
        cmbSignNotifyRequest.getNoticeData().setUserPidType("1");
        cmbSignNotifyRequest.getNoticeData()
                .setUserPidHash(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
        cmbSignNotifyRequest.getNoticeData().setRspCode("SUC0000");
        cmbSignNotifyRequest.getNoticeData().setRspMsg("SUCCESS");

        SignNoticeData noticeData = cmbSignNotifyRequest.getNoticeData();
        cmbAggrementRepository.notifyAggrement(cmbSignNotifyRequest);
        CmbAggrementPo aggrementPo = cmbAggrementMapper.selectByPrimaryKey(newAggrement.getAggId());
        assertNotNull(aggrementPo);

        // 验证数据更新的结果
        assertEquals(aggrementPo.getAggStatus(), CmbAggrementStatusEnum.SIGN.code());
        assertEquals(aggrementPo.getSignDateTime(), noticeData.getDateTime());
        assertEquals(aggrementPo.getBankSerialNo(), noticeData.getNoticeSerialNo());
        assertEquals(aggrementPo.getNoPwdPay(), noticeData.getNoPwdPay());
        assertEquals(aggrementPo.getUserPidType(), noticeData.getUserPidType());
        assertEquals(aggrementPo.getUserPidHash(), noticeData.getUserPidHash());
        assertEquals(aggrementPo.getRespCode(), noticeData.getRspCode());
        assertEquals(aggrementPo.getRespMessage(), noticeData.getRspMsg());

        // 构造签约失败
        cmbSignNotifyRequest.getNoticeData().setRspCode("ERROR");
        cmbSignNotifyRequest.getNoticeData().setRspMsg("ERROR");
        cmbAggrementRepository.notifyAggrement(cmbSignNotifyRequest);

        aggrementPo = cmbAggrementMapper.selectByPrimaryKey(newAggrement.getAggId());
        assertEquals(aggrementPo.getAggStatus(), CmbAggrementStatusEnum.FAIL.code());
        assertEquals(aggrementPo.getRespCode(), noticeData.getRspCode());
        assertEquals(aggrementPo.getRespMessage(), noticeData.getRspMsg());

    }
}
