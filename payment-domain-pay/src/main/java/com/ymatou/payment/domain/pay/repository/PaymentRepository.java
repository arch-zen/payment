package com.ymatou.payment.domain.pay.repository;

import java.util.Random;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.infrastructure.db.mapper.PaymentMapper;
import com.ymatou.payment.infrastructure.db.model.BussinessorderPo;
import com.ymatou.payment.infrastructure.db.model.PaymentPo;
import com.ymatou.payment.infrastructure.util.StringUtil;

/**
 * 支付单仓储
 * 
 * @author wangxudong
 *
 */
@Component
public class PaymentRepository {

    private static final Logger logger = LoggerFactory.getLogger(PaymentRepository.class);

    @Resource
    private PaymentMapper paymentMapper;

    @Resource
    private BussinessOrderRepository bussinessOrderRepository;

    /**
     * 根据PaymentId获取到支付单信息
     * 
     * @param paymentId
     * @return
     */
    public Payment getByPaymentId(String paymentId) {
        PaymentPo payment = paymentMapper.selectByPrimaryKey(paymentId);

        return convert(payment);
    }


    /**
     * 生成商户订单和支付单
     * 
     * @param po
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public int acquireOrder(PaymentPo payment, BussinessorderPo bussinessOrder) {

        bussinessOrderRepository.insert(bussinessOrder);

        payment.setPaymentid(genPaymentId(bussinessOrder));

        return paymentMapper.insert(payment);
    }

    /**
     * 生成支付单号
     * 
     * @param bo
     * @return
     */
    private String genPaymentId(BussinessorderPo bo) {
        String paymentId = null;

        // "PP"开头的交易单号代表补款
        if (bo.getOrderid().startsWith("PP")) {
            paymentId = String.format("%s%015d", StringUtil.getDateFormatString("yyyyMMddhhmmssSSS"),
                    new Random().nextInt(100000));
        } else {
            // 由于报关对商户订单号的长度有要求，所以生成逻辑与补款不同
            paymentId = String.format("%s%08d", bo.getOrderid(), new Random().nextInt(1000000));
        }

        logger.debug("genPaymentId:" + paymentId);

        return paymentId;
    }

    /**
     * PO转换成Model
     * 
     * @param po
     * @return
     */
    public Payment convert(PaymentPo po) {
        Payment payment = new Payment();
        payment.setPayerid(po.getPaymentid());

        return payment;
    }

}
