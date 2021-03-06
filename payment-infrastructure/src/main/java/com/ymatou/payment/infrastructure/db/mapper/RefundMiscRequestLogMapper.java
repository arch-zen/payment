package com.ymatou.payment.infrastructure.db.mapper;

import com.ymatou.payment.infrastructure.db.model.RefundMiscRequestLogExample;
import com.ymatou.payment.infrastructure.db.model.RefundMiscRequestLogPo;
import com.ymatou.payment.infrastructure.db.model.RefundMiscRequestLogWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RefundMiscRequestLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundMiscRequestLog
     *
     * @mbggenerated Mon Feb 27 18:24:23 CST 2017
     */
    int countByExample(RefundMiscRequestLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundMiscRequestLog
     *
     * @mbggenerated Mon Feb 27 18:24:23 CST 2017
     */
    int deleteByExample(RefundMiscRequestLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundMiscRequestLog
     *
     * @mbggenerated Mon Feb 27 18:24:23 CST 2017
     */
    int deleteByPrimaryKey(String logId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundMiscRequestLog
     *
     * @mbggenerated Mon Feb 27 18:24:23 CST 2017
     */
    int insert(RefundMiscRequestLogWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundMiscRequestLog
     *
     * @mbggenerated Mon Feb 27 18:24:23 CST 2017
     */
    int insertSelective(RefundMiscRequestLogWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundMiscRequestLog
     *
     * @mbggenerated Mon Feb 27 18:24:23 CST 2017
     */
    List<RefundMiscRequestLogWithBLOBs> selectByExampleWithBLOBs(RefundMiscRequestLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundMiscRequestLog
     *
     * @mbggenerated Mon Feb 27 18:24:23 CST 2017
     */
    List<RefundMiscRequestLogPo> selectByExample(RefundMiscRequestLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundMiscRequestLog
     *
     * @mbggenerated Mon Feb 27 18:24:23 CST 2017
     */
    RefundMiscRequestLogWithBLOBs selectByPrimaryKey(String logId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundMiscRequestLog
     *
     * @mbggenerated Mon Feb 27 18:24:23 CST 2017
     */
    int updateByExampleSelective(@Param("record") RefundMiscRequestLogWithBLOBs record, @Param("example") RefundMiscRequestLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundMiscRequestLog
     *
     * @mbggenerated Mon Feb 27 18:24:23 CST 2017
     */
    int updateByExampleWithBLOBs(@Param("record") RefundMiscRequestLogWithBLOBs record, @Param("example") RefundMiscRequestLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundMiscRequestLog
     *
     * @mbggenerated Mon Feb 27 18:24:23 CST 2017
     */
    int updateByExample(@Param("record") RefundMiscRequestLogPo record, @Param("example") RefundMiscRequestLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundMiscRequestLog
     *
     * @mbggenerated Mon Feb 27 18:24:23 CST 2017
     */
    int updateByPrimaryKeySelective(RefundMiscRequestLogWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundMiscRequestLog
     *
     * @mbggenerated Mon Feb 27 18:24:23 CST 2017
     */
    int updateByPrimaryKeyWithBLOBs(RefundMiscRequestLogWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table RefundMiscRequestLog
     *
     * @mbggenerated Mon Feb 27 18:24:23 CST 2017
     */
    int updateByPrimaryKey(RefundMiscRequestLogPo record);
}