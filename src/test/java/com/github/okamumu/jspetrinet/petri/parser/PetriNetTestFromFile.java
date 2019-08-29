package com.github.okamumu.jspetrinet.petri.parser;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidDefinition;
import com.github.okamumu.jspetrinet.exception.JSPNException;
import com.github.okamumu.jspetrinet.exception.ObjectNotFoundInASTEnv;
import com.github.okamumu.jspetrinet.marking.GenVec;
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
public class PetriNetTestFromFile {

	static PrintWriter out;

	@BeforeClass
    public static void setUp() throws IOException {
		BufferedWriter buf = Files.newBufferedWriter(Paths.get("makePDF.sh"), StandardCharsets.UTF_8);
		out = new PrintWriter(buf, false);
	}
	
	@AfterClass
    public static void down() throws IOException {
		out.close();
	}
	
	public Net exampleMake(String file, ASTEnv env) {
		Net net = null;
		try {
	    	NetBuilder.buildFromFile(file + ".spn", env);
	    	net = FactoryPN.compile(env);
	    	PNWriter.write(file + "_pn.dot", net, env);
			out.println("dot -Tpdf -o " + file + "_pn.pdf " + file + "_pn.dot");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidDefinition e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ASTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return net;
	}

	public void example(String file, Net net, ASTEnv env) {
		try {
			long start = System.nanoTime();
			MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFS());
			System.out.println("done");
			System.out.println("computation time    : " + (System.nanoTime() - start) / 1000000000.0 + " (sec)");
			System.out.println("Total IMM size " + mg.getTotalState(GenVec.Type.IMM));
			System.out.println("Total IMM NNZ  " + mg.getTotalNNZ(GenVec.Type.IMM));
			System.out.println("Total GEN size " + mg.getTotalState(GenVec.Type.GEN));
			System.out.println("Total GEN NNZ  " + mg.getTotalNNZ(GenVec.Type.GEN));
			System.out.println("Total ABS size " + mg.getTotalState(GenVec.Type.ABS));
			System.out.println("Total ABS NNZ  " + mg.getTotalNNZ(GenVec.Type.ABS));
			MarkingMatrix mat = MarkingMatrix.create(net, env, mg, 0);
			MarkWriter.writeMark(file + "_mark.dot", mg);
			MarkWriter.writeMarkGroup(file + "_gmark.dot", mg);
			MatlabMatrixWriter.write(file + "_mat.mat", env, mat);
			StateWriter.write(file + ".state", net, mg, mat);
			out.println("dot -Tpdf -o " + file + "_mark.pdf " + file + "_mark.dot");
			out.println("dot -Tpdf -o " + file + "_gmark.pdf " + file + "_gmark.dot");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidDefinition e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ASTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSPNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void exampleTangible(String file, Net net, ASTEnv env) {
		try {
			long start = System.nanoTime();
			MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFStangible());
			System.out.println("done");
			System.out.println("computation time    : " + (System.nanoTime() - start) / 1000000000.0 + " (sec)");
			System.out.println("Total IMM size " + mg.getTotalState(GenVec.Type.IMM));
			System.out.println("Total IMM NNZ  " + mg.getTotalNNZ(GenVec.Type.IMM));
			System.out.println("Total GEN size " + mg.getTotalState(GenVec.Type.GEN));
			System.out.println("Total GEN NNZ  " + mg.getTotalNNZ(GenVec.Type.GEN));
			System.out.println("Total ABS size " + mg.getTotalState(GenVec.Type.ABS));
			System.out.println("Total ABS NNZ  " + mg.getTotalNNZ(GenVec.Type.ABS));
			MarkingMatrix mat = MarkingMatrix.create(net, env, mg, 0);
			MarkWriter.writeMark(file + "tan_mark.dot", mg);
			MarkWriter.writeMarkGroup(file + "tan_gmark.dot", mg);
			MatlabMatrixWriter.write(file + "tan_mat.mat", env, mat);
			StateWriter.write(file + "tan.state", net, mg, mat);
			out.println("dot -Tpdf -o " + file + "tan_mark.pdf " + file + "tan_mark.dot");
			out.println("dot -Tpdf -o " + file + "tan_gmark.pdf " + file + "tan_gmark.dot");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidDefinition e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ASTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSPNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void test01() {
    	String file = "example/spnp_example1";
    	Env env = new Env();
    	Net net = this.exampleMake(file, env);
    	this.example(file, net, env);
    	this.exampleTangible(file, net, env);
	}

	@Test
	@Ignore
	public void test02() {
    	String file = "example/spnp_example2";
    	Env env = new Env();
    	Net net = this.exampleMake(file, env);
    	this.example(file, net, env);
    	this.exampleTangible(file, net, env);
	}

	@Test
	@Ignore
	public void test03() {
    	String file = "example/spnp_example3";
    	Env env = new Env();
    	Net net = this.exampleMake(file, env);
    	this.example(file, net, env);
    	this.exampleTangible(file, net, env);
	}

	@Test
	@Ignore
	public void test04() {
    	String file = "example/spnp_example4";
    	Env env = new Env();
    	Net net = this.exampleMake(file, env);
    	this.example(file, net, env);
    	this.exampleTangible(file, net, env);
	}

	@Test
	@Ignore
	public void test05() {
    	String file = "example/spnp_example5";
    	Env env = new Env();
    	Net net = this.exampleMake(file, env);
    	this.example(file, net, env);
    	this.exampleTangible(file, net, env);
	}

	@Test
	@Ignore
	public void test06() {
    	String file = "example/spnp_example6";
    	Env env = new Env();
    	Net net = this.exampleMake(file, env);
    	this.example(file, net, env);
    	this.exampleTangible(file, net, env);
	}

	@Test
	public void test09() {
    	String file = "example/spnp_example9";
    	Env env = new Env();
    	Net net = this.exampleMake(file, env);
    	this.example(file, net, env);
    	this.exampleTangible(file, net, env);
	}

	@Test
	@Ignore
	public void test10() {
    	String file = "example/spnp_example10";
    	Env env = new Env();
    	Net net = this.exampleMake(file, env);
    	this.example(file, net, env);
    	this.exampleTangible(file, net, env);
	}
}
