package com.github.okamumu.jspetrinet.ast.values;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.values.NaN;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.ObjectNotFoundInASTEnv;
import com.github.okamumu.jspetrinet.ast.ASTEnv;

/**
 * A class to represent variables in AST.
 * This is used as only a l-value.
 *
 */

public class ASTVariable implements AST {

	private final String label;

	/**
	 * Constructor
	 * @param label A name of variable
	 */

	public ASTVariable(String label) {
		this.label = label;
	}
	
	@Override
	public Object eval(ASTEnv env) throws ASTException {
		Object v;
		try {
			v = env.get(label);
		} catch (ObjectNotFoundInASTEnv e) {
			return new NaN(this);
		}
		if (v instanceof AST) {
			return ((AST) v).eval(env);
		} else {
			return v;
		}
	}
	
	@Override
	public String toString() {
		return label;
	}
}
