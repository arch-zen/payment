package com.ymatou.payment.domain.pay.model;

import java.math.BigDecimal;
import java.util.Date;

import com.ymatou.payment.facade.PrintFriendliness;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.infrastructure.db.model.PaymentPo;

/**
 * 支付单
 * 
 * @author wangxudong
 *
 */
public class Payment extends PrintFriendliness {

    private static final long serialVersionUID = 2524661669277166299L;

    private BussinessOrder bussinessOrder;
    private AcquireOrderReq acquireOrderReq;
    private String paymentId;
    private String bussinessOrderId;
    private String institutionPaymentId;
    
    //FIXME: 领域层，应该已经是枚举了
    private String payType;
    private BigDecimal payPrice;
    private BigDecimal actualPayPrice;
    private String payCurrencyType;
    private String actualPayCurrencyType;
    private Double exchangeRate;
    private String bankId;
    private Integer cardType;
    private String payerId;
    
    //FIXME: 领域模型不需要 
    private Date createdTime;
    
    //FIXME：领域层，应该已经是枚举了
    private Integer payStatus;
    private Date payTime;
    
    //FIXME: 领域模型不需要
    private Date lastUpdatedTime;
    
     //FIXME：领域层，应该已经是枚举了
    private Integer checkStatus;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getBussinessOrderId() {
        return bussinessOrderId;
    }

    public void setBussinessOrderId(String bussinessOrderId) {
        this.bussinessOrderId = bussinessOrderId;
    }

    public String getInstitutionPaymentId() {
        return institutionPaymentId;
    }

    public void setInstitutionPaymentId(String institutionPaymentId) {
        this.institutionPaymentId = institutionPaymentId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public BigDecimal getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(BigDecimal payPrice) {
        this.payPrice = payPrice;
    }

    public BigDecimal getActualPayPrice() {
        return actualPayPrice;
    }

    public void setActualPayPrice(BigDecimal actualPayPrice) {
        this.actualPayPrice = actualPayPrice;
    }

    public String getPayCurrencyType() {
        return payCurrencyType;
    }

    public void setPayCurrencyType(String payCurrencyType) {
        this.payCurrencyType = payCurrencyType;
    }

    public String getActualPayCurrencyType() {
        return actualPayCurrencyType;
    }

    public void setActualPayCurrencyType(String actualPayCurrencyType) {
        this.actualPayCurrencyType = actualPayCurrencyType;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public BussinessOrder getBussinessOrder() {
        return bussinessOrder;
    }

    public void setBussinessOrder(BussinessOrder bussinessOrder) {
        this.bussinessOrder = bussinessOrder;
    }

    public AcquireOrderReq getAcquireOrderReq() {
        return acquireOrderReq;
    }

    public void setAcquireOrderReq(AcquireOrderReq acquireOrderReq) {
        this.acquireOrderReq = acquireOrderReq;
    }

    //FIXME: needless, PrintFriendliness已实现
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", paymentId=").append(paymentId);
        sb.append(", bussinessOrderId=").append(bussinessOrderId);
        sb.append(", institutionPaymentId=").append(institutionPaymentId);
        sb.append(", payType=").append(payType);
        sb.append(", payPrice=").append(payPrice);
        sb.append(", actualPayPrice=").append(actualPayPrice);
        sb.append(", payCurrencyType=").append(payCurrencyType);
        sb.append(", actualPayCurrencyType=").append(actualPayCurrencyType);
        sb.append(", exchangeRate=").append(exchangeRate);
        sb.append(", bankId=").append(bankId);
        sb.append(", cardType=").append(cardType);
        sb.append(", payerId=").append(payerId);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", payStatus=").append(payStatus);
        sb.append(", payTime=").append(payTime);
        sb.append(", lastUpdatedTime=").append(lastUpdatedTime);
        sb.append(", checkStatus=").append(checkStatus);
        sb.append("]");
        return sb.toString();
    }

    /**
     * 从PO转换成model
     * 
     * @param po
     * @return
     */
    public static Payment convertFromPo(PaymentPo po) {
        if (po == null) {
            return null;
        }

        Payment model = new Payment();
        model.setPaymentId(po.getPaymentId());
        model.setBussinessOrderId(po.getBussinessOrderId());
        model.setInstitutionPaymentId(po.getInstitutionPaymentId());
        model.setPayType(po.getPayType());
        model.setPayPrice(po.getPayPrice());
        model.setPayCurrencyType(po.getPayCurrencyType());
        model.setActualPayPrice(po.getActualPayPrice());
        model.setActualPayCurrencyType(po.getActualPayCurrencyType());
        model.setExchangeRate(po.getExchangeRate());
        model.setBankId(po.getBankId());
        model.setCardType(po.getCardType());
        model.setPayerId(po.getPayerId());
        model.setCreatedTime(po.getCreatedTime());
        model.setPayStatus(po.getPayStatus());
        model.setPayTime(po.getPayTime());
        model.setLastUpdatedTime(po.getLastUpdatedTime());
        model.setCheckStatus(po.getCheckStatus());

        return model;
    }
}
