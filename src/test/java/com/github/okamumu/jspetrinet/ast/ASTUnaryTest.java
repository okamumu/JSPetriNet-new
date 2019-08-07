package com.github.okamumu.jspetrinet.ast;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.okamumu.jspetrinet.ast.values.*;
import com.github.okamumu.jspetrinet.ast.operators.*;
import com.github.okamumu.jspetrinet.exception.*;

public class ASTUnaryTest {

	@Test
	public void test1() throws ASTException {
		AST a1 = ASTValue.getAST(1);
		AST a2 = new ASTUnary(a1, "+");
		Object result = a2.eval(null);
		assertEquals(1, result);
	}

	@Test
	public void testUnaryPlusD() throws ASTException {
		AST a1 = ASTValue.getAST(1.2);
		AST a2 = new ASTUnary(a1, "+");
		Object result = a2.eval(null);
		assertEquals(1.2, result);
	}

	@Test(expected = InvalidOperation.class)
	public void test2() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		AST a2 = new ASTUnary(a1, "+");
		Object result = a2.eval(null);
		assertEquals(1, result);
	}

	@Test
	public void testUnaryMinus1() throws ASTException {
		AST a1 = ASTValue.getAST(1);
		AST a2 = new ASTUnary(a1, "-");
		Object result = a2.eval(null);
		assertEquals(-1, result);
	}

	@Test
	public void testUnaryMinus1D() throws ASTException {
		AST a1 = ASTValue.getAST(1.2);
		AST a2 = new ASTUnary(a1, "-");
		Object result = a2.eval(null);
		assertEquals(-1.2, result);
	}

	@Test(expected = InvalidOperation.class)
	public void testUnaryMinus2() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		AST a2 = new ASTUnary(a1, "-");
		Object result = a2.eval(null);
		assertEquals(-1, result);
	}

	@Test
	public void testUnaryMinusString() throws ASTException {
		AST a1 = ASTValue.getAST(1.2);
		AST a2 = new ASTUnary(a1, "-");
		assertEquals(a2.toString(), "(-1.2)");
	}

	@Test
	public void testUnaryNot() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		AST a2 = new ASTUnary(a1, "!");
		Object result = a2.eval(null);
		assertEquals(!true, result);
	}

	@Test
	public void testUnaryNot2() throws ASTException {
		AST a1 = ASTValue.getAST(false);
		AST a2 = new ASTUnary(a1, "!");
		Object result = a2.eval(null);
		assertEquals(!false, result);
	}

	@Test(expected = InvalidOperation.class)
	public void testUnaryNotV() throws ASTException {
		AST a1 = ASTValue.getAST(1);
		AST a2 = new ASTUnary(a1, "!");
		Object result = a2.eval(null);
		assertEquals(!true, result);
	}

	@Test(expected = InvalidOperator.class)
	public void testUnaryInvalid1() throws ASTException {
		AST a1 = ASTValue.getAST(1);
		AST a2 = new ASTUnary(a1, "&");
		Object result = a2.eval(null);
		assertEquals(!true, result);
	}

	@Test
	public void testNan() throws ASTException {
		AST a1 = ASTValue.getAST(new ASTNull());
		AST a3 = new ASTUnary(a1, "-");
		Object result = a3.eval(null);
		assertEquals(NaN.class, result.getClass());
	}
}
