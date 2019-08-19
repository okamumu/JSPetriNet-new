package com.github.okamumu.jspetrinet.petri;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Inner class for a node of Petri net.
 * This class keeps the definitions of components by hashmap.
 * Based on the definition in this class, compile an instance of PN
 *
 */
public class Node {
	public final Map<String,Object> options;
	
	/**
	 * Constructor
	 */
	public Node() {
		options = new HashMap<String,Object>();
	}
	
//	/**
//	 * A method to get an object
//	 * @param key A string for a key
//	 * @param value A default value, which is used when the given key does not exist.
//	 * @return An object
//	 */
//	public Object getOrDefault(String key, Object value) {
//		return options.getOrDefault(key, value);
//	}

	/**
	 * Get a size of options
	 * @return A size
	 */
	public int size() {
		return options.size();
	}
	
	/**
	 * Method to get the entry set of options
	 * @return A set of entries
	 */
	public Set<Map.Entry<String,Object>> getOptEntry() {
		return options.entrySet();
	}
	
	/**
	 * A method to set a key and an object
	 * @param key A string
	 * @param value An object
	 */
	public void put(String key, Object value) {
		options.put(key, value);
	}

	/**
	 * Get a string for a type of node
	 * @return A string
	 */
	public String getType() {
		return (String) options.getOrDefault("type", null);
	}

	/**
	 * Setter for a type of node
	 * @param string A name of type
	 */
	public void setType(String string) {
		options.put("type", string);
	}
}
