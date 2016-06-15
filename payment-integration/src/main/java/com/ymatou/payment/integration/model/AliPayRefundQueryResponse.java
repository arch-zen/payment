/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */
package com.ymatou.payment.integration.model;

import java.math.BigDecimal;

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
        private ChargeAliRefundStatusType chargeRefundStatus;
        private Boolean isRefundOk;
        private boolean isRefundStepOneOk;
        private String instPaymentId;
        private String batchNo;
        private BigDecimal refundAmount;

        public ChargeAliRefundStatusType getChargeRefundStatus() {
            return chargeRefundStatus;
        }

        public void setChargeRefundStatus(ChargeAliRefundStatusType chargeRefundStatus) {
            this.chargeRefundStatus = chargeRefundStatus;
        }

        public Boolean isRefundOk() {
            return isRefundOk;
        }

        public void setRefundOk(Boolean isRefundOk) {
            this.isRefundOk = isRefundOk;
        }

        public boolean isRefundStepOneOk() {
            return isRefundStepOneOk;
        }

        public void setRefundStepOneOk(boolean isRefundStepOneOk) {
            this.isRefundStepOneOk = isRefundStepOneOk;
        }

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
    }

    public static enum ChargeAliRefundStatusType {
        NA, PROCESSING, SUCCESS, FAILED,
    }

    public RefundDetailData resolveResultDetails() {
        // 201012300001^2010123016346858^0.02^SUCCESS|zen_gwen@hotmail.com^2088102210397302^alipay-test03@alipay.com^2088101568345155^0.01^SUCCESS
        String data = this.resultDetails;
        RefundDetailData detail = null;
        if (data == null) {
            return null;
        }
        String[] tempData = data.split("\\|");
        if (tempData == null || tempData.length == 0) {
            throw new IllegalArgumentException(data + "is not valid");
        }
        String[] refundTempData = tempData[0].split("$");
        if (refundTempData == null || refundTempData.length == 0) {
            throw new IllegalArgumentException(data + "is not valid");
        }
        String[] refundDetailTempData = refundTempData[0].split("\\^");
        if (refundDetailTempData == null || refundDetailTempData.length == 0) {
            throw new IllegalArgumentException(data + "is not valid");
        }

        boolean isFirstStepOk = "SUCCESS".equalsIgnoreCase(refundDetailTempData[3]);
        if (refundDetailTempData.length == 4) {
            detail = new RefundDetailData();
            detail.setBatchNo(refundDetailTempData[0]);
            detail.setInstPaymentId(refundDetailTempData[1]);
            detail.setRefundAmount(new BigDecimal(refundDetailTempData[2]));
            detail.setRefundStepOneOk(isFirstStepOk);
            detail.setChargeRefundStatus(ChargeAliRefundStatusType.NA);

            detail.setRefundOk(detail.isRefundStepOneOk()); // 由于无法获取充退信息，所以只能断言退款成功
        }

        if (refundDetailTempData.length == 6) {
            if (isFirstStepOk && "false".equalsIgnoreCase(refundDetailTempData[4])) {
                detail = new RefundDetailData();
                detail.setBatchNo(refundDetailTempData[0]);
                detail.setInstPaymentId(refundDetailTempData[1]);
                detail.setRefundAmount(new BigDecimal(refundDetailTempData[2]));
                detail.setRefundStepOneOk(isFirstStepOk);
                detail.setChargeRefundStatus(ChargeAliRefundStatusType.NA);
                detail.setRefundOk(detail.isRefundStepOneOk());
            } else {
                String refundStatusString = refundDetailTempData[5];
                ChargeAliRefundStatusType chargeAliRefundStatus;
                switch (refundStatusString.toLowerCase()) {
                    case "s":
                        chargeAliRefundStatus = ChargeAliRefundStatusType.SUCCESS;
                        break;
                    case "f": // 充退结果为f的情况，支付宝会处理为退余额
                        chargeAliRefundStatus = ChargeAliRefundStatusType.SUCCESS;
                        break;
                    case "p":
                        chargeAliRefundStatus = ChargeAliRefundStatusType.PROCESSING;
                        break;
                    case "null":
                    default:
                        chargeAliRefundStatus = ChargeAliRefundStatusType.NA;
                        break;
                }

                Boolean isRefundOk;
                switch (chargeAliRefundStatus) {
                    case PROCESSING:
                        isRefundOk = null;
                        break;
                    case SUCCESS:
                        isRefundOk = true;
                        break;
                    case NA:
                    case FAILED:
                    default:
                        isRefundOk = false;
                        break;
                }

                detail = new RefundDetailData();
                detail.setBatchNo(refundDetailTempData[0]);
                detail.setInstPaymentId(refundDetailTempData[1]);
                detail.setRefundAmount(new BigDecimal(refundDetailTempData[2]));
                detail.setRefundStepOneOk(isFirstStepOk);
                detail.setChargeRefundStatus(chargeAliRefundStatus);
                detail.setRefundOk(isRefundOk);
            }

        }
        return detail;
    }
}
