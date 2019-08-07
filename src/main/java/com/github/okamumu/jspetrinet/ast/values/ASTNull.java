package com.github.okamumu.jspetrinet.ast.values;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.exception.ASTException;

/**
 * A class to represent null
 *
 */

public final class ASTNull implements AST {

	public ASTNull() {}
	
	@Override
	public Object eval(ASTEnv env) throws ASTException {
		return new NaN(this);
	}
	
	@Override
	public String toString() {
		return "";
	}
}
