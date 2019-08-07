package com.github.okamumu.jspetrinet.petri;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.exception.InvalidDefinition;
import com.github.okamumu.jspetrinet.exception.JSPNException;
import com.github.okamumu.jspetrinet.exception.ObjectNotFoundInASTEnv;
import com.github.okamumu.jspetrinet.marking.Mark;
import com.github.okamumu.jspetrinet.marking.MarkingGraph;
import com.github.okamumu.jspetrinet.marking.method.DFS;
import com.github.okamumu.jspetrinet.matrix.MarkingMatrix;
import com.github.okamumu.jspetrinet.petri.*;
import com.github.okamumu.jspetrinet.petri.FactoryPN.DefArc;
import com.github.okamumu.jspetrinet.petri.FactoryPN.DefImmTrans;
import com.github.okamumu.jspetrinet.petri.FactoryPN.DefPlace;
import com.github.okamumu.jspetrinet.petri.nodes.*;
import com.github.okamumu.jspetrinet.writer.MarkWriter;
import com.github.okamumu.jspetrinet.writer.PNWriter;
import com.github.okamumu.jspetrinet.petri.arcs.*;

public class PetriNetMarkingTest {
	
	Net net;
	Env env;

    @Before
    public void setUp() throws ObjectNotFoundInASTEnv, InvalidDefinition {
    	env = new Env();
    	env.put("f1", ASTValue.getAST(1));
    	FactoryPN factory = FactoryPN.getInstance();
    	factory.reset();
    	FactoryPN.Node node;

    	node = factory.new DefPlace();
    	node.put("label", "P1");
    	env.put("P1", node);
    	node = factory.new DefPlace();
    	node.put("label", "P2");
    	env.put("P2", node);

    	node = factory.new DefImmTrans();
    	node.put("label", "T1");
    	env.put("T1", node);
    	node = factory.new DefArc();
    	node.put("src", "P1");
    	node.put("dest", "T1");
    	node.put("multi", env.get("f1"));
    	env.put(node);
    	node = factory.new DefArc();
    	node.put("src", "T1");
    	node.put("dest", "P2");
    	env.put(node);
    }

    @Test
	public void testPrintNet() {
    	try {
    		Net net = FactoryPN.getInstance().compilePN(env);
    		PrintWriter bw = new PrintWriter(System.out);
        	PNWriter.write(bw, net, null);
        	int[] vec = {1,0};
			MarkingGraph mg = MarkingGraph.create(new Mark(vec), net, env, new DFS());
			MarkWriter.writeMark(bw, net, mg);
			MarkWriter.writeMarkGroup(bw, net, mg);
			bw.flush();
			System.out.println(mg.immSize());
			System.out.println(mg.genSize());
			System.out.println(mg.absSize());
		} catch (JSPNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    @Test
	public void testMakingMatrix() {
    	try {
    		Net net = FactoryPN.getInstance().compilePN(env);
    		PrintWriter bw = new PrintWriter(System.out);
        	PNWriter.write(bw, net, null);
        	int[] vec = {1,0};
			MarkingGraph mg = MarkingGraph.create(new Mark(vec), net, env, new DFS());
			MarkWriter.writeMark(bw, net, mg);
			MarkWriter.writeMarkGroup(bw, net, mg);
			bw.flush();
			System.out.println(mg.immSize());
			System.out.println(mg.genSize());
			System.out.println(mg.absSize());
			
			MarkingMatrix mat = new MarkingMatrix(net, env, mg, 0);
		} catch (JSPNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}