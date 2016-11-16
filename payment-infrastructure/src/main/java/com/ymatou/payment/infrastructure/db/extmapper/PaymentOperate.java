/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.infrastructure.db.extmapper;

public interface PaymentOperate {
    /**
     * 获取到支付单递增后缀
     * 
     * @return
     */
    public long genPaymentSuffixId();
}
