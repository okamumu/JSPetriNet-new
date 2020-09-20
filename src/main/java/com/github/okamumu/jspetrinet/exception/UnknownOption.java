package com.github.okamumu.jspetrinet.exception;

/**
 * An exception when the given option is unknown.
 *
 */

@SuppressWarnings("serial")
public class UnknownOption extends ASTException {

	/**
	 * Constructor
	 * @param op A label of operator
	 */

	public UnknownOption(String op) {
		super(op);
	}

}
