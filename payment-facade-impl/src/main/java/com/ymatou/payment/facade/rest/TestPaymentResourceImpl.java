/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.rest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.pay.repository.CmbAggrementRepository;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.facade.model.AcquireOrderResp;
import com.ymatou.payment.facade.model.CmbCancelAggrementReq;
import com.ymatou.payment.infrastructure.db.model.CmbAggrementPo;
import com.ymatou.payment.integration.common.CmbSignature;
import com.ymatou.payment.integration.model.CmbAggrementCancelRequest;
import com.ymatou.payment.integration.model.CmbAggrementCancelResponse;
import com.ymatou.payment.integration.service.cmb.AggrementCancelService;

/**
 * 
 * @author qianmin 2016年6月17日 下午4:29:58
 *
 */
@Component("testPaymentResource")
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class TestPaymentResourceImpl implements TestPaymentResource {


    @Resource
    private PaymentResource paymentResource;

    @Resource
    private InstitutionConfigManager instConfigManager;

    @Resource
    private CmbAggrementRepository cmbAggrementRepository;

    @Resource
    private AggrementCancelService aggrementCancelService;

    @Override
    @GET
    @Path("/alipc")
    public String alipc(@Context HttpServletRequest servletRequest) {
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);

        req.setPayType(PayTypeEnum.AliPayPc.getCode());
        req.setPayPrice("0.02");

        AcquireOrderResp res = paymentResource.acquireOrder(req, servletRequest);

        return res.getResult();
    }

    @Override
    @GET
    @Path("/aliwap")
    public Response aliwap(@Context HttpServletRequest servletRequest) {
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);

        req.setPayType(PayTypeEnum.AliPayWap.getCode());
        req.setPayPrice("0.02");

        AcquireOrderResp res = paymentResource.acquireOrder(req, servletRequest);

        return Response.status(Status.FOUND).header("location", res.getResult()).build();
    }

    @GET
    @Path("/wxpc")
    public Response wxpc(@Context HttpServletRequest servletRequest) {
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);

        req.setPayType(PayTypeEnum.WeiXinPc.getCode());
        req.setPayPrice("0.02");

        AcquireOrderResp res = paymentResource.acquireOrder(req, servletRequest);

        return Response.status(Status.OK).entity(res.getResult()).build();
    }

    @Override
    @GET
    @Path("/cmbpay")
    public String cmbpay(@Context HttpServletRequest servletRequest) {
        AcquireOrderReq req = new AcquireOrderReq();
        buildBaseRequest(req);

        req.setPayType(PayTypeEnum.CmbApp.getCode());
        req.setPayPrice("0.01");

        AcquireOrderResp res = paymentResource.acquireOrder(req, servletRequest);

        return res.getResult();
    }

    /**
     * 构造请求报文
     * 
     * @param req
     */
    private void buildBaseRequest(AcquireOrderReq req) {
        req.setVersion(1);
        req.setBizCode(3);
        req.setOriginAppId("JavaTest");
        req.setAppId("AutoTest");
        req.setCallbackUrl("http://www.ymatou.com/pay/result");
        req.setTraceId(UUID.randomUUID().toString());
        req.setCurrency("CNY");
        req.setEncoding(65001);
        req.setNotifyUrl("http://api.trading.operate.ymatou.com/api/Trading/TradingCompletedNotify");
        req.setOrderId(getDateFormatString("yyyyMMddHHmmssSSS"));
        req.setOrderTime(getDateFormatString("yyyyMMddHHmmss"));
        req.setPayPrice("0.01");
        req.setPayType("10");
        req.setProductName("灯叔的订单");
        req.setProductDesc("");
        req.setProductUrl("www.ymatou.com");
        req.setMemo("备注");
        req.setSignMethod("MD5");
        req.setExt("{\"SHOWMODE\":\"2\",\"PAYMETHOD\":\"2\", \"IsHangZhou\":0}");
        req.setUserId(12345L);
        req.setUserIp("127.0.0.1");
        // req.setBankId("ICBCB2C");
    }

    /**
     * 构造日期字符串
     * 
     * @return
     */
    private String getDateFormatString(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(new Date());
    }

    @Override
    @POST
    @Path("/cmbCancelAggrement")
    @Consumes({"application/json; charset=UTF-8"})
    public String cmbCancelAggrement(CmbCancelAggrementReq req, @Context HttpServletRequest servletRequest) {
        CmbAggrementPo aggrement = cmbAggrementRepository.findSignAggrement(req.getUserId());
        if (aggrement == null) {
            return "not find aggment by userid:" + req.getUserId();
        }

        try {
            InstitutionConfig config = instConfigManager.getConfig(PayTypeEnum.CmbApp);
            CmbAggrementCancelRequest request = new CmbAggrementCancelRequest();
            request.getReqData().setBranchNo(config.getBranchNo());
            request.getReqData().setMerchantNo(config.getMerchantId());
            request.getReqData().setAgrNo(aggrement.getAggId().toString());
            request.getReqData().setMerchantSerialNo(String.valueOf(System.currentTimeMillis()));

            String sign = CmbSignature.shaSign(config.getMd5Key(), request.buildSignString());
            request.setSign(sign);

            CmbAggrementCancelResponse response = aggrementCancelService.doService(request, null);

            // 删除用户的签约协议
            cmbAggrementRepository.deleteByUserId(req.getUserId());

            return response.getRspData().getRspCode();
        } catch (Exception e) {

            return "exception:" + e.getMessage();
        }
    }
}
