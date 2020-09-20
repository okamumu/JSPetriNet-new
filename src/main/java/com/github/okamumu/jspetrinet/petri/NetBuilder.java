package com.github.okamumu.jspetrinet.petri;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.ast.dist.ConstDist;
import com.github.okamumu.jspetrinet.ast.dist.ExpDist;
import com.github.okamumu.jspetrinet.ast.dist.UnifDist;
import com.github.okamumu.jspetrinet.ast.operators.ASTArithmetic;
import com.github.okamumu.jspetrinet.ast.operators.ASTComparator;
import com.github.okamumu.jspetrinet.ast.operators.ASTIfThenElse;
import com.github.okamumu.jspetrinet.ast.operators.ASTList;
import com.github.okamumu.jspetrinet.ast.operators.ASTLogical;
import com.github.okamumu.jspetrinet.ast.operators.ASTMathFunc;
import com.github.okamumu.jspetrinet.ast.operators.ASTUnary;
import com.github.okamumu.jspetrinet.ast.values.ASTNull;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.ast.values.ASTVariable;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.GrammarError;
import com.github.okamumu.jspetrinet.exception.InvalidDefinition;
import com.github.okamumu.jspetrinet.exception.ObjectNotFoundInASTEnv;
import com.github.okamumu.jspetrinet.parser.JSPetriNetParser;
import com.github.okamumu.jspetrinet.petri.ast.ASTAssignNToken;
import com.github.okamumu.jspetrinet.petri.ast.ASTEnableCond;
import com.github.okamumu.jspetrinet.petri.ast.ASTNToken;

public class NetBuilder {

	static public ASTEnv buildFromFile(ASTEnv env) throws InvalidDefinition, ASTException, IOException {
		InputStream is = System.in;
		NetBuilder builder = new NetBuilder(env);
		builder.parseProg(is);
    	return env;
	}

	static public ASTEnv buildFromFile(String file, ASTEnv env) throws InvalidDefinition, ASTException, IOException {
		InputStream is = Files.newInputStream(Paths.get(file));
		NetBuilder builder = new NetBuilder(env);
		builder.parseProg(is);
    	return env;
	}

	static public ASTEnv buildFromString(String in, ASTEnv env) throws InvalidDefinition, ASTException {
		NetBuilder builder = new NetBuilder(env);
		builder.parseProg(in);
		return env;
	}

	static public AST buildExpression(String in) throws GrammarError {
		Env env = new Env();
		NetBuilder builder = new NetBuilder(env);
		return builder.parseExpression(in);
	}

	private final Logger logger;
	private final LinkedList<AST> stack;
	private final LinkedList<String> errors;
	private final ASTEnv env;
	private final LinkedList<Node> node;

	private NetBuilder(ASTEnv env) {
		this.env = env;
        logger = LoggerFactory.getLogger(NetBuilder.class);
    	stack = new LinkedList<AST>();
    	node = new LinkedList<Node>();
    	errors = new LinkedList<String>();
	}
	
	private void parseProg(InputStream in) throws IOException, GrammarError {
		JSPetriNetParser parser = new JSPetriNetParser(this, in);
		parser.parseProg();
		if (errors.size() != 0) {
			throw new GrammarError("Error in SPN");
		}
	}
	
	private void parseProg(String in) throws ASTException {
		JSPetriNetParser parser = new JSPetriNetParser(this, in);
		parser.parseProg();
		if (errors.size() != 0) {
			throw new GrammarError("Error in SPN");
		}
	}

	private AST parseExpression(String in) throws GrammarError {
		JSPetriNetParser parser = new JSPetriNetParser(this, in);
		parser.parseExpression();
		if (errors.size() != 0) {
			throw new GrammarError("Error in SPN");
		}
		return stack.pop();		
	}
	
	public List<String> getErrors() {
		return errors;
	}

	public void setNodeOption() {
		AST right = stack.pop();
		String label = "arg" + node.size();
		node.peek().put(label, right);
		logger.debug("Set option {}: {}", label, right);
	}
	
	public void setNodeOption(String label) {
		AST right = stack.pop();
		node.peek().put(label, right);
		logger.debug("Set option {}: {}", label, right);
	}
	
	public void createNewNode() {
		node.push(new Node());
		logger.debug("Create a new option node");
	}

	public void buildNode(String type, String label) {
		switch (type) {
		case "place":
			node.peek().setType("place");
			node.peek().put("label", label);
			logger.debug("Create a place {}", label);
			break;
		case "imm":
			node.peek().setType("imm");
			node.peek().put("label", label);
			logger.debug("Create an imm {}", label);
			break;
		case "exp":
			node.peek().setType("exp");
			node.peek().put("label", label);
			logger.debug("Create an exp {}", label);
			break;
		case "gen":
			node.peek().setType("gen");
			node.peek().put("label", label);
			logger.debug("Create a gen {}", label);
			break;
		case "trans":
			node.peek().put("label", label);
			logger.debug("Create a trans {}", label);
			break;
		default:
		}
		env.put(label, node.pop());		
	}
	
	public void buildArc(String type, String src, String dest) {
		switch (type) {
		case "arc":
		case "iarc":
		case "oarc":
			node.peek().setType("arc");
			node.peek().put("src", src);
			node.peek().put("dest", dest);
			logger.debug("Create arc {} to {}", src, dest);
			break;
		case "harc":
			node.peek().setType("harc");
			node.peek().put("src", src);
			node.peek().put("dest", dest);
			logger.debug("Create harc {} to {}", src, dest);
			break;
		default:
		}
		env.put(src + ":" + dest, node.pop());
	}

	public void buildReward(String label) {
		node.peek().setType("reward");
		node.peek().put("label", label);
		AST f = stack.pop();
		node.peek().put("formula", f);		
		env.put(label, node.pop());
		logger.debug("Create reward {}: {}", label, f);
	}

	
	class BlockEnd extends ASTNull {}

	public void setUpdateBlockEnd() {
		stack.push(new BlockEnd());
		logger.debug("Set an update block end");
	}
	
	public void buildUpdateBlock() {
		ASTList list = new ASTList();
		AST a = stack.pop();
		while (!(a instanceof BlockEnd)) {
			list.add(a);
			a = stack.pop();
		}
		node.peek().put("update", list);
		logger.debug("Put an update block");
	}

	public void buildAssignExpression(String label) {
		AST right = stack.pop();
		env.put(label, right);
		logger.debug("Put an assign expression {} = {}", label, right);
	}

	public void buildAssignNTokenExpression() {
		AST right = stack.pop();
		ASTNToken ntoken = (ASTNToken) stack.pop();
		stack.push(new ASTAssignNToken(ntoken.getLabel(), right));
		logger.debug("Build an assing ntoken #{} = {}", ntoken.getLabel(), right);
	}

	public void buildValueExpression(String label) {
		try {
			Object tmp = env.get(label);
			stack.push(ASTValue.getAST(tmp));
		} catch (ObjectNotFoundInASTEnv e) {
			stack.push(new ASTVariable(label));
			logger.debug("The value {} is not found in environment", label);
		}
	}

	public void buildUnaryExpression(String op) {
		AST expr = stack.pop();
		stack.push(new ASTUnary(expr, op));
	}
	
	public void buildBinaryExpression(String op) {
		AST expr2 = stack.pop();
		AST expr1 = stack.pop();
		switch(op) {
		case "*":
		case "/":
		case "div":
		case "mod":
		case "+":
		case "-":
			stack.push(new ASTArithmetic(expr1, expr2, op));
			break;
		case "<":
		case "<=":
		case ">":
		case ">=":
		case "==":
		case "!=":
			stack.push(new ASTComparator(expr1, expr2, op));
			break;
		case "&&":
		case "||":
			stack.push(new ASTLogical(expr1, expr2, op));
			break;
		default:
		}
	}

	public void buildIfThenElseExpression(String op) {
		AST expr3 = stack.pop();
		AST expr2 = stack.pop();
		AST expr1 = stack.pop();
		stack.push(new ASTIfThenElse(expr1, expr2, expr3));
	}
	
	public void buildNToken(String label) {
		stack.push(new ASTNToken(label));
	}

	public void buildEnable(String label) {
		stack.push(new ASTEnableCond(label));
	}
	
	public void buildIntegerLiteral(String value) {
		stack.push(ASTValue.getAST(Integer.parseInt(value)));		
	}

	public void buildDoubleLiteral(String value) {
		stack.push(ASTValue.getAST(Double.parseDouble(value)));
	}

	public void buildBooleanLiteral(String value) {
		stack.push(ASTValue.getAST(Boolean.parseBoolean(value)));
	}

	public void buildStringLiteral(String value) {
		stack.push(ASTValue.getAST(value.replaceAll("\"", "")));
	}

	public void buildFunc(String func) {
		AST retval;
		switch (func) {
		case ConstDist.dname:
			retval = this.buildConstDist(node.pop());
			break;
		case UnifDist.dname:
			retval = this.buildUnifDist(node.pop());
			break;
//		case TNormDist.dname:
//			this.defineTNormDist(args);
//			break;
//		case LNormDist.dname:
//			this.defineLNormDist(args);
//			break;
//		case WeibullDist.dname:
//			this.defineWeibullDist(args);
//			break;
		case ExpDist.dname:
			retval = this.buildExpDist(node.pop());
			break;
		case "min":
			retval = this.buildMinFunc(node.pop());
			break;
		case "max":
			retval = this.buildMaxFunc(node.pop());
			break;
		case "pow":
			retval = this.buildPowFunc(node.pop());
			break;
		case "sqrt":
			retval = this.buildSqrtFunc(node.pop());
			break;
		case "exp":
			retval = this.buildExpFunc(node.pop());
			break;
		case "log":
			retval = this.buildLogFunc(node.pop());
			break;
//		case "print":
//			stack.push(new ASTMathFunc(args, "print"));
//			break;
		default:
			retval = ASTValue.getAST(null);
			logger.error("Function: {} is not defined", func);
		}
		stack.push(retval);
	}

	private AST buildConstDist(Node node) {
		AST value = ASTValue.getAST(1.0);
		for (Map.Entry<String, Object> entry : node.getOptEntry()) {
			switch (entry.getKey()) {
			case "value":
			case "arg0":
				value = (AST) entry.getValue();
				break;
			default:
			}
		}
		logger.debug("Build a const dist");
		return new ConstDist(value);
	}

	private AST buildUnifDist(Node node) {
		AST min = ASTValue.getAST(0.0);
		AST max = ASTValue.getAST(1.0);
		for (Map.Entry<String, Object> entry : node.getOptEntry()) {
			switch (entry.getKey()) {
			case "min":
			case "arg0":
				min = (AST) entry.getValue();
				break;
			case "max":
			case "arg1":
				max = (AST) entry.getValue();
				break;
			default:
			}
		}
		logger.debug("Build a unif dist");
		return new UnifDist(min, max);
	}

	private AST buildExpDist(Node node) {
		AST rate = ASTValue.getAST(1.0);
		for (Map.Entry<String, Object> entry : node.getOptEntry()) {
			switch (entry.getKey()) {
			case "rate":
			case "arg0":
				rate = (AST) entry.getValue();
				break;
			default:
			}
		}
		logger.debug("Build an exp dist");
		return new ExpDist(rate);
	}

	private AST buildMinFunc(Node node) {
		ASTList args = new ASTList();
		for (Map.Entry<String, Object> entry : node.getOptEntry()) {
			args.add((AST) entry.getValue());
		}
		logger.debug("Build a min func");
		return new ASTMathFunc(args, "min");
	}

	private AST buildMaxFunc(Node node) {
		ASTList args = new ASTList();
		for (Map.Entry<String, Object> entry : node.getOptEntry()) {
			args.add((AST) entry.getValue());
		}
		logger.debug("Build a max func");
		return new ASTMathFunc(args, "max");
	}

	private AST buildPowFunc(Node node) {
		AST x = ASTValue.getAST(1.0);
		AST n = ASTValue.getAST(1);
		for (Map.Entry<String, Object> entry : node.getOptEntry()) {
			switch (entry.getKey()) {
			case "x":
			case "arg0":
				x = (AST) entry.getValue();
				break;
			case "n":
			case "arg1":
				n = (AST) entry.getValue();
				break;
			default:
			}
		}
		ASTList args = new ASTList();
		args.add(x);
		args.add(n);
		logger.debug("Build a pow func");
		return new ASTMathFunc(args, "pow");
	}

	private AST buildSqrtFunc(Node node) {
		AST x = ASTValue.getAST(1.0);
		for (Map.Entry<String, Object> entry : node.getOptEntry()) {
			switch (entry.getKey()) {
			case "x":
			case "arg0":
				x = (AST) entry.getValue();
				break;
			default:
			}
		}
		ASTList args = new ASTList();
		args.add(x);
		logger.debug("Build a sqrt func");
		return new ASTMathFunc(args, "sqrt");
	}

	private AST buildExpFunc(Node node) {
		AST x = ASTValue.getAST(1.0);
		for (Map.Entry<String, Object> entry : node.getOptEntry()) {
			switch (entry.getKey()) {
			case "x":
			case "arg0":
				x = (AST) entry.getValue();
				break;
			default:
			}
		}
		ASTList args = new ASTList();
		args.add(x);
		logger.debug("Build an exp func");
		return new ASTMathFunc(args, "exp");
	}

	private AST buildLogFunc(Node node) {
		AST x = ASTValue.getAST(1.0);
		for (Map.Entry<String, Object> entry : node.getOptEntry()) {
			switch (entry.getKey()) {
			case "x":
			case "arg0":
				x = (AST) entry.getValue();
				break;
			default:
			}
		}
		ASTList args = new ASTList();
		args.add(x);
		logger.debug("Build a log func");
		return new ASTMathFunc(args, "log");
	}

	public void addError(String msg) {
		logger.error(msg);
		errors.add(msg);
	}
}
