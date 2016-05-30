package com.ymatou.payment.test;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class WithDubboProviderBaseTest extends BaseTest {
	
	@Test
	public void testLoad() throws IOException{
		System.out.println("Spring start up!");
		System.in.read();
	}
}
