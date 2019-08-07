package com.github.okamumu.jspetrinet.ast;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.okamumu.jspetrinet.ast.values.*;
import com.github.okamumu.jspetrinet.ast.operators.*;
import com.github.okamumu.jspetrinet.exception.*;

public class ASTLogicalTest {

	@Test
	public void test1() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		AST a2 = ASTValue.getAST(true);
		AST a3 = new ASTLogical(a1, a2, "&&");
		Object result = a3.eval(null);
		assertEquals(true && true, result);
	}

	@Test
	public void test2() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		AST a2 = ASTValue.getAST(false);
		AST a3 = new ASTLogical(a1, a2, "&&");
		Object result = a3.eval(null);
		assertEquals(true && false, result);
	}

	@Test(expected = InvalidOperation.class)
	public void test3() throws ASTException {
		AST a1 = ASTValue.getAST(1);
		AST a2 = ASTValue.getAST(false);
		AST a3 = new ASTLogical(a1, a2, "&&");
		Object result = a3.eval(null);
	}

	@Test
	public void test4() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		AST a2 = ASTValue.getAST(true);
		AST a3 = new ASTLogical(a1, a2, "||");
		Object result = a3.eval(null);
		assertEquals(true || true, result);
	}

	@Test
	public void test5() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		AST a2 = ASTValue.getAST(false);
		AST a3 = new ASTLogical(a1, a2, "||");
		Object result = a3.eval(null);
		assertEquals(true || false, result);
	}

	@Test
	public void test7() throws ASTException {
		AST a1 = ASTValue.getAST(false);
		AST a2 = ASTValue.getAST(false);
		AST a3 = new ASTLogical(a1, a2, "||");
		Object result = a3.eval(null);
		assertEquals(false || false, result);
	}

	@Test(expected = InvalidOperation.class)
	public void test6() throws ASTException {
		AST a1 = ASTValue.getAST(1);
		AST a2 = ASTValue.getAST(false);
		AST a3 = new ASTLogical(a1, a2, "||");
		Object result = a3.eval(null);
	}
	
	@Test(expected = InvalidOperator.class)
	public void testInvalidOp() throws ASTException {
		AST a1 = ASTValue.getAST(1);
		AST a2 = ASTValue.getAST(false);
		AST a3 = new ASTLogical(a1, a2, "%%");
		Object result = a3.eval(null);
	}

	@Test
	public void testNan() throws ASTException {
		AST a1 = ASTValue.getAST(new ASTNull());
		AST a2 = ASTValue.getAST(false);
		AST a3 = new ASTLogical(a1, a2, "||");
		Object result = a3.eval(null);
		assertEquals(NaN.class, result.getClass());
	}
}
