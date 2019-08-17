package com.github.okamumu.jspetrinet.marking.method;

import java.util.Map;

import com.github.okamumu.jspetrinet.exception.MarkingError;
import com.github.okamumu.jspetrinet.marking.GenVec;
import com.github.okamumu.jspetrinet.marking.Mark;

/**
 * A class to represent the status of exit marks
 * that are marks as next marks when IMM transitions are vanished.
 *
 */
class ExitMark {
	
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
	 */
	public static ExitMark init(Mark m) {
		return new ExitMark(m, StatusOfExitMarks.Init);
	}
	
	/**
	 * The static method to construct an exit mark as NoVanishable
	 * @param m A next mark.
	 */
	public static ExitMark finalize(Mark m) {
		return new ExitMark(m, StatusOfExitMarks.NoVanishable);
	}

	/**
	 * The static method to construct by merging makrs of parent and child.
	 * @param mg An instance of marking graph. This is used to get the GenVec that the mark belongs to
	 * @param parent A mark for parent
	 * @param child A mark for child
	 * @return An instance of new status for the parent mark
	 * @throws MarkingError An error when the status of child mark is Init
	 */
	public static ExitMark union(Map<Mark,GenVec> mg, ExitMark parent, ExitMark child) throws MarkingError {
		switch (parent.status) {
		case Init:
			switch (child.status) {
			case Init:
				throw new  MarkingError("ExitMarking: The status of child is Init");
			case Vanishable:
			case NoVanishable:
				GenVec s1 = mg.get(parent.emark);
				GenVec s2 = mg.get(child.emark);
				if (s1.isSameClass(s2)) {
					return new ExitMark(child.emark, StatusOfExitMarks.Vanishable);
				} else {
					return new ExitMark(parent.emark, StatusOfExitMarks.NoVanishable);
				}
			}
		case Vanishable:
			switch (child.status) {
			case Init:
				throw new  MarkingError("ExitMarking: The status of child is Init");
			case Vanishable:
			case NoVanishable:
				if (parent.emark == child.emark) {
					return parent;
				} else {
					return new ExitMark(parent.emark, StatusOfExitMarks.NoVanishable);
				}
			}
		case NoVanishable:
			return parent;
		}
		throw new MarkingError("A general error in ExitMarking");
	}

	private final Mark emark;
	private final StatusOfExitMarks status;
	
	/**
	 * Constructor
	 * @param emark An instance of mark
	 * @param status A status of exit mark
	 */
	private ExitMark(Mark emark, StatusOfExitMarks status) {
		this.emark = emark;
		this.status = status;
	}

	/**
	 * Getter for the next mark
	 * @return An instance of next mark
	 */
	public final Mark get() {
		return emark;
	}
	
	/**
	 * Check the mark is vanishable or not
	 * @return A boolean
	 */
	public final boolean canVanishing() {
		return status == StatusOfExitMarks.Vanishable;
	}
	
}

