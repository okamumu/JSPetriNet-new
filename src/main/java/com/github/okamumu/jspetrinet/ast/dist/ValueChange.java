package com.github.okamumu.jspetrinet.ast.dist;

import com.github.okamumu.jspetrinet.exception.InvalidOperation;

public class ValueChange {

    public static class ValueChangeInstanceHolder {
    	private static final ValueChange instance = new ValueChange();
    }

	public static ValueChange getInstance() {
		return ValueChangeInstanceHolder.instance;
	}
	
	private ValueChange() {}

	public final boolean convertObjctToBoolean(Object obj) throws InvalidOperation {
		if (obj instanceof Boolean) {
			return (Boolean) obj;
		} else {
			throw new InvalidOperation("The object should be an object of Boolean in covertObjectToBoolean");
		}
	}

	public final double convertObjctToDouble(Object obj) throws InvalidOperation {
		double doubleType;
		if (obj instanceof Integer) {
			doubleType = (Integer) obj;
		} else if (obj instanceof Double) {
			doubleType = (Double)obj;
		} else {
			throw new InvalidOperation("The object should be an object of Integer or Double in covertObjectToDouble");
		}
		return doubleType;
	}
}
