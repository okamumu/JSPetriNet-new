package com.github.okamumu.jspetrinet.marking.method;

import java.util.Map;

import com.github.okamumu.jspetrinet.exception.MarkingError;
import com.github.okamumu.jspetrinet.marking.GenVec;
import com.github.okamumu.jspetrinet.marking.Mark;

class ExitMark {
	
	enum NumOfExitMarks {
		NotYet,
		One,
		MoreThanOne
	}
	
	public static ExitMark init(Mark m) {
		return new ExitMark(m, NumOfExitMarks.NotYet, false);
	}
	
	public static ExitMark finalize(Mark m) {
		return new ExitMark(m, NumOfExitMarks.One, false);
	}

	public static ExitMark union(Map<Mark,GenVec> mg, ExitMark m, ExitMark other) throws MarkingError {
		switch (m.numOfMark) {
		case NotYet:
			switch (other.numOfMark) {
			case NotYet:
				throw new MarkingError("An ExitMarking should not be set");
			case One:
				GenVec s1 = mg.get(m.emark);
				GenVec s2 = mg.get(other.emark);
				if (s1.isSameClass(s2)) {
					return new ExitMark(other.emark, NumOfExitMarks.One, true);
				} else {
					return new ExitMark(m.emark, NumOfExitMarks.One, false);
				}
			case MoreThanOne:
				return new ExitMark(null, NumOfExitMarks.MoreThanOne, false);
			}
			break;
		case One:
			switch (other.numOfMark) {
			case NotYet:
				throw new MarkingError("An ExitMarking should not be set");
			case One:
				if (m.emark != other.emark) {
					return new ExitMark(null, NumOfExitMarks.MoreThanOne, false);
				} else {
					return other;
				}
			case MoreThanOne:
				return other;
			}
		case MoreThanOne:
			return m;
		}
		throw new MarkingError("A general error in ExitMarking");
	}

	private final Mark emark;
	private final NumOfExitMarks numOfMark;
	private final boolean vanishing;
	
	private ExitMark(Mark emark, NumOfExitMarks numOfMark, boolean vanishing) {
		this.emark = emark;
		this.numOfMark = numOfMark;
		this.vanishing = vanishing;
	}
	
	public final Mark get() {
		return emark;
	}
	
	public final boolean canVanishing() {
		return vanishing;
	}
	
}

