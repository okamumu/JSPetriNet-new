package com.github.okamumu.jspetrinet.ast;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.okamumu.jspetrinet.ast.values.*;
import com.github.okamumu.jspetrinet.ast.operators.*;
import com.github.okamumu.jspetrinet.exception.*;

public class ASTExpFuncTest {

	@Test
	public void testFuncD() throws ASTException {
		AST a1 = ASTValue.getAST(2.0);
		ASTList a2 = new ASTList();
		a2.add(a1);
		AST a3 = new ASTMathFunc(a2, "exp");
		Object result = a3.eval(null);
		assertEquals(Math.exp(2.0), result);
	}

	@Test
	public void testFuncI() throws ASTException {
		AST a1 = ASTValue.getAST(2);
		ASTList a2 = new ASTList();
		a2.add(a1);
		AST a3 = new ASTMathFunc(a2, "exp");
		Object result = a3.eval(null);
		assertEquals(Math.exp(2), result);
	}

	@Test(expected = InvalidOperation.class)
	public void testFunc2() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		ASTList a2 = new ASTList();
		a2.add(a1);
		AST a3 = new ASTMathFunc(a2, "exp");
		Object result = a3.eval(null);
		assertEquals(Math.exp(1.0), result);
	}

	@Test(expected = InvalidOperation.class)
	public void testFunc3() throws ASTException {
		AST a1 = ASTValue.getAST(1.0);
		ASTList a2 = new ASTList();
		a2.add(a1);
		a2.add(a1);
		AST a3 = new ASTMathFunc(a2, "exp");
		Object result = a3.eval(null);
		assertEquals(Math.exp(1.0), result);
	}

}
