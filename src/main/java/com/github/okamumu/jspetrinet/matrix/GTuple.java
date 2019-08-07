package com.github.okamumu.jspetrinet.matrix;

import java.util.Objects;

import com.github.okamumu.jspetrinet.marking.GenVec;
import com.github.okamumu.jspetrinet.petri.nodes.GenTrans;

public class GTuple {

	private final GenVec src;
	private final GenVec dest;
	private final GenTrans tr;
	
	GTuple(GenVec src, GenVec dest, GenTrans tr) {
		this.src = src;
		this.dest = dest;
		this.tr = tr;
	}
	
	public final GenVec getSrc() {
		return src;
	}

	public final GenVec getDest() {
		return dest;
	}

	public final GenTrans getGenTrans() {
		return tr;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dest, src, tr);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GTuple other = (GTuple) obj;
		return Objects.equals(dest, other.dest) && Objects.equals(src, other.src) && Objects.equals(tr, other.tr);
	}

}
