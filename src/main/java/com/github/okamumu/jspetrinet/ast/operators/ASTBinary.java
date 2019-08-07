package com.github.okamumu.jspetrinet.ast.operators;

import com.github.okamumu.jspetrinet.ast.AST;

/**
 * An abstract class for binary operators
 *
 */
abstract public class ASTBinary implements AST {

	private final AST lhs;
	private final AST rhs;
	protected final String op;

	/**
	 * Constructor
	 * @param lhs An object of AST for left-hand side of operator
	 * @param rhs An object of AST for right-hand side of operator
	 * @param op A string for the operator
	 */
	public ASTBinary(AST lhs, AST rhs, String op) {
		this.lhs = lhs;
		this.rhs = rhs;
		this.op = op;
	}

	/**
	 * Getter for left-hand side
	 * @return An object of AST
	 */
	public AST getLeft() {
		return lhs;
	}
	
	/**
	 * Getter for right-hand side
	 * @return An object of AST
	 */
	public AST getRight() {
		return rhs;
	}

	@Override
	public String toString() {
		return "(" + this.getLeft().toString() + op + this.getRight().toString() + ")";
	}
}
