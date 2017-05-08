package com.ymatou.payment.domain.refund.model;

import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;
import com.ymatou.payment.facade.PrintFriendliness;
import com.ymatou.payment.facade.model.RefundNotifyRequest;
import com.ymatou.payment.integration.service.applepay.common.ApplePayConstants;
import com.ymatou.payment.integration.service.applepay.common.ApplePayMessageUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxiaoming on 2017/4/27.
 * https://open.unionpay.com/ajweb/product/detail?id=80  [退货]-商户通知
 */
public class ApplePayRefundNotifyRequest extends PrintFriendliness {

    // region 基本信息
    private String version;
    private String encoding;
    private String signMethod;
    private String signature;
    private String txnType;
    private String txnSubType;
    private String bizType;
    private String signPubKeyCert;

    // endregion

    // region 商户信息
    private String accessType;
    private String merId;

    // endregion

    // region 订单信息
    private String orderId;
    private String currencyCode;
    private String txnAmt;
    private String txnTime;
    private String reqReserved;
    private String reserved;
    // endregion

    // region 通知信息
    private String queryId;
    private String origQryId;
    private String traceNo;
    private String traceTime;
    private String settleDate;
    private String settleCurrencyCode;
    private String settleAmt;
    private String exchangeRate;
    private String exchangeDate;
    private String respCode;
    private String respMsg;
    // endregion

    // region 内部信息
    private String payType;
    private HashMap<String, String> mockHeader;
    private Map<String, String> rawMap;
    // endregion

    // region get/set

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getTxnAmt() {
        return txnAmt;
    }

    public void setTxnAmt(String txnAmt) {
        this.txnAmt = txnAmt;
    }

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }

    public String getReqReserved() {
        return reqReserved;
    }

    public void setReqReserved(String reqReserved) {
        this.reqReserved = reqReserved;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getOrigQryId() {
        return origQryId;
    }

    public void setOrigQryId(String origQryId) {
        this.origQryId = origQryId;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getTraceTime() {
        return traceTime;
    }

    public void setTraceTime(String traceTime) {
        this.traceTime = traceTime;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    public String getSettleCurrencyCode() {
        return settleCurrencyCode;
    }

    public void setSettleCurrencyCode(String settleCurrencyCode) {
        this.settleCurrencyCode = settleCurrencyCode;
    }

    public String getSettleAmt() {
        return settleAmt;
    }

    public void setSettleAmt(String settleAmt) {
        this.settleAmt = settleAmt;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(String exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public HashMap<String, String> getMockHeader() {
        return mockHeader;
    }

    public void setMockHeader(HashMap<String, String> mockHeader) {
        this.mockHeader = mockHeader;
    }

    public Map<String, String> getRawMap() {
        return rawMap;
    }

    public void setRawMap(Map<String, String> rawMap) {
        this.rawMap = rawMap;
    }

    // endregion

    // region 构造方法
    public ApplePayRefundNotifyRequest() {

    }

    public ApplePayRefundNotifyRequest(RefundNotifyRequest request) {
        this.setPayType(request.getPayType());
        this.setMockHeader(request.getMockHeader());
        parseFromBody(request.getBody());
    }
    // endregion

    // region 私有方法

    /**
     * 从Form中解析出报文
     *
     * @param body
     * @return
     */
    private void parseFromBody(String body) {
        try {

            Map<String, String> map = ApplePayMessageUtil.genResponseMessage(body);
            Map<String, String> deCodeMap = new HashMap<>();
            for (Map.Entry<String, String> item : map.entrySet()) {
                if (StringUtils.isBlank(item.getValue())) {
                    deCodeMap.put(item.getKey(), item.getValue());
                } else {
                    String value = URLDecoder.decode(item.getValue(), ApplePayConstants.encoding);
                    deCodeMap.put(item.getKey(), value);
                }
            }
            this.setRawMap(deCodeMap);
            BeanUtils.populate(this, deCodeMap);
        } catch (Exception e) {
            throw new BizException(ErrorCode.FAIL,
                    String.format("parse post form when receive applePay refund notify.payType=%s,body=%s", payType, body), e);
        }
    }
    // endregion
}
