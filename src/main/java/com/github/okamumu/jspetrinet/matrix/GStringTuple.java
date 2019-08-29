package com.github.okamumu.jspetrinet.matrix;

import java.util.Objects;

import com.github.okamumu.jspetrinet.marking.GenVec;

public class GStringTuple {

	private final String label;
	private final GenVec genv;
	
	GStringTuple(String label, GenVec genv) {
		this.label = label;
		this.genv = genv;
	}
	
	public final String getGenString() {
		return label;
	}

	public final GenVec getGenVec() {
		return genv;
	}

	@Override
	public int hashCode() {
		return Objects.hash(genv, label);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GStringTuple other = (GStringTuple) obj;
		return Objects.equals(genv, other.genv) && Objects.equals(label, other.label);
	}


}
