package com.ymatou.payment.infrastructure.db.model;

import java.math.BigDecimal;
import java.util.Date;

public class BussinessorderPo {
    /**
     * CHAR(36) 默认值[(newid())] 必填<br>
     * 
     */
    private String bussinessorderid;

    /**
     * VARCHAR(16) 必填<br>
     * 
     */
    private String appid;

    /**
     * VARCHAR(16) 必填<br>
     * 
     */
    private String originappid;

    /**
     * VARCHAR(32) 必填<br>
     * 
     */
    private String orderid;

    /**
     * VARCHAR(16) 必填<br>
     * 
     */
    private String paytype;

    /**
     * DECIMAL(18,4) 必填<br>
     * 
     */
    private BigDecimal orderprice;

    /**
     * VARCHAR(3) 必填<br>
     * 
     */
    private String currencytype;

    /**
     * INTEGER(10)<br>
     * 
     */
    private Integer userid;

    /**
     * INTEGER(10) 必填<br>
     * 
     */
    private Integer version;

    /**
     * VARCHAR(36) 必填<br>
     * 
     */
    private String traceid;

    /**
     * VARCHAR(16) 必填<br>
     * 
     */
    private String ordertime;

    /**
     * VARCHAR(64)<br>
     * 
     */
    private String thirdpartyuserid;

    /**
     * INTEGER(10)<br>
     * 
     */
    private Integer thirdpartyusertype;

    /**
     * VARCHAR(16) 必填<br>
     * 
     */
    private String clientip;

    /**
     * VARCHAR(256)<br>
     * 
     */
    private String callbackurl;

    /**
     * VARCHAR(256) 必填<br>
     * 
     */
    private String notifyurl;

    /**
     * VARCHAR(64) 必填<br>
     * 
     */
    private String productname;

    /**
     * VARCHAR(512)<br>
     * 
     */
    private String productdesc;

    /**
     * VARCHAR(512)<br>
     * 
     */
    private String producturl;

    /**
     * INTEGER(10) 必填<br>
     * 
     */
    private Integer codepage;

    /**
     * VARCHAR(512)<br>
     * 
     */
    private String ext;

    /**
     * VARCHAR(512)<br>
     * 
     */
    private String memo;

    /**
     * VARCHAR(8) 必填<br>
     * 
     */
    private String signmethod;

    /**
     * INTEGER(10) 必填<br>
     * 
     */
    private Integer bizcode;

    /**
     * TIMESTAMP(23,3) 默认值[(getdate())] 必填<br>
     * 
     */
    private Date createdtime;

    /**
     * TIMESTAMP(23,3)<br>
     * 
     */
    private Date lastupdatedtime;

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     * 
     */
    private Integer orderstatus;

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     * 
     */
    private Integer notifystatus;

    /**
     * TIMESTAMP(23,3)<br>
     * 
     */
    private Date notifytime;

    /**
     * BINARY(8) 必填<br>
     * 
     */
    private byte[] dataversion;

    /**
     * CHAR(36) 默认值[(newid())] 必填<br>
     */
    public String getBussinessorderid() {
        return bussinessorderid;
    }

    /**
     * CHAR(36) 默认值[(newid())] 必填<br>
     */
    public void setBussinessorderid(String bussinessorderid) {
        this.bussinessorderid = bussinessorderid == null ? null : bussinessorderid.trim();
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
    public String getOriginappid() {
        return originappid;
    }

    /**
     * VARCHAR(16) 必填<br>
     */
    public void setOriginappid(String originappid) {
        this.originappid = originappid == null ? null : originappid.trim();
    }

    /**
     * VARCHAR(32) 必填<br>
     */
    public String getOrderid() {
        return orderid;
    }

    /**
     * VARCHAR(32) 必填<br>
     */
    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
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
    public BigDecimal getOrderprice() {
        return orderprice;
    }

    /**
     * DECIMAL(18,4) 必填<br>
     */
    public void setOrderprice(BigDecimal orderprice) {
        this.orderprice = orderprice;
    }

    /**
     * VARCHAR(3) 必填<br>
     */
    public String getCurrencytype() {
        return currencytype;
    }

    /**
     * VARCHAR(3) 必填<br>
     */
    public void setCurrencytype(String currencytype) {
        this.currencytype = currencytype == null ? null : currencytype.trim();
    }

    /**
     * INTEGER(10)<br>
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * INTEGER(10)<br>
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * INTEGER(10) 必填<br>
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * INTEGER(10) 必填<br>
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * VARCHAR(36) 必填<br>
     */
    public String getTraceid() {
        return traceid;
    }

    /**
     * VARCHAR(36) 必填<br>
     */
    public void setTraceid(String traceid) {
        this.traceid = traceid == null ? null : traceid.trim();
    }

    /**
     * VARCHAR(16) 必填<br>
     */
    public String getOrdertime() {
        return ordertime;
    }

    /**
     * VARCHAR(16) 必填<br>
     */
    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime == null ? null : ordertime.trim();
    }

    /**
     * VARCHAR(64)<br>
     */
    public String getThirdpartyuserid() {
        return thirdpartyuserid;
    }

    /**
     * VARCHAR(64)<br>
     */
    public void setThirdpartyuserid(String thirdpartyuserid) {
        this.thirdpartyuserid = thirdpartyuserid == null ? null : thirdpartyuserid.trim();
    }

    /**
     * INTEGER(10)<br>
     */
    public Integer getThirdpartyusertype() {
        return thirdpartyusertype;
    }

    /**
     * INTEGER(10)<br>
     */
    public void setThirdpartyusertype(Integer thirdpartyusertype) {
        this.thirdpartyusertype = thirdpartyusertype;
    }

    /**
     * VARCHAR(16) 必填<br>
     */
    public String getClientip() {
        return clientip;
    }

    /**
     * VARCHAR(16) 必填<br>
     */
    public void setClientip(String clientip) {
        this.clientip = clientip == null ? null : clientip.trim();
    }

    /**
     * VARCHAR(256)<br>
     */
    public String getCallbackurl() {
        return callbackurl;
    }

    /**
     * VARCHAR(256)<br>
     */
    public void setCallbackurl(String callbackurl) {
        this.callbackurl = callbackurl == null ? null : callbackurl.trim();
    }

    /**
     * VARCHAR(256) 必填<br>
     */
    public String getNotifyurl() {
        return notifyurl;
    }

    /**
     * VARCHAR(256) 必填<br>
     */
    public void setNotifyurl(String notifyurl) {
        this.notifyurl = notifyurl == null ? null : notifyurl.trim();
    }

    /**
     * VARCHAR(64) 必填<br>
     */
    public String getProductname() {
        return productname;
    }

    /**
     * VARCHAR(64) 必填<br>
     */
    public void setProductname(String productname) {
        this.productname = productname == null ? null : productname.trim();
    }

    /**
     * VARCHAR(512)<br>
     */
    public String getProductdesc() {
        return productdesc;
    }

    /**
     * VARCHAR(512)<br>
     */
    public void setProductdesc(String productdesc) {
        this.productdesc = productdesc == null ? null : productdesc.trim();
    }

    /**
     * VARCHAR(512)<br>
     */
    public String getProducturl() {
        return producturl;
    }

    /**
     * VARCHAR(512)<br>
     */
    public void setProducturl(String producturl) {
        this.producturl = producturl == null ? null : producturl.trim();
    }

    /**
     * INTEGER(10) 必填<br>
     */
    public Integer getCodepage() {
        return codepage;
    }

    /**
     * INTEGER(10) 必填<br>
     */
    public void setCodepage(Integer codepage) {
        this.codepage = codepage;
    }

    /**
     * VARCHAR(512)<br>
     */
    public String getExt() {
        return ext;
    }

    /**
     * VARCHAR(512)<br>
     */
    public void setExt(String ext) {
        this.ext = ext == null ? null : ext.trim();
    }

    /**
     * VARCHAR(512)<br>
     */
    public String getMemo() {
        return memo;
    }

    /**
     * VARCHAR(512)<br>
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    /**
     * VARCHAR(8) 必填<br>
     */
    public String getSignmethod() {
        return signmethod;
    }

    /**
     * VARCHAR(8) 必填<br>
     */
    public void setSignmethod(String signmethod) {
        this.signmethod = signmethod == null ? null : signmethod.trim();
    }

    /**
     * INTEGER(10) 必填<br>
     */
    public Integer getBizcode() {
        return bizcode;
    }

    /**
     * INTEGER(10) 必填<br>
     */
    public void setBizcode(Integer bizcode) {
        this.bizcode = bizcode;
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
     * TIMESTAMP(23,3)<br>
     */
    public Date getLastupdatedtime() {
        return lastupdatedtime;
    }

    /**
     * TIMESTAMP(23,3)<br>
     */
    public void setLastupdatedtime(Date lastupdatedtime) {
        this.lastupdatedtime = lastupdatedtime;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public Integer getOrderstatus() {
        return orderstatus;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public void setOrderstatus(Integer orderstatus) {
        this.orderstatus = orderstatus;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public Integer getNotifystatus() {
        return notifystatus;
    }

    /**
     * INTEGER(10) 默认值[((0))] 必填<br>
     */
    public void setNotifystatus(Integer notifystatus) {
        this.notifystatus = notifystatus;
    }

    /**
     * TIMESTAMP(23,3)<br>
     */
    public Date getNotifytime() {
        return notifytime;
    }

    /**
     * TIMESTAMP(23,3)<br>
     */
    public void setNotifytime(Date notifytime) {
        this.notifytime = notifytime;
    }

    /**
     * BINARY(8) 必填<br>
     */
    public byte[] getDataversion() {
        return dataversion;
    }

    /**
     * BINARY(8) 必填<br>
     */
    public void setDataversion(byte[] dataversion) {
        this.dataversion = dataversion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PP_BussinessOrder
     *
     * @mbggenerated Thu May 19 15:10:43 CST 2016
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", bussinessorderid=").append(bussinessorderid);
        sb.append(", appid=").append(appid);
        sb.append(", originappid=").append(originappid);
        sb.append(", orderid=").append(orderid);
        sb.append(", paytype=").append(paytype);
        sb.append(", orderprice=").append(orderprice);
        sb.append(", currencytype=").append(currencytype);
        sb.append(", userid=").append(userid);
        sb.append(", version=").append(version);
        sb.append(", traceid=").append(traceid);
        sb.append(", ordertime=").append(ordertime);
        sb.append(", thirdpartyuserid=").append(thirdpartyuserid);
        sb.append(", thirdpartyusertype=").append(thirdpartyusertype);
        sb.append(", clientip=").append(clientip);
        sb.append(", callbackurl=").append(callbackurl);
        sb.append(", notifyurl=").append(notifyurl);
        sb.append(", productname=").append(productname);
        sb.append(", productdesc=").append(productdesc);
        sb.append(", producturl=").append(producturl);
        sb.append(", codepage=").append(codepage);
        sb.append(", ext=").append(ext);
        sb.append(", memo=").append(memo);
        sb.append(", signmethod=").append(signmethod);
        sb.append(", bizcode=").append(bizcode);
        sb.append(", createdtime=").append(createdtime);
        sb.append(", lastupdatedtime=").append(lastupdatedtime);
        sb.append(", orderstatus=").append(orderstatus);
        sb.append(", notifystatus=").append(notifystatus);
        sb.append(", notifytime=").append(notifytime);
        sb.append(", dataversion=").append(dataversion);
        sb.append("]");
        return sb.toString();
    }
}