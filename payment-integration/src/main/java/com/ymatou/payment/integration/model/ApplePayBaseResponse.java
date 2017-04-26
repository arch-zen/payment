package com.ymatou.payment.integration.model;

import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.PrintFriendliness;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

/**
 * Created by gejianhua on 2017/4/26.
 * applypay响应公共信息
 */
public class ApplePayBaseResponse extends PrintFriendliness {

    ///////////////基本信息////////////////////////
    /**
     * 版本号
     */
    private String version;
    /**
     * 编码方式
     */
    private String encoding;
    /**
     * 证书ID
     */
    private String certId;
    /**
     * 签名方法
     */
    private String signMethod;

    /**
     * 签名
     */
    private String signature;
    /**
     * 交易类型
     */
    private String txnType;

    /**
     * 交易子类
     */
    private String txnSubType;

    /**
     * 产品类型
     */
    private String bizType;
    /**
     * 签名公钥证书
     */
    private String signPubKeyCert;



    /////////商户信息/////////////////////////////
    /**
     * 接入类型
     */
    private String accessType;

    /**
     * 商户代码
     */
    private String merId;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getSignMethod() {
        return signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getTxnSubType() {
        return txnSubType;
    }

    public void setTxnSubType(String txnSubType) {
        this.txnSubType = txnSubType;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getSignPubKeyCert() {
        return signPubKeyCert;
    }

    public void setSignPubKeyCert(String signPubKeyCert) {
        this.signPubKeyCert = signPubKeyCert;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }


    ///////////附加信息////////////////////////////
    /**
     * 直接从响应报文生成的map
     */
    private Map<String, String> originMap;


    public Map<String, String> getOriginMap() {
        return originMap;
    }

    public void setOriginMap(Map<String, String> originMap) {
        this.originMap = originMap;
    }


    public void loadProperty(Map<String, String> map) {
        try {
            BeanUtils.populate(this, map);
            this.originMap = map;
        } catch (Exception ex) {
            throw new BizException(this.getClass().getSimpleName() + ".loadProperty exception", ex);
        }
    }

    public static <T extends ApplePayBaseResponse> T loadProperty(Map<String, String> map, Class<T> tClass){
        try {
            T response = tClass.newInstance();
            BeanUtils.populate(response, map);
            response.setOriginMap(map);
            return response;
        } catch (Exception ex) {
            throw new BizException(tClass.getSimpleName() + ".loadProperty exception", ex);
        }
    }
}














































