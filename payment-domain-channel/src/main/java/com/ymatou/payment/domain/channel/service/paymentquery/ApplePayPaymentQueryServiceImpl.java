package com.ymatou.payment.domain.channel.service.paymentquery;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.model.PaymentQueryResp;
import com.ymatou.payment.domain.channel.service.PaymentQueryService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.domain.pay.model.BussinessOrder;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.domain.pay.repository.BussinessOrderRepository;
import com.ymatou.payment.domain.pay.repository.PaymentRepository;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.facade.constants.PayTypeEnum;
import com.ymatou.payment.infrastructure.Money;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.model.ApplePayConsumeQueryRequest;
import com.ymatou.payment.integration.model.ApplePayTradeQueryResponse;
import com.ymatou.payment.integration.service.applepay.ApplePayTradeQueryService;
import com.ymatou.payment.integration.service.applepay.common.ApplePayConstants;
import com.ymatou.payment.integration.service.applepay.common.ApplePaySignatureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by gejianhua on 2017/4/28.
 * applepay消费查询服务
 */
@Service
public class ApplePayPaymentQueryServiceImpl implements PaymentQueryService {

    private static final Logger logger = LoggerFactory.getLogger(CmbPaymentQueryServiceImpl.class);

    @Resource
    private SignatureService signatureService;

    @Resource
    private InstitutionConfigManager institutionConfigManager;

    @Resource
    private IntegrationConfig integrationConfig;

    @Resource
    private PaymentRepository paymentRepository;

    @Resource
    private BussinessOrderRepository bussinessOrderRepository;

    @Resource
    private ApplePayTradeQueryService applePayTradeQueryService;

    @Override
    public PaymentQueryResp queryPayment(String paymentId, String payType, HashMap<String, String> header) {
        //get payment
        Payment payment = this.paymentRepository.getByPaymentId(paymentId);
        if (payment == null) {
            throw new BizException("payment is null, paymentId:" + paymentId);
        }
        //get bussinessOrder
        BussinessOrder bussinessOrder = bussinessOrderRepository.getBussinessOrderById(payment.getBussinessOrderId());
        if (bussinessOrder == null) {
            throw new BizException("bussinessOrder is null, bussinessOrderId:" + payment.getBussinessOrderId());
        }
        payment.setBussinessOrder(bussinessOrder);

        //build consume query request
        ApplePayConsumeQueryRequest consumeQueryRequest = this.buildConsumeQueryRequest(payment, header);

        //consume query
        ApplePayTradeQueryResponse tradeQueryResponse = this.consumeQuery(consumeQueryRequest, header);

        //build PaymentQueryResp
        if (ApplePayConstants.response_success_code.equals(tradeQueryResponse.getOrigRespCode())) {
            return this.buildPaymentQueryResp(tradeQueryResponse);

        } else if ("03".equals(tradeQueryResponse.getOrigRespCode())
                || "04".equals(tradeQueryResponse.getOrigRespCode())
                || "05".equals(tradeQueryResponse.getOrigRespCode())) {

            PaymentQueryResp resp = new PaymentQueryResp();
            resp.setOriginMessage(tradeQueryResponse.toString());
            resp.setPayStatus(PayStatusEnum.UNKNOW);
            return resp;
        } else {
            PaymentQueryResp resp = new PaymentQueryResp();
            resp.setOriginMessage(tradeQueryResponse.toString());
            resp.setPayStatus(PayStatusEnum.Failed);
            return resp;
        }
    }

    private PaymentQueryResp buildPaymentQueryResp(ApplePayTradeQueryResponse tradeQueryResponse) {

        PaymentQueryResp resp = new PaymentQueryResp();
        try {
            resp.setCardType(Integer.parseInt(tradeQueryResponse.getPayCardType()));
        } catch (Exception ex) {
            logger.error("convert cardtype error, paycardtype:" + tradeQueryResponse.getPayCardType(), ex);
        }
        resp.setActualPayCurrency("CNY");
        resp.setActualPayPrice(new BigDecimal(tradeQueryResponse.getSettleAmt()));
        resp.setBankId("");
        resp.setDiscountAmount(new Money(0));
        resp.setInstitutionPaymentId(tradeQueryResponse.getQueryId());
        resp.setPayerId("");
        resp.setPaymentId(tradeQueryResponse.getOrderId());
        resp.setPayStatus(PayStatusEnum.Paied);
        resp.setPayTime(new Date());
        resp.setTraceId(tradeQueryResponse.getTraceNo());

        return resp;
    }

    private ApplePayConsumeQueryRequest buildConsumeQueryRequest(Payment payment, HashMap<String, String> header) {

        InstitutionConfig config = this.institutionConfigManager.getConfig(PayTypeEnum.ApplePay);

        ApplePayConsumeQueryRequest request = new ApplePayConsumeQueryRequest();
        request.setCertId(config.getCertId());
        request.setMerId(config.getMerchantId());
        request.setOrderId(payment.getPaymentId());
        request.setTxnTime(payment.getBussinessOrder().getOrderTime());

        //签名
        String prikey = config.getInstYmtPrivateKey();
        if (this.integrationConfig.isMock(header)) {
            prikey = this.signatureService.getMockPrivateKey();
        }
        String sign = ApplePaySignatureUtil.sign(request.genMap(), prikey);
        request.setSignature(sign);

        return request;
    }

    private ApplePayTradeQueryResponse consumeQuery(ApplePayConsumeQueryRequest request, HashMap<String, String> header) {
        ApplePayTradeQueryResponse response = this.applePayTradeQueryService.doPost(request, header);

        if (ApplePayConstants.response_success_code.equals(response.getRespCode()) == false) {
            throw new BizException(String.format("applepay consumeQuery fail:paymentId:%s, code:%s, msg:%s",
                    request.getOrderId(),
                    response.getRespCode(),
                    response.getRespMsg()));
        }

        InstitutionConfig config = this.institutionConfigManager.getConfig(PayTypeEnum.ApplePay);
        //验商户号
        if (config.getMerchantId().equals(response.getMerId()) == false) {
            throw new BizException(String.format("applepay consumeQuery merchantId error:paymentId:%s, code:%s, msg:%s, merchantId:%s",
                    request.getOrderId(),
                    response.getRespCode(),
                    response.getRespMsg(),
                    response.getMerId()));
        }
        //验签，如果是mock不验签
        if (this.integrationConfig.isMock(header) == false) {
            String pubkey = config.getInstPublicKey();
            boolean flag = ApplePaySignatureUtil.validate(response.getOriginMap(), pubkey);
            if (flag == false) {
                throw new BizException(String.format("applepay consumeQuery validate sign fail:paymentId:%s, code:%s, msg:%s",
                        request.getOrderId(),
                        response.getRespCode(),
                        response.getRespMsg()));
            }
        }

        return response;
    }
}











































