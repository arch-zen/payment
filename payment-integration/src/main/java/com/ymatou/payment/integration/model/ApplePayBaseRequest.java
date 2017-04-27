package com.ymatou.payment.integration.model;

import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.PrintFriendliness;
import com.ymatou.payment.integration.service.applepay.common.ApplePayConstants;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

/**
 * Created by gejianhua on 2017/4/26.
 * applypay请求公共信息
 */
public class ApplePayBaseRequest extends PrintFriendliness{

    ///////////////基本信息////////////////////////
    /**
     * 版本号
     */
    private String version = ApplePayConstants.version;
    /**
     * 编码方式
     */
    private String encoding = ApplePayConstants.encoding;
    /**
     * 证书ID
     */
    private String certId;
    /**
     * 签名方法
     */
    private String signMethod = ApplePayConstants.signMethod;

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


    /////////////商户基本信息/////////////////////////////////////////////////

    /**
     * 接入类型
     */
    private  String accessType;

    /**
     * 商户代码
     */
    private String merId;



    public String getVersion() {
        return version;
    }

    private void setVersion(String version) {
        this.version = version;
    }

    public String getEncoding() {
        return encoding;
    }

    private void setEncoding(String encoding) {
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

    private void setSignMethod(String signMethod) {
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

    /**
     * 实体类生成map
     * @return
     */
    public Map<String, String> genMap(){
        try {
            Map<String, String> map = BeanUtils.describe(this);
            if(map.containsKey("class")){
                map.remove("class");
            }
            return map;

        }catch(Exception ex){
            throw new BizException(this.getClass().getSimpleName() + ".genMap exception", ex);
        }
    }
}
















































