package com.github.okamumu.jspetrinet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.LoggerFactory;

import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidDefinition;
import com.github.okamumu.jspetrinet.exception.JSPNException;
import com.github.okamumu.jspetrinet.marking.GenVec;
import com.github.okamumu.jspetrinet.marking.MarkingGraph;
import com.github.okamumu.jspetrinet.marking.method.DFS;
import com.github.okamumu.jspetrinet.marking.method.DFStangible;
import com.github.okamumu.jspetrinet.matrix.MarkingMatrix;
import com.github.okamumu.jspetrinet.petri.Env;
import com.github.okamumu.jspetrinet.petri.FactoryPN;
import com.github.okamumu.jspetrinet.petri.Net;
import com.github.okamumu.jspetrinet.petri.NetBuilder;
import com.github.okamumu.jspetrinet.petri.nodes.GenTrans;
import com.github.okamumu.jspetrinet.writer.MarkWriter;
import com.github.okamumu.jspetrinet.writer.MatlabMatrixWriter;
import com.github.okamumu.jspetrinet.writer.PNWriter;

import ch.qos.logback.classic.LoggerContext;

public class CommandLineMain {
	
	public static void main(String[] args) {

		// stop log: If the logger stops, logger.debug will be executed and makes it slow.
		// In this version, all the loggers in making marks have been commented out.
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		loggerContext.stop();

		if (args.length < 1) {
			help();
			return;
		}
		
		String mode = args[0];
		String[] newargs = new String [args.length-1];
		for (int i=1; i<args.length; i++) {
			newargs[i-1] = args[i];
		}
		
		switch (mode) {
		case "view":
			cmdView(newargs, new HelpFormatter());
			break;
		case "mark":
			cmdMark(newargs, new HelpFormatter());
			break;
		case "sim":
			break;
		default:
			help();
			return;
		}
	}

	private static void help() {
		StringBuilder msg = new StringBuilder();
		msg
		   .append("Usage: JSPetriNet mode options\n")
		   .append("JSPetriNet requires either of view, mark and sim as a mode.\n")
		   .append(" - view: Output a dot file to draw a Petrinet with Graphviz.\n")
		   .append(" - mark; Create a marking graph and output the transition matrices.\n")
		   .append(" - sim : Simulate a Petrinet.\n");
		System.err.print(msg.toString());
	}
	
	public static void cmdView(String[] args, HelpFormatter hf) {
		Options options = new Options();
		options.addOption(Option.builder("i")
				.argName("file")
				.hasArg()
				.desc("Input PetriNet definition file")
				.build());
		options.addOption(Option.builder("o")
				.argName("file")
				.hasArg()
				.desc("Name of output file of a dot file.")
				.build());

        CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println("CMD parser failed in the mode view" + e);
	        hf.printHelp("[opts]", options);
	        return;
		}

    	Env env = new Env();
    	Net net = null;
		try {
	    	if (cmd.hasOption("i")) {
	    		NetBuilder.buildFromFile(cmd.getOptionValue("i"), env);
	    	} else {
	    		NetBuilder.buildFromFile(env);
	    	}
	    	net = FactoryPN.compile(env);
		} catch (InvalidDefinition e1) {
			e1.printStackTrace();
			return;
		} catch (ASTException e1) {
			e1.printStackTrace();
			return;
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		if (cmd.hasOption("o")) {
	    	try {
				PNWriter.write(cmd.getOptionValue("o"), net, env);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
	    	PNWriter.write(net, env);
		}
	}
	
	public static void cmdMark(String[] args, HelpFormatter hf) {
		Options options = new Options();
		options.addOption(Option.builder("i")
				.argName("file")
				.hasArg(true)
				.desc("Input PetriNet definition file")
				.build());
		options.addOption(Option.builder("imark")
				.argName("string")
				.hasArg(true)
				.desc("Initial marking")
				.build());
		options.addOption(Option.builder("o")
				.argName("file")
				.hasArg(true)
				.desc("Prefix of output files")
				.build());
		options.addOption(Option.builder("bin")
				.hasArg(false)
				.desc("Output matrices are as a MATLAB matfile")
				.build());
		options.addOption(Option.builder("text")
				.hasArg(false)
				.desc("Output matrices are text files")
				.build());
		options.addOption(Option.builder("tangible")
				.hasArg(false)
				.desc("Do vanishing for IMM transitions")
				.build());
		options.addOption(Option.builder("mgraph")
				.argName("file")
				.hasArg(true)
				.desc("Output of a dot file for marking")
				.build());
		options.addOption(Option.builder("mggraph")
				.argName("file")
				.hasArg(true)
				.desc("Output of a dot file for mark groups")
				.build());
//		options.addOption(CommandLineOptions.FIRINGLIMIT, true, "test mode (input depth for DFS)");
//		options.addOption(CommandLineOptions.EXP, true, "exp trans");
//		options.addOption(CommandLineOptions.SCC, true, "scc");

        CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println("CMD parser failed in the mode view" + e);
	        hf.printHelp("[opts]", options);
	        return;
		}

    	Env env = new Env();
    	Net net = null;
		try {
	    	if (cmd.hasOption("i")) {
	    		NetBuilder.buildFromFile(cmd.getOptionValue("i"), env);
	    	} else {
	    		NetBuilder.buildFromFile(env);
	    	}
	    	if (cmd.hasOption("imark")) {
	    		Map<String,Integer> imark = parseMark(cmd.getOptionValue("imark"));
	    		net = FactoryPN.compile(imark, env);
	    	} else {
	    		net = FactoryPN.compile(env);
	    	}
		} catch (InvalidDefinition e1) {
			e1.printStackTrace();
			return;
		} catch (ASTException e1) {
			e1.printStackTrace();
			return;
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		System.out.println("Create marking...");
		long start = System.nanoTime();
		MarkingGraph mg = null;
		try {
			if (cmd.hasOption("tangible")) {
				mg = MarkingGraph.create(net.getInitMark(), net, env, new DFStangible());
			} else {
				mg = MarkingGraph.create(net.getInitMark(), net, env, new DFS());			
			}
		} catch (JSPNException e) {
			e.printStackTrace();
		}
		System.out.println("done");
		System.out.println("computation time    : " + (System.nanoTime() - start) / 1000000000.0 + " (sec)");

		System.out.println(markingToString(net, mg));

		MarkingMatrix mat;
		try {
			mat = MarkingMatrix.create(net, env, mg, 0);
			if (cmd.hasOption("o")) {
				MatlabMatrixWriter.write(cmd.getOptionValue("o") + ".mat", env, mat);
			}
		} catch (ASTException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		MarkWriter.writeMark(file + "_mark.dot", mg);
//		MarkWriter.writeMarkGroup(file + "_gmark.dot", mg);
//		MatlabMatrixWriter.write(file + "_mat.mat", env, mat);

	}

	public static Map<String,Integer> parseMark(String str) {
		Map<String,Integer> result = new HashMap<String,Integer>();
		String[] ary = str.split(",", 0);
		Pattern p = Pattern.compile("(\\w+):([0-9]+)");
		for (String s : ary) {
			Matcher m = p.matcher(s);
			if (m.matches()) {
				String key = m.group(1);
				int value = Integer.parseInt(m.group(2));
				result.put(key, value);
			}
		}
		return result;
	}
	
	public static String markingToString(Net net, MarkingGraph mp) {
		String linesep = System.getProperty("line.separator").toString();
		StringBuilder res = new StringBuilder();
		int immtotal = mp.getTotalState(GenVec.Type.IMM);
		int gentotal = mp.getTotalState(GenVec.Type.GEN);
		int abstotal = mp.getTotalState(GenVec.Type.ABS);
		int total = immtotal + gentotal + abstotal;
		int immnnz = mp.getTotalNNZ(GenVec.Type.IMM);
		int gennnz = mp.getTotalNNZ(GenVec.Type.GEN);
		int absnnz = mp.getTotalNNZ(GenVec.Type.ABS);
		int nnz = immnnz + gennnz + absnnz;
		res.append("# of total states         : ").append(total).append(" (").append(nnz).append(")").append(linesep)
		   .append("# of total IMM states     : ").append(immtotal).append(" (").append(immnnz).append(")").append(linesep)
		   .append("# of total EXP/GEN states : ").append(gentotal).append(" (").append(gennnz).append(")").append(linesep)
		   .append("# of total ABS states     : ").append(abstotal).append(" (").append(absnnz).append(")").append(linesep);
		
		Iterator<GenVec> iter = mp.getGenVec().iterator();
		GenVec prev = null;
		while (iter.hasNext()) {
			GenVec gv = iter.next();
			if (!gv.isSameClass(prev)) {
				res.append(genvecToString(net, gv)).append(linesep);
			}
			if (gv.getType() == GenVec.Type.IMM) {
				res.append("  # of IMM states     : ");
			}
			if (gv.getType() == GenVec.Type.GEN) {
				res.append("  # of Exp/GEN stat   : ");
			}
			if (gv.getType() == GenVec.Type.ABS) {
				res.append("  # of ABS states     : ");
			}
			res.append(mp.getGenVecSize(gv)).append(" (").append(mp.getGenVecNNZ(gv)).append(")").append(linesep);
			prev = gv;
		}
		return res.toString();
	}

	public static String genvecToString(Net net, GenVec g) {
		String result = "(";
		for (GenTrans t: net.getGenTransSet()) {
			switch(g.get(t.getIndex())) {
			case 0:
				break;
			case 1:
				if (!result.equals("(")) {
					result += " ";
				}
				result += t.getLabel() + "->enable";
				break;
			case 2:
				if (!result.equals("(")) {
					result += " ";
				}
				result += t.getLabel() + "->preemption";
				break;
			default:
				break;
			}
		}
		if (result.equals("(")) {
			result += "EXP";
		}
		result += ")";
		return result;
	}

}
