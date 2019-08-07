package com.github.okamumu.jspetrinet.exception;

/**
 * An exception when the operation is invalid. Ex. A plus for booleans.
 *
 */

public class InvalidOperation extends ASTException {

	/**
	 * Constructor
	 * @param desc A description for the invalid operation
	 */

	public InvalidOperation(String desc) {
		super(desc);
	}
}
