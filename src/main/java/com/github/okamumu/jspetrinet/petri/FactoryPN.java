package com.github.okamumu.jspetrinet.petri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.github.okamumu.jspetrinet.ast.AST;
import com.github.okamumu.jspetrinet.ast.ASTEnv;
import com.github.okamumu.jspetrinet.ast.values.ASTValue;
import com.github.okamumu.jspetrinet.exception.ASTException;
import com.github.okamumu.jspetrinet.exception.InvalidDefinition;
import com.github.okamumu.jspetrinet.exception.ObjectNotFoundInASTEnv;
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
		Node() {
			options = new HashMap<String,Object>();
		}
		
		/**
		 * A method to get an object
		 * @param key A string for a key
		 * @param value A default value, which is used when the given key does not exist.
		 * @return An object
		 */
		public Object getOrDefault(String key, Object value) {
			return options.getOrDefault(key, value);
		}
		
		/**
		 * A method to set a key and an object
		 * @param key A string
		 * @param value An object
		 */
		public void put(String key, Object value) {
			options.put(key, value);
		}
	}

	/**
	 * A class for the definition of place
	 *
	 */
	public class DefPlace extends Node {}

	/**
	 * A class for the definition of ImmTrans
	 *
	 */
	public class DefImmTrans extends Node {}

	/**
	 * A class for the definition of ExpTrans
	 *
	 */
	public class DefExpTrans extends Node {}

	/**
	 * A class for the definition of GenTrans
	 *
	 */
	public class DefGenTrans extends Node {}

	/**
	 * A class for the definition of Arc
	 *
	 */
	public class DefArc extends Node {}

	/**
	 * A class for the definition of InhibitArc
	 *
	 */
	public class DefInhibitArc extends Node {}

	/**
	 * A class for the definition of AST
	 *
	 */
	public class DefAST extends Node {}

	/**
	 * A class for the definition of reward
	 *
	 */
	public class DefReward extends Node {}

	/**
	 * A method to create an instance of Place from the definition.
	 * The default values are determined in this method.
	 * @param env An object of environment
	 * @param definition An instance of DefPlace
	 * @return An instance of Place
	 */
	private Place create(ASTEnv env, DefPlace definition) {
		String label = (String) definition.getOrDefault("label", String.valueOf(placeid));
		int max = (Integer) definition.getOrDefault("max", Byte.MAX_VALUE - 1);
		Place elem = new Place(label, placeid, max);
		placeid++;
		logger.trace("{} {} {} {}", elem.toString(), elem.getLabel(), elem.getIndex(), elem.getMax());
		return elem;
	}

	/**
	 * A method to create an instance of ImmTrans from the definition.
	 * The default values are determined in this method.
	 * @param env An object of environment
	 * @param definition An instance of DefImmTrans
	 * @return An instance of ImmTrans
	 */
	private ImmTrans create(ASTEnv env, DefImmTrans definition) {
		String label = (String) definition.getOrDefault("label", String.valueOf(immtransid+exptransid+gentransid));
		AST weight = (AST) definition.getOrDefault("weight", ASTValue.getAST(1.0));
		AST guard = (AST) definition.getOrDefault("guard", ASTValue.getAST(true));
		AST update = (AST) definition.getOrDefault("update", ASTValue.getAST(null));
		int priority = (Integer) definition.getOrDefault("priority", 0);
		boolean vanishable = (Boolean) definition.getOrDefault("vanishable", true);
		ImmTrans elem = new ImmTrans(label, immtransid, weight, guard, update, priority, vanishable);
		immtransid++;
		logger.trace("{} {} {} {} {} {} {} {}", elem.toString(), elem.getLabel(), elem.getIndex(), elem.getWeight().toString(), elem.getGuard().toString(), elem.getUpdate().toString(), elem.getPriority(), elem.canVanishing());
		return elem;
	}

	/**
	 * A method to create an instance of ExpTrans from the definition.
	 * The default values are determined in this method.
	 * @param env An object of environment
	 * @param definition An instance of DefPlace
	 * @return An instance of ExpTrans
	 */
	private ExpTrans create(ASTEnv env, DefExpTrans definition) {
		String label = (String) definition.getOrDefault("label", String.valueOf(immtransid+exptransid+gentransid));
		AST rate = (AST) definition.getOrDefault("rate", ASTValue.getAST(1.0));
		AST guard = (AST) definition.getOrDefault("guard", ASTValue.getAST(true));
		AST update = (AST) definition.getOrDefault("update", ASTValue.getAST(null));
		int priority = (Integer) definition.getOrDefault("priority", 0);
		boolean vanishable = (Boolean) definition.getOrDefault("vanishable", false);
		ExpTrans elem = new ExpTrans(label, exptransid, rate, guard, update, priority, vanishable);
		exptransid++;
		logger.trace("{} {} {} {} {} {} {} {}", elem.toString(), elem.getLabel(), elem.getIndex(), elem.getRate().toString(), elem.getGuard().toString(), elem.getUpdate().toString(), elem.getPriority(), elem.canVanishing());
		return elem;
	}

	/**
	 * A method to create an instance of GenTrans from the definition.
	 * The default values are determined in this method.
	 * @param env An object of environment
	 * @param definition An instance of DefGenTrans
	 * @return An instance of GenTrans
	 */
	private GenTrans create(ASTEnv env, DefGenTrans definition) {
		String label = (String) definition.getOrDefault("label", String.valueOf(immtransid+exptransid+gentransid));
		AST dist = (AST) definition.getOrDefault("dist", ASTValue.getAST(null));
		GenTrans.Policy policy = (GenTrans.Policy) definition.getOrDefault("policy", GenTrans.Policy.PRD);
		AST guard = (AST) definition.getOrDefault("guard", ASTValue.getAST(true));
		AST update = (AST) definition.getOrDefault("update", ASTValue.getAST(null));
		int priority = (Integer) definition.getOrDefault("priority", 0);
		boolean vanishable = (Boolean) definition.getOrDefault("vanishable", false);
		GenTrans elem = new GenTrans(label, gentransid, dist, policy, guard, update, priority, vanishable);
		gentransid++;
		logger.trace("{} {} {} {} {} {} {} {} {}", elem.toString(), elem.getLabel(), elem.getIndex(), elem.getDist().toString(), elem.getPolicy(), elem.getGuard().toString(), elem.getUpdate().toString(), elem.getPriority(), elem.canVanishing());
		return elem;
	}

	/**
	 * A method to create an instance of Arc from the definition.
	 * The default values are determined in this method.
	 * This method automatically decides the corresponding arc is inbound or outbound.
	 * @param env An object of environment. env should include instances of Place and Trans to be connected.
	 * @param definition An instance of DefArc
	 * @return An instance of Arc
	 */
	private Object create(ASTEnv env, DefArc definition) throws ObjectNotFoundInASTEnv, InvalidDefinition {
		String src = (String) definition.getOrDefault("src", null);
		String dest = (String) definition.getOrDefault("dest", null);
		AST multi = (AST) definition.getOrDefault("multi", ASTValue.getAST(1));
		Object sobj = env.get(src);
		Object dobj = env.get(dest);
		Object elem;
		if (sobj instanceof Place && dobj instanceof Trans) {
			Place place = (Place) sobj;
			Trans trans = (Trans) dobj;			
			elem = new InArc(place, trans, multi);
			logger.trace("iarc {} -> {} ({})", place.getLabel(), trans.getLabel(), multi.toString());
		} else if (sobj instanceof Trans && dobj instanceof Place) {
			Trans trans = (Trans) sobj;
			Place place = (Place) dobj;
			elem = new OutArc(trans, place, multi);
			logger.trace("oarc {} -> {} ({})", trans.getLabel(), place.getLabel(), multi.toString());
		} else {
			logger.error("source {}, dest {}", sobj.toString(), dobj.toString());
			throw new InvalidDefinition("The arc should connect between place and trans.");
		}
		return elem;
	}

	/**
	 * A method to create an instance of InhibitArc from the definition.
	 * The default values are determined in this method.
	 * @param env An object of environment. env should include instances of Place and Trans to be connected.
	 * @param definition An instance of DefInhibitArc
	 * @return An instance of InhibitArc
	 */
	private InhibitArc create(ASTEnv env, DefInhibitArc definition) throws ObjectNotFoundInASTEnv {
		String src = (String) definition.getOrDefault("src", null);
		String dest = (String) definition.getOrDefault("dest", null);
		Place place = (Place) env.get(src);
		Trans trans = (Trans) env.get(dest);
		AST multi = (AST) definition.getOrDefault("multi", ASTValue.getAST(1));
		InhibitArc elem = new InhibitArc(place, trans, multi);
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
	 * @param definition An instance of reward
	 * @return An instance of AST as reward
	 * @throws ASTException 
	 */
	private Reward create(ASTEnv env, DefReward definition) throws ASTException {
		String label = (String) definition.getOrDefault("label", String.valueOf(rewardid));
		AST f = (AST) definition.getOrDefault("formula", ASTValue.getAST(1));
		Reward rwd = new Reward(label, ASTValue.getAST(f.eval(env)));
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
        for(String label : keys) {
        	Object obj = env.get(label);
        	if (obj instanceof DefPlace) {
        		Place node = FactoryPN.getInstance().create(env, (DefPlace) obj);
        		place.add(node);
        		env.put(node.getLabel(), node); // replace definition by an instance
        	} else if (obj instanceof DefImmTrans) {
        		ImmTrans node = FactoryPN.getInstance().create(env, (DefImmTrans) obj);
        		immtrans.add(node);
        		env.put(node.getLabel(), node); // replace definition by an instance
        	} else if (obj instanceof DefExpTrans) {
        		ExpTrans node = FactoryPN.getInstance().create(env, (DefExpTrans) obj);
        		exptrans.add(node);
        		env.put(node.getLabel(), node); // replace definition by an instance
        	} else if (obj instanceof DefGenTrans) {
        		GenTrans node = FactoryPN.getInstance().create(env, (DefGenTrans) obj);
        		gentrans.add(node);
        		env.put(node.getLabel(), node); // replace definition by an instance
        	} else if (obj instanceof DefReward) {
        		Reward rwd = FactoryPN.getInstance().create(env, (DefReward) obj);
        		rewards.add(rwd.label);
        		env.put(rwd.label, rwd.f); // replace definition of reward by AST
        	}
		}
        
        // create arcs
        for(String label : keys) {
        	Object obj = env.get(label);
        	if (obj instanceof DefArc) {
        		FactoryPN.getInstance().create(env, (DefArc) obj);
        	} else if (obj instanceof DefInhibitArc) {
        		FactoryPN.getInstance().create(env, (DefInhibitArc) obj);
        	}
		}
        
        Collections.sort(immtrans);
        Collections.sort(exptrans);
        Collections.sort(gentrans);
        Collections.sort(rewards);
        return new Net(place, immtrans, exptrans, gentrans, rewards);
	}
}
