package com.github.okamumu.jspetrinet.ast.operators;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.ast.values.NaN;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidOperation;

/**
 * A class to represent an if-then-else operator in AST
 *
 */

public final class ASTIfThenElse implements AST {

	private final AST ifcond;
	private final AST thenvalue;
	private final AST elsevalue;
	
	/**
	 * Constructor
	 * @param ifcond An object of AST for if-condition
	 * @param thenvalue An object of AST for then-value
	 * @param elsevalue An object of AST for else-value
	 */

	public ASTIfThenElse(AST ifcond, AST thenvalue, AST elsevalue) {
		this.ifcond = ifcond;
		this.thenvalue = thenvalue;
		this.elsevalue = elsevalue;
	}
	
	@Override
	public Object eval(ASTEnv m) throws ASTException {
		Object f = this.ifcond.eval(m);
		if (f instanceof NaN) {
			return new NaN(new ASTIfThenElse(ASTValue.getAST(f), thenvalue, elsevalue));
		}
		if (f instanceof Boolean) {
			if ((Boolean) f) {
				return this.thenvalue.eval(m);
			} else {
				return this.elsevalue.eval(m);
			}
		} else {
			throw new InvalidOperation("The condition of If-Then-Else should be a boolean");
		}
	}
	
	@Override
	public String toString() {
		return "ifelse(" + ifcond.toString() +"," + thenvalue.toString() + "," + elsevalue.toString() + ")";
	}
}
