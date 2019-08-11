package com.github.okamumu.jspetrinet.petri.parser;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidDefinition;
import com.github.okamumu.jspetrinet.exception.JSPNException;
import com.github.okamumu.jspetrinet.exception.ObjectNotFoundInASTEnv;
import com.github.okamumu.jspetrinet.marking.Mark;
import com.github.okamumu.jspetrinet.marking.MarkingGraph;
import com.github.okamumu.jspetrinet.marking.method.DFS;
import com.github.okamumu.jspetrinet.matrix.MarkingMatrix;
import com.github.okamumu.jspetrinet.petri.*;
import com.github.okamumu.jspetrinet.petri.nodes.*;
import com.github.okamumu.jspetrinet.writer.MarkWriter;
import com.github.okamumu.jspetrinet.writer.MatlabMatrixWriter;
import com.github.okamumu.jspetrinet.writer.PNWriter;
import com.github.okamumu.jspetrinet.petri.arcs.*;

public class PetriNetCompileTest2 {
	
	StringBuilder str;
	
    @Test
	public void test01() {
    	str = new StringBuilder();
    	str.append("a = 1\n");
    	str.append("b = 1\n");
    	str.append("place Pservice\n");
    	str.append("exp Tarrival (rate=10.0, guard=#Pservice <= 10)\n");
    	str.append("gen Tservice (dist=unif(0,1))\n");
    	str.append("arc Tarrival to Pservice\n");
    	str.append("arc Pservice to Tservice\n");
    	str.append("reward cumstomers #Pservice\n");
    	Env env = new Env();
    	Net net = NetBuilder.build(str.toString(), env);
		PrintWriter bw = new PrintWriter(System.out);
    	PNWriter.write(bw, net, env);
    	bw.flush();
	}

    @Test
	public void test02() {
    	str = new StringBuilder();
    	str.append("a = 1\n");
    	str.append("b = 1\n");
    	str.append("place Pservice\n");
    	str.append("exp Tarrival (rate=10.0, guard=#Pservice <= 10)\n");
    	str.append("gen Tservice (dist=unif(0,1))\n");
    	str.append("arc Tarrival to Pservice\n");
    	str.append("arc Pservice to Tservice\n");
    	str.append("reward cumstomers #Pservice\n");
    	Env env = new Env();
    	Net net = NetBuilder.build(str.toString(), env);
		PrintWriter bw = new PrintWriter(System.out);
    	PNWriter.write(bw, net, env);
    	bw.flush();
		try {
			MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFS());
			MarkingMatrix mat = new MarkingMatrix(net, env, mg, 0);
			MatlabMatrixWriter matlab = new MatlabMatrixWriter(env, mat);
			matlab.writeMatlabMatrix("test2.mat");
		} catch (JSPNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    @Test
	public void test03() {
    	str = new StringBuilder();
    	str.append("a = 1\n");
    	str.append("b = 1\n");
    	str.append("place Pservice\n");
    	str.append("exp Tarrival (rate=10.0, guard=g1)\n");
    	str.append("gen Tservice (dist=unif(0,1))\n");
    	str.append("arc Tarrival to Pservice\n");
    	str.append("arc Pservice to Tservice\n");
    	str.append("g1=#Pservice <= 10\n");
    	str.append("reward cumstomers #Pservice\n");
		try {
	    	Env env = new Env();
	    	Net net = NetBuilder.build(str.toString(), env);
			PrintWriter bw = new PrintWriter(System.out);
	    	PNWriter.write(bw, net, env);
	    	bw.flush();
			MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFS());
			MarkingMatrix mat = new MarkingMatrix(net, env, mg, 0);
			MatlabMatrixWriter matlab = new MatlabMatrixWriter(env, mat);
			matlab.writeMatlabMatrix("test2.mat");
		} catch (JSPNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    @Test
	public void test04() {
    	str = new StringBuilder();
    	str.append("a = 1\n");
    	str.append("b = 1\n");
    	str.append("place Pservice\n");
    	str.append("exp Tarrival (rate=10.0, guard=g1) { #Ptest = #Ptest + 1 }\n");
    	str.append("gen Tservice (dist=unif(0,1)) { #Ptest = 0 }\n");
    	str.append("arc Tarrival to Pservice\n");
    	str.append("arc Pservice to Tservice\n");
    	str.append("g1=#Pservice <= 10\n");
    	str.append("place Ptest\n");
    	str.append("reward cumstomers #Pservice\n");
		try {
	    	Env env = new Env();
	    	Net net = NetBuilder.build(str.toString(), env);
			PrintWriter bw = new PrintWriter(System.out);
	    	PNWriter.write(bw, net, env);
			MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFS());
			MarkWriter.writeMark(bw, net, mg);
	    	bw.flush();
			MarkingMatrix mat = new MarkingMatrix(net, env, mg, 0);
			MatlabMatrixWriter matlab = new MatlabMatrixWriter(env, mat);
			matlab.writeMatlabMatrix("test2.mat");
		} catch (JSPNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}