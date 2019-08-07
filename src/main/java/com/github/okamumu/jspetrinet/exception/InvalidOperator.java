package com.github.okamumu.jspetrinet.exception;

/**
 * An exception when the operator is not defined in AST.
 *
 */

public class InvalidOperator extends ASTException {

	/**
	 * Constructor
	 * @param op A label of operator
	 */

	public InvalidOperator(String op) {
		super(op);
	}

}
