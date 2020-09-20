package com.github.okamumu.jspetrinet.ast;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.okamumu.jspetrinet.ast.values.*;
import com.github.okamumu.jspetrinet.ast.operators.*;
import com.github.okamumu.jspetrinet.exception.*;

public class ASTITETest {

	@Test
	public void testIfThenElse() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		AST a2 = ASTValue.getAST(2);
		AST a3 = ASTValue.getAST(1);
		AST a4 = new ASTIfThenElse(a1, a2, a3);
		Object result = a4.eval(null);
		assertEquals(2, result);
	}

	@Test
	public void testIfThenElse2() throws ASTException {
		AST a1 = ASTValue.getAST(false);
		AST a2 = ASTValue.getAST(2);
		AST a3 = ASTValue.getAST(1);
		AST a4 = new ASTIfThenElse(a1, a2, a3);
		Object result = a4.eval(null);
		assertEquals(1, result);
	}

	@Test
	public void testIfThenElseString() throws ASTException {
		AST a1 = ASTValue.getAST(false);
		AST a2 = ASTValue.getAST(2);
		AST a3 = ASTValue.getAST(1);
		AST a4 = new ASTIfThenElse(a1, a2, a3);
		assertEquals(a4.toString(), "ifelse(false,2,1)");
	}

	@Test(expected = InvalidOperation.class)
	public void testIfThenElse3() throws ASTException {
		AST a1 = ASTValue.getAST(1);
		AST a2 = ASTValue.getAST(2);
		AST a3 = ASTValue.getAST(1);
		AST a4 = new ASTIfThenElse(a1, a2, a3);
		Object result = a4.eval(null);
		assertEquals(1, result);
	}

	@Test
	public void testNan() throws ASTException {
		AST a1 = ASTValue.getAST(new ASTNull());
		AST a2 = ASTValue.getAST(2);
		AST a3 = ASTValue.getAST(1);
		AST a4 = new ASTIfThenElse(a1, a2, a3);
		Object result = a4.eval(null);
		assertEquals(NaN.class, result.getClass());
	}
}
