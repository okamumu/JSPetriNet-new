package com.github.okamumu.jspetrinet.marking;

/**
 * A class to represent the status of exit marks
 * that are marks as next marks when IMM transitions are vanished.
 *
 */
public final class ExitMark {
	
	/**
	 * An enum to represent the status. The status is temporally decided.
	 * After searching all the children, the status is finally decided.
	 * The status may transfer from Init to Vanishable, or from Vanishable to NoVanishable
	 * Init: Initialized
	 * Vanishable: This mark may be vanished.
	 * NoVanishable: This mark is not vanished.
	 */
	enum StatusOfExitMarks {
		Init,
		Vanishable,
		NoVanishable,
	}

	/**
	 * The static method to construct an exit mark as Init
	 * @param m A next mark
	 * @param genv An instance of GenVec
	 */
	public static ExitMark init(Mark m, GenVec genv) {
		ExitMark em = new ExitMark(m, genv);
		em.status = StatusOfExitMarks.Init;
		return em;
	}
	
	/**
	 * The static method to construct an exit mark as NoVanishable
	 * @param m A next mark.
	 * @param genv An instance of GenVec
	 */
	public static ExitMark finalize(Mark m, GenVec genv) {
		ExitMark em = new ExitMark(m, genv);
		em.status = StatusOfExitMarks.NoVanishable;
		return em;
	}

	private final Mark self;
	private final GenVec genv;
	private Mark next;
	private StatusOfExitMarks status;
	
	/**
	 * Constructor
	 * @param m An instance of mark
	 * @param status A status of exit mark
	 */
	private ExitMark(Mark m, GenVec genv) {
		this.self = m;
		this.genv = genv;
		this.next = m;
	}

	/**
	 * Getter for the next mark
	 * @return An instance of next mark
	 */
	public final Mark get() {
		return next;
	}
	
	/**
	 * Check the mark is vanishable or not
	 * @return A boolean
	 */
	public final boolean canVanishing() {
		return status == StatusOfExitMarks.Vanishable;
	}
	
	/**
	 * Set NonVanishable
	 */
	public void setNonVanishable() {
		next = self;
		status = StatusOfExitMarks.NoVanishable;		
	}

	/**
	 * The method to construct by merging marks of parent and child.
	 * @param child A mark for child
	 */
	public void union(ExitMark child) {
		switch (status) {
		case Init:
			switch (child.status) {
//			case Init:
//				throw new  MarkingError("ExitMarking0: The status of child is Init");
			case Vanishable:
			case NoVanishable:
				if (genv.isSameClass(child.genv)) {
					next = child.next;
					status = StatusOfExitMarks.Vanishable;
					return;
				} else {
					next = self;
					status = StatusOfExitMarks.NoVanishable;
					return;
				}
			default:
			}
		case Vanishable:
			switch (child.status) {
//			case Init:
//				throw new  MarkingError("ExitMarking1: The status of child is Init");
			case Vanishable:
			case NoVanishable:
				if (next != child.next) {
					next = self;
					status = StatusOfExitMarks.NoVanishable;
				}
			default:
			}
		case NoVanishable:
			return;
		}
//		throw new MarkingError("A general error in ExitMarking");
	}
}

