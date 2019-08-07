package com.github.okamumu.jspetrinet.ast;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.okamumu.jspetrinet.ast.values.*;
import com.github.okamumu.jspetrinet.ast.operators.*;
import com.github.okamumu.jspetrinet.exception.*;

public class ASTComparatorEx {

	@Test(expected = InvalidOperator.class)
	public void testInvalidOp() throws ASTException {
		AST a1 = ASTValue.getAST(1);
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTComparator(a1, a2, "<>");
		Object result = a3.eval(null);
	}

	@Test
	public void testEval2() throws ASTException {
		AST a1 = ASTValue.getAST(new ASTNull());
		AST a2 = ASTValue.getAST(2);
		AST a3 = new ASTComparator(a1, a2, "==");
		Object result = a3.eval(null);
		assertEquals(NaN.class, result.getClass());
	}
}
