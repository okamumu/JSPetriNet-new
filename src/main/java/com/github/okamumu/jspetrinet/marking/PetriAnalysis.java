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
	
	public final static String currentMarkingString = "__CurrentMarking__";
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
			logger.debug("Trans {} is diabled at {}", tr, m);
			return Trans.Status.DISABLE;
		}
		for (Arc arc : tr.getInArc()) {
			Place place = (Place) arc.getSrc();
			if (arc instanceof InhibitArc) {
				InhibitArc narc = (InhibitArc) arc;
				if (m.get(place.getIndex()) >= narc.getMulti(env)) {
					logger.debug("Trans {} is disabled at {}", tr, m);
					return Trans.Status.DISABLE;
				}
			} else {
				ArcBase narc = (ArcBase) arc;
				if (m.get(place.getIndex()) < narc.getMulti(env)) {
					logger.debug("Trans {} is diabled at {}", tr, m);
					return Trans.Status.DISABLE;
				}
			}
		}
		logger.debug("Trans {} is enabled at {}", tr, m);
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
					logger.debug("Trans {} is disabled at {}", tr, m);
					return Trans.Status.DISABLE;
				}
			}
		}
		if (maybePreemption == true) {
			if (((GenTrans) tr).getPolicy() == GenTrans.Policy.PRD) {
				logger.debug("Trans {} is disbled at {}", tr, m);
				return Trans.Status.DISABLE;
			} else {
				logger.debug("Trans {} is preempted at {}", tr, m);
				return Trans.Status.PREEMPTION;
			}
		} else {
			logger.debug("Trans {} is enabled at {}", tr, m);
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
					logger.error("The number of tokens becomes negative. Place {}, Trans {}, Marking {}", place, tr, m);
					throw new MarkingError("Error: #" + place.getLabel() + " becomes negative by firing " + tr);
				}
			}
		}
		for (Arc arc : tr.getOutArc()) {
			Place place = (Place) arc.getDest();
			ArcBase arcBase = (ArcBase) arc;
			next[place.getIndex()] = next[place.getIndex()] + arcBase.getMulti(env);
			if (next[place.getIndex()] > place.getMax()) {
				logger.error("The number of tokens exceeds maximum. Place {}, Trans {}, Marking {}", place, tr, m);
				throw new MarkingError("Error: #" + place.getLabel() + " exceeds MAX by firing " + tr);
			}
		}
		Mark tmp = new Mark(next);
		logger.debug("Making a mark from {} to {} by firing Trans {}", m, tmp, tr);
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
		logger.debug("Updating a mark to {}", tmp);
		env.put(currentMarkingString, tmp);
		return res;
	}
}
