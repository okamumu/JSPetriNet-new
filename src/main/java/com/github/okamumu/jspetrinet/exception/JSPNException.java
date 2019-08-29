package com.github.okamumu.jspetrinet.exception;

/**
 * A class for exception in JSPetriNet.
 * All exceptions are extended from this class.
 *
 */

@SuppressWarnings("serial")
public class JSPNException extends Exception {

	/**
	 * Constructor
	 * @param msg A string for exception message
	 */
	public JSPNException(String msg) {
		super(msg);
	}
}
