package com.sciencegateway.dataingestor.resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.json.JSONObject;

//This is just a test class to test the init and delegate functionalities of Zookeeper

@Path("/req")
public class Testing 
{

	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	public Response test()
	{
		String ip = null;
    	try
    	{
	    	URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(
							whatismyip.openStream()));
	
			ip = in.readLine(); //you get the IP as a String
			System.out.println(ip);
    	}
    	catch (Exception exception)
    	{
    		exception.printStackTrace();
    	}
		
		ClientConfig clientConfigR = new ClientConfig();
		Client clientR = ClientBuilder.newClient(clientConfigR);
		clientR.property(ClientProperties.CONNECT_TIMEOUT, 5000);
		WebTarget targetR = clientR.target("http://"+ip+":9000/dataingestor/webapi/ingestor/delegate");
		String url = targetR.request().get(String.class);
		System.out.println(url);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("stationName", "KIND");
		jsonObject.put("date", "2000-08-28");
		jsonObject.put("time", "123456");
		jsonObject.put("requestId", "1001");
		jsonObject.put("userName", "Sneha");
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		clientR.property(ClientProperties.CONNECT_TIMEOUT, 5000);
		WebTarget target = client.target(url);
		return target.request().post(Entity.entity(jsonObject.toString(), "application/json"),Response.class);
	}
	
}
