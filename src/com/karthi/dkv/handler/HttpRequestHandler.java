package com.karthi.dkv.handler;

import java.io.IOException;
import java.io.OutputStream;

import com.karthi.dkv.cache.KeyValueCache;
import com.sun.net.httpserver.*;


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

	// Cache storing the key-values
	private KeyValueCache cache;

	public HttpRequestHandler() {
		cache = new KeyValueCache();
	}

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {

		short responseStatusCode = 200;
		String responseMessage = null;
		String responseValue = null;

		// rawKeyValueFromRequest[0] contains key and rawKeyValueFromRequest[1] contains value from query string
		String[] rawKeyValueFromRequest = parseRequest(httpExchange);
		
		//Error Check
		//TODO: Error codes such as 404, 500 should be handled.
		if (rawKeyValueFromRequest.length != 2) {
			sendResponse(httpExchange, "Bad Request" , (short)400 , "");
			return;
		}

		if ("GET".equals(httpExchange.getRequestMethod())) {
			
			//Return the value for the key [only the 'value' part from the raw query string is used]
			responseValue = getValueForKey(rawKeyValueFromRequest[1]);
			
			if ( responseValue == null ) {
				responseMessage = "No Value found for the key!";
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

		sendResponse(httpExchange, responseMessage, responseStatusCode, responseValue);
	}

	/**
	 * Simple, modern HTTP based replication is used rather than old school RMI or JGroups based multicasting/replication. 
	 * This is similar to the CouchDB's replication protocol
	 * 
	 * @param key
	 * @param value
	 */
	private void replicateToOtherHosts(String key, String value) {
		
		//Get Host List
		
		//Replicate by invoking PUT request on all the hosts in host list
		
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

	private void sendResponse(HttpExchange httpExchange, String message, short statusCode, String value)
			throws IOException {
		OutputStream outputStream = httpExchange.getResponseBody();
		String jsonResponse = prepareJsonResponse(message, statusCode, value);
		httpExchange.sendResponseHeaders(200, jsonResponse.length());
		outputStream.write(jsonResponse.getBytes());
		outputStream.flush();
		outputStream.close();
	}

	/**
	 * Building JSON response using StringBuilder
	 * 
	 */
	private String prepareJsonResponse(String message, short statusCode, String value) {
		StringBuilder jsonResponsebuilder = new StringBuilder();
		jsonResponsebuilder.append("{");
		jsonResponsebuilder.append("\"message\":\"").append(message).append("\",");
		jsonResponsebuilder.append("\"value\":\"").append(value==null?"":value).append("\",");
		jsonResponsebuilder.append("\"statusCode\":").append(statusCode);
		jsonResponsebuilder.append("}");
		return jsonResponsebuilder.toString();
	}

	private String[] parseRequest(HttpExchange httpExchange) {
		
		return httpExchange.getRequestURI().toString().split("\\?")[1].split("=");
	}

}
