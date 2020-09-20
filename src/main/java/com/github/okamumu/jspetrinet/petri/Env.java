package com.github.okamumu.jspetrinet.petri;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.exception.ObjectNotFoundInASTEnv;

/**
 * A class to represent the environment in Petri nets
 *
 */

public final class Env implements ASTEnv {

	private final Map<String,Object> hash;

	/**
	 * Constructor
	 */

	public Env() {
		hash = new HashMap<String,Object>();
	}
	
	public void remove(String label) {
		hash.remove(label);
	}

	@Override
	public String toString() {
		String linesep = System.getProperty("line.separator").toString();
		String res = "Defined labels:" + linesep;
		for (String s: hash.keySet()) {
			res += s + ";\n";
		}
		return res;
	}

	@Override
	public final Object get(String label) throws ObjectNotFoundInASTEnv {
		if (!hash.containsKey(label)) {
			throw new ObjectNotFoundInASTEnv(label);
		}
		return hash.get(label);
	}
	
	@Override
	public final void put(String label, Object value) {
		hash.put(label, value);
	}

	public final void put(Object value) {
		hash.put(value.toString(), value);
	}

	@Override
	public final boolean contains(String label) {
		return hash.containsKey(label);
	}

	@Override
	public final Set<Map.Entry<String,Object>> entrySet() {
		return hash.entrySet();
	}

	@Override
	public Set<String> keySet() {
		return hash.keySet();
	}
}
