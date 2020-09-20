package com.github.okamumu.jspetrinet.ast;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.okamumu.jspetrinet.ast.values.*;
import com.github.okamumu.jspetrinet.ast.operators.*;
import com.github.okamumu.jspetrinet.exception.*;

public class ASTComparatorGteTest {

	@Test
	public void testGteIntInt1() throws ASTException {
		AST a1 = ASTValue.getAST(1);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTComparator(a1, a2, ">=");
		Object result = a3.eval(null);
		assertEquals(1>=2, result);
	}

	@Test
	public void testGteIntInt2() throws ASTException {
		AST a1 = ASTValue.getAST(2);
		AST a2 = ASTValue.getAST(1);
		AST a3 = new ASTComparator(a1, a2, ">=");
		Object result = a3.eval(null);
		assertEquals(2>=1, result);
	}

	@Test
	public void testGteIntInt3() throws ASTException {
		AST a1 = ASTValue.getAST(2);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTComparator(a1, a2, ">=");
		Object result = a3.eval(null);
		assertEquals(2>=2, result);
	}

	@Test
	public void testGteIntDouble() throws ASTException {
		AST a1 = ASTValue.getAST(1);
		AST a2 = ASTValue.getAST(2.0);
		AST a3 = new ASTComparator(a1, a2, ">=");
		Object result = a3.eval(null);
		assertEquals(1>=2, result);
	}

	@Test
	public void testGteDoubleInt() throws ASTException {
		AST a1 = ASTValue.getAST(1.0);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTComparator(a1, a2, ">=");
		Object result = a3.eval(null);
		assertEquals(1>=2, result);
	}

	@Test
	public void testGteIntDoubleDouble() throws ASTException {
		AST a1 = ASTValue.getAST(1.0);
		AST a2 = ASTValue.getAST(2.0);
		AST a3 = new ASTComparator(a1, a2, ">=");
		Object result = a3.eval(null);
		assertEquals(1>=2, result);
	}

	@SuppressWarnings("unused")
	@Test(expected = InvalidOperation.class)
	public void testGteIntBooleanBoolean() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		AST a2 = ASTValue.getAST(false);
		AST a3 = new ASTComparator(a1, a2, ">=");
		Object result = a3.eval(null);
	}
}
