package com.github.okamumu.jspetrinet.ast.operators;

import java.util.ArrayList;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.exception.ASTException;

/**
 * A class to represent a list for AST objects
 *
 */

@SuppressWarnings("serial")
public class ASTList extends ArrayList<AST> implements AST {

	public ASTList() {}
	
	@Override
	public Object eval(ASTEnv env) throws ASTException {
		Object obj = null;
		for (AST a : this) {
			obj = a.eval(env);
		}
		return obj;
	}

	@Override
	public String toString() {
		String ret = "";
		for (AST a : this) {
			ret += a.toString() + ";";
		}
		return ret;
	}
}
