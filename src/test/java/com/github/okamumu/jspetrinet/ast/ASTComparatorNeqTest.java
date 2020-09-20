package com.github.okamumu.jspetrinet.ast;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.okamumu.jspetrinet.ast.values.*;
import com.github.okamumu.jspetrinet.ast.operators.*;
import com.github.okamumu.jspetrinet.exception.*;

public class ASTComparatorNeqTest {

	@Test
	public void testNeqIntInt1() throws ASTException {
		AST a1 = ASTValue.getAST(1);
		AST a2 = ASTValue.getAST(1);
		AST a3 = new ASTComparator(a1, a2, "!=");
		Object result = a3.eval(null);
		assertEquals(false, result);
	}

	@Test
	public void testNeqIntInt2() throws ASTException {
		AST a1 = ASTValue.getAST(1);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTComparator(a1, a2, "!=");
		Object result = a3.eval(null);
		assertEquals(true, result);
	}

	@Test
	public void testNeqIntDouble() throws ASTException {
		AST a1 = ASTValue.getAST(1);
		AST a2 = ASTValue.getAST(1.0);
		AST a3 = new ASTComparator(a1, a2, "!=");
		Object result = a3.eval(null);
		assertEquals(false, result);
	}

	@Test
	public void testNeqDoubleInt() throws ASTException {
		AST a1 = ASTValue.getAST(1.0);
		AST a2 = ASTValue.getAST(1);
		AST a3 = new ASTComparator(a1, a2, "!=");
		Object result = a3.eval(null);
		assertEquals(false, result);
	}

	@Test
	public void testNeqIntDoubleDouble() throws ASTException {
		AST a1 = ASTValue.getAST(1.0);
		AST a2 = ASTValue.getAST(1.0);
		AST a3 = new ASTComparator(a1, a2, "!=");
		Object result = a3.eval(null);
		assertEquals(false, result);
	}

	@Test
	public void testNeqIntBooleanBoolean() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		AST a2 = ASTValue.getAST(true);
		AST a3 = new ASTComparator(a1, a2, "!=");
		Object result = a3.eval(null);
		assertEquals(false, result);
	}

	@SuppressWarnings("unused")
	@Test(expected = InvalidOperation.class)
	public void testNeqEx1() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		AST a2 = ASTValue.getAST(1);
		AST a3 = new ASTComparator(a1, a2, "!=");
		Object result = a3.eval(null);
	}
}
