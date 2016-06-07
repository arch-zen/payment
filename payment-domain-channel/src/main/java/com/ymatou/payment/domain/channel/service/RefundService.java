/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.channel.service;

import java.util.HashMap;

/**
 * 提交第三方退款
 * 
 * @author qianmin 2016年6月6日 上午11:35:50
 *
 */
public interface RefundService {

    public void notifyRefund(String refundBatchNo, HashMap<String, String> header);
}
