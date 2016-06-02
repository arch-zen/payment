/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.service.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.HttpClientUtil;
import com.ymatou.payment.integration.common.constants.Constants;
import com.ymatou.payment.integration.model.CreateTradeErrorDeatil;
import com.ymatou.payment.integration.model.CreateTradeReqDeatil;
import com.ymatou.payment.integration.model.CreateTradeRequest;
import com.ymatou.payment.integration.model.CreateTradeResponse;

/**
 * 支付宝wap即时到账接口
 * 
 * @author qianmin 2016年5月25日 下午2:12:54
 *
 */
@Component
public class TradeCreateService implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(TradeCreateService.class);

    private CloseableHttpClient httpClient;

    @Autowired
    private IntegrationConfig integrationConfig;

    /**
     * 用于调用授权接口，获取RequestToken
     * 
     * @param request
     * @param header
     * @return
     * @throws Exception
     */
    public CreateTradeResponse doService(CreateTradeRequest request, HashMap<String, String> header) throws Exception {
        String url = integrationConfig.getAliPayWapUrl(header);

        try {
            List<NameValuePair> requestEntity = generateRequest(request);

            String respStr = HttpClientUtil.sendPost(url, requestEntity, header, httpClient);

            CreateTradeResponse response = generateResponse(respStr);
            logger.info("trade create response: {}", JSONObject.toJSONString(response));
            return response;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }


    private CreateTradeResponse generateResponse(String respStr)
            throws DocumentException, UnsupportedEncodingException {
        CreateTradeResponse response = new CreateTradeResponse();
        String[] respArr = respStr.split("&");
        HashMap<String, String> map = new HashMap<>();
        for (String str : respArr) {
            if (str.startsWith("res_data")) {
                String resData = URLDecoder.decode(str.substring(str.indexOf("=") + 1), "utf-8");
                Document document = DocumentHelper.parseText(resData);
                String requestToken = document.getRootElement().elementText("request_token");

                response.setRes_data(resData);
                response.setRequestToken(requestToken);
            } else if (str.startsWith("res_error")) {
                String error = URLDecoder.decode(str.substring(str.indexOf("=") + 1), "utf-8");
                Document document = DocumentHelper.parseText(error);
                Element root = document.getRootElement();
                CreateTradeErrorDeatil errorDetail = new CreateTradeErrorDeatil();
                errorDetail.setCode(root.elementText("code"));
                errorDetail.setDetail(root.elementText("detail"));
                errorDetail.setMsg(root.elementText("msg"));
                errorDetail.setSubCode(root.elementText("sub_code"));

                response.setError(error);
                response.setErrorDetail(errorDetail);
            } else {
                String[] item = str.split("=");
                map.put(item[0], item[1]);
            }
        }

        response.setPartner(map.get("partner"));
        response.setReq_id(map.get("req_id"));
        response.setSec_id(map.get("sec_id"));
        response.setService(map.get("service"));
        response.setSign(map.get("sign"));
        response.setV(map.get("v"));

        return response;
    }


    private List<NameValuePair> generateRequest(CreateTradeRequest request) {
        List<NameValuePair> nvpList = new ArrayList<>();
        nvpList.add(new BasicNameValuePair("service", request.getService()));
        nvpList.add(new BasicNameValuePair("partner", request.getPartner()));
        nvpList.add(new BasicNameValuePair("req_id", request.getReq_id()));
        nvpList.add(new BasicNameValuePair("sec_id", request.getSec_id()));
        nvpList.add(new BasicNameValuePair("sign", request.getSign()));
        nvpList.add(new BasicNameValuePair("req_data", request.getReq_data()));
        nvpList.add(new BasicNameValuePair("format", request.getFormat()));
        nvpList.add(new BasicNameValuePair("v", request.getV()));
        return nvpList;
    }

    /**
     * 获取req_data的xml格式字符串
     * 
     */
    public String generateReqDataXml(String subject, String outTradeNo, String totalFee, String sellerAccountName,
            String callBackUrl, String notifyUrl, String outUser, String merchant_url, String payExpire) {
        CreateTradeReqDeatil detail = new CreateTradeReqDeatil();
        detail.setSubject(subject);
        detail.setOut_trade_no(outTradeNo);
        detail.setTotal_fee(totalFee);
        detail.setSeller_account_name(sellerAccountName);
        detail.setCall_back_url(callBackUrl);
        detail.setNotify_url(notifyUrl);
        detail.setOut_user(outUser);
        detail.setPay_exprie(payExpire);

        return generateReqDataXml(detail);
    }

    /**
     * 获取req_data的xml格式字符串
     * 
     * @param reqData
     * @return
     */
    public String generateReqDataXml(CreateTradeReqDeatil reqData) {
        Element root = DocumentHelper.createElement("direct_trade_create_req");
        Document document = DocumentHelper.createDocument(root);
        root.addElement("subject").addText(reqData.getSubject());
        root.addElement("out_trade_no").addText(reqData.getOut_trade_no());
        root.addElement("total_fee").addText(reqData.getTotal_fee());
        root.addElement("seller_account_name").addText(reqData.getSeller_account_name());

        if (!StringUtils.isBlank(reqData.getCall_back_url())) {
            root.addElement("call_back_url").addText(reqData.getCall_back_url());
        }
        if (!StringUtils.isBlank(reqData.getNotify_url())) {
            root.addElement("notify_url").addText(reqData.getNotify_url());
        }
        if (!StringUtils.isBlank(reqData.getNotify_url())) {
            root.addElement("out_user").addText(reqData.getOut_user());
        }
        if (!StringUtils.isBlank(reqData.getMerchant_url())) {
            root.addElement("merchant_url").addText(reqData.getMerchant_url());
        }
        if (!StringUtils.isBlank(reqData.getPay_exprie())) {
            root.addElement("pay_expire").addText(reqData.getPay_exprie());
        }
        return document.getRootElement().asXML();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setDefaultMaxPerRoute(Constants.DEFAULT_MAX_PER_ROUTE);
        cm.setMaxTotal(Constants.MAX_TOTAL);

        this.httpClient = HttpClientBuilder.create()
                .setConnectionManager(cm)
                .build();
    }
}
