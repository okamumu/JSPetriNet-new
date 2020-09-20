package com.github.okamumu.jspetrinet.ast;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.okamumu.jspetrinet.ast.values.*;
import com.github.okamumu.jspetrinet.ast.operators.*;
import com.github.okamumu.jspetrinet.exception.*;

public class ASTPowFuncTest {

	@Test
	public void testFuncDD() throws ASTException {
		AST a1 = ASTValue.getAST(2.0);
		AST a2 = ASTValue.getAST(2.0);
		ASTList a3 = new ASTList();
		a3.add(a1);
		a3.add(a2);
		AST a4 = new ASTMathFunc(a3, "pow");
		Object result = a4.eval(null);
		assertEquals(Math.pow(2.0, 2.0), result);
	}

	@Test
	public void testFuncDI() throws ASTException {
		AST a1 = ASTValue.getAST(2.0);
		AST a2 = ASTValue.getAST(2);
		ASTList a3 = new ASTList();
		a3.add(a1);
		a3.add(a2);
		AST a4 = new ASTMathFunc(a3, "pow");
		Object result = a4.eval(null);
		assertEquals(Math.pow(2.0, 2), result);
	}

	@Test
	public void testFuncID() throws ASTException {
		AST a1 = ASTValue.getAST(2);
		AST a2 = ASTValue.getAST(2.0);
		ASTList a3 = new ASTList();
		a3.add(a1);
		a3.add(a2);
		AST a4 = new ASTMathFunc(a3, "pow");
		Object result = a4.eval(null);
		assertEquals(Math.pow(2, 2.0), result);
	}

	@Test
	public void testFuncII() throws ASTException {
		AST a1 = ASTValue.getAST(2);
		AST a2 = ASTValue.getAST(2);
		ASTList a3 = new ASTList();
		a3.add(a1);
		a3.add(a2);
		AST a4 = new ASTMathFunc(a3, "pow");
		Object result = a4.eval(null);
		assertEquals(Math.pow(2, 2), result);
	}

	@Test(expected = InvalidOperation.class)
	public void testFuncEx() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		AST a2 = ASTValue.getAST(2);
		ASTList a3 = new ASTList();
		a3.add(a1);
		a3.add(a2);
		AST a4 = new ASTMathFunc(a3, "pow");
		Object result = a4.eval(null);
		assertEquals(Math.pow(2, 2), result);
	}

	@SuppressWarnings("unused")
	@Test(expected = InvalidOperation.class)
	public void testFunc2() throws ASTException {
		AST a1 = ASTValue.getAST(2);
		AST a2 = ASTValue.getAST(2);
		ASTList a3 = new ASTList();
		a3.add(a1);
		AST a4 = new ASTMathFunc(a3, "pow");
		Object result = a4.eval(null);
		assertEquals(Math.pow(2, 2), result);
	}

}
