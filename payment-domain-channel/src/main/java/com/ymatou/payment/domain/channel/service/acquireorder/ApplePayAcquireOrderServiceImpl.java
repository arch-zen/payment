package com.ymatou.payment.domain.channel.service.acquireorder;

import com.ymatou.payment.domain.channel.InstitutionConfig;
import com.ymatou.payment.domain.channel.InstitutionConfigManager;
import com.ymatou.payment.domain.channel.model.AcquireOrderPackageResp;
import com.ymatou.payment.domain.channel.service.AcquireOrderService;
import com.ymatou.payment.domain.channel.service.SignatureService;
import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.BizException;
import com.ymatou.payment.facade.constants.AcquireOrderResultTypeEnum;
import com.ymatou.payment.infrastructure.util.StringUtil;
import com.ymatou.payment.integration.IntegrationConfig;
import com.ymatou.payment.integration.model.ApplePayConsumeRequest;
import com.ymatou.payment.integration.model.ApplePayConsumeResponse;
import com.ymatou.payment.integration.service.applepay.ApplePayConsumeService;
import com.ymatou.payment.integration.service.applepay.common.ApplePayConstants;
import com.ymatou.payment.integration.service.applepay.common.ApplePaySignatureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * Created by gejianhua on 2017/4/27.
 * applepay acquire order
 */
@Service
public class ApplePayAcquireOrderServiceImpl implements AcquireOrderService {

    private static Logger logger = LoggerFactory.getLogger(AliPayAppAcquireOrderServiceImpl.class);

    @Resource
    private InstitutionConfigManager instConfigManager;

    @Resource
    private IntegrationConfig integrationConfig;

    @Resource
    private SignatureService signatureService;

    @Resource
    private ApplePayConsumeService applePayConsumeService;

    @Override
    public AcquireOrderPackageResp acquireOrderPackage(Payment payment, HashMap<String, String> mockHeader) {
        // 获取第三方机构配置
        InstitutionConfig instConfig = instConfigManager.getConfig(payment.getPayType());

        //组装消费请求报文
        ApplePayConsumeRequest request = this.buildRequest(payment, instConfig, mockHeader);

        //调用消费接口及验签
        ApplePayConsumeResponse consumeResponse = this.consume(request, instConfig, mockHeader);

        //返回报文结果
        AcquireOrderPackageResp resp = new AcquireOrderPackageResp();
        resp.setResultType(AcquireOrderResultTypeEnum.Text);
        resp.setResult(consumeResponse.getTn());

        return resp;
    }

    private ApplePayConsumeRequest buildRequest(Payment payment, InstitutionConfig config, HashMap<String, String> mockHeader) {
        ApplePayConsumeRequest request = new ApplePayConsumeRequest();

        request.setTxnType("01");
        request.setTxnSubType("01");
        request.setBizType("000201");
        request.setMerId(config.getMerchantId());
        request.setOrderId(payment.getPaymentId());
        request.setTxnTime(StringUtil.getDateFormatString());
        request.setTxnAmt(String.valueOf(payment.getPayPrice().getCent()));
        request.setBackUrl(String.format("%s/notify/%s", integrationConfig.getYmtPaymentBaseUrl(), payment.getPayType()));
        request.setCertId(config.getCertId());

        //签名
        String prikey = config.getInstYmtPrivateKey();
        if (this.integrationConfig.isMock(mockHeader)) {
            prikey = this.signatureService.getMockPrivateKey();
        }
        String sign = ApplePaySignatureUtil.sign(request.genMap(), prikey);
        request.setSignature(sign);

        return request;
    }

    private ApplePayConsumeResponse consume(ApplePayConsumeRequest request, InstitutionConfig config, HashMap<String, String> mockHeader) {
        ApplePayConsumeResponse response = this.applePayConsumeService.doPost(request, mockHeader);

        if (ApplePayConstants.response_success_code.equals(response.getRespCode()) == false) {
            throw new BizException(String.format("applepay acquireorder fail:paymentId:%s, code:%s, msg:%s",
                    request.getOrderId(),
                    response.getRespCode(),
                    response.getRespMsg()));
        }
        //验商户号
        if(config.getMerchantId().equals(response.getMerId()) == false){
            throw new BizException(String.format("applepay acquireorder merchantId error:paymentId:%s, code:%s, msg:%s, merchantId:%s",
                    request.getOrderId(),
                    response.getRespCode(),
                    response.getRespMsg(),
                    response.getMerId()));
        }
        //验签，如果是mock则不验签
        if (this.integrationConfig.isMock(mockHeader) == false) {
            String pubkey = config.getInstPublicKey();
            boolean flag = ApplePaySignatureUtil.validate(response.getOriginMap(), pubkey);
            if(flag == false){
                throw new BizException(String.format("applepay acquireorder validate sign fail:paymentId:%s, code:%s, msg:%s",
                        request.getOrderId(),
                        response.getRespCode(),
                        response.getRespMsg()));
            }
        }


        return response;
    }


}


































































































