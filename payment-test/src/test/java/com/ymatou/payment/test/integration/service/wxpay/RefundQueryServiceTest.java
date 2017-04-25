package com.ymatou.payment.test.integration.service.wxpay;

import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.payment.integration.common.Signature;
import com.ymatou.payment.integration.model.QueryRefundRequest;
import com.ymatou.payment.integration.model.QueryRefundResponse;
import com.ymatou.payment.integration.model.RefundOrderData;
import com.ymatou.payment.integration.service.wxpay.WxRefundQueryService;
import com.ymatou.payment.test.RestBaseTest;

public class RefundQueryServiceTest extends RestBaseTest {

    @Autowired
    private WxRefundQueryService refundQueryService;

    @Test
    public void testDoServiceSuccess() throws Exception {
        QueryRefundRequest request = new QueryRefundRequest();
        request.setAppid("wxa06ebe9f39751792");
        request.setMch_id("1278350701");
        request.setDevice_info("WEB");
        request.setNonce_str("weixin" + String.valueOf(new Random().nextInt(10)));
        request.setOut_trade_no("407300842881200246326");
        request.setTransaction_id("294be28a62a34aa38b0e38e0cdcbfd7");

        // 加签
        String sign = Signature.getSign(request, "es839gnc8451lp0s943n568xzskjgdbv");
        request.setSign(sign);

        HashMap<String, String> header = new HashMap<>();
        // header.put("mock", "1");
        // header.put("mockId", "888888");
        QueryRefundResponse response = refundQueryService.doService(request, header);
        Assert.assertNotNull(response);
        Assert.assertEquals("TRANSACTION_ID_INVALID", response.getErr_code());
    }

    @Test
    public void testMutiRefund() throws Exception {
        String refundNo = "201607250000425035";
        QueryRefundRequest request = new QueryRefundRequest();
        request.setAppid("wxf51a439c0416f182");
        request.setMch_id("1234079001");
        request.setDevice_info("WEB");
        request.setNonce_str("weixin" + String.valueOf(new Random().nextInt(10)));
        request.setOut_refund_no(refundNo);

        // 加签
        String sign = Signature.getSign(request, "c5781df6b8f149adca6094cdac4ac684");
        request.setSign(sign);

        HashMap<String, String> header = new HashMap<>();
        QueryRefundResponse response = refundQueryService.doService(request, header);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getRefundOrderDataList());

        Optional<RefundOrderData> refundOrderData = response.getRefundOrderDataList().stream()
                .filter(refund -> refund.getOutRefundNo().equals(refundNo))
                .findFirst();

        Assert.assertEquals(true, refundOrderData.isPresent());
        Assert.assertEquals(7500, refundOrderData.get().getRefundFee());
    }

    @Test
    public void testDoServiceSuccessWithMock() throws Exception {
        QueryRefundRequest request = new QueryRefundRequest();
        request.setAppid("wxa06ebe9f39751792");
        request.setMch_id("1278350702");
        request.setDevice_info("WEB");
        request.setNonce_str("weixin" + String.valueOf(new Random().nextInt(10)));
        request.setOut_refund_no("201605310000000743");
        request.setTransaction_id("294be28a62a34aa38b0e38e0cdcbfd7");

        // 加签
        String sign = Signature.getSign(request, "es839gnc8451lp0s943n568xzskjgdbv");
        request.setSign(sign);

        HashMap<String, String> header = buildMockHeader();
        header.put("MockResult-WeiXin-refund_status", "CHANGE");
        header.put("MockResult-WeiXin-refund_fee", "200");
        header.put("MockResult-WeiXin-out_trade_no", "10192657500891166");

        QueryRefundResponse response = refundQueryService.doService(request, header);
        Assert.assertNotNull(response);
        Assert.assertEquals("CHANGE", response.getRefundOrderDataList().get(0).getRefundStatus());
        Assert.assertEquals(200, response.getRefundOrderDataList().get(0).getRefundFee());
        Assert.assertEquals("1278350702", response.getMch_id());
        Assert.assertEquals("201605310000000743", response.getRefundOrderDataList().get(0).getOutRefundNo());
        Assert.assertEquals("10192657500891166", response.getOut_trade_no());
    }
}
