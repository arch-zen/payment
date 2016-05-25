/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.service.alipay;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
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

import com.alibaba.fastjson.JSON;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.HttpClientUtil;
import com.ymatou.payment.integration.common.constants.Constants;
import com.ymatou.payment.integration.model.SingleTradeQueryRequest;
import com.ymatou.payment.integration.model.SingleTradeQueryResponse;

/**
 * 调用支付宝单笔交易查询接口
 * 
 * @author qianmin 2016年5月19日 上午11:29:05
 *
 */
@Component
public class SingleTradeQueryService implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(SingleTradeQueryService.class);

    private CloseableHttpClient httpClient;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private IntegrationConfig integrationConfig;

    /**
     * 支付宝单笔交易查询服务
     * 
     * @param req
     * @param header
     * @return
     * @throws Exception
     */
    public SingleTradeQueryResponse doService(SingleTradeQueryRequest req, HashMap<String, String> header)
            throws Exception {
        String url = integrationConfig.getAliPayBaseUrl(header);
        try {
            String respXmlStr = HttpClientUtil.sendPost(url, getRequestBody(req), header, httpClient);
            SingleTradeQueryResponse response = generateResponseData(respXmlStr);

            logger.info("singleTradeQueryResponse: " + JSON.toJSONString(response));
            response.setResponseOriginString(respXmlStr);
            return response;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    private List<NameValuePair> getRequestBody(SingleTradeQueryRequest req) {
        List<NameValuePair> nvp = new ArrayList<>();
        nvp.add(new BasicNameValuePair("service", req.getService()));
        nvp.add(new BasicNameValuePair("partner", req.getPartner()));
        nvp.add(new BasicNameValuePair("_input_charset", req.get_input_charset()));
        nvp.add(new BasicNameValuePair("sign", req.getSign()));
        nvp.add(new BasicNameValuePair("sign_type", req.getSign_type()));
        nvp.add(new BasicNameValuePair("trade_no", req.getTrade_no()));
        nvp.add(new BasicNameValuePair("out_trade_no", req.getOut_trade_no()));
        return nvp;
    }

    private SingleTradeQueryResponse generateResponseData(String respXmlStr) throws DocumentException, ParseException {
        SingleTradeQueryResponse response = new SingleTradeQueryResponse();
        Document document = DocumentHelper.parseText(respXmlStr);
        Element root = document.getRootElement();
        response.setIs_success(root.elementText("is_success"));

        if ("F".equals(response.getIs_success())) {// 当is_success=F时, 只有error信息
            response.setError(root.elementText("error"));
        } else {// 当is_success=T时
            Element trade = root.element("response").element("trade");
            response.setBuyer_email(trade.elementText("buyer_email"));
            response.setBuyer_id(trade.elementText("buyer_id"));
            response.setTrade_status(trade.elementText("trade_status"));
            response.setIs_total_fee_adjust(trade.elementText("is_total_fee_adjust"));
            response.setOut_trade_no(trade.elementText("out_trade_no"));
            response.setTrade_no(trade.elementText("trade_no"));
            response.setSubject(trade.elementText("subject"));
            response.setFlag_trade_locked(trade.elementText("flag_trade_locked"));
            response.setBody(trade.elementText("body"));
            response.setGmt_create(StringUtils.isBlank(trade.elementText("gmt_create"))
                    ? null : sdf.parse(trade.elementText("gmt_create")));
            response.setSeller_email(trade.elementText("seller_email"));
            response.setSeller_id(trade.elementText("seller_id"));
            response.setTotal_fee(trade.elementText("total_fee"));
            response.setPrice(trade.elementText("price"));
            response.setQuantity(trade.elementText("quantity"));
            response.setLogistics_fee(trade.elementText("logistics_fee"));
            response.setCoupon_discount(trade.elementText("coupon_discount"));
            response.setUse_coupon(trade.elementText("use_coupon"));
            response.setDiscount(trade.elementText("discount"));
            response.setRefund_status(trade.elementText("refund_status"));
            response.setLogistics_status(trade.elementText("logistics_status"));
            response.setAdditional_trade_status(trade.elementText("additional_trade_status"));
            response.setGmt_last_modified_time(StringUtils.isBlank(trade.elementText("gmt_last_modified_time"))
                    ? null : sdf.parse(trade.elementText("gmt_last_modified_time")));
            response.setGmt_payment(StringUtils.isBlank(trade.elementText("gmt_payment"))
                    ? null : sdf.parse(trade.elementText("gmt_payment")));
            response.setGmt_send_goods(StringUtils.isBlank(trade.elementText("gmt_send_goods"))
                    ? null : sdf.parse(trade.elementText("gmt_send_goods")));
            response.setGmt_refund(StringUtils.isBlank(trade.elementText("gmt_refund"))
                    ? null : sdf.parse(trade.elementText("gmt_refund")));
            response.setTime_out(StringUtils.isBlank(trade.elementText("time_out"))
                    ? null : sdf.parse(trade.elementText("time_out")));
            response.setGmt_close(StringUtils.isBlank(trade.elementText("gmt_close"))
                    ? null : sdf.parse(trade.elementText("gmt_close")));
            response.setGmt_logistics_modify(StringUtils.isBlank(trade.elementText("gmt_logistics_modify"))
                    ? null : sdf.parse(trade.elementText("gmt_logistics_modify")));
            response.setTime_out_type(trade.elementText("time_out_type"));
            response.setRefund_fee(trade.elementText("refund_fee"));
            response.setRefund_flow_type(trade.elementText("refund_flow_type"));
            response.setRefund_id(trade.elementText("refund_id"));
            response.setRefund_cash_fee(trade.elementText("refund_cash_fee"));
            response.setRefund_coupon_fee(trade.elementText("refund_coupon_fee"));
            response.setRefund_agent_pay_fee(trade.elementText("refund_agent_pay_fee"));
            response.setCoupon_used_fee(trade.elementText("coupon_used_fee"));
            response.setTo_buyer_fee(trade.elementText("to_buyer_fee"));
            response.setTo_seller_fee(trade.elementText("to_seller_fee"));
            response.setFund_bill_list(trade.elementText("fund_bill_list"));
            response.setPayment_type(trade.elementText("payment_type"));
            response.setOperator_role(trade.elementText("operator_role"));

            response.setSign(root.elementText("sign"));
            response.setSign_type(root.elementText("sign_type"));
        }
        return response;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SSLContext ctx = SSLContext.getInstance("SSL");
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
        };
        ctx.init(null, new TrustManager[] {tm}, null);

        SSLConnectionSocketFactory ssf =
                new SSLConnectionSocketFactory(ctx, SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setDefaultMaxPerRoute(Constants.DEFAULT_MAX_PER_ROUTE);
        cm.setMaxTotal(Constants.MAX_TOTAL);

        this.httpClient = HttpClientBuilder.create()
                .setSSLSocketFactory(ssf)
                .setConnectionManager(cm)
                .build();
    }
}
