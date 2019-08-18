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
import java.util.Iterator;
import java.util.List;

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

public class PetriNetTestSPNP {
	
	@Test
	public void testEx1() throws IOException, JSPNException {
    	String file = "example/spnp_example1";
    	Env env = new Env();
    	Net net = NetBuilder.buildFromFile(file + ".spn", env);
		long start = System.nanoTime();
		MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFS());
		double ctime = (System.nanoTime() - start) / 1000000000.0;
		assertTrue(ctime < 300);
		assertEquals("IMMsize", 0, mg.getTotalState(GenVec.Type.IMM));
		assertEquals("GENsize", 30, mg.getTotalState(GenVec.Type.GEN));
		assertEquals("ABSsize", 0, mg.getTotalState(GenVec.Type.ABS));
		assertEquals("IMM NNZ", 0, mg.getTotalNNZ(GenVec.Type.IMM));
		assertEquals("GEN NNZ", 88, mg.getTotalNNZ(GenVec.Type.GEN));
		assertEquals("ABS NNZ", 0, mg.getTotalNNZ(GenVec.Type.ABS));
	}

	@Test
	public void testEx1tan() throws IOException, JSPNException {
    	String file = "example/spnp_example1";
    	Env env = new Env();
    	Net net = NetBuilder.buildFromFile(file + ".spn", env);
		long start = System.nanoTime();
		MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFStangible());
		double ctime = (System.nanoTime() - start) / 1000000000.0;
		assertTrue(ctime < 300);
		assertEquals("IMMsize", 0, mg.getTotalState(GenVec.Type.IMM));
		assertEquals("GENsize", 30, mg.getTotalState(GenVec.Type.GEN));
		assertEquals("ABSsize", 0, mg.getTotalState(GenVec.Type.ABS));
		assertEquals("IMM NNZ", 0, mg.getTotalNNZ(GenVec.Type.IMM));
		assertEquals("GEN NNZ", 88, mg.getTotalNNZ(GenVec.Type.GEN));
		assertEquals("ABS NNZ", 0, mg.getTotalNNZ(GenVec.Type.ABS));
	}

	@Test
	public void testEx2() throws IOException, JSPNException {
    	String file = "example/spnp_example2";
    	Env env = new Env();
    	Net net = NetBuilder.buildFromFile(file + ".spn", env);
		long start = System.nanoTime();
		MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFS());
		double ctime = (System.nanoTime() - start) / 1000000000.0;
		assertTrue(ctime < 300);
		assertEquals("IMMsize", 4, mg.getTotalState(GenVec.Type.IMM));
		assertEquals("GENsize", 6, mg.getTotalState(GenVec.Type.GEN));
		assertEquals("ABSsize", 1, mg.getTotalState(GenVec.Type.ABS));
		assertEquals("IMM NNZ", 7, mg.getTotalNNZ(GenVec.Type.IMM));
		assertEquals("GEN NNZ", 8, mg.getTotalNNZ(GenVec.Type.GEN));
		assertEquals("ABS NNZ", 0, mg.getTotalNNZ(GenVec.Type.ABS));
	}

	@Test
	public void testEx2tan() throws IOException, JSPNException {
    	String file = "example/spnp_example2";
    	Env env = new Env();
    	Net net = NetBuilder.buildFromFile(file + ".spn", env);
		long start = System.nanoTime();
		MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFStangible());
		double ctime = (System.nanoTime() - start) / 1000000000.0;
		assertTrue(ctime < 300);
		assertEquals("IMMsize", 3, mg.getTotalState(GenVec.Type.IMM));
		assertEquals("GENsize", 6, mg.getTotalState(GenVec.Type.GEN));
		assertEquals("ABSsize", 1, mg.getTotalState(GenVec.Type.ABS));
		assertEquals("IMM NNZ", 6, mg.getTotalNNZ(GenVec.Type.IMM));
		assertEquals("GEN NNZ", 8, mg.getTotalNNZ(GenVec.Type.GEN));
		assertEquals("ABS NNZ", 0, mg.getTotalNNZ(GenVec.Type.ABS));
	}

	@Test
	public void testEx3() throws IOException, JSPNException {
    	String file = "example/spnp_example3";
    	Env env = new Env();
    	Net net = NetBuilder.buildFromFile(file + ".spn", env);
		long start = System.nanoTime();
		MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFS());
		double ctime = (System.nanoTime() - start) / 1000000000.0;
		assertTrue(ctime < 300);
		assertEquals("IMMsize", 0, mg.getTotalState(GenVec.Type.IMM));
		assertEquals("GENsize", 51, mg.getTotalState(GenVec.Type.GEN));
		assertEquals("ABSsize", 0, mg.getTotalState(GenVec.Type.ABS));
		assertEquals("IMM NNZ", 0, mg.getTotalNNZ(GenVec.Type.IMM));
		assertEquals("GEN NNZ", 100, mg.getTotalNNZ(GenVec.Type.GEN));
		assertEquals("ABS NNZ", 0, mg.getTotalNNZ(GenVec.Type.ABS));
	}

	@Test
	public void testEx3tan() throws IOException, JSPNException {
    	String file = "example/spnp_example3";
    	Env env = new Env();
    	Net net = NetBuilder.buildFromFile(file + ".spn", env);
		long start = System.nanoTime();
		MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFStangible());
		double ctime = (System.nanoTime() - start) / 1000000000.0;
		assertTrue(ctime < 300);
		assertEquals("IMMsize", 0, mg.getTotalState(GenVec.Type.IMM));
		assertEquals("GENsize", 51, mg.getTotalState(GenVec.Type.GEN));
		assertEquals("ABSsize", 0, mg.getTotalState(GenVec.Type.ABS));
		assertEquals("IMM NNZ", 0, mg.getTotalNNZ(GenVec.Type.IMM));
		assertEquals("GEN NNZ", 100, mg.getTotalNNZ(GenVec.Type.GEN));
		assertEquals("ABS NNZ", 0, mg.getTotalNNZ(GenVec.Type.ABS));
	}

	@Test
	public void testEx4() throws IOException, JSPNException {
    	String file = "example/spnp_example4";
    	Env env = new Env();
    	Net net = NetBuilder.buildFromFile(file + ".spn", env);
		long start = System.nanoTime();
		MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFS());
		double ctime = (System.nanoTime() - start) / 1000000000.0;
		assertTrue(ctime < 300);
		assertEquals("IMMsize", 63, mg.getTotalState(GenVec.Type.IMM));
		assertEquals("GENsize", 49, mg.getTotalState(GenVec.Type.GEN));
		assertEquals("ABSsize", 1, mg.getTotalState(GenVec.Type.ABS));
		assertEquals("IMM NNZ", 63, mg.getTotalNNZ(GenVec.Type.IMM));
		assertEquals("GEN NNZ", 147, mg.getTotalNNZ(GenVec.Type.GEN));
		assertEquals("ABS NNZ", 0, mg.getTotalNNZ(GenVec.Type.ABS));
	}

	@Test
	public void testEx4tan() throws IOException, JSPNException {
    	String file = "example/spnp_example4";
    	Env env = new Env();
    	Net net = NetBuilder.buildFromFile(file + ".spn", env);
		long start = System.nanoTime();
		MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFStangible());
		double ctime = (System.nanoTime() - start) / 1000000000.0;
		assertTrue(ctime < 300);
		assertEquals("IMMsize", 0, mg.getTotalState(GenVec.Type.IMM));
		assertEquals("GENsize", 49, mg.getTotalState(GenVec.Type.GEN));
		assertEquals("ABSsize", 1, mg.getTotalState(GenVec.Type.ABS));
		assertEquals("IMM NNZ", 0, mg.getTotalNNZ(GenVec.Type.IMM));
		assertEquals("GEN NNZ", 147, mg.getTotalNNZ(GenVec.Type.GEN));
		assertEquals("ABS NNZ", 0, mg.getTotalNNZ(GenVec.Type.ABS));
	}

	@Test
	public void testEx5() throws IOException, JSPNException {
    	String file = "example/spnp_example5";
    	Env env = new Env();
    	Net net = NetBuilder.buildFromFile(file + ".spn", env);
		long start = System.nanoTime();
		MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFS());
		double ctime = (System.nanoTime() - start) / 1000000000.0;
		assertTrue(ctime < 300);
		assertEquals("IMMsize", 72, mg.getTotalState(GenVec.Type.IMM));
		assertEquals("GENsize", 44, mg.getTotalState(GenVec.Type.GEN));
		assertEquals("ABSsize", 235, mg.getTotalState(GenVec.Type.ABS));
		assertEquals("IMM NNZ", 144, mg.getTotalNNZ(GenVec.Type.IMM));
		assertEquals("GEN NNZ", 346, mg.getTotalNNZ(GenVec.Type.GEN));
		assertEquals("ABS NNZ", 0, mg.getTotalNNZ(GenVec.Type.ABS));
	}

	@Test
	public void testEx5tan() throws IOException, JSPNException {
    	String file = "example/spnp_example5";
    	Env env = new Env();
    	Net net = NetBuilder.buildFromFile(file + ".spn", env);
		long start = System.nanoTime();
		MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFStangible());
		double ctime = (System.nanoTime() - start) / 1000000000.0;
		assertTrue(ctime < 300);
		assertEquals("IMMsize", 72, mg.getTotalState(GenVec.Type.IMM));
		assertEquals("GENsize", 44, mg.getTotalState(GenVec.Type.GEN));
		assertEquals("ABSsize", 235, mg.getTotalState(GenVec.Type.ABS));
		assertEquals("IMM NNZ", 144, mg.getTotalNNZ(GenVec.Type.IMM));
		assertEquals("GEN NNZ", 346, mg.getTotalNNZ(GenVec.Type.GEN));
		assertEquals("ABS NNZ", 0, mg.getTotalNNZ(GenVec.Type.ABS));
	}

	@Test
	public void testEx6() throws IOException, JSPNException {
    	String file = "example/spnp_example6";
    	Env env = new Env();
    	Net net = NetBuilder.buildFromFile(file + ".spn", env);
		long start = System.nanoTime();
		MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFS());
		double ctime = (System.nanoTime() - start) / 1000000000.0;
		assertTrue(ctime < 300);
		assertEquals("IMMsize", 19868, mg.getTotalState(GenVec.Type.IMM));
		assertEquals("GENsize", 26244, mg.getTotalState(GenVec.Type.GEN));
		assertEquals("ABSsize", 0, mg.getTotalState(GenVec.Type.ABS));
		assertEquals("IMM NNZ", 9844+10084, mg.getTotalNNZ(GenVec.Type.IMM));
		assertEquals("GEN NNZ", 11016+142560, mg.getTotalNNZ(GenVec.Type.GEN));
		assertEquals("ABS NNZ", 0, mg.getTotalNNZ(GenVec.Type.ABS));
	}

	@Test
	public void testEx6tan() throws IOException, JSPNException {
    	String file = "example/spnp_example6";
    	Env env = new Env();
    	Net net = NetBuilder.buildFromFile(file + ".spn", env);
		long start = System.nanoTime();
		MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFStangible());
		double ctime = (System.nanoTime() - start) / 1000000000.0;
		assertTrue(ctime < 300);
		assertEquals("IMMsize", 0, mg.getTotalState(GenVec.Type.IMM));
		assertEquals("GENsize", 26244, mg.getTotalState(GenVec.Type.GEN));
		assertEquals("ABSsize", 0, mg.getTotalState(GenVec.Type.ABS));
		assertEquals("IMM NNZ", 0, mg.getTotalNNZ(GenVec.Type.IMM));
		assertEquals("GEN NNZ", 153576, mg.getTotalNNZ(GenVec.Type.GEN));
		assertEquals("ABS NNZ", 0, mg.getTotalNNZ(GenVec.Type.ABS));
	}

	@Test
	public void testExRaid6() throws IOException, JSPNException {
    	String file = "example/raid6";
    	Env env = new Env();
    	Net net = NetBuilder.buildFromFile(file + ".spn", env);
		long start = System.nanoTime();
		MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFS());
		double ctime = (System.nanoTime() - start) / 1000000000.0;
		assertTrue(ctime < 300);
		int[] size = {4,1,1,2,1};
		int[] nnz = {4,1,1,4,1};
		for (int i=0; i<size.length; i++) {
			GenVec g = mg.getGenVec().get(i);
			assertEquals("Size" + g.getString(net), Integer.valueOf(size[i]), mg.getGenVecSize(g));
			assertEquals("NNZ" + g.getString(net), Integer.valueOf(nnz[i]), mg.getGenVecNNZ(g));
		}
	}

	@Test
	public void testExRaid6tan() throws IOException, JSPNException {
    	String file = "example/raid6";
    	Env env = new Env();
    	Net net = NetBuilder.buildFromFile(file + ".spn", env);
		long start = System.nanoTime();
		MarkingGraph mg = MarkingGraph.create(net.getInitMark(), net, env, new DFStangible());
		double ctime = (System.nanoTime() - start) / 1000000000.0;
		assertTrue(ctime < 300);
		int[] size = {1,1,2,1};
		int[] nnz = {1,1,4,1};
		for (int i=0; i<size.length; i++) {
			GenVec g = mg.getGenVec().get(i);
			assertEquals("Size" + g.getString(net), Integer.valueOf(size[i]), mg.getGenVecSize(g));
			assertEquals("NNZ" + g.getString(net), Integer.valueOf(nnz[i]), mg.getGenVecNNZ(g));
		}
	}
}
