package com.karthi.dkv.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings("restriction")
public class HttpUtils {
	
	protected static void sendResponse(HttpExchange httpExchange, String message, short statusCode, String value)
			throws IOException {
		OutputStream outputStream = httpExchange.getResponseBody();
		String jsonResponse = prepareJsonResponse(message, statusCode, value);
		httpExchange.sendResponseHeaders(statusCode, jsonResponse.length());
		outputStream.write(jsonResponse.getBytes());
		outputStream.flush();
		outputStream.close();
	}
	
	/**
	 * Building JSON response using StringBuilder
	 * 
	 */
	private static String prepareJsonResponse(String message, short statusCode, String value) {
		StringBuilder jsonResponsebuilder = new StringBuilder();
		jsonResponsebuilder.append("{");
		jsonResponsebuilder.append("\"message\":\"").append(message).append("\",");
		jsonResponsebuilder.append("\"value\":\"").append(value==null?"":value).append("\",");
		jsonResponsebuilder.append("\"statusCode\":").append(statusCode);
		jsonResponsebuilder.append("}");
		return jsonResponsebuilder.toString();
	}
	
	
	protected static void sendPutRequest(String url, String key, String value) throws UnsupportedEncodingException {
		
		String charset = java.nio.charset.StandardCharsets.UTF_8.name();
		String query = String.format("%s=%s", key, URLEncoder.encode(value, charset));
		
		try {
			HttpURLConnection connection = (HttpURLConnection)new URL(url + "?" + query).openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
			try (OutputStream output = connection.getOutputStream()) {
			    output.write(query.getBytes(charset));
			}
		} catch (IOException e) {
			System.out.println("Server May not be available.. skipping...");
		}
		
	}
	
	protected static String[] parseRequest(HttpExchange httpExchange) {	
		return httpExchange.getRequestURI().toString().split("\\?")[1].split("=");
	}
	
}
