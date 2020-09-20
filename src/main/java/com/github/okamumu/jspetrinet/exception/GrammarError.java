package com.github.okamumu.jspetrinet.exception;

/**
 * An exception when SPN definition includes grammatical errors.
 *
 */

@SuppressWarnings("serial")
public class GrammarError extends ASTException {

	/**
	 * Constructor
	 * @param desc A description for the invalid operation
	 */

	public GrammarError(String desc) {
		super(desc);
	}

}
