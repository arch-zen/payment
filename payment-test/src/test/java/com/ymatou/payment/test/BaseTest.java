package com.ymatou.payment.test;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Before;
import org.springframework.jdbc.core.JdbcTemplate;

public class BaseTest {
	/**
	 * 方便测试用例做些临时的数据库操作
	 */
    protected JdbcTemplate jdbcTemplate;

    @Resource(name = "dataSource")
    private DataSource dataSource;

    @Before
    public void prepare() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
