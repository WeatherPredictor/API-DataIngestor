package com.sciencegateway.dataingestor.resource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.json.JSONObject;

import com.sciencegateway.dataingestor.POJO.URLObjects;
import com.sciencegateway.dataingestor.service.URLConverter;

@Path("/service")
public class DataIngestorResource 
{	
	
	private static Logger logger = Logger.getLogger(DataIngestorResource.class);
	
	private URLConverter urlConverter = new URLConverter();
	
	@GET
	@Path("/try")
	@Produces(MediaType.TEXT_PLAIN)
	public String generateString() throws IOException
	{
		return "Got it!";
	}
	
	@POST
	@Path("/url")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response generateURL(URLObjects urlObjects)	
	{
		logger.info("Receiving data from UI...");
		logger.info(urlObjects);
		logger.info("Sending URL to Storm Detector...");
		JSONObject jsonObject = new JSONObject();
		try 
		{
			urlObjects = urlConverter.getURL(urlObjects);
			logger.info(urlObjects);
			jsonObject.put("requestId", urlObjects.getRequestId());
			jsonObject.put("userName", urlObjects.getUserName());
			jsonObject.put("url", urlObjects.getUrl());	
			logger.info(jsonObject.toString());
			
			generateLOG(urlObjects);
			return Response.ok(jsonObject.toString(), MediaType.APPLICATION_JSON).build();
		} 
		catch (MalformedURLException | ParseException exception)
		{
			exception.printStackTrace();
			logger.error(exception.toString(),exception);
			return Response.status(Status.BAD_REQUEST).entity(exception.getMessage()).build();
		}		
		catch (Exception exception)
		{
			logger.error(exception.toString(),exception);
			return Response.ok(jsonObject.toString(), MediaType.APPLICATION_JSON).build();
		}
	}
	
	public int generateLOG(URLObjects urlObjects) throws Exception
	{
		logger.info("Sending log Registry...");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("requestId", urlObjects.getRequestId());
		jsonObject.put("userName", urlObjects.getUserName());
		jsonObject.put("serviceName", "Data Ingestor");
		jsonObject.put("description", urlObjects.getUrl());
		logger.info(jsonObject.toString());
		ClientConfig clientConfigR = new ClientConfig();
		Client clientR = ClientBuilder.newClient(clientConfigR);
		clientR.property(ClientProperties.CONNECT_TIMEOUT, 5000);
		WebTarget targetR = clientR.target("http://35.164.24.104:8080/registry/v1/service/logger");
		logger.info(targetR.toString());
		Response responseToR = targetR.request().post(Entity.entity(jsonObject.toString(), "application/json"),Response.class);
		logger.info(responseToR.toString());
		return responseToR.getStatus();
	}
	
}
