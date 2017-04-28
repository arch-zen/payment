package com.ymatou.payment.facade.model;

import com.ymatou.payment.facade.BaseRequest;

import java.util.HashMap;

/**
 * Created by zhangxiaoming on 2017/4/27.
 */
public class RefundNotifyRequest extends BaseRequest {
    private String body;
    private String payType;
    private HashMap<String, String> mockHeader;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public HashMap<String, String> getMockHeader() {
        return mockHeader;
    }

    public void setMockHeader(HashMap<String, String> mockHeader) {
        this.mockHeader = mockHeader;
    }
}
