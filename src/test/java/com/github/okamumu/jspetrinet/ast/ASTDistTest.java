package com.github.okamumu.jspetrinet.ast;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.okamumu.jspetrinet.ast.values.*;
import com.github.okamumu.jmtrandom.MT64;
import com.github.okamumu.jmtrandom.Random;
import com.github.okamumu.jspetrinet.ast.dist.ConstDist;
import com.github.okamumu.jspetrinet.ast.dist.*;
import com.github.okamumu.jspetrinet.exception.*;

public class ASTDistTest {

	@Test
	public void test1() throws ASTException {
		Dist a1 = new ConstDist(ASTValue.getAST(10));
		Random rnd = new Random(new MT64(1));
		double x = a1.next(null, rnd);
		assertEquals(10.0, (Object) Double.valueOf(x));
	}

	@Test
	public void test2() throws ASTException {
		Dist a1 = new UnifDist(ASTValue.getAST(0), ASTValue.getAST(10));
		Random rnd = new Random(new MT64(1));
		double x = a1.next(null, rnd);
		System.out.println(x);
	}
}
