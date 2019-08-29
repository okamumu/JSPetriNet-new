package com.github.okamumu.jspetrinet.exception;

/**
 * An exception when the value is invalid.
 *
 */

@SuppressWarnings("serial")
public class InvalidValue extends ASTException {

	/**
	 * Constructor
	 * @param desc A description for the invalid value
	 */

	public InvalidValue(String desc) {
		super(desc);
	}
}
