/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.payment.test;

import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContextRestTest.xml"})
public class RestBaseTest extends BaseTest {

    @Autowired
    private SqlSession sqlSession;

    /**
     * 构建MockHeader
     * 
     * @return
     */
    protected HashMap<String, String> buildMockHeader() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("mock", "1");
        header.put("mockId", UUID.randomUUID().toString());

        return header;
    }

    /**
     * 构建带Mock
     * 
     * @param servletRequest
     */
    protected MockHttpServletRequest buildServletRequestWithMock() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader("mock", 1);
        servletRequest.addHeader("mockId", UUID.randomUUID().toString());

        return servletRequest;
    }


    /**
     * 获取到资金流水
     * 
     * @param bizNo
     * @return
     */
    protected AccountEntry getAccountEntry(String bizNo) {
        return sqlSession.selectOne("test-accountEntry.selectAccountEntry", bizNo);
    }

    /**
     * 获取到账户信息
     * 
     * @param accountId
     * @return
     */
    protected AccountInfo getAccountInfo(String accountId) {
        return sqlSession.selectOne("test-accountInfo.selectAccount", accountId);
    }
}
