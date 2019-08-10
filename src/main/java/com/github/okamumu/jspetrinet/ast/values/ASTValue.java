package com.github.okamumu.jspetrinet.ast.values;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.exception.ASTException;

public final class ASTValue implements AST {
	
	private final Object value;

	/**
	 * A static method to convert an object to an object of AST
	 * @param obj An object of Java class, either, null, Integer, Double, Boolean or ASTNaN
	 * @return An object of AST that wraps the value
	 */

	public static AST getAST(Object obj) {
		if (obj instanceof AST) {
			return (AST) obj;
		}
		if (obj instanceof Integer
				|| obj instanceof Double
				|| obj instanceof Boolean
				|| obj instanceof String) {
			return new ASTValue(obj);
		}
		if (obj instanceof NaN) {
			return ((NaN) obj).getFormula();
		}
		return new ASTNull();
	}

	/**
	 * A static method to convert a value of fundamental types to an object of AST
	 * @param value A value of integer
	 * @return An object of AST that wraps the value
	 */

	public static AST getAST(int value) {
		return new ASTValue(new Integer(value));
	}

	/**
	 * A static method to convert a value of fundamental types to an object of AST
	 * @param value A value of double
	 * @return An object of AST that wraps the value
	 */

	public static AST getAST(double value) {
		return new ASTValue(new Double(value));
	}

	/**
	 * A static method to convert a value of fundamental types to an object of AST
	 * @param value A value of boolean
	 * @return An object of AST that wraps the value
	 */

	public static AST getAST(boolean value) {
		return new ASTValue(new Boolean(value));
	}
	
	/**
	 * A static method to convert a value of fundamental types to an object of AST
	 * @param value A value of string
	 * @return An object of AST that wraps the value
	 */

//	public static AST getAST(String value) {
//		return new ASTValue(value);
//	}
	
	/**
	 * A constructor. This is a private method. An instance should be created by a factory getAST method.
	 * @param obj A Java object. Either Integer, Double and Boolean
	 */

	private ASTValue(Object obj) {
		this.value = obj;
	}

	@Override
	public Object eval(ASTEnv env) throws ASTException {
		return value;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}

}
