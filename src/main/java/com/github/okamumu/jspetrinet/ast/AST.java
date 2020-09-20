package com.github.okamumu.jspetrinet.ast;

import com.github.okamumu.jspetrinet.exception.ASTException;

/**
 * An interface for AST (abstract syntax tree)
 *
 */
public interface AST {

	/**
	 * A method to evaluate AST
	 * @param env An object of environment (ASTEnv) that define variables, equations, etc.
	 * @return An object of Java
	 * @throws ASTException A fail to convert AST to Object
	 */
	Object eval(ASTEnv env) throws ASTException;

}
