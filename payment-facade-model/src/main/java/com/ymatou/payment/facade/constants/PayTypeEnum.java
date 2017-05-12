/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 * <p>
 * All rights reserved.
 */
package com.ymatou.payment.facade.constants;

import org.apache.commons.lang3.StringUtils;

import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.ErrorCode;

/**
 * 支付渠道枚举
 *
 * @author wangxudong 2016年5月20日 上午11:08:41
 */
public enum PayTypeEnum {

    AliPayPc("10"),

    AliPayApp("13"),

    AliPayWap("11"),

    WeiXinJSAPI("14"),

    WeiXinApp("15"),

    WeiXinPc("16"),

    CmbApp("50"),

    ApplePay("60");

    private String code;

    private PayTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    @Override
    public String toString() {
        return this.getCode();
    }

    public static PayTypeEnum parse(String code) {
        if (StringUtils.isBlank(code)) {
            throw new BizException(ErrorCode.INVALID_PAY_TYPE, "payType is null or empty");
        }

        switch (code) {
            case "10":
                return AliPayPc;
            case "11":
                return AliPayWap;
            case "13":
                return AliPayApp;
            case "14":
                return WeiXinJSAPI;
            case "15":
                return WeiXinApp;
            case "16":
                return WeiXinPc;
            case "50":
                return CmbApp;
            case "60":
                return ApplePay;
            default:
                throw new BizException(ErrorCode.INVALID_PAY_TYPE, code);
        }
    }

    public static String getThirdPartyName(PayTypeEnum payType) {
        switch (payType) {
            case AliPayApp:
            case AliPayPc:
            case AliPayWap:
                return "Alipay";
            case WeiXinApp:
            case WeiXinJSAPI:
            case WeiXinPc:
                return "Weixin";
            case CmbApp:
                return "CmbPay";
            case ApplePay:
                return "ApplePay";
            default:
                return payType.toString();
        }
    }

    public static ChannelTypeEnum getChannelType(PayTypeEnum payType) {
        switch (payType) {
            case AliPayPc:
            case AliPayWap:
            case AliPayApp:
                return ChannelTypeEnum.AliPay;
            case CmbApp:
                return ChannelTypeEnum.CmbPay;
            case ApplePay:
                return ChannelTypeEnum.ApplePay;
            default:
                return ChannelTypeEnum.WeiXinPay;
        }
    }

    public static ChannelTypeEnum getChannelType(String payType) {
        if (payType.equals(PayTypeEnum.AliPayApp.getCode()) || payType.equals(PayTypeEnum.AliPayPc.getCode())
                || payType.equals(PayTypeEnum.AliPayWap.getCode())) {
            return ChannelTypeEnum.AliPay;
        } else if (payType.equals(PayTypeEnum.CmbApp.getCode())) {
            return ChannelTypeEnum.CmbPay;
        } else if (PayTypeEnum.ApplePay.getCode().equals(payType)) {
            return ChannelTypeEnum.ApplePay;
        } else {
            return ChannelTypeEnum.WeiXinPay;
        }
    }
}
