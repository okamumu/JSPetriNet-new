package com.github.okamumu.jspetrinet.petri;

import java.util.List;

import com.github.okamumu.jspetrinet.marking.Mark;
import com.github.okamumu.jspetrinet.petri.nodes.*;

/**
 * A class for Petri net
 * Contents: Places, ImmTrans, ExpTrans and GenTrans
 *
 */

public class Net {

	private final Mark init;
	private final List<Place> placeList;
	private final List<ImmTrans> immTransList;
	private final List<ExpTrans> expTransList;
	private final List<GenTrans> genTransList;
	private final List<String> rewardList;

	/**
	 * Constructor
	 * @param init An initial mark
	 * @param placeList A list of places
	 * @param immTransList A list of imm trans
	 * @param expTransList A list of exp trans
	 * @param genTransList A list of gen trans
	 * @param rewardList A map of AST as reward functions
	 */
	public Net(Mark init, List<Place> placeList, List<ImmTrans> immTransList,
			List<ExpTrans> expTransList, List<GenTrans> genTransList,
			List<String> rewardList) {
		this.init = init;
		this.placeList = placeList;
		this.immTransList = immTransList;
		this.expTransList = expTransList;
		this.genTransList = genTransList;
		this.rewardList = rewardList;
	}

	/**
	 * Getter for an initial marking
	 * @return An instance of initial marking
	 */
	public Mark getInitMark() {
		return init;
	}

	/**
	 * Getter for a list of places
	 * @return A list of places
	 */
	public final List<Place> getPlaceSet() {
		return placeList;
	}
	
	/**
	 * Getter for a list of ImmTrans
	 * @return A list of immtrans
	 */
	public final List<ImmTrans> getImmTransSet() {
		return immTransList;
	}

	/**
	 * Getter for a list of ExpTrans
	 * @return A list of exptrans
	 */
	public final List<ExpTrans> getExpTransSet() {
		return expTransList;
	}

	/**
	 * Getter for a list of GenTrans
	 * @return A list of gentrans
	 */
	public final List<GenTrans> getGenTransSet() {
		return genTransList;
	}

	/**
	 * Getter for a list of reward
	 * @return A list of rewards
	 */
	public final List<String> getRewardSet() {
		return rewardList;
	}
}
