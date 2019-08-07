package com.github.okamumu.jspetrinet.exception;

/**
 * An exception when an object is not found in ASTEnv.
 *
 */

public class ObjectNotFoundInASTEnv extends ASTException {

	/**
	 * Constructor
	 * @param label A name of object that is not found in Env
	 */

	public ObjectNotFoundInASTEnv(String label) {
		super(label);
	}
}
