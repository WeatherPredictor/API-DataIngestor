package com.sciencegateway.dataingestor.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.sciencegateway.dataingestor.POJO.URLObjects;

public class URLConverterTest
{
	
	private URLObjects urlObjects;
	private URLConverter urlConverter;
	
	@Before
	public void setUp() throws Exception
	{
		urlObjects = new URLObjects();
		urlConverter = new URLConverter();
	}
	
	@Test
	public void testGetURL() throws Exception
	{
		urlObjects.setStationName("KIND");
		urlObjects.setDate("2001-4-27");
		urlObjects.setTime("123456");
		urlObjects = urlConverter.getURL(urlObjects);
		assertEquals(urlObjects.getFilename(), "KIND2001427_123456.gz");
		assertEquals(urlObjects.getUrl(),"https://noaa-nexrad-level2.s3.amazonaws.com/2001/4/27/KIND/KIND2001427_123456.gz");
	}
	
}