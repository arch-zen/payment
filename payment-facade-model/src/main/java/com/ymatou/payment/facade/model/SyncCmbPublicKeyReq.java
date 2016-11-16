/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.facade.model;

import java.util.HashMap;

import com.ymatou.payment.facade.BaseRequest;

/**
 * 同步招行公钥Request
 * 
 * @author qianmin 2016年6月24日 下午2:11:19
 *
 */
public class SyncCmbPublicKeyReq extends BaseRequest {

    private static final long serialVersionUID = -3031119076319521164L;

    /**
     * MockHeader
     */
    private HashMap<String, String> mockHeader;

    /**
     * @return the mockHeader
     */
    public HashMap<String, String> getMockHeader() {
        return mockHeader;
    }

    /**
     * @param mockHeader the mockHeader to set
     */
    public void setMockHeader(HashMap<String, String> mockHeader) {
        this.mockHeader = mockHeader;
    }

}
