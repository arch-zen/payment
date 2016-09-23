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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.common.HttpClientUtil;
import com.ymatou.payment.integration.common.XmlParser;
import com.ymatou.payment.integration.common.constants.Constants;
import com.ymatou.payment.integration.model.WxCouponData;
import com.ymatou.payment.integration.model.WxCouponRefundData;
import com.ymatou.payment.integration.model.WxRefundRequest;
import com.ymatou.payment.integration.model.WxRefundResponse;

/**
 * 微信退款申请
 * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_4
 * 
 * @author qianmin 2016年5月30日 下午2:09:08
 *
 */
@Component
public class WxRefundService {

    private CloseableHttpClient jsapiHttpClient;
    private CloseableHttpClient appHttpClient;
    private volatile boolean isInit = false;

    @Autowired
    private IntegrationConfig integrationConfig;

    public WxRefundResponse doService(WxRefundRequest request, HashMap<String, String> header) throws Exception {
        // 根据mchId获取不同的加签盐值和httpClient(不同商户证书及密码不同)
        String mchId = request.getMch_id();
        String respXmlStr = HttpClientUtil.sendPost(integrationConfig.getWxRefundUrl(header),
                getPostDataXml(request), Constants.CONTENT_TYPE_XML, header, getHttpClient(mchId));

        if (!StringUtils.isEmpty(respXmlStr) && respXmlStr.startsWith(Constants.WEIXIN_RESPONSE_BODY_START)) {
            Map<String, Object> respMap = XmlParser.getMapFromXML(respXmlStr);
            WxRefundResponse response = generateResponseData(respMap);
            response.setOriginalResponse(respXmlStr);
            return response;
        }
        return null;
    }

    private WxRefundResponse generateResponseData(Map<String, Object> responseMap) {
        WxRefundResponse response = new WxRefundResponse();
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
            response.setOut_refund_no((String) responseMap.get("out_refund_no"));
            response.setRefund_id((String) responseMap.get("refund_id"));
            response.setRefund_channel((String) responseMap.get("refund_channel"));
            response.setRefund_fee(NumberUtils.toInt((String) responseMap.get("refund_fee")));
            response.setTotal_fee(NumberUtils.toInt((String) responseMap.get("total_fee")));
            response.setSettlement_total_fee(NumberUtils.toInt((String) responseMap.get("settlement_total_fee")));
            response.setFee_type((String) responseMap.get("fee_type"));
            response.setCash_fee(NumberUtils.toInt((String) responseMap.get("cash_fee")));
            response.setCash_refund_fee(NumberUtils.toInt((String) responseMap.get("cash_refund_fee")));

            List<WxCouponData> wxCouponDatas = new ArrayList<WxCouponData>();
            int index = 0;
            while (true) {
                String coupon_type = (String) responseMap.get("coupon_type_" + index);

                if (!StringUtils.isBlank(coupon_type)) { // 若couponTypeN有值时
                    WxCouponData data = new WxCouponData();
                    data.setCoupon_type(coupon_type);
                    data.setSettlement_refund_fee(
                            NumberUtils.toInt((String) responseMap.get("settlement_refund_fee_" + index)));
                    data.setCoupon_refund_fee(
                            NumberUtils.toInt((String) responseMap.get("coupon_refund_fee_" + index)));
                    int couponRefundCount = NumberUtils.toInt((String) responseMap.get("coupon_refund_count_" + index));
                    data.setCoupon_refund_count(couponRefundCount);

                    if (couponRefundCount > 0) { // 当退款代金券使用数量大于0
                        List<WxCouponRefundData> refundDatas = new ArrayList<>();
                        for (int loop = 0; loop < couponRefundCount; ++loop) {
                            WxCouponRefundData refundData = new WxCouponRefundData();
                            refundData.setCoupon_refund_batch_id(
                                    (String) responseMap.get("coupon_refund_batch_id_" + index + "_" + loop));
                            refundData.setCoupon_refund_id(
                                    (String) responseMap.get("coupon_refund_id_" + index + "_" + loop));
                            refundData.setCoupon_refund_fee(NumberUtils.toInt(
                                    (String) responseMap.get("coupon_refund_fee_" + index + "_" + loop)));
                            refundDatas.add(refundData);
                        }
                        data.setCouponRefundDatas(refundDatas);
                    }

                    wxCouponDatas.add(data);
                } else {
                    break;
                }
                index++;
            }
            response.setCouponDatas(wxCouponDatas);
        }

        return response;
    }

    private String getPostDataXml(WxRefundRequest request) {
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8",
                new XmlFriendlyNameCoder("-_", "_")));// 解决XStream对出现双下划线的bug
        String postDataXML = xStreamForRequestPostData.toXML(request);
        postDataXML = postDataXML.replace("com.ymatou.payment.integration.model.WxRefundRequest", "xml");
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
        // Create a registry of custom connection socket factories for supported
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", sslsf)
                .build();
        // Create a connection manager
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setDefaultMaxPerRoute(Constants.DEFAULT_MAX_PER_ROUTE);
        cm.setMaxTotal(Constants.MAX_TOTAL);

        return HttpClientBuilder.create()
                .setSSLSocketFactory(sslsf)
                .setConnectionManager(cm)
                .build();
    }
}
