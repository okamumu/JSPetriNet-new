package com.github.okamumu.jspetrinet;

import java.io.IOException;

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
import com.github.okamumu.jspetrinet.petri.Env;
import com.github.okamumu.jspetrinet.petri.Net;
import com.github.okamumu.jspetrinet.petri.NetBuilder;
import com.github.okamumu.jspetrinet.writer.PNWriter;

import ch.qos.logback.classic.LoggerContext;

public class CommandLineMain {
	
	public static void main(String[] args) {
		// stop logback in the case of command line
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
		options.addOption(Option.builder("spn")
				.argName("infile")
				.hasArg()
				.desc("Input PetriNet definition file")
				.build());
		options.addOption(Option.builder("o")
				.argName("outfile")
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
    	Net net;
		try {
			net = buildNet(cmd, env);
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
		options.addOption(Option.builder("spn")
				.argName("infile")
				.hasArg(true)
				.desc("Input PetriNet definition file")
				.build());
		options.addOption(Option.builder("imark")
				.argName("initialmarking")
				.hasArg(true)
				.desc("Initial marking")
				.build());
		options.addOption(Option.builder("o")
				.argName("outfile")
				.hasArg(true)
				.desc("Prefix of output files")
				.build());
		options.addOption(Option.builder("bin")
				.argName("matlab")
				.hasArg(false)
				.desc("Output matrices are as a MATLAB matfile")
				.build());
		options.addOption(Option.builder("text")
				.argName("matlab")
				.hasArg(false)
				.desc("Output matrices are text files")
				.build());
		options.addOption(Option.builder("tangible")
				.argName("tangible")
				.hasArg(false)
				.desc("Do vanishing for IMM transitions")
				.build());
		options.addOption(Option.builder("mgraph")
				.argName("markgraph")
				.hasArg(true)
				.desc("Output of a dot file for marking")
				.build());
		options.addOption(Option.builder("mggraph")
				.argName("groupmarkgraph")
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
    	Net net;
		try {
			net = buildNet(cmd, env);
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

	private static Net buildNet(CommandLine cmd, Env env) throws InvalidDefinition, ASTException, IOException {
    	if (cmd.hasOption("i")) {
    		return NetBuilder.buildFromFile(cmd.getOptionValue("i"), env);
		} else {
			return NetBuilder.buildFromFile(env);
		}
	}
}
