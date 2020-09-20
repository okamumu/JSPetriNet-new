package com.github.okamumu.jspetrinet.ast.operators;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.ast.values.NaN;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidOperation;
import com.github.okamumu.jspetrinet.exception.InvalidOperator;

/**
 * A class to represent a logical operator in AST
 *
 */

public final class ASTLogical extends ASTBinary {
	
	/**
	 * Constructor
	 * @param lhs An object of AST for left-hand side of operator
	 * @param rhs An object of AST for right-hand side of operator
	 * @param op A string for the operator
	 */

	public ASTLogical(AST lhs, AST rhs, String op) {
		super(lhs, rhs, op);
	}

	@Override
	public final Object eval(ASTEnv m) throws ASTException {
		Object lhs = this.getLeft().eval(m);
		Object rhs = this.getRight().eval(m);
		
		if (lhs instanceof NaN || rhs instanceof NaN) {
			return new NaN(new ASTLogical(ASTValue.getAST(lhs), ASTValue.getAST(rhs), op));
		}

		switch(op) {
		case "&&":
			return and(lhs, rhs);
		case "||":
			return or(lhs, rhs);
		default:
			throw new InvalidOperator(op);
		}
	}

	public final Object and(Object lhs, Object rhs) throws InvalidOperation {
		if (lhs instanceof Boolean && rhs instanceof Boolean) {
			return (Boolean) lhs && (Boolean) rhs;
		} else {
			throw new InvalidOperation("AND operator can take only booleans");
		}
	}

	public final Object or(Object lhs, Object rhs) throws InvalidOperation {
		if (lhs instanceof Boolean && rhs instanceof Boolean) {
			return (Boolean) lhs || (Boolean) rhs;
		} else {
			throw new InvalidOperation("OR operator can take only booleans");
		}
	}

}
