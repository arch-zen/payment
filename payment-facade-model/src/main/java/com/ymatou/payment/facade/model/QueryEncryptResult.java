/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.facade.model;

/**
 * 支付宝APP收单时返回给APP的信息，此类需要序列化给客户端，所以直接用public的大写字段
 * 
 * @author wangxudong 2016年5月13日 下午7:38:03
 *
 */
public class QueryEncryptResult {
    /// <summary>
    /// 加密后的字符串
    /// </summary>
    public String Sign;

    /// <summary>
    /// 商户跟支付宝合作的ID
    /// </summary>
    public String Partner;

    /// <summary>
    /// 合作方收款账户
    /// </summary>
    public String SellerId;

    /// <summary>
    /// 外部交易号
    /// </summary>
    public String OutTradeNo;

    /// <summary>
    /// 商品名称
    /// </summary>
    public String Subject;

    /// <summary>
    /// 交易描述
    /// </summary>
    public String Body;

    /// <summary>
    /// 交易总金额
    /// </summary>
    public String TotalFee;

    /// <summary>
    /// 支付宝服务器主动发起通知，调用商户在请求时设定好的页面路径
    /// </summary>
    public String NotifyUrl;

    /// <summary>
    /// 支付宝的支付通道
    /// </summary>
    public String Service;

    /// <summary>
    /// 支付类型
    /// </summary>
    public String PaymentType;

    /// <summary>
    /// 输入内容的编码类型
    /// </summary>
    public String InputCharset;

    /// <summary>
    /// 超时时间 ，默认30分钟.
    /// 设置未付款交易的超时时间，一旦超时，该笔交易就会自动被关闭。
    /// 取值范围：1m～15d。
    /// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
    /// 该参数数值不接受小数点，如1.5h，可转换为90m。
    /// 该功能需要联系支付宝配置关闭时间。
    /// </summary>
    public String ItBPay;

    /// <summary>
    /// 商品展示网址,客户端可不加此参数
    /// </summary>
    public String ShowUrl;

    /// <summary>
    /// 签名加密类型
    /// </summary>
    public String SignType;

    /// <summary>
    /// 拼接成的字符串
    /// </summary>
    public String EncryptStr;

    /// <summary>
    /// http请求处理是否成功
    /// </summary>
    public boolean QuerySuccess;

    /// <summary>
    /// 回传处理的信息
    /// </summary>
    public String Message;

}
