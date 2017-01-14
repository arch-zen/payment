/**
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test.integration.service.cmb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.integration.common.CmbSignature;
import com.ymatou.payment.integration.model.CmbQuerySettledOrderRequest;
import com.ymatou.payment.integration.model.CmbQuerySettledOrderResponse;
import com.ymatou.payment.integration.model.CmbQuerySettledOrderResponse.QuerySettledOrderRspData;
import com.ymatou.payment.integration.model.CmbSettledOrderInfo;
import com.ymatou.payment.integration.service.cmb.QuerySettledOrderService;
import com.ymatou.payment.test.RestBaseTest;

/**
 * 按商户日期查询已结账订单-测试用例
 * 
 * @see http://58.61.30.110/OpenAPI2/API/IntDeclareAPI8.aspx
 * @author Administrator 2017年1月14日 上午10:46:47
 *
 */
public class QuerySettledOrderServiceTest extends RestBaseTest {
    @Resource
    private QuerySettledOrderService querySettledOrderService;

    @Resource
    private SignatureService singatureService;

    @Resource
    private InstitutionConfigManager instConfigManager;

    @Test
    public void testDoService() throws Exception {
        InstitutionConfig config = instConfigManager.getConfig(PayTypeEnum.CmbApp);
        CmbQuerySettledOrderRequest request = new CmbQuerySettledOrderRequest();
        request.getReqData().setOperatorNo(config.getOperatorNo());
        request.getReqData().setBranchNo(config.getBranchNo());
        request.getReqData().setMerchantNo(config.getMerchantId());
        request.getReqData().setBeginDate("20161201");
        request.getReqData().setEndDate("20170101");

        String sign = CmbSignature.shaSign(config.getMd5Key(), request.buildSignString());
        request.setSign(sign);

        CmbQuerySettledOrderResponse response = querySettledOrderService.doService(request, null);
        QuerySettledOrderRspData rspData = response.getRspData();

        assertNotNull(rspData);
        assertEquals("SUC0000", rspData.getRspCode());
        assertEquals("N", rspData.getHasNext());

        int dataCount = Integer.parseInt(rspData.getDataCount());
        assertEquals(true, dataCount > 0);

        String[] dataList = rspData.getDataList().split("\\\r\\\n");
        assertEquals(true, dataList.length > 0);

        assertEquals(dataCount, dataList.length - 1);

        List<CmbSettledOrderInfo> orderList = response.parseOrderList();
        assertEquals(dataCount, orderList.size());
        assertEquals("0", orderList.get(0).getOrderStatus());
        assertEquals("110.00", orderList.get(0).getOrderAmount());
    }
}
