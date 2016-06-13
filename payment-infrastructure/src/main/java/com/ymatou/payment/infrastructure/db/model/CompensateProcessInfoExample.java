package com.ymatou.payment.infrastructure.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompensateProcessInfoExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table PP_CompensateProcessInfo
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table PP_CompensateProcessInfo
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table PP_CompensateProcessInfo
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PP_CompensateProcessInfo
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    public CompensateProcessInfoExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PP_CompensateProcessInfo
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PP_CompensateProcessInfo
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PP_CompensateProcessInfo
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PP_CompensateProcessInfo
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PP_CompensateProcessInfo
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PP_CompensateProcessInfo
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PP_CompensateProcessInfo
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PP_CompensateProcessInfo
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PP_CompensateProcessInfo
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PP_CompensateProcessInfo
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table PP_CompensateProcessInfo
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andCompensateIdIsNull() {
            addCriterion("CompensateId is null");
            return (Criteria) this;
        }

        public Criteria andCompensateIdIsNotNull() {
            addCriterion("CompensateId is not null");
            return (Criteria) this;
        }

        public Criteria andCompensateIdEqualTo(Long value) {
            addCriterion("CompensateId =", value, "compensateId");
            return (Criteria) this;
        }

        public Criteria andCompensateIdNotEqualTo(Long value) {
            addCriterion("CompensateId <>", value, "compensateId");
            return (Criteria) this;
        }

        public Criteria andCompensateIdGreaterThan(Long value) {
            addCriterion("CompensateId >", value, "compensateId");
            return (Criteria) this;
        }

        public Criteria andCompensateIdGreaterThanOrEqualTo(Long value) {
            addCriterion("CompensateId >=", value, "compensateId");
            return (Criteria) this;
        }

        public Criteria andCompensateIdLessThan(Long value) {
            addCriterion("CompensateId <", value, "compensateId");
            return (Criteria) this;
        }

        public Criteria andCompensateIdLessThanOrEqualTo(Long value) {
            addCriterion("CompensateId <=", value, "compensateId");
            return (Criteria) this;
        }

        public Criteria andCompensateIdIn(List<Long> values) {
            addCriterion("CompensateId in", values, "compensateId");
            return (Criteria) this;
        }

        public Criteria andCompensateIdNotIn(List<Long> values) {
            addCriterion("CompensateId not in", values, "compensateId");
            return (Criteria) this;
        }

        public Criteria andCompensateIdBetween(Long value1, Long value2) {
            addCriterion("CompensateId between", value1, value2, "compensateId");
            return (Criteria) this;
        }

        public Criteria andCompensateIdNotBetween(Long value1, Long value2) {
            addCriterion("CompensateId not between", value1, value2, "compensateId");
            return (Criteria) this;
        }

        public Criteria andCorrelateIdIsNull() {
            addCriterion("CorrelateId is null");
            return (Criteria) this;
        }

        public Criteria andCorrelateIdIsNotNull() {
            addCriterion("CorrelateId is not null");
            return (Criteria) this;
        }

        public Criteria andCorrelateIdEqualTo(String value) {
            addCriterion("CorrelateId =", value, "correlateId");
            return (Criteria) this;
        }

        public Criteria andCorrelateIdNotEqualTo(String value) {
            addCriterion("CorrelateId <>", value, "correlateId");
            return (Criteria) this;
        }

        public Criteria andCorrelateIdGreaterThan(String value) {
            addCriterion("CorrelateId >", value, "correlateId");
            return (Criteria) this;
        }

        public Criteria andCorrelateIdGreaterThanOrEqualTo(String value) {
            addCriterion("CorrelateId >=", value, "correlateId");
            return (Criteria) this;
        }

        public Criteria andCorrelateIdLessThan(String value) {
            addCriterion("CorrelateId <", value, "correlateId");
            return (Criteria) this;
        }

        public Criteria andCorrelateIdLessThanOrEqualTo(String value) {
            addCriterion("CorrelateId <=", value, "correlateId");
            return (Criteria) this;
        }

        public Criteria andCorrelateIdLike(String value) {
            addCriterion("CorrelateId like", value, "correlateId");
            return (Criteria) this;
        }

        public Criteria andCorrelateIdNotLike(String value) {
            addCriterion("CorrelateId not like", value, "correlateId");
            return (Criteria) this;
        }

        public Criteria andCorrelateIdIn(List<String> values) {
            addCriterion("CorrelateId in", values, "correlateId");
            return (Criteria) this;
        }

        public Criteria andCorrelateIdNotIn(List<String> values) {
            addCriterion("CorrelateId not in", values, "correlateId");
            return (Criteria) this;
        }

        public Criteria andCorrelateIdBetween(String value1, String value2) {
            addCriterion("CorrelateId between", value1, value2, "correlateId");
            return (Criteria) this;
        }

        public Criteria andCorrelateIdNotBetween(String value1, String value2) {
            addCriterion("CorrelateId not between", value1, value2, "correlateId");
            return (Criteria) this;
        }

        public Criteria andAppIdIsNull() {
            addCriterion("AppId is null");
            return (Criteria) this;
        }

        public Criteria andAppIdIsNotNull() {
            addCriterion("AppId is not null");
            return (Criteria) this;
        }

        public Criteria andAppIdEqualTo(String value) {
            addCriterion("AppId =", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotEqualTo(String value) {
            addCriterion("AppId <>", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdGreaterThan(String value) {
            addCriterion("AppId >", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdGreaterThanOrEqualTo(String value) {
            addCriterion("AppId >=", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLessThan(String value) {
            addCriterion("AppId <", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLessThanOrEqualTo(String value) {
            addCriterion("AppId <=", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLike(String value) {
            addCriterion("AppId like", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotLike(String value) {
            addCriterion("AppId not like", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdIn(List<String> values) {
            addCriterion("AppId in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotIn(List<String> values) {
            addCriterion("AppId not in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdBetween(String value1, String value2) {
            addCriterion("AppId between", value1, value2, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotBetween(String value1, String value2) {
            addCriterion("AppId not between", value1, value2, "appId");
            return (Criteria) this;
        }

        public Criteria andPayTypeIsNull() {
            addCriterion("PayType is null");
            return (Criteria) this;
        }

        public Criteria andPayTypeIsNotNull() {
            addCriterion("PayType is not null");
            return (Criteria) this;
        }

        public Criteria andPayTypeEqualTo(String value) {
            addCriterion("PayType =", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotEqualTo(String value) {
            addCriterion("PayType <>", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeGreaterThan(String value) {
            addCriterion("PayType >", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeGreaterThanOrEqualTo(String value) {
            addCriterion("PayType >=", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeLessThan(String value) {
            addCriterion("PayType <", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeLessThanOrEqualTo(String value) {
            addCriterion("PayType <=", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeLike(String value) {
            addCriterion("PayType like", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotLike(String value) {
            addCriterion("PayType not like", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeIn(List<String> values) {
            addCriterion("PayType in", values, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotIn(List<String> values) {
            addCriterion("PayType not in", values, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeBetween(String value1, String value2) {
            addCriterion("PayType between", value1, value2, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotBetween(String value1, String value2) {
            addCriterion("PayType not between", value1, value2, "payType");
            return (Criteria) this;
        }

        public Criteria andMethodNameIsNull() {
            addCriterion("MethodName is null");
            return (Criteria) this;
        }

        public Criteria andMethodNameIsNotNull() {
            addCriterion("MethodName is not null");
            return (Criteria) this;
        }

        public Criteria andMethodNameEqualTo(String value) {
            addCriterion("MethodName =", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameNotEqualTo(String value) {
            addCriterion("MethodName <>", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameGreaterThan(String value) {
            addCriterion("MethodName >", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameGreaterThanOrEqualTo(String value) {
            addCriterion("MethodName >=", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameLessThan(String value) {
            addCriterion("MethodName <", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameLessThanOrEqualTo(String value) {
            addCriterion("MethodName <=", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameLike(String value) {
            addCriterion("MethodName like", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameNotLike(String value) {
            addCriterion("MethodName not like", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameIn(List<String> values) {
            addCriterion("MethodName in", values, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameNotIn(List<String> values) {
            addCriterion("MethodName not in", values, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameBetween(String value1, String value2) {
            addCriterion("MethodName between", value1, value2, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameNotBetween(String value1, String value2) {
            addCriterion("MethodName not between", value1, value2, "methodName");
            return (Criteria) this;
        }

        public Criteria andRequestUrlIsNull() {
            addCriterion("RequestUrl is null");
            return (Criteria) this;
        }

        public Criteria andRequestUrlIsNotNull() {
            addCriterion("RequestUrl is not null");
            return (Criteria) this;
        }

        public Criteria andRequestUrlEqualTo(String value) {
            addCriterion("RequestUrl =", value, "requestUrl");
            return (Criteria) this;
        }

        public Criteria andRequestUrlNotEqualTo(String value) {
            addCriterion("RequestUrl <>", value, "requestUrl");
            return (Criteria) this;
        }

        public Criteria andRequestUrlGreaterThan(String value) {
            addCriterion("RequestUrl >", value, "requestUrl");
            return (Criteria) this;
        }

        public Criteria andRequestUrlGreaterThanOrEqualTo(String value) {
            addCriterion("RequestUrl >=", value, "requestUrl");
            return (Criteria) this;
        }

        public Criteria andRequestUrlLessThan(String value) {
            addCriterion("RequestUrl <", value, "requestUrl");
            return (Criteria) this;
        }

        public Criteria andRequestUrlLessThanOrEqualTo(String value) {
            addCriterion("RequestUrl <=", value, "requestUrl");
            return (Criteria) this;
        }

        public Criteria andRequestUrlLike(String value) {
            addCriterion("RequestUrl like", value, "requestUrl");
            return (Criteria) this;
        }

        public Criteria andRequestUrlNotLike(String value) {
            addCriterion("RequestUrl not like", value, "requestUrl");
            return (Criteria) this;
        }

        public Criteria andRequestUrlIn(List<String> values) {
            addCriterion("RequestUrl in", values, "requestUrl");
            return (Criteria) this;
        }

        public Criteria andRequestUrlNotIn(List<String> values) {
            addCriterion("RequestUrl not in", values, "requestUrl");
            return (Criteria) this;
        }

        public Criteria andRequestUrlBetween(String value1, String value2) {
            addCriterion("RequestUrl between", value1, value2, "requestUrl");
            return (Criteria) this;
        }

        public Criteria andRequestUrlNotBetween(String value1, String value2) {
            addCriterion("RequestUrl not between", value1, value2, "requestUrl");
            return (Criteria) this;
        }

        public Criteria andRetryCountIsNull() {
            addCriterion("RetryCount is null");
            return (Criteria) this;
        }

        public Criteria andRetryCountIsNotNull() {
            addCriterion("RetryCount is not null");
            return (Criteria) this;
        }

        public Criteria andRetryCountEqualTo(Integer value) {
            addCriterion("RetryCount =", value, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountNotEqualTo(Integer value) {
            addCriterion("RetryCount <>", value, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountGreaterThan(Integer value) {
            addCriterion("RetryCount >", value, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("RetryCount >=", value, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountLessThan(Integer value) {
            addCriterion("RetryCount <", value, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountLessThanOrEqualTo(Integer value) {
            addCriterion("RetryCount <=", value, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountIn(List<Integer> values) {
            addCriterion("RetryCount in", values, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountNotIn(List<Integer> values) {
            addCriterion("RetryCount not in", values, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountBetween(Integer value1, Integer value2) {
            addCriterion("RetryCount between", value1, value2, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountNotBetween(Integer value1, Integer value2) {
            addCriterion("RetryCount not between", value1, value2, "retryCount");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIsNull() {
            addCriterion("CreatedTime is null");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIsNotNull() {
            addCriterion("CreatedTime is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeEqualTo(Date value) {
            addCriterion("CreatedTime =", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotEqualTo(Date value) {
            addCriterion("CreatedTime <>", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeGreaterThan(Date value) {
            addCriterion("CreatedTime >", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CreatedTime >=", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeLessThan(Date value) {
            addCriterion("CreatedTime <", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeLessThanOrEqualTo(Date value) {
            addCriterion("CreatedTime <=", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIn(List<Date> values) {
            addCriterion("CreatedTime in", values, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotIn(List<Date> values) {
            addCriterion("CreatedTime not in", values, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeBetween(Date value1, Date value2) {
            addCriterion("CreatedTime between", value1, value2, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotBetween(Date value1, Date value2) {
            addCriterion("CreatedTime not between", value1, value2, "createdTime");
            return (Criteria) this;
        }

        public Criteria andProcessMachineNameIsNull() {
            addCriterion("ProcessMachineName is null");
            return (Criteria) this;
        }

        public Criteria andProcessMachineNameIsNotNull() {
            addCriterion("ProcessMachineName is not null");
            return (Criteria) this;
        }

        public Criteria andProcessMachineNameEqualTo(String value) {
            addCriterion("ProcessMachineName =", value, "processMachineName");
            return (Criteria) this;
        }

        public Criteria andProcessMachineNameNotEqualTo(String value) {
            addCriterion("ProcessMachineName <>", value, "processMachineName");
            return (Criteria) this;
        }

        public Criteria andProcessMachineNameGreaterThan(String value) {
            addCriterion("ProcessMachineName >", value, "processMachineName");
            return (Criteria) this;
        }

        public Criteria andProcessMachineNameGreaterThanOrEqualTo(String value) {
            addCriterion("ProcessMachineName >=", value, "processMachineName");
            return (Criteria) this;
        }

        public Criteria andProcessMachineNameLessThan(String value) {
            addCriterion("ProcessMachineName <", value, "processMachineName");
            return (Criteria) this;
        }

        public Criteria andProcessMachineNameLessThanOrEqualTo(String value) {
            addCriterion("ProcessMachineName <=", value, "processMachineName");
            return (Criteria) this;
        }

        public Criteria andProcessMachineNameLike(String value) {
            addCriterion("ProcessMachineName like", value, "processMachineName");
            return (Criteria) this;
        }

        public Criteria andProcessMachineNameNotLike(String value) {
            addCriterion("ProcessMachineName not like", value, "processMachineName");
            return (Criteria) this;
        }

        public Criteria andProcessMachineNameIn(List<String> values) {
            addCriterion("ProcessMachineName in", values, "processMachineName");
            return (Criteria) this;
        }

        public Criteria andProcessMachineNameNotIn(List<String> values) {
            addCriterion("ProcessMachineName not in", values, "processMachineName");
            return (Criteria) this;
        }

        public Criteria andProcessMachineNameBetween(String value1, String value2) {
            addCriterion("ProcessMachineName between", value1, value2, "processMachineName");
            return (Criteria) this;
        }

        public Criteria andProcessMachineNameNotBetween(String value1, String value2) {
            addCriterion("ProcessMachineName not between", value1, value2, "processMachineName");
            return (Criteria) this;
        }

        public Criteria andLastProcessedTimeIsNull() {
            addCriterion("LastProcessedTime is null");
            return (Criteria) this;
        }

        public Criteria andLastProcessedTimeIsNotNull() {
            addCriterion("LastProcessedTime is not null");
            return (Criteria) this;
        }

        public Criteria andLastProcessedTimeEqualTo(Date value) {
            addCriterion("LastProcessedTime =", value, "lastProcessedTime");
            return (Criteria) this;
        }

        public Criteria andLastProcessedTimeNotEqualTo(Date value) {
            addCriterion("LastProcessedTime <>", value, "lastProcessedTime");
            return (Criteria) this;
        }

        public Criteria andLastProcessedTimeGreaterThan(Date value) {
            addCriterion("LastProcessedTime >", value, "lastProcessedTime");
            return (Criteria) this;
        }

        public Criteria andLastProcessedTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("LastProcessedTime >=", value, "lastProcessedTime");
            return (Criteria) this;
        }

        public Criteria andLastProcessedTimeLessThan(Date value) {
            addCriterion("LastProcessedTime <", value, "lastProcessedTime");
            return (Criteria) this;
        }

        public Criteria andLastProcessedTimeLessThanOrEqualTo(Date value) {
            addCriterion("LastProcessedTime <=", value, "lastProcessedTime");
            return (Criteria) this;
        }

        public Criteria andLastProcessedTimeIn(List<Date> values) {
            addCriterion("LastProcessedTime in", values, "lastProcessedTime");
            return (Criteria) this;
        }

        public Criteria andLastProcessedTimeNotIn(List<Date> values) {
            addCriterion("LastProcessedTime not in", values, "lastProcessedTime");
            return (Criteria) this;
        }

        public Criteria andLastProcessedTimeBetween(Date value1, Date value2) {
            addCriterion("LastProcessedTime between", value1, value2, "lastProcessedTime");
            return (Criteria) this;
        }

        public Criteria andLastProcessedTimeNotBetween(Date value1, Date value2) {
            addCriterion("LastProcessedTime not between", value1, value2, "lastProcessedTime");
            return (Criteria) this;
        }

        public Criteria andProcessStatusIsNull() {
            addCriterion("ProcessStatus is null");
            return (Criteria) this;
        }

        public Criteria andProcessStatusIsNotNull() {
            addCriterion("ProcessStatus is not null");
            return (Criteria) this;
        }

        public Criteria andProcessStatusEqualTo(Integer value) {
            addCriterion("ProcessStatus =", value, "processStatus");
            return (Criteria) this;
        }

        public Criteria andProcessStatusNotEqualTo(Integer value) {
            addCriterion("ProcessStatus <>", value, "processStatus");
            return (Criteria) this;
        }

        public Criteria andProcessStatusGreaterThan(Integer value) {
            addCriterion("ProcessStatus >", value, "processStatus");
            return (Criteria) this;
        }

        public Criteria andProcessStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("ProcessStatus >=", value, "processStatus");
            return (Criteria) this;
        }

        public Criteria andProcessStatusLessThan(Integer value) {
            addCriterion("ProcessStatus <", value, "processStatus");
            return (Criteria) this;
        }

        public Criteria andProcessStatusLessThanOrEqualTo(Integer value) {
            addCriterion("ProcessStatus <=", value, "processStatus");
            return (Criteria) this;
        }

        public Criteria andProcessStatusIn(List<Integer> values) {
            addCriterion("ProcessStatus in", values, "processStatus");
            return (Criteria) this;
        }

        public Criteria andProcessStatusNotIn(List<Integer> values) {
            addCriterion("ProcessStatus not in", values, "processStatus");
            return (Criteria) this;
        }

        public Criteria andProcessStatusBetween(Integer value1, Integer value2) {
            addCriterion("ProcessStatus between", value1, value2, "processStatus");
            return (Criteria) this;
        }

        public Criteria andProcessStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("ProcessStatus not between", value1, value2, "processStatus");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeIsNull() {
            addCriterion("CompensateType is null");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeIsNotNull() {
            addCriterion("CompensateType is not null");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeEqualTo(Integer value) {
            addCriterion("CompensateType =", value, "compensateType");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeNotEqualTo(Integer value) {
            addCriterion("CompensateType <>", value, "compensateType");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeGreaterThan(Integer value) {
            addCriterion("CompensateType >", value, "compensateType");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("CompensateType >=", value, "compensateType");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeLessThan(Integer value) {
            addCriterion("CompensateType <", value, "compensateType");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeLessThanOrEqualTo(Integer value) {
            addCriterion("CompensateType <=", value, "compensateType");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeIn(List<Integer> values) {
            addCriterion("CompensateType in", values, "compensateType");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeNotIn(List<Integer> values) {
            addCriterion("CompensateType not in", values, "compensateType");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeBetween(Integer value1, Integer value2) {
            addCriterion("CompensateType between", value1, value2, "compensateType");
            return (Criteria) this;
        }

        public Criteria andCompensateTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("CompensateType not between", value1, value2, "compensateType");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table PP_CompensateProcessInfo
     *
     * @mbggenerated do_not_delete_during_merge Wed Jun 08 14:12:44 CST 2016
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table PP_CompensateProcessInfo
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}