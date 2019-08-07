package com.github.okamumu.jspetrinet.ast.operators;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.ast.values.NaN;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidOperation;
import com.github.okamumu.jspetrinet.exception.InvalidOperator;

/**
 * A class to represent a comparator in AST
 *
 */

public final class ASTComparator extends ASTBinary {
	
	/**
	 * Constructor
	 * @param lhs An object of AST for left-hand side of operator
	 * @param rhs An object of AST for right-hand side of operator
	 * @param op A string for the operator
	 */

	public ASTComparator(AST lhs, AST rhs, String op) {
		super(lhs, rhs, op);
	}

	@Override
	public final Object eval(ASTEnv m) throws ASTException {
		Object lhs = this.getLeft().eval(m);
		Object rhs = this.getRight().eval(m);
		
		if (lhs instanceof NaN || rhs instanceof NaN) {
			return new NaN(new ASTComparator(ASTValue.getAST(lhs), ASTValue.getAST(rhs), op));
		}

		switch(op) {
		case "==":
			return eq(lhs, rhs);
		case "!=":
			return neq(lhs, rhs);
		case "<":
			return lt(lhs, rhs);
		case "<=":
			return lte(lhs, rhs);
		case ">":
			return gt(lhs, rhs);
		case ">=":
			return gte(lhs, rhs);
		default:
			throw new InvalidOperator(op);
		}
	}

	private final Object eq(Object lhs, Object rhs) throws InvalidOperation {
		if (lhs instanceof Integer && rhs instanceof Integer) {
			return ((Integer) lhs).intValue() == ((Integer) rhs).intValue();
		} else if (lhs instanceof Integer && rhs instanceof Double) {
			return ((Integer) lhs).doubleValue() == ((Double) rhs).doubleValue();
		} else if (lhs instanceof Double && rhs instanceof Integer) {
			return ((Double) lhs).doubleValue() == ((Integer) rhs).doubleValue();
		} else if (lhs instanceof Double && rhs instanceof Double) {
			return ((Double) lhs).doubleValue() == ((Double) rhs).doubleValue();
		} else if (lhs instanceof Boolean && rhs instanceof Boolean) {
			return ((Boolean) lhs).equals((Boolean) rhs);				
		} else {
			throw new InvalidOperation("An equal operator cannot execute for a pair of boolean and value");
		}
	}

	private final Object neq(Object lhs, Object rhs) throws InvalidOperation {
		if (lhs instanceof Integer && rhs instanceof Integer) {
			return ((Integer) lhs).intValue() != ((Integer) rhs).intValue();
		} else if (lhs instanceof Integer && rhs instanceof Double) {
			return ((Integer) lhs).doubleValue() != ((Double) rhs).doubleValue();
		} else if (lhs instanceof Double && rhs instanceof Integer) {
			return ((Double) lhs).doubleValue() != ((Integer) rhs).doubleValue();
		} else if (lhs instanceof Double && rhs instanceof Double) {
			return ((Double) lhs).doubleValue() != ((Double) rhs).doubleValue();
		} else if (lhs instanceof Boolean && rhs instanceof Boolean) {
			return !((Boolean) lhs).equals((Boolean) rhs);				
		} else {
			throw new InvalidOperation("An not-equal operator cannot execute for a pair of boolean and value");
		}
	}

	private final Object lt(Object lhs, Object rhs) throws InvalidOperation {
		if (lhs instanceof Integer && rhs instanceof Integer) {
			return ((Integer) lhs).intValue() < ((Integer) rhs).intValue();
		} else if (lhs instanceof Integer && rhs instanceof Double) {
			return ((Integer) lhs).doubleValue() < ((Double) rhs).doubleValue();
		} else if (lhs instanceof Double && rhs instanceof Integer) {
			return ((Double) lhs).doubleValue() < ((Integer) rhs).doubleValue();
		} else if (lhs instanceof Double && rhs instanceof Double) {
			return ((Double) lhs).doubleValue() < ((Double) rhs).doubleValue();
		} else {
			throw new InvalidOperation("An less-than operator should take either integer or double");
		}
	}

	private final Object lte(Object lhs, Object rhs) throws InvalidOperation {
		if (lhs instanceof Integer && rhs instanceof Integer) {
			return ((Integer) lhs).intValue() <= ((Integer) rhs).intValue();
		} else if (lhs instanceof Integer && rhs instanceof Double) {
			return ((Integer) lhs).doubleValue() <= ((Double) rhs).doubleValue();
		} else if (lhs instanceof Double && rhs instanceof Integer) {
			return ((Double) lhs).doubleValue() <= ((Integer) rhs).doubleValue();
		} else if (lhs instanceof Double && rhs instanceof Double) {
			return ((Double) lhs).doubleValue() <= ((Double) rhs).doubleValue();
		} else {
			throw new InvalidOperation("An less-than-or-euqal-to operator should take either integer or double");
		}
	}

	private final Object gt(Object lhs, Object rhs) throws InvalidOperation {
		if (lhs instanceof Integer && rhs instanceof Integer) {
			return ((Integer) lhs).intValue() > ((Integer) rhs).intValue();
		} else if (lhs instanceof Integer && rhs instanceof Double) {
			return ((Integer) lhs).doubleValue() > ((Double) rhs).doubleValue();
		} else if (lhs instanceof Double && rhs instanceof Integer) {
			return ((Double) lhs).doubleValue() > ((Integer) rhs).doubleValue();
		} else if (lhs instanceof Double && rhs instanceof Double) {
			return ((Double) lhs).doubleValue() > ((Double) rhs).doubleValue();
		} else {
			throw new InvalidOperation("An greather-than operator should take either integer or double");
		}
	}

	private final Object gte(Object lhs, Object rhs) throws InvalidOperation {
		if (lhs instanceof Integer && rhs instanceof Integer) {
			return ((Integer) lhs).intValue() >= ((Integer) rhs).intValue();
		} else if (lhs instanceof Integer && rhs instanceof Double) {
			return ((Integer) lhs).doubleValue() >= ((Double) rhs).doubleValue();
		} else if (lhs instanceof Double && rhs instanceof Integer) {
			return ((Double) lhs).doubleValue() >= ((Integer) rhs).doubleValue();
		} else if (lhs instanceof Double && rhs instanceof Double) {
			return ((Double) lhs).doubleValue() >= ((Double) rhs).doubleValue();
		} else {
			throw new InvalidOperation("An greather-than-or-equal-to operator should take either integer or double");
		}
	}

}
