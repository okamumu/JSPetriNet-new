package com.github.okamumu.jspetrinet.petri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidDefinition;
import com.github.okamumu.jspetrinet.exception.InvalidValue;
import com.github.okamumu.jspetrinet.exception.ObjectNotFoundInASTEnv;
import com.github.okamumu.jspetrinet.marking.Mark;
import com.github.okamumu.jspetrinet.petri.arcs.InArc;
import com.github.okamumu.jspetrinet.petri.arcs.InhibitArc;
import com.github.okamumu.jspetrinet.petri.arcs.OutArc;
import com.github.okamumu.jspetrinet.petri.nodes.ExpTrans;
import com.github.okamumu.jspetrinet.petri.nodes.GenTrans;
import com.github.okamumu.jspetrinet.petri.nodes.ImmTrans;
import com.github.okamumu.jspetrinet.petri.nodes.Place;
import com.github.okamumu.jspetrinet.petri.nodes.Trans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class to compile PN.
 * This is implemented as a singleton.
 *
 */
public class FactoryPN {
	
	/**
	 * A method to compile Petri net from definitions
	 * @param env An instance of environment. All the instances of components are registered in this environment.
	 * @return An instance of Net
	 * @throws InvalidDefinition Exception when the definition is invalid
	 * @throws ASTException A exception to evaluate AST
	 */
	public static Net compile(ASTEnv env) throws InvalidDefinition, ASTException {
		FactoryPN factory = new FactoryPN();
		factory.compilePN(env);
		int[] vec = factory.createInitMark();
		return new Net(new Mark(vec), factory.place,
				factory.immtrans, factory.exptrans, factory.gentrans, factory.rewards);
	}

	/**
	 * A method to compile Petri net from definitions
	 * @param imark A map to represent the initial marking
	 * @param env An instance of environment. All the instances of components are registered in this environment.
	 * @return An instance of Net
	 * @throws InvalidDefinition Exception when the definition is invalid
	 * @throws ASTException A exception to evaluate AST
	 */
	public static Net compile(Map<String,Integer> imark, ASTEnv env) throws InvalidDefinition, ASTException {
		FactoryPN factory = new FactoryPN();
		factory.compilePN(env);
		int[] vec = factory.createInitMark(env, imark);
		return new Net(new Mark(vec), factory.place,
				factory.immtrans, factory.exptrans, factory.gentrans, factory.rewards);
	}

	private int placeid;
	private int immtransid;
	private int exptransid;
	private int gentransid;
	private int rewardid;
	
	private final ArrayList<Integer> initMark;
	
    private final ArrayList<Place> place;
    private final ArrayList<ImmTrans> immtrans;
    private final ArrayList<ExpTrans> exptrans;
    private final ArrayList<GenTrans> gentrans;
    private final ArrayList<String> rewards;

    private final Logger logger;

//	/**
//	 * A inner class to hold an instance of FactoryPN
//	 *
//	 */
//    public static class FactoryPNInstanceHolder {
//    	private static final FactoryPN instance = new FactoryPN();
//    }
//
//    /**
//     * A method to get an instance
//     * @return An object of FactoryPN
//     */
//	public static FactoryPN getInstance() {
//		return FactoryPNInstanceHolder.instance;
//	}

	private FactoryPN() {
        logger = LoggerFactory.getLogger(FactoryPN.class);
		placeid = 0;
		immtransid = 0;
		exptransid = 0;
		gentransid = 0;
		rewardid = 0;
		initMark = new ArrayList<Integer>();
        place = new ArrayList<Place>();
        immtrans = new ArrayList<ImmTrans>();
        exptrans = new ArrayList<ExpTrans>();
        gentrans = new ArrayList<GenTrans>();
        rewards = new ArrayList<String>();
	}

	private Integer evalInteger(Object f, ASTEnv env) throws ASTException {
		Object obj = ((AST) f).eval(env);
		if (obj instanceof Integer) {
			return (Integer) obj;
		} else if (obj instanceof Double) {
			return ((Double) obj).intValue();
		} else {
			throw new InvalidValue("Object " + f + " cannot convert to Integer");
		}
	}

//	private Double evalDouble(Object f, ASTEnv env) throws ASTException {
//		Object obj = ((AST) f).eval(env);
//		if (obj instanceof Integer) {
//			return ((Integer) obj).doubleValue();
//		} else if (obj instanceof Double) {
//			return (Double) obj;
//		} else {
//			throw new InvalidValue("Object " + f + " cannot convert to Double");
//		}
//	}

	private Boolean evalBoolean(Object f, ASTEnv env) throws ASTException {
		Object obj = ((AST) f).eval(env);
		if (obj instanceof Boolean) {
			return (Boolean) obj;
		} else {
			throw new InvalidValue("Object " + f + " cannot convert to Boolean");
		}
	}

	private String evalString(Object f, ASTEnv env) throws ASTException {
		Object obj = ((AST) f).eval(env);
		if (obj instanceof String) {
			return (String) obj;
		} else {
			throw new InvalidValue("Object " + f + " cannot convert to String");
		}
	}

	/**
	 * A method to create an instance of Place from the definition.
	 * The default values are determined in this method.
	 * @param env An object of environment
	 * @param definition A definition of place
	 * @return An instance of Place
	 * @throws ASTException 
	 * @throws InvalidDefinition 
	 */
	private Place createPlace(ASTEnv env, Node definition) throws ASTException, InvalidDefinition {
		// default values
		String label = String.valueOf(placeid);
		int init = 0;
		int max = Byte.MAX_VALUE - 1;
		for (Map.Entry<String,Object> entry : definition.getOptEntry()) {
			switch (entry.getKey()) {
			case "label":
				label = (String) entry.getValue();
				break;
			case "init":
				init = evalInteger(entry.getValue(), env);
				break;
			case "max":
				max = evalInteger(entry.getValue(), env);
				break;
			case "type":
				break;
			default:
				throw new InvalidDefinition("Option " + entry.getKey() + " is unknown in Place:" + label);
			}
		}
		Place elem = new Place(label, placeid, max);
		initMark.add(init);
		placeid++;
		logger.debug("{} {} {} {}", elem, elem.getLabel(), elem.getIndex(), elem.getMax());
		return elem;
	}

	/**
	 * A method to create an instance of ImmTrans from the definition.
	 * The default values are determined in this method.
	 * @param env An object of environment
	 * @param definition A definition of immtrans
	 * @return An instance of ImmTrans
	 * @throws ASTException 
	 * @throws InvalidDefinition 
	 */
	private ImmTrans createImmTrans(ASTEnv env, Node definition) throws ASTException, InvalidDefinition {
		// default values
		String label = String.valueOf(immtransid+exptransid+gentransid);
		AST weight = ASTValue.getAST(1.0);
		AST guard = ASTValue.getAST(true);
		AST update = ASTValue.getAST(null);
		int priority = 0;
		boolean vanishable = true;
		for (Map.Entry<String,Object> entry : definition.getOptEntry()) {
			switch (entry.getKey()) {
			case "label":
				label = (String) entry.getValue();
				break;
			case "weight":
				weight = (AST) entry.getValue();
				break;
			case "guard":
				guard = (AST) entry.getValue();
				break;
			case "update":
				update = (AST) entry.getValue();
				break;
			case "priority":
				priority = evalInteger(entry.getValue(), env);
				break;
			case "vanishable":
				vanishable = evalBoolean(entry.getValue(), env);
				break;
			case "type":
				break;
			default:
				throw new InvalidDefinition("Option " + entry.getKey() + " is unknown in IMM:" + label);
			}
		}
		ImmTrans elem = new ImmTrans(label, immtransid, weight, guard, update, priority, vanishable);
		immtransid++;
		logger.debug("{} {} {} {} {} {} {} {}", elem, elem.getLabel(), elem.getIndex(), elem.getWeight(), elem.getGuard(), elem.getUpdate(), elem.getPriority(), elem.canVanishing());
		return elem;
	}

	/**
	 * A method to create an instance of ExpTrans from the definition.
	 * The default values are determined in this method.
	 * @param env An object of environment
	 * @param definition A definition of exptrans
	 * @return An instance of ExpTrans
	 * @throws ASTException 
	 * @throws InvalidDefinition 
	 */
	private ExpTrans createExpTrans(ASTEnv env, Node definition) throws ASTException, InvalidDefinition {
		String label = String.valueOf(immtransid+exptransid+gentransid);
		AST rate = ASTValue.getAST(1.0);
		AST guard = ASTValue.getAST(true);
		AST update = ASTValue.getAST(null);
		int priority = 0;
		boolean vanishable = true;
		for (Map.Entry<String,Object> entry : definition.getOptEntry()) {
			switch (entry.getKey()) {
			case "label":
				label = (String) entry.getValue();
				break;
			case "rate":
				rate = (AST) entry.getValue();
				break;
			case "guard":
				guard = (AST) entry.getValue();
				break;
			case "update":
				update = (AST) entry.getValue();
				break;
			case "priority":
				priority = evalInteger(entry.getValue(), env);
				break;
			case "vanishable":
				vanishable = evalBoolean(entry.getValue(), env);
				break;
			case "type":
				break;
			default:
				throw new InvalidDefinition("Option " + entry.getKey() + " is unknown in EXP:" + label);
			}
		}
		ExpTrans elem = new ExpTrans(label, exptransid, rate, guard, update, priority, vanishable);
		exptransid++;
		logger.debug("{} {} {} {} {} {} {} {}", elem, elem.getLabel(), elem.getIndex(), elem.getRate(), elem.getGuard(), elem.getUpdate(), elem.getPriority(), elem.canVanishing());
		return elem;
	}

	/**
	 * A method to create an instance of GenTrans from the definition.
	 * The default values are determined in this method.
	 * @param env An object of environment
	 * @param definition A definition of gentrans
	 * @return An instance of GenTrans
	 * @throws ASTException 
	 * @throws InvalidDefinition 
	 */
	private GenTrans createGenTrans(ASTEnv env, Node definition) throws ASTException, InvalidDefinition {
		String label = String.valueOf(immtransid+exptransid+gentransid);
		AST dist = ASTValue.getAST(null);
		String policy = "prd";
		AST guard = ASTValue.getAST(true);
		AST update = ASTValue.getAST(null);
		int priority = 0;
		boolean vanishable = true;
		for (Map.Entry<String,Object> entry : definition.getOptEntry()) {
			switch (entry.getKey()) {
			case "label":
				label = (String) entry.getValue();
				break;
			case "dist":
				dist = (AST) entry.getValue();
				break;
			case "guard":
				guard = (AST) entry.getValue();
				break;
			case "update":
				update = (AST) entry.getValue();
				break;
			case "policy":
				policy = evalString(entry.getValue(), env);
				break;
			case "priority":
				priority = evalInteger(entry.getValue(), env);
				break;
			case "vanishable":
				vanishable = evalBoolean(entry.getValue(), env);
				break;
			case "type":
				break;
			default:
				throw new InvalidDefinition("Option " + entry.getKey() + " is unknown in GEN:" + label);
			}
		}
		GenTrans.Policy pol;
		switch (policy) {
		case "prd":
			pol = GenTrans.Policy.PRD;
			break;
		case "prs":
			pol = GenTrans.Policy.PRS;
			break;
		case "pri":
			pol = GenTrans.Policy.PRI;
			break;
		default:
			throw new InvalidDefinition("Policy " + policy + " is unknown in GEN:" + label);
		}
		GenTrans elem = new GenTrans(label, gentransid, dist, pol, guard, update, priority, vanishable);
		gentransid++;
		logger.debug("{} {} {} {} {} {} {} {} {}", elem, elem.getLabel(), elem.getIndex(), elem.getDist(), elem.getPolicy(), elem.getGuard(), elem.getUpdate(), elem.getPriority(), elem.canVanishing());
		return elem;
	}

	/**
	 * A method to create an instance of Arc from the definition.
	 * The default values are determined in this method.
	 * This method automatically decides the corresponding arc is inbound or outbound.
	 * @param env An object of environment. env should include instances of Place and Trans to be connected.
	 * @param definition A definition of arc
	 * @return An instance of Arc
	 * @throws InvalidDefinition 
	 * @throws ObjectNotFoundInASTEnv 
	 */
	private Object createArc(ASTEnv env, Node definition) throws InvalidDefinition, ObjectNotFoundInASTEnv {
		// default values
		String src = null;
		String dest = null;
		AST multi = ASTValue.getAST(1);
		for (Map.Entry<String,Object> entry : definition.getOptEntry()) {
			switch (entry.getKey()) {
			case "src":
				src = (String) entry.getValue();
				break;
			case "dest":
				dest = (String) entry.getValue();
				break;
			case "multi":
				multi = (AST) entry.getValue();
				break;
			case "type":
				break;
			default:
				throw new InvalidDefinition("Option " + entry.getKey() + " is unknown in Arc:" + src + "->" + dest);
			}
		}
		Object sobj = env.get(src);
		Object dobj = env.get(dest);
		Object elem;
		if (sobj instanceof Place && dobj instanceof Trans) {
			Place place = (Place) sobj;
			Trans trans = (Trans) dobj;			
			elem = new InArc(place, trans, multi);
			logger.debug("iarc {} -> {} ({})", place.getLabel(), trans.getLabel(), multi);
		} else if (sobj instanceof Trans && dobj instanceof Place) {
			Trans trans = (Trans) sobj;
			Place place = (Place) dobj;
			elem = new OutArc(trans, place, multi);
			logger.debug("oarc {} -> {} ({})", trans.getLabel(), place.getLabel(), multi);
		} else {
			logger.error("source {}, dest {}", sobj, dobj);
			throw new InvalidDefinition("The arc should connect between place and trans.");
		}
		return elem;
	}

	/**
	 * A method to create an instance of InhibitArc from the definition.
	 * The default values are determined in this method.
	 * @param env An object of environment. env should include instances of Place and Trans to be connected.
	 * @param definition A definition of inhibited arc
	 * @return An instance of InhibitArc
	 * @throws InvalidDefinition 
	 */
	private InhibitArc createHArc(ASTEnv env, Node definition) throws ObjectNotFoundInASTEnv, InvalidDefinition {
		// default values
		String src = null;
		String dest = null;
		AST multi = ASTValue.getAST(1);
		for (Map.Entry<String,Object> entry : definition.getOptEntry()) {
			switch (entry.getKey()) {
			case "src":
				src = (String) entry.getValue();
				break;
			case "dest":
				dest = (String) entry.getValue();
				break;
			case "multi":
				multi = (AST) entry.getValue();
				break;
			case "type":
				break;
			default:
				throw new InvalidDefinition("Option " + entry.getKey() + " is unknown in HArc:" + src + "->" + dest);
			}
		}
		Place place = (Place) env.get(src);
		Trans trans = (Trans) env.get(dest);
		InhibitArc elem = new InhibitArc(place, trans, multi);
		logger.debug("harc {} -> {} ({})", place.getLabel(), trans.getLabel(), multi);
		return elem;
	}

	/**
	 * A class for reward
	 *
	 */
	class Reward {
		String label;
		AST f;
		
		Reward(String label, AST f) {
			this.label = label;
			this.f = f;
		}
	}

	/**
	 * A method to create an instance of AST as reward from the definition.
	 * The default values are determined in this method.
	 * @param env An object of environment.
	 * @param definition A definition of reward
	 * @return An instance of AST as reward
	 * @throws ASTException 
	 * @throws InvalidDefinition 
	 */
	private Reward createReward(ASTEnv env, Node definition) throws ASTException, InvalidDefinition {
		String label = String.valueOf(rewardid);
		AST f = ASTValue.getAST(1);
		for (Map.Entry<String,Object> entry : definition.getOptEntry()) {
			switch (entry.getKey()) {
			case "label":
				label = (String) entry.getValue();
				break;
			case "formula":
				f = (AST) entry.getValue();
				break;
			case "type":
				break;
			default:
				throw new InvalidDefinition("Option " + entry.getKey() + " is unknown in Reward:" + label);
			}
		}		
		Reward rwd = new Reward(label, ASTValue.getAST(f.eval(env)));
		logger.debug("reward {} ({})", label, f);
		rewardid++;
		return rwd;
	}

	/**
	 * A method to create instances of places and transitions from definitions.
	 * @param env An instance of environment. All the instances of components are registered in this environment.
	 * @throws InvalidDefinition Exception when the definition is invalid
	 * @throws ASTException A exception to evaluate AST
	 */
	private void compilePN(ASTEnv env) throws InvalidDefinition, ASTException {
        ArrayList<String> keys = new ArrayList<String>(env.keySet());
        Collections.sort(keys);

        // TODO: Compile AST to make it faster
//        // compile ast
//        for(String label : keys) {
//        	Object obj = env.get(label);
//		}

        // create nodes and AST
        // priority: place, trans, reward
        for(String label : keys) {
        	Object obj = env.get(label);
        	if (obj instanceof Node) {
        		Node defnode = (Node) obj;
        		String type = defnode.getType();
        		switch (type) {
        		case "place": {
            		Place node = createPlace(env, defnode);
            		place.add(node);
            		env.put(node.getLabel(), node); // replace definition by an instance
        			break;
        		}
        		default:
        		}
        	}
		}
        
        for(String label : keys) {
        	Object obj = env.get(label);
        	if (obj instanceof Node) {
        		Node defnode = (Node) obj;
        		String type = defnode.getType();
        		switch (type) {
        		case "imm": {
            		ImmTrans node = createImmTrans(env, defnode);
            		immtrans.add(node);
            		env.put(node.getLabel(), node); // replace definition by an instance
        			break;
        		}
        		case "exp": {
            		ExpTrans node = createExpTrans(env, defnode);
            		exptrans.add(node);
            		env.put(node.getLabel(), node); // replace definition by an instance
        			break;
        		}
        		case "gen": {
            		GenTrans node = createGenTrans(env, defnode);
            		gentrans.add(node);
            		env.put(node.getLabel(), node); // replace definition by an instance
        			break;
        		}
        		default:
        		}
        	}
		}

        for(String label : keys) {
        	Object obj = env.get(label);
        	if (obj instanceof Node) {
        		Node defnode = (Node) obj;
        		String type = defnode.getType();
        		switch (type) {
        		case "reward": {
            		Reward rwd = createReward(env, defnode);
            		rewards.add(rwd.label);
            		env.put(rwd.label, rwd.f); // replace definition of reward by AST
        			break;
        		}
        		default:
        		}
        	}
		}

        // create arcs
        for(String label : keys) {
        	Object obj = env.get(label);
        	if (obj instanceof Node) {
        		Node defnode = (Node) obj;
        		String type = defnode.getType();
        		switch (type) {
        		case "arc":
            		createArc(env, (Node) obj);
        			break;
        		case "harc":
            		createHArc(env, (Node) obj);
        			break;
        		default:
        		}
        	}
		}

        // sort
		Collections.sort(immtrans);
		Collections.sort(exptrans);
		Collections.sort(gentrans);
		Collections.sort(rewards);
	}
	
	private int[] createInitMark() {
        int[] vec = new int [place.size()];
        for (int i=0; i<vec.length; i++) {
        	vec[i] = initMark.get(i);
        }
        return vec;		
	}

	private int[] createInitMark(ASTEnv env, Map<String,Integer> imark) throws ObjectNotFoundInASTEnv {
        int[] vec = new int [place.size()];
        for (Map.Entry<String,Integer> entry : imark.entrySet()) {
        	Place p = (Place) env.get(entry.getKey());
        	vec[p.getIndex()] = entry.getValue();
        }
        return vec;
	}
}
