package com.sciencegateway.dataingestor.POJO;

import org.junit.Test;
import org.meanbean.test.BeanTester;

public class URLObjectsTest
{
	
	@Test
	public void testPOJO() throws Exception
	{
		new BeanTester().testBean(URLObjects.class);
	}
}