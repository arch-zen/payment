/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.channel.service.acquireorder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.constants.AliPayConsts;
import com.ymatou.payment.domain.channel.model.AcquireOrderPackageResp;
import com.ymatou.payment.domain.channel.service.AcquireOrderService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.constants.AcquireOrderResultTypeEnum;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.model.TradeCreateReqDeatil;
import com.ymatou.payment.integration.model.TradeCreateRequest;
import com.ymatou.payment.integration.model.TradeCreateResponse;
import com.ymatou.payment.integration.service.alipay.TradeCreateService;

/**
 * 支付宝Wap支付
 * 
 * @author qianmin 2016年5月25日 下午6:45:58
 *
 */
@Component
public class AliPayWapAcquireOrderServiceImpl implements AcquireOrderService {

    private static final Logger logger = LoggerFactory.getLogger(AliPayWapAcquireOrderServiceImpl.class);

    @Autowired
    private InstitutionConfigManager instConfigManager;

    @Autowired
    private IntegrationConfig integrationConfig;

    @Autowired
    private TradeCreateService tradeCreateService;

    @Autowired
    private SignatureService signatureService;

    @Override
    public AcquireOrderPackageResp acquireOrderPackage(Payment payment) {
        // 获取第三方机构配置
        InstitutionConfig instConfig = instConfigManager.getConfig(payment.getPayType());

        // 调用授权接口，获取RequestToken
        String requestToken = getRequestToken(payment, instConfig);

        // 拼装请求Map
        HashMap<String, String> reqMap = buildReqMap(requestToken, instConfig);

        // 签名
        String sign = signatureService.signMessage(reqMap, instConfig, payment.getAcquireOrderReq().getMockHeader());
        reqMap.put("sign", sign);

        // 拼装请求报文
        String reqQuery = buildQuery(reqMap,
                integrationConfig.getAliPayWapUrl(payment.getAcquireOrderReq().getMockHeader()), true);

        // 返回报文结果
        AcquireOrderPackageResp resp = new AcquireOrderPackageResp();
        resp.setResultType(AcquireOrderResultTypeEnum.Query);
        resp.setResult(reqQuery);

        return resp;
    }

    /**
     * 构建支付接口的请求参数map，用于签名
     * 
     * @param payment
     * @return
     */
    private HashMap<String, String> buildReqMap(String requestToken, InstitutionConfig instConfig) {
        HashMap<String, String> map = new HashMap<>();
        map.put("req_data",
                String.format("<auth_and_execute_req><request_token>%s</request_token></auth_and_execute_req>",
                        requestToken));
        map.put("service", AliPayConsts.WAP_ACQUIRE_ORDER_SERVICE);
        map.put("sec_id", TradeCreateRequest.SEC_ID);
        map.put("partner", instConfig.getMerchantId());
        map.put("format", TradeCreateRequest.FORMAT);
        map.put("v", TradeCreateRequest.VERSION);

        return map;
    }

    /***
     * 获取支付宝wap支付的RequestToken
     * 
     * @param payment
     * @param instConfig
     * @return
     */
    private String getRequestToken(Payment payment, InstitutionConfig instConfig) {
        TradeCreateRequest request = new TradeCreateRequest();
        HashMap<String, String> header = payment.getAcquireOrderReq().getMockHeader();

        // 组织支付宝授权接口的业务参数
        BussinessOrder bussinessOrder = payment.getBussinessOrder();
        TradeCreateReqDeatil detail = new TradeCreateReqDeatil();
        detail.setCall_back_url(integrationConfig.getYmtPaymentBaseUrl() + "/callback/" + payment.getPayType());
        detail.setMerchant_url("");
        detail.setNotify_url(integrationConfig.getYmtPaymentBaseUrl() + "/notify/" + payment.getPayType());
        detail.setOut_trade_no(payment.getPaymentId());
        detail.setOut_user(String.valueOf(bussinessOrder.getUserId()));
        detail.setPay_exprie("");
        detail.setSeller_account_name(instConfig.getSellerEmail());
        detail.setSubject(StringUtils.isBlank(bussinessOrder.getProductName())
                ? (StringUtils.isBlank(bussinessOrder.getProductDesc()) ? String.valueOf(payment.getPayPrice())
                        : bussinessOrder.getProductDesc())
                : bussinessOrder.getProductName());
        detail.setTotal_fee(payment.getPayPrice().toString());

        // 组织支付宝授权接口的通用参数
        String reqData = tradeCreateService.generateReqDataXml(detail);
        request.setReq_data(reqData);
        request.setPartner(instConfig.getMerchantId());
        request.setReq_id(bussinessOrder.getTraceId());
        String sign = signForTradeCreate(request, instConfig, header); // 签名
        request.setSign(sign);

        try {
            TradeCreateResponse response = tradeCreateService.doService(request, header);
            if (StringUtils.isBlank(response.getRequestToken())) {
                throw new BizException(ErrorCode.SERVER_SIDE_ACQUIRE_ORDER_FAILED,
                        "request token is empty.");
            }
            return response.getRequestToken(); // 获取RequestToken
        } catch (Exception e) {
            logger.error("call alipay tradeCreateDirect fail", e);
            throw new BizException(ErrorCode.SERVER_SIDE_ACQUIRE_ORDER_FAILED, "call alipay tradeCreateDirect fail", e);
        }
    }

    /**
     * 支付宝授权接口(TradeCreateRedirect)签名
     * 
     * @param request
     * @param instConfig
     * @param header
     * @return
     */
    private String signForTradeCreate(TradeCreateRequest request, InstitutionConfig instConfig,
            HashMap<String, String> header) {
        HashMap<String, String> map = new HashMap<>();
        map.put("req_data", request.getReq_data());
        map.put("service", request.getService());
        map.put("sec_id", request.getSec_id());
        map.put("partner", request.getPartner());
        map.put("req_id", request.getReq_id());
        map.put("format", request.getFormat());
        map.put("v", request.getV());
        String sign = signatureService.signMessage(map, instConfig, header);
        return sign;
    }

    /***
     * 拼接请求url
     * 
     * @param params
     * @param baseUrl
     * @param ignoreEmpty
     * @return
     */
    private String buildQuery(HashMap<String, String> params, String baseUrl, boolean ignoreEmpty) {
        StringBuilder sb = new StringBuilder(baseUrl);

        if (!baseUrl.endsWith("?")) {
            sb.append("?");
        }

        try {
            String separator = StringUtils.EMPTY;
            for (Entry<String, String> entry : params.entrySet()) {
                if (ignoreEmpty && StringUtils.isBlank(entry.getValue())) {
                    continue;
                }

                sb.append(separator).append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                separator = "&";
            }
        } catch (UnsupportedEncodingException e) {
            throw new BizException(ErrorCode.UNKNOWN, "URLEncode error.", e);
        }
        return sb.toString();
    }

}
