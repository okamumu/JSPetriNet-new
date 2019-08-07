package com.github.okamumu.jspetrinet.ast;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.okamumu.jspetrinet.ast.values.*;
import com.github.okamumu.jspetrinet.ast.operators.*;
import com.github.okamumu.jspetrinet.exception.*;

public class ASTComparatorEqTest {

	@Test
	public void testEqIntInt1() throws ASTException {
		AST a1 = ASTValue.getAST(1);
		AST a2 = ASTValue.getAST(1);
		AST a3 = new ASTComparator(a1, a2, "==");
		Object result = a3.eval(null);
		assertEquals("1==1", true, result);
	}

	@Test
	public void testEqIntInt2() throws ASTException {
		AST a1 = ASTValue.getAST(1);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTComparator(a1, a2, "==");
		Object result = a3.eval(null);
		assertEquals("1==2", false, result);
	}

	@Test
	public void testEqIntDouble() throws ASTException {
		AST a1 = ASTValue.getAST(1);
		AST a2 = ASTValue.getAST(1.5);
		AST a3 = new ASTComparator(a1, a2, "==");
		Object result = a3.eval(null);
		assertEquals(1 == 1.5, result);
	}

	@Test
	public void testEqDoubleInt() throws ASTException {
		AST a1 = ASTValue.getAST(1.0);
		AST a2 = ASTValue.getAST(1);
		AST a3 = new ASTComparator(a1, a2, "==");
		Object result = a3.eval(null);
		assertEquals("1==1", true, result);
	}

	@Test
	public void testEqIntDoubleDouble() throws ASTException {
		AST a1 = ASTValue.getAST(1.0);
		AST a2 = ASTValue.getAST(1.0);
		AST a3 = new ASTComparator(a1, a2, "==");
		Object result = a3.eval(null);
		assertEquals("1==1", true, result);
	}

	@Test
	public void testEqIntBooleanBoolean() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		AST a2 = ASTValue.getAST(true);
		AST a3 = new ASTComparator(a1, a2, "==");
		Object result = a3.eval(null);
		assertEquals("true==true", true, result);
	}

	@Test(expected = InvalidOperation.class)
	public void testEqEx1() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		AST a2 = ASTValue.getAST(1);
		AST a3 = new ASTComparator(a1, a2, "==");
		Object result = a3.eval(null);
	}
}
