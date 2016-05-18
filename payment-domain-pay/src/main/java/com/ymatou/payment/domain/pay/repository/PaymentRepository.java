package com.ymatou.payment.domain.pay.repository;

import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.infrastructure.db.mapper.PaymentMapper;
import com.ymatou.payment.infrastructure.db.model.BussinessorderPo;
import com.ymatou.payment.infrastructure.db.model.PaymentExample;
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

    @Resource
    private SqlSession sqlSession;

    /**
     * 根据PaymentId获取到支付单信息
     * 
     * @param paymentId
     * @return
     */
    public Payment getByPaymentId(String paymentId) {
        PaymentExample example = new PaymentExample();
        example.createCriteria().andPaymentidEqualTo(paymentId);

        List<PaymentPo> paymentList = sqlSession.selectList("ext-ppPayment.selectByExample", example);

        if (paymentList == null || paymentList.size() == 0)
            return null;
        else
            return Payment.convertFromPo(paymentList.get(0));
    }

    /**
     * 根据BussinessId获取到支付单信息
     *
     * @param bussinessId
     * @return
     */
    public Payment getByBussinessOrderId(String bussinessId) {
        PaymentExample example = new PaymentExample();
        example.createCriteria().andBussinessorderidEqualTo(bussinessId);

        List<PaymentPo> poList = sqlSession.selectList("ext-ppPayment.selectByExample", example);
        if (poList.size() == 0)
            return null;
        else
            return Payment.convertFromPo(poList.get(0));
    }


    /**
     * 生成商户订单和支付单
     * 
     * @param po
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public int acquireOrder(PaymentPo payment, BussinessorderPo bussinessOrder) {
        // 商户订单落地
        bussinessOrderRepository.insert(bussinessOrder);

        // 支付单落地
        payment.setPaymentid(genPaymentId(bussinessOrder));
        int rows = sqlSession.insert("ext-ppPayment.insert", payment);

        return rows;
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
     * 查找可以退款的Payment
     * 
     * @param bussinessOrderId
     * @param payStatus
     * @return
     */
    public Payment getPaymentCanRefund(String bussinessOrderId, Integer payStatus) {
        PaymentExample example = new PaymentExample();
        example.createCriteria().andBussinessorderidEqualTo(bussinessOrderId)
                .andPaystatusEqualTo(payStatus);
        List<PaymentPo> pos = sqlSession.selectList("ext-ppPayment.selectByExample", example);
        if (pos == null || pos.size() == 0) {
            return null;
        }
        return Payment.convertFromPo(pos.get(0));
    }
}
