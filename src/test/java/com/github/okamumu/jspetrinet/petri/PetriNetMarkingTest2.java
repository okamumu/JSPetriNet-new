package com.github.okamumu.jspetrinet.petri;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.operators.ASTArithmetic;
import com.github.okamumu.jspetrinet.ast.operators.ASTComparator;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.exception.InvalidDefinition;
import com.github.okamumu.jspetrinet.exception.JSPNException;
import com.github.okamumu.jspetrinet.exception.ObjectNotFoundInASTEnv;
import com.github.okamumu.jspetrinet.marking.Mark;
import com.github.okamumu.jspetrinet.marking.MarkingGraph;
import com.github.okamumu.jspetrinet.marking.method.DFS;
import com.github.okamumu.jspetrinet.matrix.ASTMatrix;
import com.github.okamumu.jspetrinet.matrix.GTuple;
import com.github.okamumu.jspetrinet.matrix.MarkingMatrix;
import com.github.okamumu.jspetrinet.petri.*;
import com.github.okamumu.jspetrinet.writer.MarkWriter;
import com.github.okamumu.jspetrinet.writer.PNWriter;
import com.github.okamumu.jspetrinet.petri.arcs.*;
import com.github.okamumu.jspetrinet.petri.ast.ASTNToken;

@SuppressWarnings("unused")
public class PetriNetMarkingTest2 {
	
	Net net;
	Env env;

    @Before
    public void setUp() throws ObjectNotFoundInASTEnv, InvalidDefinition {
    	env = new Env();
//    	FactoryPN factory = FactoryPN.getInstance();
//    	factory.reset();
    	Node node;

    	node = new Node();
    	node.put("type", "place");
    	node.put("label", "PService");
    	env.put("PService", node);

    	node = new Node();
    	node.put("type", "exp");
    	node.put("label", "TArrival");
    	node.put("rate", ASTValue.getAST(0.1));
    	node.put("guard", new ASTComparator(new ASTNToken("PService"), ASTValue.getAST(10), "<="));
    	env.put("TArrival", node);
    	node = new Node();
    	node.put("type", "exp");
    	node.put("label", "TService");
    	node.put("rate", new ASTArithmetic(new ASTNToken("PService"), ASTValue.getAST(1.0), "*"));
    	env.put("TService", node);

    	node = new Node();
    	node.put("type", "arc");
    	node.put("src", "TArrival");
    	node.put("dest", "PService");
    	env.put(node);
    	node = new Node();
    	node.put("type", "arc");
    	node.put("src", "PService");
    	node.put("dest", "TService");
    	env.put(node);
    }

    @Test
	public void testPrintNet() {
    	try {
    		Net net = FactoryPN.compile(env);
    		PrintWriter bw = new PrintWriter(System.out);
        	PNWriter.write(net, env);
        	bw.flush();
		} catch (JSPNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    @Test
	public void testMarking() {
    	try {
    		Net net = FactoryPN.compile(env);
    		PrintWriter bw = new PrintWriter(System.out);
        	int[] vec = {0};
			MarkingGraph mg = MarkingGraph.create(new Mark(vec), net, env, new DFS());
			MarkWriter.writeMark(mg);
        	bw.flush();
		} catch (JSPNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    @Test
	public void testMarkingMat() {
    	try {
    		Net net = FactoryPN.compile(env);
        	int[] vec = {0};
			MarkingGraph mg = MarkingGraph.create(new Mark(vec), net, env, new DFS());
			MarkingMatrix mat = MarkingMatrix.create(net, env, mg, 0);
			for (Map.Entry<GTuple,ASTMatrix> m : mat.getMatrixSet().entrySet()) {
				System.out.println(Arrays.toString(m.getValue().getI()));
				System.out.println(Arrays.toString(m.getValue().getJ()));
				System.out.println(Arrays.toString(m.getValue().getValue(env)));
			}
		} catch (JSPNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
