package com.ymatou.payment.infrastructure.db.mapper;

import com.ymatou.payment.infrastructure.db.model.RefundRequestExample;
import com.ymatou.payment.infrastructure.db.model.RefundRequestPo;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RefundRequestMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundRequest
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    int countByExample(RefundRequestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundRequest
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    int deleteByExample(RefundRequestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundRequest
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    int deleteByPrimaryKey(String paymentId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundRequest
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    int insert(RefundRequestPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundRequest
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    int insertSelective(RefundRequestPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundRequest
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    List<RefundRequestPo> selectByExampleWithBLOBs(RefundRequestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundRequest
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    List<RefundRequestPo> selectByExample(RefundRequestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundRequest
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    RefundRequestPo selectByPrimaryKey(String paymentId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundRequest
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    int updateByExampleSelective(@Param("record") RefundRequestPo record, @Param("example") RefundRequestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundRequest
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    int updateByExampleWithBLOBs(@Param("record") RefundRequestPo record, @Param("example") RefundRequestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundRequest
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    int updateByExample(@Param("record") RefundRequestPo record, @Param("example") RefundRequestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundRequest
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    int updateByPrimaryKeySelective(RefundRequestPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundRequest
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    int updateByPrimaryKeyWithBLOBs(RefundRequestPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundRequest
     *
     * @mbggenerated Wed Jun 08 14:12:44 CST 2016
     */
    int updateByPrimaryKey(RefundRequestPo record);
}