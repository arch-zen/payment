/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.domain.refund.constants;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author qianmin 2016年5月23日 下午6:01:36
 *
 */
public class RefundConstants {

    // 在退款查询时，当不传refundStatus时，默认的refundStatusList
    public static final List<Integer> REFUND_QUERY_DEFULT_STATUS = Arrays.asList(new Integer[] {-2, -1, 0, 1, 2, 3, 4});
}
