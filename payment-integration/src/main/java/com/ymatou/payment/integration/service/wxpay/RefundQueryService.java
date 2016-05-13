/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.service.wxpay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.HttpClientUtil;
import com.ymatou.payment.integration.common.XmlParser;
import com.ymatou.payment.integration.model.CouponRefundData;
import com.ymatou.payment.integration.model.RefundOrderData;
import com.ymatou.payment.integration.model.RefundQueryRequest;
import com.ymatou.payment.integration.model.RefundQueryResponse;

/**
 * 微信支付退款查询
 * 
 * @author qianmin 2016年5月9日 上午10:42:18
 *
 */
@Component
public class RefundQueryService {

    private static Logger logger = LoggerFactory.getLogger(RefundQueryService.class);

    private CloseableHttpClient jsapiHttpClient;
    private CloseableHttpClient appHttpClient;
    private volatile boolean isInit = false;

    @Autowired
    private IntegrationConfig integrationConfig;

    /**
     * 微信支付退款查询服务
     * 
     * @param request
     * @param header
     * @return RefundQueryResponse
     * @throws ParserConfigurationException
     * @throws KeyManagementException
     * @throws UnrecoverableKeyException
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws SAXException
     * @throws IntegerationException
     */
    public RefundQueryResponse doService(RefundQueryRequest request, HashMap<String, String> header)
            throws Exception {
        try {
            // 根据mchId获取不同的加签盐值和httpClient(不同商户证书及密码不同)
            String mchId = request.getMch_id();

            if (header == null) {
                header = new HashMap<String, String>();
            }
            header.put("Content-Type", "text/xml");

            String respXmlStr = HttpClientUtil.sendPost(integrationConfig.getWxRefundQueryUrl(header),
                    getPostDataXml(request), header, getHttpClient(mchId));
            if (!StringUtils.isEmpty(respXmlStr)) {
                Map<String, Object> respMap = XmlParser.getMapFromXML(respXmlStr);
                RefundQueryResponse response = generateResponseData(respMap);
                return response;
            }
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    private RefundQueryResponse generateResponseData(Map<String, Object> responseMap) {
        RefundQueryResponse response = new RefundQueryResponse();
        response.setReturn_code((String) responseMap.get("return_code"));
        response.setReturn_msg((String) responseMap.get("return_msg"));

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
            refundOrderData.setRefundFee(Integer.valueOf((String) responseMap.get("refund_fee_" + index)));
            refundOrderData.setCouponRefundFee(Integer.valueOf((String) responseMap.get("coupon_refund_fee_" + index)));
            refundOrderData.setRefundStatus((String) responseMap.get("refund_status_" + index));
            refundOrderData
                    .setCouponRefundCount(NumberUtils.toInt((String) responseMap.get("coupon_refund_count_" + index)));

            List<CouponRefundData> couponRefundDataList = new ArrayList<>();
            for (int loop = 0; loop < refundOrderData.getCouponRefundCount(); ++loop) {
                CouponRefundData couponRefundData = new CouponRefundData();
                couponRefundData.setCouponRefundBatchId(
                        (String) responseMap.get("coupon_refund_batch_id_" + index + "_" + loop));
                couponRefundData.setCouponRefundId((String) responseMap.get("coupon_refund_id_" + index + "_" + loop));
                couponRefundData.setCouponRefundFee(
                        NumberUtils.toInt((String) responseMap.get("coupon_refund_fee_" + index + "_" + loop)));
                couponRefundDataList.add(couponRefundData);
            }
            refundOrderData.setCouponRefundData(couponRefundDataList);

            refundOrderDataList.add(refundOrderData);
        }
        response.setRefundOrderDataList(refundOrderDataList);

        return response;
    }

    private String getPostDataXml(RefundQueryRequest request) {
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8",
                new XmlFriendlyNameCoder("-_", "_")));// 解决XStream对出现双下划线的bug
        String postDataXML = xStreamForRequestPostData.toXML(request);
        postDataXML = postDataXML.replace("com.ymatou.payment.integration.model.RefundQueryRequest", "xml");
        return postDataXML;
    }

    private CloseableHttpClient getHttpClient(String mchId) throws KeyManagementException, UnrecoverableKeyException,
            KeyStoreException, FileNotFoundException, NoSuchAlgorithmException, CertificateException, IOException {
        if (!isInit) {
            this.jsapiHttpClient =
                    initHttpClient(integrationConfig.getWxJsapiCertPath(), integrationConfig.getWxJsapiCertPass());
            this.appHttpClient =
                    initHttpClient(integrationConfig.getWxAppCertPath(), integrationConfig.getWxAppCertPass());
            isInit = true;
        }
        if (mchId.equals(integrationConfig.getWxAppMchId())) {
            return appHttpClient;
        } else {
            return jsapiHttpClient;
        }
    }

    public CloseableHttpClient initHttpClient(String certLocalPath, String certPassWord)
            throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException,
            CertificateException, KeyManagementException, UnrecoverableKeyException {

        // Load keyStore
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream instream = new FileInputStream(new File(certLocalPath));) {
            keyStore.load(instream, certPassWord.toCharArray());// 设置证书密码
        }
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, certPassWord.toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] {"TLSv1"},
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        return HttpClientBuilder.create().setSSLSocketFactory(sslsf).build();
    }
}
