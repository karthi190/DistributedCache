package com.karthi.dkv.http;

import java.io.IOException;

import com.karthi.dkv.App;
import com.karthi.dkv.cache.KeyValueCache;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


/**
 * URL format remains the same for all three types of action, namely SET, GET and REPLICATE. Action is taken based on the HTTP verb
 * POST, GET and PUT respectively
 * 
 * URL format : http://localhost:8001/dkv?key=value
 * HTTP Verb : POST - Sets a new value for the key in the cache
 *           : GET - Gets the value from the cache for the key
 *           : PUT - replicate the key&value in the current server.
 * 
 * 
 */
@SuppressWarnings("restriction")
public class HttpRequestHandler implements HttpHandler {

	private KeyValueCache cache = new KeyValueCache();

	public HttpRequestHandler() {
		cache = new KeyValueCache();
	}

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {

		short responseStatusCode = 200;
		String responseMessage = null;
		String responseValue = null;

		// rawKeyValueFromRequest[0] contains key and rawKeyValueFromRequest[1] contains value from query string
		String[] rawKeyValueFromRequest = HttpUtils.parseRequest(httpExchange);
		
		//TODO: Error codes such as 404, 500 should be handled.
		if (rawKeyValueFromRequest.length != 2) {
			HttpUtils.sendResponse(httpExchange, "Bad Request" , (short)400 , "");
			return;
		}

		if ("GET".equals(httpExchange.getRequestMethod())) {
			
			//Return the value for the key [only the 'value' part from the raw query string is used]
			responseValue = getValueForKey(rawKeyValueFromRequest[1]);
			
			if ( responseValue == null ) {
				responseMessage = "No Value found for the key !";
			} else {
				responseMessage = "success";
			}
			

		} else if ("POST".equals(httpExchange.getRequestMethod())) {

			//Set the value for the key in cache
			createCacheEntry(rawKeyValueFromRequest[0], rawKeyValueFromRequest[1]);
			
			//replicate to other hosts
			replicateToOtherHosts(rawKeyValueFromRequest[0], rawKeyValueFromRequest[1]);
			
			responseMessage = "success";

		} else if ("PUT".equals(httpExchange.getRequestMethod())) {

			//Set the value for the key in cache
			replicateHere(rawKeyValueFromRequest[0], rawKeyValueFromRequest[1]);
			responseMessage = "success";
		}

		HttpUtils.sendResponse(httpExchange, responseMessage, responseStatusCode, responseValue);
	}

	/**
	 * Simple, modern HTTP based replication is used rather than old school RMI or JGroups based multicasting/replication. 
	 * This is similar to the CouchDB's replication protocol
	 * 
	 * @param key
	 * @param value
	 * @throws IOException 
	 */
	private void replicateToOtherHosts(String key, String value) throws IOException {
		
		//Get Host List
		String[] urlList = App.configProperties.getProperty("dkv.replication.host_list").split(",");
		
		//Replicate by invoking PUT request on all the hosts in host list
		for (String url:urlList) {
			System.out.println("Replicating to " + url);
			HttpUtils.sendPutRequest(url, key, value);
		}
	}
	
	
	private void replicateHere(String key, String value) {
		cache.set(key, value);
	}

	private void createCacheEntry(String key, String value) {
		cache.set(key, value);
	}

	private String getValueForKey(String key) {
		return cache.get(key);
	}

}
