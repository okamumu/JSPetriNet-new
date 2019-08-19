package com.github.okamumu.jspetrinet.petri;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidDefinition;
import com.github.okamumu.jspetrinet.exception.ObjectNotFoundInASTEnv;
import com.github.okamumu.jspetrinet.petri.*;
import com.github.okamumu.jspetrinet.petri.nodes.*;
import com.github.okamumu.jspetrinet.writer.PNWriter;
import com.github.okamumu.jspetrinet.petri.arcs.*;

public class PetriNetCompileTest {
	
	Env env;

    @Before
    public void setUp() throws ObjectNotFoundInASTEnv, InvalidDefinition {
    	env = new Env();
    	env.put("f1", ASTValue.getAST(10));
//    	FactoryPN factory = FactoryPN.getInstance();
//    	factory.reset();

    	Node node;
    	node = new Node();
    	node.put("type", "place");
    	node.put("label", "P1");
    	env.put("P1", node);
    	node = new Node();
    	node.put("type", "place");
    	node.put("label", "P2");
    	env.put("P2", node);
    	node = new Node();
    	node.put("type", "imm");
    	node.put("label", "T1");
    	env.put("T1", node);
    	node = new Node();
    	node.put("type", "arc");
    	node.put("src", "P1");
    	node.put("dest", "T1");
    	node.put("multi", env.get("f1"));
    	env.put(node);
    	node = new Node();
    	node.put("type", "arc");
    	node.put("src", "T1");
    	node.put("dest", "P2");
    	env.put(node);
    }

    @Test
	public void testPrintNet() {
    	try {
			Net net = FactoryPN.compile(env);
	    	PNWriter.write(net, null);
		} catch (ObjectNotFoundInASTEnv e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidDefinition e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ASTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
