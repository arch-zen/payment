package com.ymatou.payment.domain.pay.model;

import java.math.BigDecimal;
import java.util.Date;

import com.ymatou.payment.facade.PrintFriendliness;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.model.AcquireOrderReq;
import com.ymatou.payment.infrastructure.Money;
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

    private PayTypeEnum payType;
    private Money payPrice;
    private Money actualPayPrice;
    private String payCurrencyType;
    private String actualPayCurrencyType;
    private Double exchangeRate;
    private String bankId;
    private Integer cardType;
    private String payerId;
    private PayStatusEnum payStatus;
    private Date payTime;
    private BigDecimal refundAmt;
    private BigDecimal completedRefundAmt;

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

    public PayTypeEnum getPayType() {
        return payType;
    }

    public void setPayType(PayTypeEnum payType) {
        this.payType = payType;
    }

    public Money getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Money payPrice) {
        this.payPrice = payPrice;
    }

    public Money getActualPayPrice() {
        return actualPayPrice;
    }

    public void setActualPayPrice(Money actualPayPrice) {
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

    public PayStatusEnum getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(PayStatusEnum payStatus) {
        this.payStatus = payStatus;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
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

    public BigDecimal getRefundAmt() {
        return refundAmt;
    }

    public void setRefundAmt(BigDecimal refundAmt) {
        this.refundAmt = refundAmt;
    }

    public BigDecimal getCompletedRefundAmt() {
        return completedRefundAmt;
    }

    public void setCompletedRefundAmt(BigDecimal completedRefundAmt) {
        this.completedRefundAmt = completedRefundAmt;
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
        model.setPayType(PayTypeEnum.parse(po.getPayType()));
        model.setPayPrice(parseToMoney(po.getPayPrice()));
        model.setPayCurrencyType(po.getPayCurrencyType());
        model.setActualPayPrice(parseToMoney(po.getActualPayPrice()));
        model.setActualPayCurrencyType(po.getActualPayCurrencyType());
        model.setExchangeRate(po.getExchangeRate());
        model.setBankId(po.getBankId());
        model.setCardType(po.getCardType());
        model.setPayerId(po.getPayerId());
        model.setPayStatus(PayStatusEnum.parse(po.getPayStatus()));
        model.setPayTime(po.getPayTime());
        model.setCheckStatus(po.getCheckStatus());
        model.setRefundAmt(new BigDecimal("0.00")); // TODO
        model.setCompletedRefundAmt(new BigDecimal("0.00"));// TODO

        return model;
    }

    public static Money parseToMoney(BigDecimal amount) {
        if (amount == null) {
            return null;
        } else {
            return new Money(amount);
        }
    }
}
