/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.service.wxpay;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.HttpClientUtil;
import com.ymatou.payment.integration.common.XmlParser;
import com.ymatou.payment.integration.common.constants.Constants;
import com.ymatou.payment.integration.model.CouponData;
import com.ymatou.payment.integration.model.QueryOrderRequest;
import com.ymatou.payment.integration.model.QueryOrderResponse;

/**
 * 微信支付查询订单
 * 
 * @author qianmin 2016年5月19日 下午2:14:12
 *
 */
@Component
public class OrderQueryService implements InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(OrderQueryService.class);

    private CloseableHttpClient httpClient;

    @Autowired
    private IntegrationConfig integrationConfig;

    /**
     * 微信支付查询订单
     * 
     * @param request
     * @param header
     * @return
     * @throws Exception
     */
    public QueryOrderResponse doService(QueryOrderRequest request, HashMap<String, String> header)
            throws Exception {
        try {
            String respXmlStr = HttpClientUtil.sendPost(integrationConfig.getWxOrderQueryUrl(header),
                    getPostDataXml(request), Constants.CONTENT_TYPE_XML, header, httpClient);
            if (!StringUtils.isEmpty(respXmlStr) && respXmlStr.startsWith(Constants.WEIXIN_RESPONSE_BODY_START)) {
                Map<String, Object> respMap = XmlParser.getMapFromXML(respXmlStr);
                QueryOrderResponse response = generateResponseData(respMap);
                response.setResponseOriginString(respXmlStr); // 返回原始应答
                return response;
            }
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    private String getPostDataXml(QueryOrderRequest request) {
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8",
                new XmlFriendlyNameCoder("-_", "_"))); // 解决XStream对出现双下划线的bug
        String postDataXML = xStreamForRequestPostData.toXML(request);
        postDataXML = postDataXML.replace("com.ymatou.payment.integration.model.QueryOrderRequest", "xml");
        return postDataXML;
    }

    private QueryOrderResponse generateResponseData(Map<String, Object> responseMap) {
        QueryOrderResponse response = new QueryOrderResponse();

        response.setReturn_code((String) responseMap.get("return_code"));
        response.setReturn_msg((String) responseMap.get("return_msg"));

        if ("SUCCESS".equals(response.getReturn_code())) {
            // 以下字段在return_code为SUCCESS的时候有返回
            response.setAppid((String) responseMap.get("appid"));
            response.setMch_id((String) responseMap.get("mch_id"));
            response.setNonce_str((String) responseMap.get("nonce_str"));
            response.setSign((String) responseMap.get("sign"));
            response.setResult_code((String) responseMap.get("result_code"));
            response.setErr_code((String) responseMap.get("err_code"));
            response.setErr_code_des((String) responseMap.get("err_code_des"));

            if ("SUCCESS".equals(response.getResult_code())) {
                // 以下字段在return_code 和result_code都为SUCCESS的时候有返回
                response.setDevice_info((String) responseMap.get("device_info"));
                response.setOpenid((String) responseMap.get("openid"));
                response.setIs_subscribe((String) responseMap.get("is_subscribe"));
                response.setTrade_type((String) responseMap.get("trade_type"));
                response.setTrade_state((String) responseMap.get("trade_state"));
                response.setBank_type((String) responseMap.get("bank_type"));
                response.setTotal_fee(NumberUtils.toInt((String) responseMap.get("total_fee")));
                response.setSettlement_total_fee(NumberUtils.toInt((String) responseMap.get("settlement_total_fee")));
                response.setFee_type((String) responseMap.get("fee_type"));
                response.setCash_fee(NumberUtils.toInt((String) responseMap.get("cash_fee")));
                response.setCash_fee_type((String) responseMap.get("cash_fee_type"));
                response.setCoupon_fee(NumberUtils.toInt((String) responseMap.get("coupon_fee")));
                response.setCoupon_count(NumberUtils.toInt((String) responseMap.get("coupon_count")));
                List<CouponData> couponDatas = new ArrayList<CouponData>(response.getCoupon_count());
                for (int index = 0; index < response.getCoupon_count(); ++index) {
                    CouponData couponData = new CouponData();
                    couponData.setCoupon_batch_id((String) responseMap.get("coupon_batch_id_" + index));
                    couponData.setCoupon_fee(NumberUtils.toInt((String) responseMap.get("coupon_fee_" + index)));
                    couponData.setCoupon_id((String) responseMap.get("coupon_id_" + index));
                    couponData.setCoupon_type((String) responseMap.get("coupon_type_" + index));
                    couponDatas.add(couponData);
                }
                response.setCouponDatas(couponDatas);
                response.setTransaction_id((String) responseMap.get("transaction_id"));
                response.setOut_trade_no((String) responseMap.get("out_trade_no"));
                response.setAttach((String) responseMap.get("attach"));
                response.setTime_end((String) responseMap.get("time_end"));
                response.setTrade_state_desc((String) responseMap.get("trade_state_desc"));
            }
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

        SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(ctx,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setDefaultMaxPerRoute(Constants.DEFAULT_MAX_PER_ROUTE);
        cm.setMaxTotal(Constants.MAX_TOTAL);

        this.httpClient = HttpClientBuilder.create()
                .setSSLSocketFactory(ssf)
                .setConnectionManager(cm)
                .build();
    }
}
