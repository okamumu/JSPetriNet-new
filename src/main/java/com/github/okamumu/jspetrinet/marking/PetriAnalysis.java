package com.github.okamumu.jspetrinet.marking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.MarkingError;
import com.github.okamumu.jspetrinet.exception.ObjectNotFoundInASTEnv;
import com.github.okamumu.jspetrinet.graph.Arc;
import com.github.okamumu.jspetrinet.petri.arcs.ArcBase;
import com.github.okamumu.jspetrinet.petri.arcs.InhibitArc;
import com.github.okamumu.jspetrinet.petri.nodes.GenTrans;
import com.github.okamumu.jspetrinet.petri.nodes.Place;
import com.github.okamumu.jspetrinet.petri.nodes.Trans;

public final class PetriAnalysis {
	
	public final static String currentMarkingString = "*currentMarking*";
	private final Logger logger;

    public static class PetriAnalysisInstanceHolder {
    	private static final PetriAnalysis instance = new PetriAnalysis();
    }

	public static PetriAnalysis getInstance() {
		return PetriAnalysisInstanceHolder.instance;
	}
	
	private PetriAnalysis() {
        logger = LoggerFactory.getLogger(PetriAnalysis.class);
	}

	public final Trans.Status isEnable(ASTEnv env, Trans tr) throws ASTException {
		return isEnable((Mark) env.get(currentMarkingString), env, tr);
	}

	public final Trans.Status isEnable(Mark m, ASTEnv env, Trans tr) throws ASTException {
		env.put(currentMarkingString, m);
		if (!tr.guardEval(env)) {
			logger.trace("Trans {} is diabled at {}", tr.getLabel(), m.toString());
			return Trans.Status.DISABLE;
		}
		for (Arc arc : tr.getInArc()) {
			Place place = (Place) arc.getSrc();
			if (arc instanceof InhibitArc) {
				InhibitArc narc = (InhibitArc) arc;
				if (m.get(place.getIndex()) >= narc.getMulti(env)) {
					logger.trace("Trans {} is disabled at {}", tr.getLabel(), m.toString());
					return Trans.Status.DISABLE;
				}				
			} else {
				ArcBase narc = (ArcBase) arc;
				if (m.get(place.getIndex()) < narc.getMulti(env)) {
					logger.trace("Trans {} is diabled at {}", tr.getLabel(), m.toString());
					return Trans.Status.DISABLE;
				}
			}
		}
		logger.trace("Trans {} is enabled at {}", tr.getLabel(), m.toString());
		return Trans.Status.ENABLE;
	}

	public final Trans.Status isEnableGenTrans(ASTEnv env, Trans tr) throws ASTException {
		return isEnableGenTrans((Mark) env.get(currentMarkingString), env, tr);
	}

	public final Trans.Status isEnableGenTrans(Mark m, ASTEnv env, Trans tr) throws ASTException {
		env.put(currentMarkingString, m);
		Boolean maybePreemption = false;
		if (!tr.guardEval(env)) {
			maybePreemption = true;
		}
		for (Arc arc : tr.getInArc()) {
			Place place = (Place) arc.getSrc();
			if (arc instanceof InhibitArc) {
				InhibitArc narc = (InhibitArc) arc;
				if (m.get(place.getIndex()) >= narc.getMulti(env)) {
					maybePreemption = true;
				}
			} else {
				ArcBase narc = (ArcBase) arc;
				if (m.get(place.getIndex()) < narc.getMulti(env)) {
					logger.trace("Trans {} is disabled at {}", tr.getLabel(), m.toString());
					return Trans.Status.DISABLE;
				}
			}
		}
		if (maybePreemption == true) {
			if (((GenTrans) tr).getPolicy() == GenTrans.Policy.PRD) {
				logger.trace("Trans {} is disbled at {}", tr.getLabel(), m.toString());
				return Trans.Status.DISABLE;
			} else {
				logger.trace("Trans {} is preempted at {}", tr.getLabel(), m.toString());
				return Trans.Status.PREEMPTION;
			}
		} else {
			logger.trace("Trans {} is enabled at {}", tr.getLabel(), m.toString());
			return Trans.Status.ENABLE;
		}
	}

	public final Mark doFiring(Mark m, ASTEnv env, Trans tr) throws ASTException, MarkingError {
		env.put(currentMarkingString, m);
		int[] next = m.copy();
		for (Arc arc : tr.getInArc()) {
			if (!(arc instanceof InhibitArc)) {
				Place place = (Place) arc.getSrc();
				ArcBase arcBase = (ArcBase) arc;
				next[place.getIndex()] = next[place.getIndex()] - arcBase.getMulti(env);
				if (next[place.getIndex()] < 0) {
					logger.error("The number of tokens becomes negative. Place {}(index {}), Trans {}, Marking {}", place.getLabel(), place.getIndex(), tr.getLabel(), m.toString());
					throw new MarkingError("Error: #" + place.getLabel() + " becomes negative by firing " + tr.getLabel());
				}
			}
		}
		for (Arc arc : tr.getOutArc()) {
			Place place = (Place) arc.getDest();
			ArcBase arcBase = (ArcBase) arc;
			next[place.getIndex()] = next[place.getIndex()] + arcBase.getMulti(env);
			if (next[place.getIndex()] > place.getMax()) {
				logger.error("The number of tokens exceeds maximum. Place {}(index {}), Trans {}, Marking {}", place.getLabel(), place.getIndex(), tr.getLabel(), m.toString());
				throw new MarkingError("Error: #" + place.getLabel() + " exceeds MAX by firing " + tr.getLabel());
			}
		}
		Mark tmp = new Mark(next);
		logger.trace("Making a mark from {} to {} by firing Trans {}", m.toString(), tmp.toString(), tr.getLabel());
		env.put(currentMarkingString, tmp);
		if (tr.getUpdate() == null) {
			return tmp;
		} else {
			tr.getUpdate().eval(env);
			return (Mark) env.get(currentMarkingString);
		}
	}

	public final Object astEval(AST f, Mark m, ASTEnv env) throws ASTException {
		env.put(currentMarkingString, m);
		return f.eval(env);
	}

	public final Object getNToken(ASTEnv env, Place place) throws ObjectNotFoundInASTEnv {
		return getNToken((Mark) env.get(currentMarkingString), env, place);
	}

	public final Object getNToken(Mark m, ASTEnv env, Place place) {
		return m.get(place.getIndex());
	}

	public final Object updateNToken(ASTEnv env, Place p, Integer res) throws ObjectNotFoundInASTEnv {
		return updateNToken((Mark) env.get(currentMarkingString), env, p, res);
	}

	public final Object updateNToken(Mark m, ASTEnv env, Place p, Integer res) {
		int[] vec = m.copy();
		vec[p.getIndex()] = res;
		Mark tmp = new Mark(vec);
		logger.trace("Updating a mark to {}", tmp.toString());
		env.put(currentMarkingString, tmp);
		return res;
	}
}
