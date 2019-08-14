package com.github.okamumu.jspetrinet.marking.method;

import java.util.Map;

import com.github.okamumu.jspetrinet.exception.MarkingError;
import com.github.okamumu.jspetrinet.marking.GenVec;
import com.github.okamumu.jspetrinet.marking.Mark;

class ExitMark {
	
	enum StatusOfExitMarks {
		Init,
		Vanishable,
		NoVanishable,		
	}
	
	public static ExitMark init(Mark m) {
		return new ExitMark(m, StatusOfExitMarks.Init);
	}
	
	public static ExitMark finalize(Mark m) {
		return new ExitMark(m, StatusOfExitMarks.NoVanishable);
	}

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
	
	private ExitMark(Mark emark, StatusOfExitMarks status) {
		this.emark = emark;
		this.status = status;
	}
	
	public final Mark get() {
		return emark;
	}
	
	public final boolean canVanishing() {
		return status == StatusOfExitMarks.Vanishable;
	}
	
}

