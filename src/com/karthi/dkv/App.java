package com.karthi.dkv;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.karthi.dkv.config.ConfigProperties;
import com.karthi.dkv.http.HttpRequestHandler;
import com.sun.net.httpserver.*;

/**
 * Implements a simple HTTP server to handle a GET, POST and PUT requests, storing key-value pairs in memory.
 * 
 * @author Karthi
 *
 */
public class App {

	//A public config to be available across the application
	public static ConfigProperties configProperties;
	
	// Initialize the config properties
	static {
		configProperties = new ConfigProperties();
	}

	
	@SuppressWarnings("restriction")
	public static void main( String[] args ) {
		try {
			
			System.out.println("\nInitializing server... ");
			
			//Create a HTTP Server
			HttpServer server = createHttpServer();
			
			//Start the Server
			server.start();
			
			System.out.println("Server started on port 8001!... ");

		} catch (IOException e) {
			System.out.println(" Error: " + e.getMessage());
		}

	}

	

	@SuppressWarnings("restriction")
	private static HttpServer createHttpServer() throws IOException {
		
		//Create Server at the specified HOST and PORT
		HttpServer server = HttpServer.create( new InetSocketAddress( configProperties.getProperty("dkv.host"), Integer.parseInt( configProperties.getProperty("dkv.port") ) ), 0 );
		
		//Create a Fixed ThreadPool
		ThreadPoolExecutor threadPoolExecutor = ( ThreadPoolExecutor ) Executors.newFixedThreadPool( 10 );
		server.setExecutor( threadPoolExecutor );
		
		//Set Context
		server.createContext( "/dkv", new HttpRequestHandler() );
		
		return server;
	}

}
