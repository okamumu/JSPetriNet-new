package com.github.okamumu.jspetrinet.ast;

import java.util.Map;
import java.util.Set;

import com.github.okamumu.jspetrinet.exception.ObjectNotFoundInASTEnv;

/**
 * An interface for the environment in AST
 *
 */

public interface ASTEnv {
	
	/**
	 * Get an object from Env
	 * @param label A name of object
	 * @return An object of a given name.
	 * @throws ObjectNotFoundInASTEnv A fail to find an objected named by a given name.
	 */

	Object get(String label) throws ObjectNotFoundInASTEnv;

	/**
	 * Set an object to Env. If there already exists the same name, it will be replaced.
	 * @param label A name of object
	 * @param value An object
	 */

	void put(String label, Object value);
	
	/**
	 * Get a set of keys.
	 * @return A set of key strings
	 */
	
	Set<String> keySet();

	/**
	 * Check whether a named object exists or not. 
	 * @param label A name of object
	 * @return boolean value
	 */

	boolean contains(String label);
	
	/**
	 * A method to get entrySet for the HashMap of Env to apply a for-loop.
	 * @return An object of EntrySet
	 */

	Set<Map.Entry<String,Object>> entrySet();
}
