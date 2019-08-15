package com.github.okamumu.jspetrinet.petri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidDefinition;
import com.github.okamumu.jspetrinet.exception.InvalidValue;
import com.github.okamumu.jspetrinet.exception.ObjectNotFoundInASTEnv;
import com.github.okamumu.jspetrinet.exception.UnknownOption;
import com.github.okamumu.jspetrinet.exception.UnknownPolicy;
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
	private int placeid;
	private int immtransid;
	private int exptransid;
	private int gentransid;
	private int rewardid;
	
	private ArrayList<Integer> initMark;
	
	private Logger logger;

	/**
	 * A inner class to hold an instance of FactoryPN
	 *
	 */
    public static class FactoryPNInstanceHolder {
    	private static final FactoryPN instance = new FactoryPN();
    }

    /**
     * A method to get an instance
     * @return An object of FactoryPN
     */
	public static FactoryPN getInstance() {
		return FactoryPNInstanceHolder.instance;
	}

	private FactoryPN() {
		reset();
        logger = LoggerFactory.getLogger(FactoryPN.class);
	}

	/**
	 * A method to reset the indices for all components.
	 */
	public void reset() {
		placeid = 0;
		immtransid = 0;
		exptransid = 0;
		gentransid = 0;
		rewardid = 0;
		initMark = new ArrayList<Integer>();
	}

	/**
	 * Inner class for a node of Petri net.
	 * This class keeps the definitions of components by hashmap.
	 * Based on the definition in this class, compile an instance of PN
	 *
	 */
	public class Node {
		public final Map<String,Object> options;
		
		/**
		 * Constructor
		 */
		public Node() {
			options = new HashMap<String,Object>();
		}
		
//		/**
//		 * A method to get an object
//		 * @param key A string for a key
//		 * @param value A default value, which is used when the given key does not exist.
//		 * @return An object
//		 */
//		public Object getOrDefault(String key, Object value) {
//			return options.getOrDefault(key, value);
//		}

		/**
		 * Get a size of options
		 * @return A size
		 */
		public int size() {
			return options.size();
		}
		
		/**
		 * Method to get the entry set of options
		 * @return A set of entries
		 */
		public Set<Map.Entry<String,Object>> getOptEntry() {
			return options.entrySet();
		}
		
		/**
		 * A method to set a key and an object
		 * @param key A string
		 * @param value An object
		 */
		public void put(String key, Object value) {
			options.put(key, value);
		}

		/**
		 * Get a string for a type of node
		 * @return A string
		 */
		public String getType() {
			return (String) options.getOrDefault("type", null);
		}

		/**
		 * Setter for a type of node
		 * @param string A name of type
		 */
		public void setType(String string) {
			options.put("type", string);
		}
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
		logger.trace("{} {} {} {}", elem.toString(), elem.getLabel(), elem.getIndex(), elem.getMax());
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
		if (logger.isTraceEnabled())
			logger.trace("{} {} {} {} {} {} {} {}", elem.toString(), elem.getLabel(), elem.getIndex(), elem.getWeight().toString(), elem.getGuard().toString(), elem.getUpdate().toString(), elem.getPriority(), elem.canVanishing());
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
		if (logger.isTraceEnabled())
			logger.trace("{} {} {} {} {} {} {} {}", elem.toString(), elem.getLabel(), elem.getIndex(), elem.getRate().toString(), elem.getGuard().toString(), elem.getUpdate().toString(), elem.getPriority(), elem.canVanishing());
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
		if (logger.isTraceEnabled())
			logger.trace("{} {} {} {} {} {} {} {} {}", elem.toString(), elem.getLabel(), elem.getIndex(), elem.getDist().toString(), elem.getPolicy(), elem.getGuard().toString(), elem.getUpdate().toString(), elem.getPriority(), elem.canVanishing());
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
			if (logger.isTraceEnabled())
				logger.trace("iarc {} -> {} ({})", place.getLabel(), trans.getLabel(), multi.toString());
		} else if (sobj instanceof Trans && dobj instanceof Place) {
			Trans trans = (Trans) sobj;
			Place place = (Place) dobj;
			elem = new OutArc(trans, place, multi);
			if (logger.isTraceEnabled())
				logger.trace("oarc {} -> {} ({})", trans.getLabel(), place.getLabel(), multi.toString());
		} else {
			if (logger.isErrorEnabled())
				logger.error("source {}, dest {}", sobj.toString(), dobj.toString());
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
		if (logger.isTraceEnabled())
			logger.trace("harc {} -> {} ({})", place.getLabel(), trans.getLabel(), multi.toString());
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
		if (logger.isTraceEnabled())
			logger.trace("reward {} ({})", label, f.toString());
		rewardid++;
		return rwd;
	}

	/**
	 * A method to compile Petri net from definitions
	 * @param env An instance of environment. All the instances of components are registered in this environment.
	 * @return An instance of Net
	 * @throws InvalidDefinition Exception when the definition is invalid
	 * @throws ASTException A exception to evaluate AST
	 */
	public Net compilePN(ASTEnv env) throws InvalidDefinition, ASTException {
        ArrayList<Place> place = new ArrayList<Place>();
        ArrayList<ImmTrans> immtrans = new ArrayList<ImmTrans>();
        ArrayList<ExpTrans> exptrans = new ArrayList<ExpTrans>();
        ArrayList<GenTrans> gentrans = new ArrayList<GenTrans>();
        ArrayList<String> rewards = new ArrayList<String>();

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
        
        // create init
        int[] vec = new int [place.size()];
        for (int i=0; i<vec.length; i++) {
        	vec[i] = initMark.get(i);
        }
        
        Collections.sort(immtrans);
        Collections.sort(exptrans);
        Collections.sort(gentrans);
        Collections.sort(rewards);
        return new Net(new Mark(vec), place, immtrans, exptrans, gentrans, rewards);
	}
}
