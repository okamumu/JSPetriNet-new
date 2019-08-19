package com.github.okamumu.jspetrinet.petri;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.petri.*;
import com.github.okamumu.jspetrinet.petri.nodes.*;
import com.github.okamumu.jspetrinet.petri.arcs.*;

public class PetriNetEnvTest {
	
	Net net;
	Env env;

    @Before
    public void setUp() {
    	env = new Env();
//    	FactoryPN factory = FactoryPN.getInstance();
    	Node node = new Node();
    	node.put("vanishable", true);
    	env.put("P1", node);
    }

    @Test
	public void testPrintNet() {
    	System.out.println(env.toString());
	}

}
