package com.github.okamumu.jspetrinet.exception;

/**
 * An exception when the given gen policy is unknown.
 *
 */

public class UnknownPolicy extends ASTException {

	/**
	 * Constructor
	 * @param op A label of operator
	 */

	public UnknownPolicy(String op) {
		super(op);
	}

}
