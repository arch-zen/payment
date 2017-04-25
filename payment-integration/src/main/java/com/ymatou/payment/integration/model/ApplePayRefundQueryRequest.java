package com.ymatou.payment.integration.model;

import com.ymatou.payment.integration.common.utils.UrlEncoder;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxiaoming on 2017/4/24.
 */
public class ApplePayRefundQueryRequest {

    //银联全渠道系统，产品参数
    private String version = "5.0.0";
    private String encoding = "UTF-8";
    private String signMethod = "01";//固定填写01（表示采用 RSA 签名）
    private String txnType = "00";//交易类型(查询交易)
    private String txnSubType = "00";//交易子类型  默认00
    private String bizType = "000201";//业务类型, 000201：B2C 网关支付

    //商户接入参数
    private String merId;
    private String accessType = "0";//接入类型，固定填写0：商户直连接入

    //订单信息参数
    private String orderId;//商户订单号，每次发交易测试需修改为被查询的交易的订单号
    private String txnTime;//订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间


    public String getVersion() {
        return version;
    }

    public String getEncoding() {
        return encoding;
    }

    public String getSignMethod() {
        return signMethod;
    }

    public String getTxnType() {
        return txnType;
    }

    public String getTxnSubType() {
        return txnSubType;
    }

    public String getBizType() {
        return bizType;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getAccessType() {
        return accessType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }

    public HashMap<String, String> mapForSign() {
        HashMap<String, String> map = new HashMap<>();
        map.put("version", this.getVersion());
        map.put("encoding", this.getEncoding());
        map.put("signMethod", this.getSignMethod());
        map.put("txnType", this.getTxnType());
        map.put("txnSubType", this.getTxnSubType());
        map.put("bizType", this.getBizType());

        map.put("merId", this.getMerId());
        map.put("accessType", this.getAccessType());

        map.put("orderId", this.getOrderId());
        map.put("txnTime", this.getTxnTime());
        return map;
    }

    public String getRequestData(String sign) {
        HashMap<String, String> map = mapForSign();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (StringUtils.isBlank(entry.getValue()))
                continue;
            String value = UrlEncoder.encode(entry.getValue(), this.getEncoding());
            sb.append(entry.getKey()).append("=").append(value).append("&");
        }
        sb.append("signature").append("=").append(sign).append("&");

        return StringUtils.removeEnd(sb.toString(), "&");
    }
}
