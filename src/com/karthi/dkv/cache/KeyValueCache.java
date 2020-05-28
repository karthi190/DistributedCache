package com.karthi.dkv.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Contains all the key-value pairs
 * 
 * @author Karthi
 *
 */
public class KeyValueCache {

	private Map<String, String> map;
	
	public KeyValueCache() {
		map = new ConcurrentHashMap<String, String>();
	}
	
	public void set(String key, String value) {
		map.put(key, value);
	}
	
	public String get(String key) {
		return map.get(key);
	}
}
