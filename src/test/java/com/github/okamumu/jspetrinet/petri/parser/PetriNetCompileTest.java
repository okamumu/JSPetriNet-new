package com.github.okamumu.jspetrinet.petri.parser;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.GrammarError;
import com.github.okamumu.jspetrinet.exception.InvalidDefinition;
import com.github.okamumu.jspetrinet.exception.ObjectNotFoundInASTEnv;
import com.github.okamumu.jspetrinet.petri.*;
import com.github.okamumu.jspetrinet.petri.nodes.*;
import com.github.okamumu.jspetrinet.writer.PNWriter;
import com.github.okamumu.jspetrinet.petri.arcs.*;

public class PetriNetCompileTest {
	
//	StringBuilder str;
//	
//    @Before
//    public void setUp() {
//    	str = new StringBuilder();
//    	str.append("1");
//    }

    @Test
	public void test01() throws GrammarError {
    	AST f = NetBuilder.buildExpression("1+2");
    	System.out.println(f);
	}

    @Test
	public void test02() throws GrammarError {
    	AST f = NetBuilder.buildExpression("1.0-2");
    	System.out.println(f);
	}

    @Test
	public void test03() throws GrammarError {
    	AST f = NetBuilder.buildExpression("1*2.5");
    	System.out.println(f);
	}

    @Test
	public void test04() throws GrammarError {
    	AST f = NetBuilder.buildExpression("1/2.0");
    	System.out.println(f);
	}

    @Test
	public void test05() throws GrammarError {
    	AST f = NetBuilder.buildExpression("1 <= 5");
    	System.out.println(f);
	}

    @Test
	public void test06() throws GrammarError {
    	AST f = NetBuilder.buildExpression("1 < 5");
    	System.out.println(f);
	}

    @Test
	public void test07() throws GrammarError {
    	AST f = NetBuilder.buildExpression("1 >= 2");
    	System.out.println(f);
	}

    @Test
	public void test08() throws GrammarError {
    	AST f = NetBuilder.buildExpression("1 > 5");
    	System.out.println(f);
	}
    
    @Test
	public void test09() throws GrammarError {
    	AST f = NetBuilder.buildExpression("true && false");
    	System.out.println(f);
	}

    @Test
	public void test10() throws GrammarError {
    	AST f = NetBuilder.buildExpression("true || false");
    	System.out.println(f);
	}

    @Test
	public void test11() throws GrammarError {
    	AST f = NetBuilder.buildExpression("ifelse(true || false, 1e-10, 0.5)");
    	System.out.println(f);
	}

    @Test
	public void test12() throws GrammarError {
    	AST f = NetBuilder.buildExpression("det(value=5)");
    	System.out.println(f);
	}

    @Test (expected = GrammarError.class)
	public void test13() throws GrammarError {
    	// parser error
    	AST f = NetBuilder.buildExpression("unif(0.5, max=5)");
    	System.out.println(f);
	}

    @Test
	public void test14() throws GrammarError {
    	AST f = NetBuilder.buildExpression("unif(max=5, min=0.5)");
    	System.out.println(f);
	}

    @Test
	public void test15() throws GrammarError {
    	AST f = NetBuilder.buildExpression("expdist(rate=5)");
    	System.out.println(f);
	}

    @Test
	public void test16() throws GrammarError {
    	AST f = NetBuilder.buildExpression("exp(5)");
    	System.out.println(f);
	}

    @Test
	public void test17() throws GrammarError {
    	AST f = NetBuilder.buildExpression("exp(5,6)");
    	System.out.println(f); // exp(5)
	}

    @Test
	public void test18() throws GrammarError {
    	AST f = NetBuilder.buildExpression("sqrt(x=5)");
    	System.out.println(f);
	}

    @Test
	public void test19() throws GrammarError {
    	AST f = NetBuilder.buildExpression("pow(x=5, n=10)");
    	System.out.println(f);
	}

    @Test
	public void test20() throws GrammarError {
    	AST f = NetBuilder.buildExpression("min(1,2,3,4,5)");
    	System.out.println(f);
	}

    @Test
	public void test21() throws GrammarError {
    	AST f = NetBuilder.buildExpression("max(1,2,3,4,5)");
    	System.out.println(f);
	}

    @Test
	public void test25() throws GrammarError {
    	AST f = NetBuilder.buildExpression("log(100)");
    	System.out.println(f);
	}

    @Test
	public void test22() throws GrammarError {
    	AST f = NetBuilder.buildExpression("\"string\"");
    	System.out.println(f);
	}

    @Test
	public void test23() throws GrammarError {
    	AST f = NetBuilder.buildExpression("#p1");
    	System.out.println(f);
	}

    @Test
	public void test24() throws GrammarError {
    	AST f = NetBuilder.buildExpression("?t1");
    	System.out.println(f);
	}
}
