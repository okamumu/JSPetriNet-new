package com.github.okamumu.jspetrinet.ast.operators;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.ast.values.NaN;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidOperation;
import com.github.okamumu.jspetrinet.exception.InvalidOperator;

/**
 * A class to represent an unary operator in AST
 *
 */

public final class ASTUnary implements AST {

	private final AST child;
	private final String op;
	
	/**
	 * Constructor
	 * @param child An object of AST for operand
	 * @param op A string for the operator
	 */

	public ASTUnary(AST child, String op) {
		this.child = child;
		this.op = op;
	}
	
	@Override
	public Object eval(ASTEnv m) throws ASTException {
		Object res = this.child.eval(m);
		
		if (res instanceof NaN) {
			return new NaN(new ASTUnary(ASTValue.getAST(res), op));
		}
		
		switch(op) {
		case "+":
			return plus(res);
		case "-":
			return minus(res);
		case "!":
			return not(res);
		default:
			throw new InvalidOperator(op);
		}
	}
	
	@Override
	public String toString() {
		return "(" + op + this.child.toString() + ")";
	}

	private final Object plus(Object res) throws InvalidOperation {
		if (res instanceof Integer) {
			return (Integer) res;
		} else if (res instanceof Double) {
			return (Double) res;
		} else {
			throw new InvalidOperation("Unary plus should take either integer or double");
		}
	}
	
	private final Object minus(Object res) throws InvalidOperation {
		if (res instanceof Integer) {
			return -((Integer) res);
		} else if (res instanceof Double) {
			return -((Double) res);
		} else {
			throw new InvalidOperation("Unary minus should take either integer or double");
		}
	}

	private Object not(Object res) throws InvalidOperation {
		if (res instanceof Boolean) {
			return !((Boolean) res);
		} else {
			throw new InvalidOperation("Unary not should take only boolean");
		}
	}

}

