package com.github.okamumu.jspetrinet.ast.operators;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.ast.values.NaN;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidOperation;
import com.github.okamumu.jspetrinet.exception.InvalidOperator;

public final class ASTArithmetic extends ASTBinary {

	/**
	 * Constructor
	 * @param lhs An object of AST for left-hand side of operator
	 * @param rhs An object of AST for right-hand side of operator
	 * @param op A string for the operator
	 */

	public ASTArithmetic(AST lhs, AST rhs, String op) {
		super(lhs, rhs, op);
	}

	@Override
	public final Object eval(ASTEnv m) throws ASTException {
		Object lhs = this.getLeft().eval(m);
		Object rhs = this.getRight().eval(m);
		
		if (lhs instanceof NaN || rhs instanceof NaN) {
			return new NaN(new ASTArithmetic(ASTValue.getAST(lhs), ASTValue.getAST(rhs), op));
		}

		switch(op) {
		case "+":
			return plus(lhs, rhs);
		case "-":
			return minus(lhs, rhs);
		case "*":
			return multi(lhs, rhs);
		case "/":
			return divide(lhs, rhs);
		case "div":
			return idivide(lhs, rhs);
		case "mod":
			return mod(lhs, rhs);
		default:
			throw new InvalidOperator(op);
		}
	}

	private final Object plus(Object lhs, Object rhs) throws InvalidOperation {
		if (lhs instanceof Integer && rhs instanceof Integer) {
			return (Integer) lhs + (Integer) rhs;
		} else if (lhs instanceof Integer && rhs instanceof Double) {
			return (Integer) lhs + (Double) rhs;
		} else if (lhs instanceof Double && rhs instanceof Integer) {
			return (Double) lhs + (Integer) rhs;
		} else if (lhs instanceof Double && rhs instanceof Double) {
			return (Double) lhs + (Double) rhs;
		} else {
			throw new InvalidOperation("An arithmetic plus should take either Integer or Double");
		}
	}

	private final Object minus(Object lhs, Object rhs) throws InvalidOperation {
		if (lhs instanceof Integer && rhs instanceof Integer) {
			return (Integer) lhs - (Integer) rhs;
		} else if (lhs instanceof Integer && rhs instanceof Double) {
			return (Integer) lhs - (Double) rhs;
		} else if (lhs instanceof Double && rhs instanceof Integer) {
			return (Double) lhs - (Integer) rhs;
		} else if (lhs instanceof Double && rhs instanceof Double) {
			return (Double) lhs - (Double) rhs;
		} else {
			throw new InvalidOperation("An arithmetic minus should take either Integer or Double");
		}
	}

	private final Object multi(Object lhs, Object rhs) throws InvalidOperation {
		if (lhs instanceof Integer && rhs instanceof Integer) {
			return (Integer) lhs * (Integer) rhs;
		} else if (lhs instanceof Integer && rhs instanceof Double) {
			return (Integer) lhs * (Double) rhs;
		} else if (lhs instanceof Double && rhs instanceof Integer) {
			return (Double) lhs * (Integer) rhs;
		} else if (lhs instanceof Double && rhs instanceof Double) {
			return (Double) lhs * (Double) rhs;
		} else {
			throw new InvalidOperation("An arithmetic multiple should take either Integer or Double");
		}
	}

	private final Object divide(Object lhs, Object rhs) throws InvalidOperation {
		if (rhs instanceof Integer) {
			if ((Integer) rhs == 0) {
				return new NaN(new ASTArithmetic(ASTValue.getAST(lhs), ASTValue.getAST(rhs), "/"));
			}
			if (lhs instanceof Integer) {
				return ((Integer) lhs).doubleValue() / (Integer) rhs;				
			} else if (lhs instanceof Double) {
				return (Double) lhs / (Integer) rhs;
			} else {
				throw new InvalidOperation("An arithmetic divide should take either Integer or Double");				
			}			
		} else if (rhs instanceof Double) {
			if ((Double) rhs == 0.0) {
				return new NaN(new ASTArithmetic(ASTValue.getAST(lhs), ASTValue.getAST(rhs), "/"));
			}
			if (lhs instanceof Integer) {
				return (Integer) lhs / (Double) rhs;
			} else if (lhs instanceof Double) {
				return (Double) lhs / (Double) rhs;
			} else {
				throw new InvalidOperation("An arithmetic divide should take either Integer or Double");				
			}
		} else {
			throw new InvalidOperation("An arithmetic divide should take either Integer or Double");
		}
	}

	private final Object idivide(Object lhs, Object rhs) throws InvalidOperation {
		if (rhs instanceof Integer) {
			Integer tmp = (Integer) rhs;
			if (tmp == 0) {
				return new NaN(new ASTArithmetic(ASTValue.getAST(lhs), ASTValue.getAST(rhs), "/"));
			}
			if (lhs instanceof Integer) {
				return (Integer) lhs / tmp;
			} else if (lhs instanceof Double) {
				return ((Double) lhs).intValue() / tmp;
			} else {
				throw new InvalidOperation("An arithmetic divide for integers should take only Integers");
			}			
		} else if (rhs instanceof Double) {
			int tmp = ((Double) rhs).intValue();
			if (tmp == 0) {
				return new NaN(new ASTArithmetic(ASTValue.getAST(lhs), ASTValue.getAST(rhs), "/"));
			}
			if (lhs instanceof Integer) {
				return (Integer) lhs / tmp;
			} else if (lhs instanceof Double) {
				return ((Double) lhs).intValue() / tmp;
			} else {
				throw new InvalidOperation("An arithmetic divide for integers should take only Integers");
			}
		} else {
			throw new InvalidOperation("An arithmetic divide for integers should take only Integers");
		}
	}

	private final Object mod(Object lhs, Object rhs) throws InvalidOperation {
		if (lhs instanceof Integer && rhs instanceof Integer) {
			return (Integer) lhs % (Integer) rhs;
		} else {
			throw new InvalidOperation("An arithmetic modulo should take only Integers");
		}
	}
	
}
