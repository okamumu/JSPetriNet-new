package com.github.okamumu.jspetrinet.parser;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.github.okamumu.jspetrinet.petri.NetBuilder;

public class JSPetriNetParser implements JSPNLListener {
	
	private final JSPNLLexer lexer;
	private final JSPNLParser parser;
	private final CharStream is;
	private final NetBuilder astbuilder;
	
	public JSPetriNetParser(NetBuilder astbuilder, InputStream in) throws IOException {
		this.astbuilder = astbuilder;
		is = CharStreams.fromStream(in);
		lexer = new JSPNLLexer(is);
		parser = new JSPNLParser(new CommonTokenStream(lexer));
		parser.addParseListener(this);
	}
	
	public JSPetriNetParser(NetBuilder astbuilder, String text) {
		this.astbuilder = astbuilder;
		is = CharStreams.fromString(text);
		lexer = new JSPNLLexer(is);
		parser = new JSPNLParser(new CommonTokenStream(lexer));
		parser.addErrorListener(new BaseErrorListener() {
			@Override
			public void syntaxError(Recognizer<?,?> recognizer, Object offendingSymbol, int line,
					int charPositionInLine, String msg, RecognitionException e) {
				String err = String.format("Failed to parse at line %d position %d due to %s", line, charPositionInLine, msg);
				astbuilder.addError(err);
//				throw new IllegalStateException(String.format(
//						"Failed to parse at line %d position %d due to %s", line, charPositionInLine, msg), e);				
			}
		});
		parser.addParseListener(this);
	}

	public void parseProg() {
		parser.prog();		
	}

	public void parseExpression() {
		parser.expression();
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterProg(JSPNLParser.ProgContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitProg(JSPNLParser.ProgContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterStatement(JSPNLParser.StatementContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitStatement(JSPNLParser.StatementContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDeclaration(JSPNLParser.DeclarationContext ctx) {
		astbuilder.createNewNode();		
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDeclaration(JSPNLParser.DeclarationContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNode_declaration(JSPNLParser.Node_declarationContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNode_declaration(JSPNLParser.Node_declarationContext ctx) {
		astbuilder.buildNode(ctx.node.getText(), ctx.id.getText());
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterArc_declaration(JSPNLParser.Arc_declarationContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitArc_declaration(JSPNLParser.Arc_declarationContext ctx) {
		astbuilder.buildArc(ctx.type.getText(), ctx.srcName.getText(), ctx.destName.getText());
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterReward_declaration(JSPNLParser.Reward_declarationContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitReward_declaration(JSPNLParser.Reward_declarationContext ctx) {
		astbuilder.buildReward(ctx.id.getText());
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNode_options(JSPNLParser.Node_optionsContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNode_options(JSPNLParser.Node_optionsContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterOption_list(JSPNLParser.Option_listContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitOption_list(JSPNLParser.Option_listContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterOption_value(JSPNLParser.Option_valueContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitOption_value(JSPNLParser.Option_valueContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterLabel_expression(JSPNLParser.Label_expressionContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitLabel_expression(JSPNLParser.Label_expressionContext ctx) {
		astbuilder.setNodeOption(ctx.id.getText());
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterUpdate_block(JSPNLParser.Update_blockContext ctx) {
		astbuilder.setUpdateBlockEnd();
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitUpdate_block(JSPNLParser.Update_blockContext ctx) {
		astbuilder.buildUpdateBlock();
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSimple_block(JSPNLParser.Simple_blockContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSimple_block(JSPNLParser.Simple_blockContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSimple(JSPNLParser.SimpleContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSimple(JSPNLParser.SimpleContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterAssign_expression(JSPNLParser.Assign_expressionContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitAssign_expression(JSPNLParser.Assign_expressionContext ctx) {
		switch (ctx.type) {
		case 1:
			astbuilder.buildAssignExpression(ctx.id.getText());
			break;
		case 2:
			astbuilder.buildAssignNTokenExpression();
			break;
		default:
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterExpression(JSPNLParser.ExpressionContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitExpression(JSPNLParser.ExpressionContext ctx) {
		switch (ctx.type) {
		case 1:
			astbuilder.buildUnaryExpression(ctx.op.getText());
			break;
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
			astbuilder.buildBinaryExpression(ctx.op.getText());
			break;
		case 8:
			astbuilder.buildIfThenElseExpression(ctx.op.getText());
			break;
		case 12:
			astbuilder.buildValueExpression(ctx.id.getText());
			break;
//		case 9:
//		case 10:
//		case 11:
//		case 13:
//		case 14:
//			// nop
//			break;
		default:
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterFunction_expression(JSPNLParser.Function_expressionContext ctx) {
		astbuilder.createNewNode();
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitFunction_expression(JSPNLParser.Function_expressionContext ctx) {
		astbuilder.buildFunc(ctx.id.getText());
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterFunction_args(JSPNLParser.Function_argsContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitFunction_args(JSPNLParser.Function_argsContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterArgs_list(JSPNLParser.Args_listContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitArgs_list(JSPNLParser.Args_listContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterArgs_value(JSPNLParser.Args_valueContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitArgs_value(JSPNLParser.Args_valueContext ctx) {
		astbuilder.setNodeOption();
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNtoken_expression(JSPNLParser.Ntoken_expressionContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNtoken_expression(JSPNLParser.Ntoken_expressionContext ctx) {
		astbuilder.buildNToken(ctx.id.getText());
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterEnable_expression(JSPNLParser.Enable_expressionContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitEnable_expression(JSPNLParser.Enable_expressionContext ctx) {
		astbuilder.buildEnable(ctx.id.getText());		
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterLiteral_expression(JSPNLParser.Literal_expressionContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitLiteral_expression(JSPNLParser.Literal_expressionContext ctx) {
		switch (ctx.type) {
		case 1: // integer
			astbuilder.buildIntegerLiteral(ctx.val.getText());
			break;
		case 2: // float
			astbuilder.buildDoubleLiteral(ctx.val.getText());
			break;
		case 3: // logical
			astbuilder.buildBooleanLiteral(ctx.val.getText());
			break;
		case 4: // string
			astbuilder.buildStringLiteral(ctx.val.getText());
			break;
		default:
		}		
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterEveryRule(ParserRuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitEveryRule(ParserRuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void visitTerminal(TerminalNode node) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void visitErrorNode(ErrorNode node) {
//		astbuilder.addErrorNode(node);
	}

}
