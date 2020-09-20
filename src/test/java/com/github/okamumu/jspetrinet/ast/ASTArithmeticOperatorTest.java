package com.github.okamumu.jspetrinet.ast;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.okamumu.jspetrinet.ast.values.*;
import com.github.okamumu.jspetrinet.ast.operators.*;
import com.github.okamumu.jspetrinet.exception.*;

public class ASTArithmeticOperatorTest {

	@Test
	public void testPlusIntInt() throws ASTException {
		AST a1 = ASTValue.getAST(1);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTArithmetic(a1, a2, "+");
		Object result = a3.eval(null);
		assertEquals("1+2", 3, result);
	}

	@Test
	public void testPlusIntDouble() throws ASTException {
		AST a1 = ASTValue.getAST(1);
		AST a2 = ASTValue.getAST(2.0);
		AST a3 = new ASTArithmetic(a1, a2, "+");
		Object result = a3.eval(null);
		assertEquals("1+2", 3.0, result);
	}

	@Test
	public void testPlusDoubleInt() throws ASTException {
		AST a1 = ASTValue.getAST(1.0);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTArithmetic(a1, a2, "+");
		Object result = a3.eval(null);
		assertEquals("1+2", 3.0, result);
	}

	@Test
	public void testPlusDoubleDouble() throws ASTException {
		AST a1 = ASTValue.getAST(1.0);
		AST a2 = ASTValue.getAST(2.0);
		AST a3 = new ASTArithmetic(a1, a2, "+");
		Object result = a3.eval(null);
		assertEquals("1+2", 3.0, result);
	}

	@SuppressWarnings("unused")
	@Test(expected = InvalidOperation.class)
	public void testPlusBooleanDouble() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		AST a2 = ASTValue.getAST(2.0);
		AST a3 = new ASTArithmetic(a1, a2, "+");
		Object result = a3.eval(null);
	}

	@Test
	public void testMinusIntInt() throws ASTException {
		AST a1 = ASTValue.getAST(1);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTArithmetic(a1, a2, "-");
		Object result = a3.eval(null);
		assertEquals("1-2", -1, result);
	}

	@Test
	public void testMinusIntDouble() throws ASTException {
		AST a1 = ASTValue.getAST(1);
		AST a2 = ASTValue.getAST(2.0);
		AST a3 = new ASTArithmetic(a1, a2, "-");
		Object result = a3.eval(null);
		assertEquals("1-2", -1.0, result);
	}

	@Test
	public void testMinusDoubleInt() throws ASTException {
		AST a1 = ASTValue.getAST(1.0);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTArithmetic(a1, a2, "-");
		Object result = a3.eval(null);
		assertEquals("1-2", -1.0, result);
	}

	@Test
	public void testMinusDoubleDouble() throws ASTException {
		AST a1 = ASTValue.getAST(1.0);
		AST a2 = ASTValue.getAST(2.0);
		AST a3 = new ASTArithmetic(a1, a2, "-");
		Object result = a3.eval(null);
		assertEquals("1-2", -1.0, result);
	}

	@Test(expected = InvalidOperation.class)
	public void testMinusEx() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTArithmetic(a1, a2, "-");
		Object result = a3.eval(null);
		assertEquals("1-2", new Integer(-1), result);
	}

	@Test
	public void testMultiIntInt() throws ASTException {
		AST a1 = ASTValue.getAST(5);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTArithmetic(a1, a2, "*");
		Object result = a3.eval(null);
		assertEquals("5*2", 10, result);
	}

	@Test
	public void testMultiIntDouble() throws ASTException {
		AST a1 = ASTValue.getAST(5);
		AST a2 = ASTValue.getAST(2.0);
		AST a3 = new ASTArithmetic(a1, a2, "*");
		Object result = a3.eval(null);
		assertEquals("5*2", 10.0, result);
	}

	@Test
	public void testMultiDoubleInt() throws ASTException {
		AST a1 = ASTValue.getAST(5.0);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTArithmetic(a1, a2, "*");
		Object result = a3.eval(null);
		assertEquals("5*2", 10.0, result);
	}

	@Test
	public void testMultiDoubleDouble() throws ASTException {
		AST a1 = ASTValue.getAST(5.0);
		AST a2 = ASTValue.getAST(2.0);
		AST a3 = new ASTArithmetic(a1, a2, "*");
		Object result = a3.eval(null);
		assertEquals("5*2", 10.0, result);
	}

	@SuppressWarnings("unused")
	@Test(expected = InvalidOperation.class)
	public void testMultiEx() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		AST a2 = ASTValue.getAST(2.0);
		AST a3 = new ASTArithmetic(a1, a2, "*");
		Object result = a3.eval(null);
	}

	@Test
	public void testDivideIntInt() throws ASTException {
		AST a1 = ASTValue.getAST(5);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTArithmetic(a1, a2, "/");
		Object result = a3.eval(null);
		assertEquals("5/2", 5.0/2.0, result);
	}

	@Test
	public void testDivideIntDouble() throws ASTException {
		AST a1 = ASTValue.getAST(5);
		AST a2 = ASTValue.getAST(2.0);
		AST a3 = new ASTArithmetic(a1, a2, "/");
		Object result = a3.eval(null);
		assertEquals("5/2", 5.0/2.0, result);
	}

	@Test
	public void testDivideDoubleInt() throws ASTException {
		AST a1 = ASTValue.getAST(5.0);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTArithmetic(a1, a2, "/");
		Object result = a3.eval(null);
		assertEquals("5/2", 5.0/2.0, result);
	}

	@Test
	public void testDivideDoubleDouble() throws ASTException {
		AST a1 = ASTValue.getAST(5.0);
		AST a2 = ASTValue.getAST(2.0);
		AST a3 = new ASTArithmetic(a1, a2, "/");
		Object result = a3.eval(null);
		assertEquals("5/2", 5.0/2.0, result);
	}

	@SuppressWarnings("unused")
	@Test(expected = InvalidOperation.class)
	public void testDivideEx1() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		AST a2 = ASTValue.getAST(2.0);
		AST a3 = new ASTArithmetic(a1, a2, "/");
		Object result = a3.eval(null);
	}

	@SuppressWarnings("unused")
	@Test(expected = InvalidOperation.class)
	public void testDivideEx2() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTArithmetic(a1, a2, "/");
		Object result = a3.eval(null);
	}

	@SuppressWarnings("unused")
	@Test(expected = InvalidOperation.class)
	public void testDivideEx3() throws ASTException {
		AST a1 = ASTValue.getAST(5);
		AST a2 = ASTValue.getAST(true);
		AST a3 = new ASTArithmetic(a1, a2, "/");
		Object result = a3.eval(null);
	}

	@Test
	public void testDivideByZero1() throws ASTException {
		AST a1 = ASTValue.getAST(5.0);
		AST a2 = ASTValue.getAST(0);
		AST a3 = new ASTArithmetic(a1, a2, "/");
		Object result = a3.eval(null);
		assertEquals("5/2", NaN.class, result.getClass());
	}

	@Test
	public void testDivideByZero2() throws ASTException {
		AST a1 = ASTValue.getAST(5.0);
		AST a2 = ASTValue.getAST(0.0);
		AST a3 = new ASTArithmetic(a1, a2, "/");
		Object result = a3.eval(null);
		assertEquals("5/2", NaN.class, result.getClass());
	}

	@Test
	public void testIDivideIntInt() throws ASTException {
		AST a1 = ASTValue.getAST(5);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTArithmetic(a1, a2, "div");
		Object result = a3.eval(null);
		assertEquals("5/2", 5/2, result);
	}

	@Test
	public void testIDivideIntDouble() throws ASTException {
		AST a1 = ASTValue.getAST(5);
		AST a2 = ASTValue.getAST(2.2);
		AST a3 = new ASTArithmetic(a1, a2, "div");
		Object result = a3.eval(null);
		assertEquals("5/2", 5/2, result);
	}

	@Test
	public void testIDivideDoubleInt() throws ASTException {
		AST a1 = ASTValue.getAST(5.1);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTArithmetic(a1, a2, "div");
		Object result = a3.eval(null);
		assertEquals("5/2", 5/2, result);
	}

	@Test
	public void testIDivideDoubleDouble() throws ASTException {
		AST a1 = ASTValue.getAST(5.1);
		AST a2 = ASTValue.getAST(2.2);
		AST a3 = new ASTArithmetic(a1, a2, "div");
		Object result = a3.eval(null);
		assertEquals("5/2", 5/2, result);
	}

	@SuppressWarnings("unused")
	@Test(expected = InvalidOperation.class)
	public void testIDivideEx1() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		AST a2 = ASTValue.getAST(2.0);
		AST a3 = new ASTArithmetic(a1, a2, "div");
		Object result = a3.eval(null);
	}

	@SuppressWarnings("unused")
	@Test(expected = InvalidOperation.class)
	public void testIDivideEx2() throws ASTException {
		AST a1 = ASTValue.getAST(true);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTArithmetic(a1, a2, "div");
		Object result = a3.eval(null);
	}

	@SuppressWarnings("unused")
	@Test(expected = InvalidOperation.class)
	public void testIDivideEx3() throws ASTException {
		AST a1 = ASTValue.getAST(5.5);
		AST a2 = ASTValue.getAST(true);
		AST a3 = new ASTArithmetic(a1, a2, "div");
		Object result = a3.eval(null);
	}

	@Test
	public void testIDivideByZero1() throws ASTException {
		AST a1 = ASTValue.getAST(5.0);
		AST a2 = ASTValue.getAST(0);
		AST a3 = new ASTArithmetic(a1, a2, "div");
		Object result = a3.eval(null);
		assertEquals("5/2", NaN.class, result.getClass());
	}

	@Test
	public void testIDivideByZero2() throws ASTException {
		AST a1 = ASTValue.getAST(5.0);
		AST a2 = ASTValue.getAST(0.0);
		AST a3 = new ASTArithmetic(a1, a2, "div");
		Object result = a3.eval(null);
		assertEquals("5/2", NaN.class, result.getClass());
	}

	@Test
	public void testMod() throws ASTException {
		AST a1 = ASTValue.getAST(5);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTArithmetic(a1, a2, "mod");
		Object result = a3.eval(null);
		assertEquals("5%2", 5 % 2, result);
	}

	@SuppressWarnings("unused")
	@Test(expected = InvalidOperation.class)
	public void testMod2() throws ASTException {
		AST a1 = ASTValue.getAST(5.0);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTArithmetic(a1, a2, "mod");
		Object result = a3.eval(null);
	}

	@SuppressWarnings("unused")
	@Test(expected = InvalidOperator.class)
	public void testEval1() throws ASTException {
		AST a1 = ASTValue.getAST(5.0);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTArithmetic(a1, a2, "mul");
		Object result = a3.eval(null);
	}

	@Test
	public void testEval2() throws ASTException {
		AST a1 = ASTValue.getAST(new ASTNull());
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTArithmetic(a1, a2, "+");
		Object result = a3.eval(null);
		assertEquals("5/2", NaN.class, result.getClass());
	}
}
