package com.karthi.dkv.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class to hold the configuration properties for the Application
 * 
 * @author Karthi
 *
 */
public class ConfigProperties {

	// File containing the configuration properties
	String CONFIG_PATH = "config.properties";

	private Properties configProperties;

	public ConfigProperties() {

		try {

			ClassLoader classloader = Thread.currentThread().getContextClassLoader();

			configProperties = new Properties();
			
			try (InputStream in = classloader.getResourceAsStream(CONFIG_PATH)) {
				configProperties.load(in);
			}

			// Log the loaded properties in console
			System.out.println("\n*** Loading Config properties : ***");
			configProperties.forEach((k, v) -> {
				System.out.printf("%s : %s\n", k, v);
			});
			System.out.println("*** Loaded Config properties : ***");

		} catch (IOException e) {
			System.out.println(" Error loading config properties : " + e.getMessage());
		}

	}

	public String getProperty(String key) {
		return configProperties.getProperty(key);
	}
}
