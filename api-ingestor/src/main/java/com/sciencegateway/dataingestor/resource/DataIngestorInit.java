package com.sciencegateway.dataingestor.resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.log4j.Logger;

@SuppressWarnings("unchecked")
public class DataIngestorInit implements ServletContextListener
{
	
	private static Logger logger = Logger.getLogger(DataIngestorInit.class);
	
	private ServletContext context = null;

    @Override
    public void contextDestroyed(ServletContextEvent event) 
    {
        this.context = null;
    }

    @Override
    public void contextInitialized(ServletContextEvent event) 
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
    	
        this.context = event.getServletContext();
        logger.info("Registering Service...");
    	String serviceURI = "http://"+ip+":9000/dataingestor/webapi/service/url";
    	String serviceName = "dataIngestor";
    	int port = 9000;
    	
    	CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(ip+":2181", new RetryNTimes(5, 1000));
        curatorFramework.start();
        
        try 
        {            
            @SuppressWarnings("rawtypes")
			ServiceInstance serviceInstance = ServiceInstance.builder().uriSpec(new UriSpec(serviceURI)).address(ip).port(port)
					.name(serviceName).build();
            ServiceDiscoveryBuilder.builder(Void.class).basePath("weather-predictor").client(curatorFramework)
			.thisInstance(serviceInstance).build().start();
        } 
        catch (Exception exception) 
        {
        	logger.error(exception.toString(),exception);
            throw new RuntimeException("Could not register service \"" + serviceName + "\", with URI \"" + serviceURI + "\": " + exception.getMessage());
        }
    }

}
