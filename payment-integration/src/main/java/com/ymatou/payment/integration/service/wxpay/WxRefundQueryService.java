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
import com.ymatou.payment.integration.model.CouponRefundData;
import com.ymatou.payment.integration.model.QueryRefundRequest;
import com.ymatou.payment.integration.model.QueryRefundResponse;
import com.ymatou.payment.integration.model.RefundOrderData;

/**
 * 微信支付退款查询
 * 
 * @author qianmin 2016年5月9日 上午10:42:18
 *
 */
@Component
public class WxRefundQueryService implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(WxRefundQueryService.class);

    private CloseableHttpClient httpClient;

    @Autowired
    private IntegrationConfig integrationConfig;

    /**
     * 微信支付退款查询服务
     * 
     * @param request
     * @param header
     * @return
     * @throws Exception
     */
    public QueryRefundResponse doService(QueryRefundRequest request, HashMap<String, String> header)
            throws Exception {
        try {
            // 根据mchId获取不同的加签盐值和httpClient(不同商户证书及密码不同)
            String respXmlStr = HttpClientUtil.sendPost(integrationConfig.getWxRefundQueryUrl(header),
                    getPostDataXml(request), Constants.CONTENT_TYPE_XML, header, httpClient);

            if (!StringUtils.isEmpty(respXmlStr) && respXmlStr.startsWith(Constants.WEIXIN_RESPONSE_BODY_START)) {
                Map<String, Object> respMap = XmlParser.getMapFromXML(respXmlStr);
                QueryRefundResponse response = generateResponseData(respMap);
                response.setOriginalResponse(respXmlStr);
                return response;
            }
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    private QueryRefundResponse generateResponseData(Map<String, Object> responseMap) {
        QueryRefundResponse response = new QueryRefundResponse();
        response.setReturn_code((String) responseMap.get("return_code"));
        response.setReturn_msg((String) responseMap.get("return_msg"));

        if ("SUCCESS".equals(response.getReturn_code())) {
            // 以下字段在return_code为SUCCESS的时候有返回
            response.setResult_code((String) responseMap.get("result_code"));
            response.setErr_code((String) responseMap.get("err_code"));
            response.setErr_code_des((String) responseMap.get("err_code_des"));
            response.setAppid((String) responseMap.get("appid"));
            response.setMch_id((String) responseMap.get("mch_id"));
            response.setDevice_info((String) responseMap.get("device_info"));
            response.setNonce_str((String) responseMap.get("nonce_str"));
            response.setSign((String) responseMap.get("sign"));
            response.setTransaction_id((String) responseMap.get("transaction_id"));
            response.setOut_trade_no((String) responseMap.get("out_trade_no"));
            response.setTotal_fee(NumberUtils.toInt((String) responseMap.get("total_fee")));
            response.setFee_type((String) responseMap.get("fee_type"));
            response.setCash_fee(NumberUtils.toInt((String) responseMap.get("cash_fee")));
            response.setRefund_count(NumberUtils.toInt((String) responseMap.get("refund_count")));

            List<RefundOrderData> refundOrderDataList = new ArrayList<RefundOrderData>();
            for (int index = 0; index < response.getRefund_count(); ++index) {
                RefundOrderData refundOrderData = new RefundOrderData();
                refundOrderData.setOutRefundNo((String) responseMap.get("out_refund_no_" + index));
                refundOrderData.setRefundID((String) responseMap.get("refund_id_" + index));
                refundOrderData.setRefundChannel((String) responseMap.get("refund_channel_" + index));
                refundOrderData.setRefundFee(NumberUtils.toInt((String) responseMap.get("refund_fee_" + index)));
                refundOrderData
                        .setCouponRefundFee(NumberUtils.toInt((String) responseMap.get("coupon_refund_fee_" + index)));
                refundOrderData.setRefundStatus((String) responseMap.get("refund_status_" + index));
                refundOrderData
                        .setCouponRefundCount(
                                NumberUtils.toInt((String) responseMap.get("coupon_refund_count_" + index)));

                List<CouponRefundData> couponRefundDataList = new ArrayList<>();
                for (int loop = 0; loop < refundOrderData.getCouponRefundCount(); ++loop) {
                    CouponRefundData couponRefundData = new CouponRefundData();
                    couponRefundData.setCouponRefundBatchId(
                            (String) responseMap.get("coupon_refund_batch_id_" + index + "_" + loop));
                    couponRefundData
                            .setCouponRefundId((String) responseMap.get("coupon_refund_id_" + index + "_" + loop));
                    couponRefundData.setCouponRefundFee(
                            NumberUtils.toInt((String) responseMap.get("coupon_refund_fee_" + index + "_" + loop)));
                    couponRefundDataList.add(couponRefundData);
                }
                refundOrderData.setCouponRefundData(couponRefundDataList);

                refundOrderDataList.add(refundOrderData);
            }
            response.setRefundOrderDataList(refundOrderDataList);
        }

        return response;
    }

    private String getPostDataXml(QueryRefundRequest request) {
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8",
                new XmlFriendlyNameCoder("-_", "_")));// 解决XStream对出现双下划线的bug
        String postDataXML = xStreamForRequestPostData.toXML(request);
        postDataXML = postDataXML.replace("com.ymatou.payment.integration.model.QueryRefundRequest", "xml");
        return postDataXML;
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
