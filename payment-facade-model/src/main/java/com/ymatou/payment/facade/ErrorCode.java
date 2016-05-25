package com.ymatou.payment.facade;

/**
 * 
 * @author tuwenjie
 *
 */
public enum ErrorCode {
    SING_SERVER_ERROR(-5000, "服务端验签异常"),

    SIGN_NOT_MATCH(-2001, "验签失败"),

    NOT_SUPPORT_VERSION_OBJECT(-2002, "余额不足"),

    NOT_SUPPORT_VERSION(-2003, "不支持的版本"),

    EXT_MESSAGE_NOT_RECOGNIZE(-2004, "不可识别的扩展信息"),

    SERVER_SIDE_ACQUIRE_ORDER_FAILED(-2100, "第三方收单失败"),

    USER_ID_SHOULD_NOT_BE_EMPTY(-2101, "用户ID不能为空"),

    NOTIFY_VERIFY_FAILED(-2101, "通知验证失败"),

    UNKNOWN_ERROR(-2103, "未知的异常"),

    DATA_NOT_FOUND(-2104, "数据未找到"),

    EXCHANGE_RATE_CHECK_FAILED(-2105, "汇率检测失败"),

    DB_ERROR(-2106, "数据库异常"),

    REFUND_FAILED(-2107, "退款失败"),

    CERTIFICATION_NOT_FOUND(-2108, "证书未找到"),

    CHECK_PAY_NOT_MATCH(-3000, "对账不匹配"),

    ILLEGAL_ARGUMENT(1000, "参数异常"),

    NOT_EXIST_PAYMENTID(1001, "支付单号不存在"),

    INCONSISTENT_PAYMENTID_AND_TRADINGID(1002, "支付单号和交易号不一致"),

    INVALID_PAYMENT_STATUS(1003, "支付单状态不正确"),

    NOT_EXIST_BUSSINESS_ORDERID(1004, "商户订单不存在"),

    INVALID_PAYTYPE(1005, "无效的支付渠道"),

    NOT_ALL_TRADE_CAN_REFUND(-3, "非所有订单都可退款"),

    INVALID_ORDER_ID(-2, "订单号非法"),

    REFUND_REQUEST_NOT_EXIST(-2, "退款请求不存在"),

    FAIL_QUERY_ANTI_FISHING_KEY(1006, "获取防钓鱼参数失败"),

    INVALID_SIGN_TYPE(1007, "无效的签名方式"),

    FAIL(5000, "请求处理失败"),

    UNKNOWN(-9999, "未知错误，系统异常");

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
