package com.github.okamumu.jspetrinet.marking.method;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.JSPNException;
import com.github.okamumu.jspetrinet.exception.MarkingError;
import com.github.okamumu.jspetrinet.marking.GenVec;
import com.github.okamumu.jspetrinet.marking.Mark;
import com.github.okamumu.jspetrinet.marking.MarkingGraph;
import com.github.okamumu.jspetrinet.marking.PetriAnalysis;
import com.github.okamumu.jspetrinet.petri.Net;
import com.github.okamumu.jspetrinet.petri.nodes.ExpTrans;
import com.github.okamumu.jspetrinet.petri.nodes.GenTrans;
import com.github.okamumu.jspetrinet.petri.nodes.ImmTrans;
import com.github.okamumu.jspetrinet.petri.nodes.Trans;

/**
 * A class for the method to generate a marking graph
 *
 */
public class DFS implements CreateMarking {
	
	private int imm;
	private int gen;
	private int abs;

	private MarkingGraph markGraph;

	private final Map<Mark,Mark> createdMarks;
	private final Set<Mark> visited;
	private final LinkedList<Mark> novisited;	
	private final Map<GenVec,GenVec> genvecSet;

	private final Logger logger;
	private final PetriAnalysis analysis;

	/**
	 * Constructor
	 */
	public DFS() {
		createdMarks = new HashMap<Mark,Mark>();
		visited = new HashSet<Mark>();
		novisited = new LinkedList<Mark>();
		logger = LoggerFactory.getLogger(DFS.class);
		analysis = PetriAnalysis.getInstance();
		genvecSet = new HashMap<GenVec,GenVec>();
	}
	
	@Override
	public void create(MarkingGraph mg, Mark init, Net net, ASTEnv env) throws JSPNException {
		imm = 0;
		gen = 0;
		abs = 0;
		markGraph = mg;
		createdMarks.put(init, init);
		novisited.push(init);
		createMarking(net, env);
	}
	
	/**
	 * A method to generate a vector of status of transitions.
	 * 0: disabled, 1: enabled, 2: preempted
	 * @param m An instance of current marking
	 * @param net An instance of Petri net. This is required to get a set of gen trans.
	 * @param env An instance of environment
	 * @return A vector of status of transitions
	 * @throws ASTException An error for the evaluation of AST with the current marking
	 */
	private int[] createGenVec(Mark m, Net net, ASTEnv env) throws ASTException {
		int[] vec = new int [net.getGenTransSet().size()];
		for (GenTrans tr : net.getGenTransSet()) {
			switch (analysis.isEnableGenTrans(m, env, tr)) {
			case ENABLE:
				vec[tr.getIndex()] = 1;
				break;
			case PREEMPTION:
				vec[tr.getIndex()] = 2;
				break;
			default:
			}
		}
		return vec;
	}
	
	/**
	 * A method to generate a list of transitions
	 * @param m An instance of current marking
	 * @param net An instance of Petri net. This is required to get a set of imm trans.
	 * @param env An instance of environment
	 * @return A list of enabled IMM transitions
	 * @throws ASTException An error for the evaluation of AST with the current marking
	 */
	private List<Trans> createEnabledIMM(Mark m, Net net, ASTEnv env) throws ASTException {
		List<Trans> enabledIMMList = new ArrayList<Trans>();
		int highestPriority = 0;
		for (ImmTrans tr : net.getImmTransSet()) {
			if (highestPriority > tr.getPriority()) {
				break;
			}
			switch (analysis.isEnable(m, env, tr)) {
			case ENABLE:
				highestPriority = tr.getPriority();
				enabledIMMList.add(tr);
				break;
			default:
			}
		}
		return enabledIMMList;
	}
	
	/**
	 * A method to set a making as an element of marking group with one or more IMM transitions are enabled
	 * @param m An instance of current marking
	 * @param vec A vector of status of gen transitions
	 */
	private void setGenVecToImm(Mark m, int[] vec) {
		GenVec genv = new GenVec(vec, GenVec.Type.IMM);
		if (genvecSet.containsKey(genv)) {
			genv = genvecSet.get(genv);
		} else {
			genvecSet.put(genv, genv);
		}
		markGraph.setGenVec(m, genv);
		if (logger.isTraceEnabled()) {
			logger.trace("Add {} as Imm {}", m.toString(), genv.toString());
			logger.trace("IMM {}, GEN {}, ABS {}", imm, gen, abs);
		}
		imm++;
	}

	/**
	 * A method to set a making as an element of marking group with one or more no IMM transition is enabled
	 * @param m An instance of current marking
	 * @param vec A vector of status of gen transitions
	 */
	private void setGenVecToGen(Mark m, int[] vec) {
		GenVec genv = new GenVec(vec, GenVec.Type.GEN);
		if (genvecSet.containsKey(genv)) {
			genv = genvecSet.get(genv);
		} else {
			genvecSet.put(genv, genv);
		}
		markGraph.setGenVec(m, genv);
		if (logger.isTraceEnabled()) {
			logger.trace("Add {} as Gen {}", m.toString(), genv.toString());
			logger.trace("IMM {}, GEN {}, ABS {}", imm, gen, abs);
		}
		gen++;
	}

	/**
	 * A method to set a making as an element of absorbing marking group
	 * @param m An instance of current marking
	 * @param vec A vector of status of gen transitions
	 */
	private void setGenVecToAbs(Mark m, int[] vec) {
		GenVec genv = new GenVec(vec, GenVec.Type.ABS);
		if (genvecSet.containsKey(genv)) {
			genv = genvecSet.get(genv);
		} else {
			genvecSet.put(genv, genv);
		}
		markGraph.setGenVec(m, genv);
		if (logger.isTraceEnabled()) {
			logger.trace("Add {} as Abs {}", m.toString(), genv.toString());
			logger.trace("IMM {}, GEN {}, ABS {}", imm, gen, abs);
		}
		abs++;
	}

	/**
	 * A method to create next marking from the current marking by a firing of given transition
	 * @param m An instance of current marking
	 * @param env An instance of environment
	 * @param tr An instance of transition to be fired
	 * @return An instance of marking. The generated mark should keep its uniqueness.
	 * @throws ASTException An error for the evaluation of AST with the current marking
	 * @throws MarkingError An error when the generated marking takes negative or exceeds the maximum
	 */
	private Mark createNextMarking(Mark m, ASTEnv env, Trans tr) throws ASTException, MarkingError {
		Mark dest = analysis.doFiring(m, env, tr);
		if (createdMarks.containsKey(dest)) {
			dest = createdMarks.get(dest);
		} else {
			createdMarks.put(dest, dest);
		}
		return dest;		
	}

	/**
	 * A method to add next markings generated by enabled IMM transitions to the novisited list
	 * @param m An instance of current marking
	 * @param enabledIMMList A list of enabled imm transitions
	 * @param env An instance of environment
	 * @throws ASTException An error for the evaluation of AST with the current marking
	 * @throws MarkingError An error when the generated marking takes negative or exceeds the maximum
	 */
	private void visitImmMark(Mark m, List<Trans> enabledIMMList, ASTEnv env) throws ASTException, MarkingError {
		for (Trans tr : enabledIMMList) {
			Mark dest = createNextMarking(m, env, tr);
			novisited.push(dest);
			connectTo(m, dest, tr);
		}
		visited.add(m);
	}
	
	/**
	 * A method to add next markings generated by enabled EXP and GEN transitions to the novisited list
	 * @param m An instance of current marking
	 * @param net An instance of net to get lists of EXP and GEN transitions
	 * @param env An instance of environment
	 * @return A boolean whether the current mark is absorbing state or not
	 * @throws ASTException An error for the evaluation of AST with the current marking
	 * @throws MarkingError An error when the generated marking takes negative or exceeds the maximum
	 */
	private boolean visitGenMark(Mark m, Net net, ASTEnv env) throws ASTException, MarkingError {
		boolean noenabled = true;
		for (GenTrans tr : net.getGenTransSet()) {
			switch (analysis.isEnableGenTrans(m, env, tr)) {
			case ENABLE:
				noenabled = false;
				Mark dest = createNextMarking(m, env, tr);
				novisited.push(dest);
				connectTo(m, dest, tr);
				break;
			default:
			}
		}
		for (ExpTrans tr : net.getExpTransSet()) {
			switch (analysis.isEnable(m, env, tr)) {
			case ENABLE:
				noenabled = false;
				Mark dest = createNextMarking(m, env, tr);
				novisited.push(dest);
				connectTo(m, dest, tr);
				break;
			default:
			}
		}
		visited.add(m);
		return noenabled;
	}

	/**
	 * A method to connect two marks and add the information of transition
	 * @param src A source marking
	 * @param dest A destination marking
	 * @param tr A transition
	 */
	private void connectTo(Mark src, Mark dest, Trans tr) {
		src.new Arc(src, dest, tr);
	}

	private void createMarking(Net net, ASTEnv env) throws JSPNException {
		while (!novisited.isEmpty()) {
			Mark m = novisited.pop();
			if (visited.contains(m)) {
				continue;
			}

			// new visit
			int[] vec = createGenVec(m, net, env);
			if (logger.isTraceEnabled())
				logger.trace("New visit {} (GenVec {})", m.toString(), Arrays.toString(vec));

			List<Trans> enabledIMMList = createEnabledIMM(m, net, env);
			if (enabledIMMList.size() > 0) {
				visitImmMark(m, enabledIMMList, env);
				setGenVecToImm(m, vec);
			} else {
				if (visitGenMark(m, net, env) == false) {
					setGenVecToGen(m, vec);
				} else {
					setGenVecToAbs(m, vec);
				}
			}
		}
	}
}
