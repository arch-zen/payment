package com.ymatou.payment.domain.pay.model;

import java.math.BigDecimal;
import java.util.Date;

import com.ymatou.payment.facade.PrintFriendliness;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.facade.constants.PaymentNotifyStatusEnum;
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

    /**
     * 商户订单
     */
    private BussinessOrder bussinessOrder;
    /**
     * 支付单Id
     */
    private String paymentId;
    /**
     * 商户订单Id
     */
    private String bussinessOrderId;
    /**
     * 第三方支付单Id
     */
    private String institutionPaymentId;
    /**
     * 支付类型
     */
    private PayTypeEnum payType;
    /**
     * 支付金额
     */
    private Money payPrice;
    /**
     * 实际支付金额
     */
    private Money actualPayPrice;
    /**
     * 支付货币
     */
    private String payCurrencyType;
    /**
     * 实际支付货币
     */
    private String actualPayCurrencyType;
    /**
     * 汇率
     */
    private Double exchangeRate;
    /**
     * 银行Id
     */
    private String bankId;
    /**
     * 银行卡Id
     */
    private Integer cardType;
    /**
     * 第三方支付者Id
     */
    private String payerId;

    /**
     * 第三方支付者Email
     */
    private String payerEmail;
    /**
     * 支付状态枚举
     */
    private PayStatusEnum payStatus;
    /**
     * 支付时间
     */
    private Date payTime;
    /**
     * 退款金额
     */
    private BigDecimal refundAmt;
    /**
     * 完成退款金额
     */
    private BigDecimal completedRefundAmt;

    /**
     * 第三方优惠金额
     */
    private Money discountAmt;
    /**
     * 对账状态
     */
    private Integer checkStatus;
    /**
     * 通知状态
     */
    private PaymentNotifyStatusEnum notifyStatus;
    /**
     * 重试次数
     */
    private Integer retryCount;

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
        model.setRefundAmt(po.getRefundAmt());
        model.setCompletedRefundAmt(po.getCompletedRefundAmt());
        model.setRetryCount(po.getRetryCount());
        model.setDiscountAmt(parseToMoney(po.getDiscountAmt()));

        if (po.getNotifyStatus() != null)
            model.setNotifyStatus(PaymentNotifyStatusEnum.parse(po.getNotifyStatus()));

        return model;
    }

    public static Money parseToMoney(BigDecimal amount) {
        if (amount == null) {
            return null;
        } else {
            return new Money(amount);
        }
    }

    /**
     * @return the notifyStatus
     */
    public PaymentNotifyStatusEnum getNotifyStatus() {
        return notifyStatus;
    }

    /**
     * @param notifyStatus the notifyStatus to set
     */
    public void setNotifyStatus(PaymentNotifyStatusEnum notifyStatus) {
        this.notifyStatus = notifyStatus;
    }

    /**
     * @return the retryCount
     */
    public Integer getRetryCount() {
        return retryCount;
    }

    /**
     * @param retryCount the retryCount to set
     */
    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    /**
     * @return the discountAmt
     */
    public Money getDiscountAmt() {
        if (discountAmt == null) {
            return new Money(0);
        } else {
            return discountAmt;
        }
    }

    /**
     * @param discountAmt the discountAmt to set
     */
    public void setDiscountAmt(Money discountAmt) {
        this.discountAmt = discountAmt;
    }
}
