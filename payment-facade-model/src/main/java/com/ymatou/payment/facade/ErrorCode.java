package com.ymatou.payment.facade;

/**
 * 
 * @author tuwenjie
 *
 */
public enum ErrorCode {
    /*
     * 参数错误 1000
     */
    ILLEGAL_ARGUMENT(1000, "参数异常"),

    /*
     * 验签错误 2000
     */
    SIGN_NOT_MATCH(2000, "验签失败"),

    /*
     * 业务逻辑错误 3000
     */
    // 数据不存在 3100
    NOT_EXIST_PAYMENT_ID(3101, "支付单号不存在"),

    NOT_EXIST_BUSINESS_ORDER_ID(3102, "商户订单不存在"),

    NOT_EXIST_REFUND_NO(3103, "退款单号不存在"),

    INVALID_PAY_TYPE(3104, "支付渠道错误"),

    INVALID_SIGN_TYPE(3105, "签名方式错误"),

    INVALID_MERCHANT_ID(3106, "无效的商户号"),

    // 数据不一致 3300
    INCONSISTENT_PAYMENTID_AND_TRADINGID(3301, "支付单号和交易号不一致"),

    INCONSISTENT_PAYPRICE_AND_ACTUALPAYPRICE(3302, "支付金额和实际支付金额不一致"),

    // 数据错误 3400
    INVALID_PAYMENT_STATUS(3401, "支付单状态错误"),

    INVALID_EXT_MESSAGE(3402, "扩展信息错误"),

    // 逻辑错误 3500
    UNABLE_REFUND(3501, "不能退款"),


    /*
     * 第三方错误 4000
     */
    // 调用失败4000
    SERVER_SIDE_ACQUIRE_ORDER_FAILED(4001, "第三方收单失败"),

    QUERY_ANTI_FISHING_KEY_FAILED(4002, "获取防钓鱼参数失败"),

    PAYMENT_NOTIFY_ACCOUNTING_FAILED(4003, "支付成功充值失败"),

    PAYMENT_NOTIFY_INNER_SYSTEM_FAILED(4004, "支付成功通知内部系统失败"),

    // 验证失败4500
    PAYMENT_NOTIFY_VERIFY_FAILED(4501, "支付回调验证失败"),

    REFUND_NOTIFY_VERIFY_FAILED(4502, "退款回调验证失败"),


    /*
     * 通用错误 5000
     */
    FAIL(5000, "请求处理失败"),

    /*
     * 系统错误 9999
     */
    UNKNOWN(9999, "未知错误，系统异常");

    private int code;

    private String message;

    private ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 通过代码获取枚举项
     * 
     * @param code
     * @return
     */
    public static ErrorCode getByCode(int code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        return null;
    }
}
