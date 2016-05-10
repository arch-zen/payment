package com.ymatou.payment.infrastructure.db.model;

import java.math.BigDecimal;
import java.util.Date;

public class RefundrequestPo {
    /**
     * VARCHAR(32) 必填<br>
     * 
     */
    private String paymentid;

    /**
     * VARCHAR(64) 必填<br>
     * 
     */
    private String tradeno;

    /**
     * VARCHAR(64)<br>
     * 
     */
    private String orderid;

    /**
     * VARCHAR(32) 必填<br>
     * 
     */
    private String traceid;

    /**
     * VARCHAR(16) 必填<br>
     * 
     */
    private String appid;

    /**
     * VARCHAR(16) 必填<br>
     * 
     */
    private String paytype;

    /**
     * DECIMAL(18,4) 必填<br>
     * 
     */
    private BigDecimal refundamount;

    /**
     * VARCHAR(8) 必填<br>
     * 
     */
    private String currencytype;

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     * 
     */
    private Integer approvestatus;

    /**
     * TIMESTAMP(23,3)<br>
     * 
     */
    private Date approvedtime;

    /**
     * BIT(1) 默认值[((0))] 必填<br>
     * 
     */
    private Boolean softdeleteflag;

    /**
     * TIMESTAMP(23,3) 默认值[(getdate())] 必填<br>
     * 
     */
    private Date createdtime;

    /**
     * VARCHAR(128)<br>
     * 
     */
    private String approveduser;

    /**
     * INTEGER(10) 默认值[((0))]<br>
     * 
     */
    private Integer refundstatus;

    /**
     * TIMESTAMP(23,3)<br>
     * 
     */
    private Date refundtime;

    /**
     * VARCHAR(32)<br>
     * 
     */
    private String refundbatchno;

    /**
     * VARCHAR(64)<br>
     * 
     */
    private String instpaymentid;

    /**
     * INTEGER(10)<br>
     * 
     */
    private Integer tradetype;

    /**
     * VARCHAR(32) 必填<br>
     */
    public String getPaymentid() {
        return paymentid;
    }

    /**
     * VARCHAR(32) 必填<br>
     */
    public void setPaymentid(String paymentid) {
        this.paymentid = paymentid == null ? null : paymentid.trim();
    }

    /**
     * VARCHAR(64) 必填<br>
     */
    public String getTradeno() {
        return tradeno;
    }

    /**
     * VARCHAR(64) 必填<br>
     */
    public void setTradeno(String tradeno) {
        this.tradeno = tradeno == null ? null : tradeno.trim();
    }

    /**
     * VARCHAR(64)<br>
     */
    public String getOrderid() {
        return orderid;
    }

    /**
     * VARCHAR(64)<br>
     */
    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    /**
     * VARCHAR(32) 必填<br>
     */
    public String getTraceid() {
        return traceid;
    }

    /**
     * VARCHAR(32) 必填<br>
     */
    public void setTraceid(String traceid) {
        this.traceid = traceid == null ? null : traceid.trim();
    }

    /**
     * VARCHAR(16) 必填<br>
     */
    public String getAppid() {
        return appid;
    }

    /**
     * VARCHAR(16) 必填<br>
     */
    public void setAppid(String appid) {
        this.appid = appid == null ? null : appid.trim();
    }

    /**
     * VARCHAR(16) 必填<br>
     */
    public String getPaytype() {
        return paytype;
    }

    /**
     * VARCHAR(16) 必填<br>
     */
    public void setPaytype(String paytype) {
        this.paytype = paytype == null ? null : paytype.trim();
    }

    /**
     * DECIMAL(18,4) 必填<br>
     */
    public BigDecimal getRefundamount() {
        return refundamount;
    }

    /**
     * DECIMAL(18,4) 必填<br>
     */
    public void setRefundamount(BigDecimal refundamount) {
        this.refundamount = refundamount;
    }

    /**
     * VARCHAR(8) 必填<br>
     */
    public String getCurrencytype() {
        return currencytype;
    }

    /**
     * VARCHAR(8) 必填<br>
     */
    public void setCurrencytype(String currencytype) {
        this.currencytype = currencytype == null ? null : currencytype.trim();
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public Integer getApprovestatus() {
        return approvestatus;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public void setApprovestatus(Integer approvestatus) {
        this.approvestatus = approvestatus;
    }

    /**
     * TIMESTAMP(23,3)<br>
     */
    public Date getApprovedtime() {
        return approvedtime;
    }

    /**
     * TIMESTAMP(23,3)<br>
     */
    public void setApprovedtime(Date approvedtime) {
        this.approvedtime = approvedtime;
    }

    /**
     * BIT(1) 默认值[((0))] 必填<br>
     */
    public Boolean getSoftdeleteflag() {
        return softdeleteflag;
    }

    /**
     * BIT(1) 默认值[((0))] 必填<br>
     */
    public void setSoftdeleteflag(Boolean softdeleteflag) {
        this.softdeleteflag = softdeleteflag;
    }

    /**
     * TIMESTAMP(23,3) 默认值[(getdate())] 必填<br>
     */
    public Date getCreatedtime() {
        return createdtime;
    }

    /**
     * TIMESTAMP(23,3) 默认值[(getdate())] 必填<br>
     */
    public void setCreatedtime(Date createdtime) {
        this.createdtime = createdtime;
    }

    /**
     * VARCHAR(128)<br>
     */
    public String getApproveduser() {
        return approveduser;
    }

    /**
     * VARCHAR(128)<br>
     */
    public void setApproveduser(String approveduser) {
        this.approveduser = approveduser == null ? null : approveduser.trim();
    }

    /**
     * INTEGER(10) 默认值[((0))]<br>
     */
    public Integer getRefundstatus() {
        return refundstatus;
    }

    /**
     * INTEGER(10) 默认值[((0))]<br>
     */
    public void setRefundstatus(Integer refundstatus) {
        this.refundstatus = refundstatus;
    }

    /**
     * TIMESTAMP(23,3)<br>
     */
    public Date getRefundtime() {
        return refundtime;
    }

    /**
     * TIMESTAMP(23,3)<br>
     */
    public void setRefundtime(Date refundtime) {
        this.refundtime = refundtime;
    }

    /**
     * VARCHAR(32)<br>
     */
    public String getRefundbatchno() {
        return refundbatchno;
    }

    /**
     * VARCHAR(32)<br>
     */
    public void setRefundbatchno(String refundbatchno) {
        this.refundbatchno = refundbatchno == null ? null : refundbatchno.trim();
    }

    /**
     * VARCHAR(64)<br>
     */
    public String getInstpaymentid() {
        return instpaymentid;
    }

    /**
     * VARCHAR(64)<br>
     */
    public void setInstpaymentid(String instpaymentid) {
        this.instpaymentid = instpaymentid == null ? null : instpaymentid.trim();
    }

    /**
     * INTEGER(10)<br>
     */
    public Integer getTradetype() {
        return tradetype;
    }

    /**
     * INTEGER(10)<br>
     */
    public void setTradetype(Integer tradetype) {
        this.tradetype = tradetype;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundRequest
     *
     * @mbggenerated Wed May 04 11:33:50 CST 2016
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", paymentid=").append(paymentid);
        sb.append(", tradeno=").append(tradeno);
        sb.append(", orderid=").append(orderid);
        sb.append(", traceid=").append(traceid);
        sb.append(", appid=").append(appid);
        sb.append(", paytype=").append(paytype);
        sb.append(", refundamount=").append(refundamount);
        sb.append(", currencytype=").append(currencytype);
        sb.append(", approvestatus=").append(approvestatus);
        sb.append(", approvedtime=").append(approvedtime);
        sb.append(", softdeleteflag=").append(softdeleteflag);
        sb.append(", createdtime=").append(createdtime);
        sb.append(", approveduser=").append(approveduser);
        sb.append(", refundstatus=").append(refundstatus);
        sb.append(", refundtime=").append(refundtime);
        sb.append(", refundbatchno=").append(refundbatchno);
        sb.append(", instpaymentid=").append(instpaymentid);
        sb.append(", tradetype=").append(tradetype);
        sb.append("]");
        return sb.toString();
    }
}