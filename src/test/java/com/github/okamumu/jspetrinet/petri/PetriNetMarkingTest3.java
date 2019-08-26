package com.github.okamumu.jspetrinet.petri;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.github.okamumu.jmatout.MATLABDataElement;
import com.github.okamumu.jmatout.MATLABEndian;
import com.github.okamumu.jmatout.MATLABMatFile;
import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.dist.ConstDist;
import com.github.okamumu.jspetrinet.ast.operators.ASTArithmetic;
import com.github.okamumu.jspetrinet.ast.operators.ASTComparator;
import com.github.okamumu.jspetrinet.ast.operators.ASTIfThenElse;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.exception.InvalidDefinition;
import com.github.okamumu.jspetrinet.exception.JSPNException;
import com.github.okamumu.jspetrinet.exception.ObjectNotFoundInASTEnv;
import com.github.okamumu.jspetrinet.marking.GenVec;
import com.github.okamumu.jspetrinet.marking.Mark;
import com.github.okamumu.jspetrinet.marking.MarkingGraph;
import com.github.okamumu.jspetrinet.marking.method.DFS;
import com.github.okamumu.jspetrinet.matrix.ASTMatrix;
import com.github.okamumu.jspetrinet.matrix.ASTVector;
import com.github.okamumu.jspetrinet.matrix.GTuple;
import com.github.okamumu.jspetrinet.matrix.MarkingMatrix;
import com.github.okamumu.jspetrinet.petri.*;
import com.github.okamumu.jspetrinet.writer.MarkWriter;
import com.github.okamumu.jspetrinet.writer.MatlabMatrixWriter;
import com.github.okamumu.jspetrinet.writer.PNWriter;
import com.github.okamumu.jspetrinet.petri.arcs.*;
import com.github.okamumu.jspetrinet.petri.ast.ASTEnableCond;
import com.github.okamumu.jspetrinet.petri.ast.ASTNToken;

public class PetriNetMarkingTest3 {
	
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
    	
    	node = new Node();
    	node.put("type", "reward");
    	node.put("label", "reward1");
    	node.put("formula", new ASTIfThenElse(new ASTComparator(new ASTNToken("PService"), ASTValue.getAST(5), ">="), ASTValue.getAST(1), ASTValue.getAST(0)));
    	env.put(node);

    	node = new Node();
    	node.put("type", "reward");
    	node.put("label", "reward2");
    	node.put("formula", new ASTIfThenElse(new ASTEnableCond("TArrival"), ASTValue.getAST(1), ASTValue.getAST(0)));
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
			for (Map.Entry<GenVec,List<Mark>> entry : mg.getMarkSet().entrySet()) {
				System.out.println(entry.getKey());
				for (Mark m : entry.getValue()) {
//					System.out.print(mat.getMarkIndex().get(m) + " : ");
//					System.out.println(m.getString(net));
				}
			}
			for (Map.Entry<GTuple,ASTMatrix> m : mat.getMatrixSet().entrySet()) {
				System.out.print(mat.getMarkingGraph().getGenVecLabel().get(m.getKey().getSrc()));
				System.out.print(mat.getMarkingGraph().getGenVecLabel().get(m.getKey().getDest()));
				System.out.println(mat.getMarkingGraph().getGenTransLabel().get(m.getKey().getGenTrans()));
				System.out.print(m.getKey().getSrc());
				System.out.print(" => ");
				System.out.print(m.getKey().getDest());
				System.out.println(" Trans:" + m.getKey().getGenTrans());
				System.out.print(m.getValue().getISize());
				System.out.print(" ");
				System.out.println(m.getValue().getJSize());
				System.out.println(Arrays.toString(m.getValue().getI()));
				System.out.println(Arrays.toString(m.getValue().getJ()));
				System.out.println(Arrays.toString(m.getValue().getValue(env)));
			}
			for (Map.Entry<GTuple,ASTVector> m : mat.getSumVectorSet().entrySet()) {
				System.out.print(mat.getMarkingGraph().getGenVecLabel().get(m.getKey().getSrc()));
				System.out.println(mat.getMarkingGraph().getGenTransLabel().get(m.getKey().getGenTrans()));
				System.out.print(m.getKey().getSrc());
				System.out.println(" Trans:" + m.getKey().getGenTrans());
				System.out.println(Arrays.toString(m.getValue().getValue(env)));
			}
		} catch (JSPNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    @Test
	public void testMarkingMatlabMat() {
    	try {
    		Net net = FactoryPN.compile(env);
        	int[] vec = {0};
			MarkingGraph mg = MarkingGraph.create(new Mark(vec), net, env, new DFS());
			MarkingMatrix mat = MarkingMatrix.create(net, env, mg, 0);
			MatlabMatrixWriter.write("test2.mat", env, mat);
		} catch (JSPNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    @Test
    public void testJmatout() {
    	// create MATLABMatFile object with LittleEndian
    	MATLABMatFile matfile = new MATLABMatFile(MATLABEndian.LittleEndian);

    	// create and add DataElement; 2x5 matrix labeled by A. Note that the values are given by column major.
    	matfile.addDataElement(MATLABDataElement.doubleMatrix("A", new int[] {2,5}, new double[] {1,2,3,4,5,6,7,8,9,10}));

    	// create and add DataElement; 3x3 sparse matrix, which is CSC (compressed sparse column)
    	matfile.addDataElement(MATLABDataElement.doubleSparseMatrix("B", new int[] {3,3}, 3, new int[] {0,1,2}, new int[] {0,1,1,3}, new double[] {1,2,3}));

    	try {
    	  // write the two matrices into the file "test1.mat"
    	  matfile.writeToFile(Paths.get("test1.mat").toFile());
    	} catch (IOException e) {
    	  e.printStackTrace();    	
    	}
    }
}
