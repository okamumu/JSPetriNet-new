package com.github.okamumu.jspetrinet.exception;

/**
 * An exception when the conversion is failed.
 *
 */

@SuppressWarnings("serial")
public class FailToConvertObject extends ASTException {

	/**
	 * Constructor
	 * @param desc A description for the invalid operation
	 */

	public FailToConvertObject(String desc) {
		super(desc);
	}

}
