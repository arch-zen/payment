/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.integration.model;

public abstract class CmbDTO {
    /**
     * 接口版本 默认 1.0
     */
    private String version = "1.0";

    /**
     * 参数编码 默认 UTF-8
     */
    private String charset = "UTF-8";

    /**
     * 报文签名
     */
    private String sign;

    /**
     * 签名算法 默认 SHA-256
     */
    private String signType = "SHA-256";

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the charset
     */
    public String getCharset() {
        return charset;
    }

    /**
     * @param charset the charset to set
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * @return the sign
     */
    public String getSign() {
        return sign;
    }

    /**
     * @param sign the sign to set
     */
    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * @return the signType
     */
    public String getSignType() {
        return signType;
    }

    /**
     * @param signType the signType to set
     */
    public void setSignType(String signType) {
        this.signType = signType;
    }

    /**
     * 获取到待签名的字符串
     * 
     * @return
     */
    public abstract String buildSignString();
}
