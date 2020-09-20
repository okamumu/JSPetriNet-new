package com.github.okamumu.jspetrinet.ast.operators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.ast.values.NaN;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidOperation;
import com.github.okamumu.jspetrinet.exception.InvalidOperator;

/**
 * A class for math functions for AST
 *
 */
public class ASTMathFunc implements AST {
	
	private final List<AST> arg;
	private final String func;

	/**
	 * Constructor
	 * @param arg A list of arguments
	 * @param func A string of function
	 */
	public ASTMathFunc(List<AST> arg, String func) {
		this.arg = arg;
		this.func = func;
	}

	@Override
	public Object eval(ASTEnv m) throws ASTException {
		ArrayList<Object> res = new ArrayList<Object>();
		boolean hasNaN = false;
		for (AST a : arg) {
			Object tmp = a.eval(m);
			if (tmp instanceof NaN) {
				hasNaN = true;
			}
			res.add(tmp);
		}
		
		if (hasNaN) {
			return makeNaN(res, func);
		}

		switch(func) {
		case "pow":
			return pow(res);
		case "exp":
			return exp(res);
		case "sqrt":
			return sqrt(res);
		case "log":
			return log(res);
		case "min":
			return min(res);
		case "max":
			return max(res);
		default:
			throw new InvalidOperator(func + " is not defined");
		}
	}
	
	private Object makeNaN(ArrayList<Object> res, String func) {
		ASTList x = new ASTList();
		for (Object obj : res) {
			x.add(ASTValue.getAST(obj));
		}
		return new NaN(new ASTMathFunc(x, func));
	}

	@Override
	public String toString() {
		ArrayList<String> args = new ArrayList<String>();
		for (AST a : arg) {
			args.add(a.toString());
		}
		return func + "(" + String.join(",", args) + ")";
	}

	private final Object exp(ArrayList<Object> res) throws InvalidOperation {
		if (res.size() != 1) {
			throw new InvalidOperation("The number of arguments of EXP should be one");
		}

		Object arg1 = res.iterator().next();
		if (arg1 instanceof Integer) {
			return Math.exp((Integer) arg1);
		} else if (arg1 instanceof Double) {
			return Math.exp((Double) arg1);
		} else {
			throw new InvalidOperation("EXP should take either integer or double");
		}
	}

	private final Object sqrt(ArrayList<Object> res) throws InvalidOperation {
		if (arg.size() != 1) {
			throw new InvalidOperation("The number of arguments of SQRT should be one");
		}

		Object arg1 = res.iterator().next();
		if (arg1 instanceof Integer) {
			if ((Integer) arg1 < 0) {
				return makeNaN(res, "sqrt");
			}
			return Math.sqrt((Integer) arg1);
		} else if (arg1 instanceof Double) {
			if ((Double) arg1 < 0.0) {
				return makeNaN(res, "sqrt");
			}
			return Math.sqrt((Double) arg1);
		} else {
			throw new InvalidOperation("SQRT should take either integer or double");
		}
	}

	private final Object log(ArrayList<Object> res) throws InvalidOperation {
		if (arg.size() != 1) {
			throw new InvalidOperation("The number of arguments of LOG should be one");
		}

		Object arg1 = res.iterator().next();
		if (arg1 instanceof Integer) {
			if ((Integer) arg1 <= 0) {
				return makeNaN(res, "log");
			}
			return Math.log((Integer) arg1);
		} else if (arg1 instanceof Double) {
			if ((Double) arg1 <= 0.0) {
				return makeNaN(res, "log");
			}
			return Math.log((Double) arg1);
		} else {
			throw new InvalidOperation("LOG should take either integer or double");
		}
	}

	private final Object pow(ArrayList<Object> res) throws InvalidOperation {
		if (arg.size() != 2) {
			throw new InvalidOperation("The number of arguments of POW should be two");
		}

		Iterator<Object> ite = res.iterator();
		Object arg1 = ite.next();
		Object arg2 = ite.next();
		if (arg1 instanceof Integer && arg2 instanceof Integer) {
			return Math.pow((Integer) arg1, (Integer) arg2);
		} else if (arg1 instanceof Integer && arg2 instanceof Double) {
			return Math.pow((Integer) arg1, (Double) arg2);
		} else if (arg1 instanceof Double && arg2 instanceof Integer) {
			return Math.pow((Double) arg1, (Integer) arg2);
		} else if (arg1 instanceof Double && arg2 instanceof Double) {
			return Math.pow((Double) arg1, (Double) arg2);
		} else {
			throw new InvalidOperation("POW should take either integer or double");
		}
	}

	private final Object min(ArrayList<Object> res) throws InvalidOperation {
		if (arg.size() <= 1) {
			throw new InvalidOperation("The number of arguments of MIN should be greather than or equal to one");
		}

		Iterator<Object> ite = res.iterator();
		Object tmp1 = ite.next();
		while (ite.hasNext()) {
			Object tmp2 = ite.next();
			if (tmp1 instanceof Integer && tmp2 instanceof Integer) {
				tmp1 = Math.min((Integer) tmp1, (Integer) tmp2);
			} else if (tmp1 instanceof Integer && tmp2 instanceof Double) {
				tmp1 = Math.min((Integer) tmp1, (Double) tmp2);
			} else if (tmp1 instanceof Double && tmp2 instanceof Integer) {
				tmp1 = Math.min((Double) tmp1, (Integer) tmp2);
			} else if (tmp1 instanceof Double && tmp2 instanceof Double) {
				tmp1 = Math.min((Double) tmp1, (Double) tmp2);
			} else {
				throw new InvalidOperation("MIN should take either integer or double");
			}			
		}
		return tmp1;
	}

	private final Object max(ArrayList<Object> res) throws InvalidOperation {
		if (arg.size() <= 1) {
			throw new InvalidOperation("The number of arguments of MAX should be greather than or equal to one");
		}

		Iterator<Object> ite = res.iterator();
		Object tmp1 = ite.next();
		while (ite.hasNext()) {
			Object tmp2 = ite.next();
			if (tmp1 instanceof Integer && tmp2 instanceof Integer) {
				tmp1 = Math.max((Integer) tmp1, (Integer) tmp2);
			} else if (tmp1 instanceof Integer && tmp2 instanceof Double) {
				tmp1 = Math.max((Integer) tmp1, (Double) tmp2);
			} else if (tmp1 instanceof Double && tmp2 instanceof Integer) {
				tmp1 = Math.max((Double) tmp1, (Integer) tmp2);
			} else if (tmp1 instanceof Double && tmp2 instanceof Double) {
				tmp1 = Math.max((Double) tmp1, (Double) tmp2);
			} else {
				throw new InvalidOperation("MAX should take either integer or double");
			}			
		}
		return tmp1;
	}

}
