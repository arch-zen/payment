/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

import java.math.BigDecimal;

import com.ymatou.payment.facade.constants.RefundStatusEnum;

/**
 * 支付宝退款查询接口Response
 * 
 * @author qianmin 2016年5月31日 下午2:50:35
 *
 */
public class AliPayRefundQueryResponse {
    /**
     * 是否成功(T：查询成功； F：查询失败)
     */
    private String isSuccess;
    /**
     * 退款结果明细
     */
    private String resultDetails;
    /**
     * 解冻结果明细
     */
    private String unfreezedDetails;
    /**
     * 错误代码
     */
    private String errorCode;
    /**
     * 签名方式
     */
    private String signType;
    /**
     * 签名
     */
    private String sign;

    /**
     * 原始应答
     */
    private String originalResponse;

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getResultDetails() {
        return resultDetails;
    }

    public void setResultDetails(String resultDetails) {
        this.resultDetails = resultDetails;
    }

    public String getUnfreezedDetails() {
        return unfreezedDetails;
    }

    public void setUnfreezedDetails(String unfreezedDetails) {
        this.unfreezedDetails = unfreezedDetails;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOriginalResponse() {
        return originalResponse;
    }

    public void setOriginalResponse(String originalResponse) {
        this.originalResponse = originalResponse;
    }

    public static class RefundDetailData {
        private String instPaymentId;
        private String batchNo;
        private BigDecimal refundAmount;
        private RefundStatusEnum refundStatus;

        public String getInstPaymentId() {
            return instPaymentId;
        }

        public void setInstPaymentId(String instPaymentId) {
            this.instPaymentId = instPaymentId;
        }

        public String getBatchNo() {
            return batchNo;
        }

        public void setBatchNo(String batchNo) {
            this.batchNo = batchNo;
        }

        public BigDecimal getRefundAmount() {
            return refundAmount;
        }

        public void setRefundAmount(BigDecimal refundAmount) {
            this.refundAmount = refundAmount;
        }

        public RefundStatusEnum getRefundStatus() {
            return refundStatus;
        }

        public void setRefundStatus(RefundStatusEnum refundStatus) {
            this.refundStatus = refundStatus;
        }
    }

    /*
     * is_success=T&result_details=201606160000375820^20160415210010255^210.00^SUCCESS^true^P
     * is_success=T&result_details=201606150000373568^20160527210010776^195.00^SUCCESS^true^S
     * is_success=T&result_details=201510160000000001^20151016210010640^2.40^SUCCESS^false^null
     * is_success=T&result_details=201607130000409537^20151016210010640^252.00^TRADE_HAS_CLOSED^
     * false^null
     * is_success=T&result_details=201612030000706479^2016111821001004690283673855^1879.00^
     * SELLER_BALANCE_NOT_ENOUGH^false^null
     * is_success=F&error_code=REFUND_NOT_EXIST
     */
    public RefundDetailData resolveResultDetails() {
        RefundDetailData detail = null;
        if (this.resultDetails == null) {
            return null;
        }
        String[] tempData = this.resultDetails.split("\\|");
        String[] refundTempData = tempData[0].split("$");
        String[] refundDetailTempData = refundTempData[0].split("\\^");

        // result_details=BatchNo^InstPaymentId^RefundAmount^ProcessResult^isChargeBack^ChargeBackResult
        RefundStatusEnum refundStatus = null;
        if (refundDetailTempData.length == 4) {
            // resultDetail长度为4时， 以处理结果为准
            if ("SUCCESS".equalsIgnoreCase(refundDetailTempData[3])) {
                refundStatus = RefundStatusEnum.THIRDPART_REFUND_SUCCESS;
            } else {
                refundStatus = RefundStatusEnum.REFUND_FAILED;
            }
        } else if (refundDetailTempData.length == 6) {
            if ("false".equalsIgnoreCase(refundDetailTempData[4])) {
                // 非充退时， 以处理结果为准
                if ("SUCCESS".equalsIgnoreCase(refundDetailTempData[3])) {
                    refundStatus = RefundStatusEnum.THIRDPART_REFUND_SUCCESS;
                } else if ("TRADE_HAS_CLOSED".equalsIgnoreCase(refundDetailTempData[3])) {
                    refundStatus = RefundStatusEnum.COMPLETE_FAILED;
                } else if ("SELLER_BALANCE_NOT_ENOUGH".equalsIgnoreCase(refundDetailTempData[3])) { // 商家余额不足，此时需要将退款单设置为初始化转态，等待重新提交
                    refundStatus = RefundStatusEnum.INIT;
                } else {
                    refundStatus = RefundStatusEnum.REFUND_FAILED;
                }
            } else {
                // 冲退时，以充退结果为准
                switch (refundDetailTempData[5].toUpperCase()) {
                    case "S":
                    case "F":
                        refundStatus = RefundStatusEnum.THIRDPART_REFUND_SUCCESS;
                        break;
                    case "P":
                        refundStatus = RefundStatusEnum.WAIT_THIRDPART_REFUND;
                        break;
                    case "null":
                    default:
                        refundStatus = RefundStatusEnum.REFUND_FAILED;
                        break;
                }
            }
        } else {
            refundStatus = RefundStatusEnum.REFUND_FAILED;
        }

        detail = new RefundDetailData();
        detail.setBatchNo(refundDetailTempData[0]);
        detail.setInstPaymentId(refundDetailTempData[1]);
        detail.setRefundAmount(new BigDecimal(refundDetailTempData[2]));
        detail.setRefundStatus(refundStatus);
        return detail;
    }
}
