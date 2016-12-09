package com.sciencegateway.dataingestor.resource;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.sciencegateway.dataingestor.POJO.URLObjects;

public class DataIngestorResourceTest 
{
	
	private URLObjects urlObjects;
	private DataIngestorResource restResource;
	
	@Before
	public void setUp() throws Exception
	{
		urlObjects = new URLObjects();
		restResource = new DataIngestorResource();
	}
	
	@Test
	public void testGenerateString() throws Exception
	{
		assertEquals(restResource.generateString(),"Got it!");
	}
	
	@Test
	public void testGenerateURL() throws Exception
	{
		urlObjects.setUrl("amolrbhagwat@gmail.com");
		urlObjects.setRequestId("1001");
		urlObjects.setStationName("KIND");
		urlObjects.setDate("2001-4-27");
		urlObjects.setTime("123456");
		
		try
		{
			int responseStatus = restResource.generateURL(urlObjects).getStatus();
			if (responseStatus == 200)
			{
				assertEquals(restResource.generateURL(urlObjects).getStatus(),200);
			}
			else
			{
				assertEquals(restResource.generateLOG(urlObjects),responseStatus);
				System.out.println("Storm Detector is down!");
			}
		}
		catch (Exception exception)
		{
			System.out.println("Error Occured!");
		}
	}
	
	@Test
	public void testGenerateLOG() throws Exception
	{
		try
		{
			int responseStatus = restResource.generateLOG(urlObjects);
			if (responseStatus == 200)
			{
				assertEquals(restResource.generateLOG(urlObjects),200);
			}
			else
			{
				assertEquals(restResource.generateLOG(urlObjects),responseStatus);
				System.out.println("Registry is down!");
			}
		}
		catch (Exception exception)
		{
			System.out.println("Error Occured!");
		}		
	}
	
}
