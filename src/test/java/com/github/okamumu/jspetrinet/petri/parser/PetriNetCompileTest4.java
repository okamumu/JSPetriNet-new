package com.github.okamumu.jspetrinet.petri.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.GrammarError;
import com.github.okamumu.jspetrinet.exception.InvalidDefinition;
import com.github.okamumu.jspetrinet.exception.JSPNException;
import com.github.okamumu.jspetrinet.exception.ObjectNotFoundInASTEnv;
import com.github.okamumu.jspetrinet.marking.Mark;
import com.github.okamumu.jspetrinet.marking.MarkingGraph;
import com.github.okamumu.jspetrinet.marking.method.DFS;
import com.github.okamumu.jspetrinet.marking.method.DFStangible;
import com.github.okamumu.jspetrinet.matrix.MarkingMatrix;
import com.github.okamumu.jspetrinet.petri.*;
import com.github.okamumu.jspetrinet.petri.nodes.*;
import com.github.okamumu.jspetrinet.writer.MarkWriter;
import com.github.okamumu.jspetrinet.writer.MatlabMatrixWriter;
import com.github.okamumu.jspetrinet.writer.PNWriter;
import com.github.okamumu.jspetrinet.writer.StateWriter;
import com.github.okamumu.jspetrinet.petri.arcs.*;

@SuppressWarnings("unused")
public class PetriNetCompileTest4 {
	
	StringBuilder str;
	
    @Test
    @Ignore
	public void test01() {
    	str = new StringBuilder();
    	str.append("place P0 (init=1)\n");
    	str.append("place P1\n");
    	str.append("place P2\n");
    	str.append("place P3\n");
    	str.append("exp T1\n");
    	str.append("imm T2\n");
    	str.append("imm T3\n");
    	str.append("imm T4\n");
    	str.append("arc P0 to T1\n");
    	str.append("arc T1 to P1\n");
    	str.append("arc P1 to T2\n");
    	str.append("arc T2 to P2\n");
    	str.append("arc P1 to T3\n");
    	str.append("arc T3 to P3\n");
    	str.append("arc P3 to T4\n");
    	str.append("arc T4 to P1\n");
		try {
	    	Env env = new Env();
	    	NetBuilder.buildFromString(str.toString(), env);
	    	Net net = FactoryPN.compile(env);
	    	PNWriter.write(net, env);
			MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFS());
			MarkWriter.writeMark(mg);
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
	public void test02() {
    	str = new StringBuilder();
    	str.append("place P0 (init=1)\n");
    	str.append("place P1\n");
    	str.append("place P2\n");
    	str.append("place P3\n");
    	str.append("exp T1\n");
    	str.append("imm T2\n");
    	str.append("imm T3\n");
    	str.append("imm T4\n");
    	str.append("arc P0 to T1\n");
    	str.append("arc T1 to P1\n");
    	str.append("arc P1 to T2\n");
    	str.append("arc T2 to P2\n");
    	str.append("arc P1 to T3\n");
    	str.append("arc T3 to P3\n");
    	str.append("arc P3 to T4\n");
    	str.append("arc T4 to P1\n");
		try {
	    	Env env = new Env();
	    	NetBuilder.buildFromString(str.toString(), env);
	    	Net net = FactoryPN.compile(env);
	    	PNWriter.write(net, env);
			MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFStangible());
			MarkWriter.writeMark(mg);
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
}
