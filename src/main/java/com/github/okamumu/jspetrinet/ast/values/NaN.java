package com.github.okamumu.jspetrinet.ast.values;

import com.github.okamumu.jspetrinet.ast.AST;

/**
 * A class to represent NaN (not a number) in general
 *
 */
public final class NaN {

	private final AST value;
	
	/**
	 * Constructor
	 * @param value An object of AST
	 */
	
	public NaN(AST value) {
		this.value = value;
	}
	
	/**
	 * A method to get an AST object to cause NaN
	 * @return An object of AST
	 */

	public AST getFormula() {
		return value;
	}
	
	@Override
	public String toString() {
		if (value != null) {
			return value.toString();
		} else {
			return "null";
		}
	}
}
