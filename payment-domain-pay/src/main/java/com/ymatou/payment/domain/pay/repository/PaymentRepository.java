package com.ymatou.payment.domain.pay.repository;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ymatou.payment.domain.pay.model.Payment;
import com.ymatou.payment.facade.constants.PayStatusEnum;
import com.ymatou.payment.infrastructure.db.mapper.CompensateProcessInfoMapper;
import com.ymatou.payment.infrastructure.db.mapper.PaymentMapper;
import com.ymatou.payment.infrastructure.db.model.BussinessOrderPo;
import com.ymatou.payment.infrastructure.db.model.PaymentExample;
import com.ymatou.payment.infrastructure.db.model.PaymentPo;
import com.ymatou.payment.infrastructure.db.operate.PaymentOperate;
import com.ymatou.payment.infrastructure.util.StringUtil;
import com.ymatou.payment.integration.IntegrationConfig;

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
    private PaymentOperate paymentOperate;

    @Resource
    private BussinessOrderRepository bussinessOrderRepository;

    @Resource
    private CompensateProcessInfoMapper compensateProcessInfoMapper;

    @Resource
    private IntegrationConfig integrationConfig;

    /**
     * 根据PaymentId获取到支付单信息
     * 
     * @param paymentId
     * @return
     */
    public Payment getByPaymentId(String paymentId) {
        PaymentPo paymentPo = paymentMapper.selectByPrimaryKey(paymentId);

        return Payment.convertFromPo(paymentPo);
    }

    /**
     * 根据BussinessId获取到支付单信息
     *
     * @param bussinessId
     * @return
     */
    public Payment getByBussinessOrderId(String bussinessId) {
        PaymentExample example = new PaymentExample();
        example.createCriteria().andBussinessOrderIdEqualTo(bussinessId);

        List<PaymentPo> poList = paymentMapper.selectByExample(example);
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
    @Transactional
    public int acquireOrder(PaymentPo payment, BussinessOrderPo bussinessOrder) {
        // 商户订单落地
        bussinessOrderRepository.insert(bussinessOrder);

        // 支付单落地
        payment.setPaymentId(genPaymentId(bussinessOrder));
        int rows = paymentMapper.insertSelective(payment);

        return rows;
    }

    /**
     * 设置支付单状态为已支付
     * 
     * @param paymentPo
     */
    @Transactional
    public void setPaymentOrderPaid(PaymentPo paymentPo, String traceId) {
        // 更新支付单
        PaymentExample paymentExample = new PaymentExample();
        paymentExample.createCriteria().andPaymentIdEqualTo(paymentPo.getPaymentId());
        paymentMapper.updateByExampleSelective(paymentPo, paymentExample);

        // 更新商户订单
        bussinessOrderRepository.updateOrderStatus(paymentPo.getBussinessOrderId(), paymentPo.getPayStatus());
    }

    /**
     * 生成支付单号
     * 
     * @param bo
     * @return
     */
    private String genPaymentId(BussinessOrderPo bo) {
        long paymentSuffixId = paymentOperate.genPaymentSuffixId();
        String prefix = StringUtil.getDateFormatString("yyMMddHHmmss");
        String suffix = String.format("%05d", paymentSuffixId);
        String paymentId = prefix + StringUtils.right(suffix, 5);

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
        example.createCriteria().andBussinessOrderIdEqualTo(bussinessOrderId)
                .andPayStatusEqualTo(payStatus);
        List<PaymentPo> pos = paymentMapper.selectByExample(example);
        if (pos == null || pos.size() == 0) {
            return null;
        }
        return Payment.convertFromPo(pos.get(0));
    }

    /**
     * 查找可以退款的Payment
     * 
     * @param bussinessOrderId
     * @param payStatus
     * @return
     */
    public Payment getPaymentCanPartRefund(String bussinessOrderId) {
        PaymentExample example = new PaymentExample();
        example.createCriteria().andBussinessOrderIdEqualTo(bussinessOrderId)
                .andPayStatusBetween(PayStatusEnum.Paied.getIndex(), PayStatusEnum.Refunded.getIndex());
        List<PaymentPo> pos = paymentMapper.selectByExample(example);
        if (pos == null || pos.size() == 0) {
            return null;
        }
        return Payment.convertFromPo(pos.get(0));
    }

    /**
     * 更新对账状态
     * 
     * @param checkStatus
     * @param paymentId
     */
    @Transactional
    public void updatePaymentCheckStatus(int checkStatus, String paymentId) {
        PaymentPo paymentPo = new PaymentPo();
        paymentPo.setCheckStatus(checkStatus);
        paymentPo.setPaymentId(paymentId);
        paymentMapper.updateByPrimaryKeySelective(paymentPo);
    }

    /**
     * 更新支付通知状态
     * 
     * @param payment
     */
    public void updatePaymentNotifyStatus(Payment payment) {
        PaymentPo paymentPo = new PaymentPo();

        paymentPo.setPaymentId(payment.getPaymentId());

        if (payment.getNotifyStatus() != null) {
            paymentPo.setNotifyStatus(payment.getNotifyStatus().code());
        }

        if (payment.getRetryCount() == null) {
            paymentPo.setRetryCount(0);
        } else {
            paymentPo.setRetryCount(payment.getRetryCount() + 1);
        }

        paymentMapper.updateByPrimaryKeySelective(paymentPo);
    }

    /**
     * 更新支付通知重试次数
     * 
     * @param payment
     */
    public void increaseRetryCount(Payment payment) {
        PaymentPo paymentPo = new PaymentPo();

        paymentPo.setPaymentId(payment.getPaymentId());
        if (payment.getRetryCount() == null) {
            paymentPo.setRetryCount(0);
        } else {
            paymentPo.setRetryCount(payment.getRetryCount() + 1);
        }

        paymentMapper.updateByPrimaryKeySelective(paymentPo);
    }
}
