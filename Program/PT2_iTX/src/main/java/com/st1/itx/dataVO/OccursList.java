package com.st1.itx.dataVO;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

public class OccursList extends LinkedHashMap<String, String> {

	private static final long serialVersionUID = -7040735063622310849L;

	/**
	 * Associates the specified value with the specified key in this map. If the map
	 * previously contained a mapping for the key, the old value is replaced.
	 *
	 * @param key   key with which the specified value is to be associated
	 * @param param value to be associated with the specified key
	 */
	public void putParam(String key, Object param) {
		if (param instanceof Integer)
			this.put(key, new BigDecimal((Integer) (param == null ? 0 : param)).toPlainString());
		else if (param instanceof Double)
			this.put(key, new BigDecimal((Double) (param == null ? 0 : param)).toPlainString());
		else if (param instanceof Float)
			this.put(key, new BigDecimal((Float) (param == null ? 0 : param)).toPlainString());
		else if (param instanceof Long)
			this.put(key, new BigDecimal((Long) (param == null ? 0 : param)).toPlainString());
		else if (param instanceof BigDecimal)
			this.put(key, ((BigDecimal) (param == null ? 0 : param)).toPlainString());
		else
			this.put(key, (String) (param == null ? "" : param));
	}
}
