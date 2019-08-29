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
public class PetriNetCompileTest3 {
	
	StringBuilder str;
	
    @Test
	public void test01() {
    	str = new StringBuilder();
    	str.append("place P1 (init=1)\n");
    	str.append("place P2\n");
    	str.append("place P3 (init=1)\n");
    	str.append("place P4\n");
    	str.append("exp T1\n");
    	str.append("exp T2\n");
    	str.append("exp T3\n");
    	str.append("arc P1 to T1\n");
    	str.append("arc T1 to P2\n");
    	str.append("arc P3 to T2\n");
    	str.append("arc T2 to P4\n");
    	str.append("harc P3 to T1\n");
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

    @Test
	public void test02() {
    	str = new StringBuilder();
    	str.append("a = 1\n");
    	str.append("b = 1\n");
    	str.append("place Pservice\n");
    	str.append("exp Tarrival (rate=lambda)\n");
    	str.append("exp Tservice (rate=mu)\n");
    	str.append("imm Tkeep (guard=#Pservice >= 10)\n");
    	str.append("arc Tarrival to Pservice\n");
    	str.append("arc Pservice to Tservice\n");
    	str.append("arc Pservice to Tkeep\n");
    	str.append("reward cumstomers #Pservice\n");
    	str.append("lambda = 0.1\n");
    	str.append("mu = 1\n");
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

    @Test
	public void test03() {
    	str = new StringBuilder();
    	str.append("place P1 (init=1)\n");
    	str.append("place P2\n");
    	str.append("place P3\n");
    	str.append("place P4\n");
    	str.append("exp T1\n");
    	str.append("imm T2\n");
    	str.append("imm T3\n");
    	str.append("arc P1 to T1\n");
    	str.append("arc T1 to P2\n");
    	str.append("arc P2 to T2\n");
    	str.append("arc T2 to P3\n");
    	str.append("arc P2 to T3\n");
    	str.append("arc T3 to P4\n");
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

    @Test
	public void test04() {
    	str = new StringBuilder();
    	str.append("place P1 (init=1)\n");
    	str.append("place P2\n");
    	str.append("place P3\n");
    	str.append("place P4\n");
    	str.append("exp T1\n");
    	str.append("imm T2\n");
    	str.append("imm T3\n");
    	str.append("arc P1 to T1\n");
    	str.append("arc T1 to P2\n");
    	str.append("arc P2 to T2\n");
    	str.append("arc T2 to P3\n");
    	str.append("arc P2 to T3\n");
    	str.append("arc T3 to P4\n");
		try {
	    	Env env = new Env();
	    	NetBuilder.buildFromString(str.toString(), env);
	    	Net net = FactoryPN.compile(env);
	    	PNWriter.write(net, env);
			MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFStangible());
			MarkWriter.writeMark(mg);
			MarkingMatrix mat = MarkingMatrix.create(net, env, mg, 0);
			MatlabMatrixWriter.write("test2.mat", env, mat);
			StateWriter.write(net, mg, mat);
		} catch (JSPNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    @Test (expected = GrammarError.class)
	public void test05() throws IOException, JSPNException {
    	str = new StringBuilder();
    	str.append("place P1 (init 1)\n");
    	str.append("place P2\n");
    	str.append("place P3 (init=1)\n");
    	str.append("place P4\n");
    	str.append("exp T1 6\n");
    	str.append("exp T2\n");
    	str.append("exp T3\n");
    	str.append("arc P1 to T1\n");
    	str.append("arc T1 to P2\n");
    	str.append("arc P3 to T2\n");
    	str.append("arc T2 to P4\n");
    	str.append("harc P3 to T1\n");
    	Env env = new Env();
    	NetBuilder.buildFromString(str.toString(), env);
    	Net net = FactoryPN.compile(env);
    	PNWriter.write(net, env);
		MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFStangible());
		MarkWriter.writeMark(mg);
		MarkingMatrix mat = MarkingMatrix.create(net, env, mg, 0);
		MatlabMatrixWriter.write("test2.mat", env, mat);
	}
}
