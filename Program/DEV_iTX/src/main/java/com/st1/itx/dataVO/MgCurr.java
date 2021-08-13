package com.st1.itx.dataVO;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * Currency of map
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class MgCurr extends LinkedHashMap<String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 457116655454248799L;

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

	/**
	 * get Currency Name
	 * 
	 * @param curcd Code of Integer
	 * @return String Currency Name
	 */
	public String getCurnm(int curcd) {
		return this.get(String.valueOf(curcd));
	}

	/**
	 * get Currency Name
	 * 
	 * @param curcd Code of String
	 * @return String Currency Name
	 */
	public String getCurnm(String curcd) {
		return this.get(curcd);
	}

	/**
	 * get Currency code
	 * 
	 * @param curnm String Currency Name
	 * @return Integer of Currency code
	 */
	public int getCurcdInt(String curnm) {
		return Integer.parseInt(this.get(curnm) == null ? "0" : this.get(curnm));
	}

	/**
	 * get Currency Name
	 * 
	 * @param curnm String of Currency Name
	 * @return Integer of Currency code
	 */
	public String getCurcdString(String curnm) {
		return this.get(curnm) == null ? "0" : this.get(curnm);
	}
}
