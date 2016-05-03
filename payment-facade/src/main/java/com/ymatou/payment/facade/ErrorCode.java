package com.ymatou.payment.facade;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author tuwenjie
 *
 */
public enum ErrorCode {
	
    //请求参数非法
    ILLEGAL_ARGUMENT("01", "请求参数非法"),
    
    /**
     * 客户端需要知道的具体错误码定义在这里
     * 譬如，账户余额不足, 账户不存在等
     */
    BALANCE_NOT_ENOUGH("02", "余额不足"),
    ACCT_NOT_EXIST("03", "余额不足"),
    
    //明确知道失败原因，但客户端不关心，统一返回请求处理失败
    FAIL("98", "请求处理失败"),
    
    //请求处理过程中，出现未知错误
    UNKNOWN("99", "未知错误，系统异常"),
    
    ;
    
    private String code;
    
    private String message;
    
    private ErrorCode( String code, String message ) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    
    /**
     * 通过代码获取枚举项
     * @param code
     * @return
     */
    public static ErrorCode getByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }

        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return null;
    }
}
